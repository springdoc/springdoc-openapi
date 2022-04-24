/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */
package org.springdoc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * The type Controller advice info.
 * @author bnasslahsen
 */
public class ControllerAdviceInfo {

	/**
	 * The Controller advice.
	 */
	private final Object controllerAdvice;

	/**
	 * The Api response map.
	 */
	private final Map<String, ApiResponse> apiResponseMap = new LinkedHashMap<>();

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
		return apiResponseMap;
	}
}
