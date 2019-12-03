package org.springdoc.core;

import java.lang.reflect.Parameter;

class ParameterInfo {

    private final java.lang.reflect.Parameter parameter;
    private final int index;
    private String pName;
    private io.swagger.v3.oas.models.parameters.Parameter parameterModel;

    public ParameterInfo(String pName, Parameter parameter,
                         io.swagger.v3.oas.models.parameters.Parameter parameterModel, int index) {
        super();
        this.pName = pName;
        this.parameter = parameter;
        this.parameterModel = parameterModel;
        this.index = index;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public java.lang.reflect.Parameter getParameter() {
        return parameter;
    }

    public io.swagger.v3.oas.models.parameters.Parameter getParameterModel() {
        return parameterModel;
    }

    public void setParameterModel(io.swagger.v3.oas.models.parameters.Parameter parameterModel) {
        this.parameterModel = parameterModel;
    }

    public int getIndex() {
        return index;
    }
}
