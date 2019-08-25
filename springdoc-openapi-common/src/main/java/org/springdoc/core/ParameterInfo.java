package org.springdoc.core;

import java.lang.reflect.Parameter;

public class ParameterInfo {

	private String pName;

	private java.lang.reflect.Parameter parameter;

	private io.swagger.v3.oas.annotations.Parameter parameterDoc;

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

	public void setParameter(java.lang.reflect.Parameter parameter) {
		this.parameter = parameter;
	}

	public io.swagger.v3.oas.annotations.Parameter getParameterDoc() {
		return parameterDoc;
	}

	public void setParameterDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc) {
		this.parameterDoc = parameterDoc;
	}

}
