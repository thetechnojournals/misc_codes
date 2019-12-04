package com.ttj.dtogen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * This annotation is used to mark the class which
 * will be read by annotation processor to generate the DTO classes
 */
public @interface DtoClass {
    /**
     * Class name, if not given then annotated class name will be used
     * by appending Dto to it.
     * @return
     */
    String name() default "";

    /**
     * Package where DTO class need to be generated, if not given
     * then annotated class's package will be use by adding "dto"
     * subpackage under it.
     * @return
     */
    String classPackage() default "";

    /**
     * if true then all the fields will be used for DTO generation
     * @return
     */
    boolean includeAllFields() default false;
}
