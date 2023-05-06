package com.dinosaur.reactive.webclient.core.factory;

import com.dinosaur.reactive.webclient.core.handler.WebClientInvocationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

/**
 * 构建WebclientBean对象
 * @author wanghu
 */
public class HRpcFactoryBean implements FactoryBean, ApplicationContextAware, BeanFactoryAware {

    private String name;
    private String contextId;
//    Class<?>[] configuration;
    private Class<?> type;
    private Class<?> fallback = void.class;
    private String path;
    private String url;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;

    private static final Log log =LogFactory.getLog(HRpcFactoryBean.class);

    /**
     * 1.创建url
     * 2.解析param，解析get post
     * 3.拼接url
     * 4.创建client
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() {
        return getTargetObject();
    }

    <T> T getTargetObject() {
        //如果url有值并且不是以http开头
        if (StringUtils.hasText(url)) {
            if (!url.startsWith("http")) {
                log.debug("URL must be stated with 'http'.");
                throw new IllegalArgumentException("the param url must be started with http in @HRpc");
            }
        }else{
            //service name设置url
            Assert.notNull(name, "service name must be not null");
            url = "http://" + name;
        }
        String url = this.url + cleanPath();
        WebClientInvocationHandler invocationHandler = new WebClientInvocationHandler();
        invocationHandler.newInstance(type, url);
        T proxy = (T)Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, invocationHandler);
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private String cleanPath() {
        if (path == null) {
            return "";
        }
        String path = this.path.trim();
        if (StringUtils.hasLength(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, url.length() - 1);
            }
        }
        return path;
    }
}
