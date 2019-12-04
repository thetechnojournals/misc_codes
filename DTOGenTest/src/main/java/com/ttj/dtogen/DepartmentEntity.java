package com.ttj.dtogen;

import com.ttj.dtogen.annotations.DtoClass;
import com.ttj.dtogen.annotations.DtoProperty;

import java.util.List;
import java.util.Map;

@DtoClass(includeAllFields = true)
public class DepartmentEntity {
    private String deptName;

    @DtoProperty(name="specialNumber", getter = false, setter = true)
    private Double splNumber;

    private Map<String, List<String>> empList;
}
