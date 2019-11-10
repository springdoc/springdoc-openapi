package org.springdoc.core;

public class RequestInfo {

    private final String value;
    private final boolean required;
    private final String defaultValue;
    private final ParameterType paramType;

    public RequestInfo(ParameterType paramType, String value, boolean required, String defaultValue) {
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
        private final String value;
        ParameterType(String s) {
            value = s;
        }
        @Override
        public String toString() {
            return value;
        }
    }

}
