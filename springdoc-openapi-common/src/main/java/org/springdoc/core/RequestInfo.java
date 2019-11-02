package org.springdoc.core;

public class RequestInfo {

	private String value;
	private boolean required;
	private String defaultValue;
	private ParameterType paramType;

	public RequestInfo(ParameterType paramType, String value, boolean required, String defaultValue) {
		super();
		this.value = value;
		this.required = required;
		this.defaultValue = defaultValue;
		this.paramType = paramType;
	}

	public String value() {
		return value;
	}

	public boolean required() {
		return required;
	}

	public String defaultValue() {
		return defaultValue;
	}

	public String type() {
		return paramType.toString();
	}

	public enum ParameterType {
		QUERY_PARAM("query"), HEADER_PARAM("header"), PATH_PARAM("path");

		private String value;
		private ParameterType(String s) {
			value = s;
		}
		@Override
		public String toString() {
			return value;
		}
	}

}
