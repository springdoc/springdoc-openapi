package org.springdoc.core;

import java.lang.reflect.Parameter;

class ParameterInfo {

    private String pName;
    private final java.lang.reflect.Parameter parameter;
    private io.swagger.v3.oas.annotations.Parameter parameterDoc;
    private io.swagger.v3.oas.models.parameters.Parameter parameterModel;
    private int index;

    public ParameterInfo(String pName, Parameter parameter,
                         io.swagger.v3.oas.models.parameters.Parameter parameterModel, int index) {
        super();
        this.pName = pName;
        this.parameter = parameter;
        this.parameterModel = parameterModel;
        this.index = index;
    }

    public ParameterInfo(String pName, Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc) {
        super();
        this.pName = pName;
        this.parameter = parameter;
        this.parameterDoc = parameterDoc;
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

    public io.swagger.v3.oas.annotations.Parameter getParameterDoc() {
        return parameterDoc;
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
