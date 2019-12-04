package com.ttj.dtogen;

import com.ttj.dtogen.annotations.DtoClass;
import com.ttj.dtogen.annotations.DtoProperty;

@DtoClass(name="TestDto", classPackage = "com.ttj.dtogen.dto")
public class TestEntity {
    private String testId;
    @DtoProperty
    private String name;
    @DtoProperty(name="age", getter=true, setter = true)
    private Integer age;
    @DtoProperty(name="address", getter=true, setter = false)
    private String address;
}
