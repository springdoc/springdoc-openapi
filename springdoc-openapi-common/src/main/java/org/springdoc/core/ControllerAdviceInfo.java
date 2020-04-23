package org.springdoc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class ControllerAdviceInfo {

	private ControllerAdvice controllerAdvice;

	private Map<String, ApiResponse> apiResponseMap = new LinkedHashMap<>();

	public ControllerAdviceInfo(ControllerAdvice controllerAdvice) {
		this.controllerAdvice = controllerAdvice;
	}

	public ControllerAdvice getControllerAdvice() {
		return controllerAdvice;
	}

	public Map<String, ApiResponse> getApiResponseMap() {
		return apiResponseMap;
	}
}
