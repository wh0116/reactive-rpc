package com.dinosaur.reactive.webclient.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author wanghu
 * @since 2022/12/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HRpc {

    @AliasFor("name")
    String value() default "";
    String contextId() default "";
    Class<?>[] configuration() default {};
    Class<?> fallback() default void.class;
    String path() default "";
    String url() default "";
    @AliasFor("value")
    String name() default "";
    boolean primary() default true;
}
