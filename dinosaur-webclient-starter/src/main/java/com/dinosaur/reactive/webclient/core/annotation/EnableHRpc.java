package com.dinosaur.reactive.webclient.core.annotation;

import com.dinosaur.reactive.webclient.core.HRpcRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wanghu
 * @since 2022/12/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HRpcRegistrar.class)
public @interface EnableHRpc {

    String[] value() default {};
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
    Class<?>[] defaultConfiguration() default {};
    Class<?>[] clients() default {};
}
