package com.dinosaur.reactive.webclient.core;

import com.dinosaur.reactive.webclient.core.annotation.EnableHRpc;
import com.dinosaur.reactive.webclient.core.annotation.HRpc;
import com.dinosaur.reactive.webclient.core.factory.HRpcFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wanghu
 */
public class HRpcRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        //注册configuration
        registerDefaultConfiguration(metadata, registry);
        //注册HRpc注解
        registerHRpc(metadata, registry);
    }

    private void registerDefaultConfiguration(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        //获取@EnableHRpc注解下所有的属性
        Map<String, Object> defaultAttrs = metadata.getAnnotationAttributes(EnableHRpc.class.getName(), true);

        if (defaultAttrs != null && defaultAttrs.containsKey("defaultConfiguration")) {
            String name;
            //判断传入的defaultConfiguration是不是topClass
            if (metadata.hasEnclosingClass()) {
                name = "default." + metadata.getEnclosingClassName();
            }
            else {
                name = "default." + metadata.getClassName();
            }
            registerClientConfiguration(registry, name, "default", defaultAttrs.get("defaultConfiguration"));
        }
    }

    /**
     * 构建一个BeanDefinition(WebClientSpecification)，并注册
     * @param registry BeanDefinitionRegistry
     * @param name Object
     * @param className Object
     * @param configuration Object
     */
    private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object className,
                                             Object configuration) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(WebClientSpecification.class);
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(className);
        builder.addConstructorArgValue(configuration);
        registry.registerBeanDefinition(name + "." + WebClientSpecification.class.getSimpleName(),
                builder.getBeanDefinition());
    }

    public void registerHRpc(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        //获取@EnableHRpc注解下所有的属性
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableHRpc.class.getName());
        //获取clients属性设置的Class对象，并且对象上使用了@HRpc注解
        final Class<?>[] clients = attrs == null ? null : (Class<?>[]) attrs.get("clients");
        if (clients == null || clients.length == 0) {
            ClassPathScanningCandidateComponentProvider scanner = getScanner();
            scanner.setResourceLoader(this.resourceLoader);
            //过滤带有@HRpc的component
            scanner.addIncludeFilter(new AnnotationTypeFilter(HRpc.class));
            Set<String> basePackages = getBasePackages(metadata);
            //查找base packages下满足条件的组件
            for (String basePackage : basePackages) {
                candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
            }
        }
        else {
            for (Class<?> clazz : clients) {
                candidateComponents.add(new AnnotatedGenericBeanDefinition(clazz));
            }
        }

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                // verify annotated class is an interface
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@HRpc can only be specified on an interface");
                //get annotation attributes of @HRpc
                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(HRpc.class.getName());

                String name = getClientName(attributes);
                String className = annotationMetadata.getClassName();
                registerClientConfiguration(registry, name, className, attributes.get("configuration"));
                //注册RPC
                registerHRpc(registry, annotationMetadata, attributes);
            }
        }
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        //ClassPathScanningCandidateComponentProvider扫描classpath下的class文件
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    //candidate component must not be an annotation
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata metadata) {
        Map<String, Object> attributes = metadata
                .getAnnotationAttributes(EnableHRpc.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return basePackages;
    }

    private void registerHRpc(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata,
                                     Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        lazilyRegisterHRpcBeanDefinition(className, attributes, registry);
    }

    /**
     * 构建并注册HRpc BeanDefinition
     * @param className
     * @param attributes
     * @param registry
     */
    private void lazilyRegisterHRpcBeanDefinition(String className, Map<String, Object> attributes,
                                                         BeanDefinitionRegistry registry) {
        ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory
                ? (ConfigurableBeanFactory) registry : null;
        Class clazz = ClassUtils.resolveClassName(className, null);
        String contextId = getContextId(beanFactory, attributes);
        String name = getName(attributes);
        HRpcFactoryBean factoryBean = new HRpcFactoryBean();
        factoryBean.setBeanFactory(beanFactory);
        factoryBean.setName(name);
        factoryBean.setContextId(contextId);
        factoryBean.setType(clazz);
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz, () -> {
            factoryBean.setUrl(getUrl(beanFactory, attributes));
            factoryBean.setPath(getPath(beanFactory, attributes));

            //获取代理对象
            return factoryBean.getObject();
        });
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.setLazyInit(true);
        //定义BeanDefinition
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        beanDefinition.setAttribute("hrpcFactoryBean", factoryBean);

        boolean primary = (Boolean) attributes.get("primary");

        beanDefinition.setPrimary(primary);
        //BeanDefinition包装器，会创建一个新的BeanDefinitionHolder，包含bean name，alias和bean definition
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        //注册bean definition
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("contextId");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("value");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("serviceId");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException(
                "Either 'name' or 'value' must be provided in @" + HRpc.class.getSimpleName());
    }

    /**
     * 根据service name
     * @param name
     * @return
     */
    private String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = null;
        try {
            String url;
            if (!name.startsWith("http://") && !name.startsWith("https://")) {
                url = "http://" + name;
            }
            else {
                url = name;
            }
            host = new URI(url).getHost();

        }
        catch (URISyntaxException ignored) {
        }
        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    private String getUrl(String url) {
        if (StringUtils.hasText(url) && !(url.startsWith("#{") && url.contains("}"))) {
            if (!url.contains("://")) {
                url = "http://" + url;
            }
            try {
                new URL(url);
            }
            catch (MalformedURLException e) {
                throw new IllegalArgumentException(url + " is malformed", e);
            }
        }
        return url;
    }

    private String getPath(String path) {
        if (StringUtils.hasText(path)) {
            path = path.trim();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    private String getPath(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String path = resolve(beanFactory, (String) attributes.get("path"));
        return getPath(path);
    }

    private String getUrl(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String url = resolve(beanFactory, (String) attributes.get("url"));
        return getUrl(url);
    }

    private String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {
            if (beanFactory == null) {
                //解析占位符${key}，解析不了原样输出
                return this.environment.resolvePlaceholders(value);
            }
            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            //处理占位符
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }

    String getName(Map<String, Object> attributes) {
        return getName(null, attributes);
    }

    String getName(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(beanFactory, name);
        return getName(name);
    }

    private String getContextId(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String contextId = (String) attributes.get("contextId");
        if (!StringUtils.hasText(contextId)) {
            return getName(attributes);
        }

        contextId = resolve(beanFactory, contextId);
        return getName(contextId);
    }
}
