package org.springdoc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.models.responses.ApiResponse;

public class ControllerAdviceInfo {

	private Object controllerAdvice;

	private Map<String, ApiResponse> apiResponseMap = new LinkedHashMap<>();

	public ControllerAdviceInfo(Object controllerAdvice) {
		this.controllerAdvice = controllerAdvice;
	}

	public Object getControllerAdvice() {
		return controllerAdvice;
	}

	public Map<String, ApiResponse> getApiResponseMap() {
		return apiResponseMap;
	}
}
