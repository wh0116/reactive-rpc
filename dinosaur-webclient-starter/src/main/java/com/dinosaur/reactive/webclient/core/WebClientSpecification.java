package com.dinosaur.reactive.webclient.core;

import org.springframework.cloud.context.named.NamedContextFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * 创建子容器，容器间进行数据隔离
 * @author whu
 * @since 2022/12/13
 */
public class WebClientSpecification implements NamedContextFactory.Specification {

    private String name;
    private String className;
    private Class<?>[] configuration;

    public WebClientSpecification(){}

    public WebClientSpecification(String name, String className, Class<?>[] configuration){
        this.name = name;
        this.className = className;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        this.configuration = configuration;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof WebClientSpecification that)) {
//            return false;
//        }
//        return Objects.equals(name, that.name) && Objects.equals(className, that.className)
//                && Arrays.equals(configuration, that.configuration);
//    }

//    @Override
//    public int hashCode() {
//        int result = Objects.hash(name, className);
//        result = 31 * result + Arrays.hashCode(configuration);
//        return result;
//    }

    @Override
    public String toString() {
        return "WebClientSpecification{" + "name='" + name + "', " + "className='" + className + "', "
                + "configuration=" + Arrays.toString(configuration) + "}";
    }
}
