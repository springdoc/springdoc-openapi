/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */
package org.springdoc.core.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.util.CollectionUtils;

/**
 * The type Controller advice info.
 *
 * @author bnasslahsen
 */
public class ControllerAdviceInfo {

	/**
	 * The Controller advice.
	 */
	private final Object controllerAdvice;

	/**
	 * The Method advice infos.
	 */
	private List<MethodAdviceInfo> methodAdviceInfos = new ArrayList<>();

	/**
	 * Instantiates a new Controller advice info.
	 *
	 * @param controllerAdvice the controller advice
	 */
	public ControllerAdviceInfo(Object controllerAdvice) {
		this.controllerAdvice = controllerAdvice;
	}

	/**
	 * Gets controller advice.
	 *
	 * @return the controller advice
	 */
	public Object getControllerAdvice() {
		return controllerAdvice;
	}

	/**
	 * Gets api response map.
	 *
	 * @return the api response map
	 */
	public Map<String, ApiResponse> getApiResponseMap() {
		Map<String, ApiResponse> apiResponseMap = new LinkedHashMap<>();
		for (MethodAdviceInfo methodAdviceInfo : methodAdviceInfos) {
			if (!CollectionUtils.isEmpty(methodAdviceInfo.getApiResponses()))
				apiResponseMap.putAll(methodAdviceInfo.getApiResponses());
		}
		return apiResponseMap;
	}

	/**
	 * Gets method advice infos.
	 *
	 * @return the method advice infos
	 */
	public List<MethodAdviceInfo> getMethodAdviceInfos() {
		return methodAdviceInfos;
	}

	/**
	 * Add method advice infos.
	 *
	 * @param methodAdviceInfo the method advice info
	 */
	public void addMethodAdviceInfos(MethodAdviceInfo methodAdviceInfo) {
		this.methodAdviceInfos.add(methodAdviceInfo);
	}
}
