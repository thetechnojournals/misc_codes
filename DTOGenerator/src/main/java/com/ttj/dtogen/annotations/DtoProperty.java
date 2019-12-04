package com.ttj.dtogen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * This annotation is used to annotate the fields in entity classes
 * which we want to include in DTO class
 */
public @interface DtoProperty {
    /**
     * new name for the field, if not given then annotated
     * field's name will be used
     * @return
     */
    String name() default "";

    /**
     * if true then  getter method will be created
     * @return
     */
    boolean getter() default true;

    /**
     * if true then setter method will be generated
     * @return
     */
    boolean setter() default true;
}
