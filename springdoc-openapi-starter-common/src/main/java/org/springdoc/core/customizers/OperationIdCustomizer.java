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
package org.springdoc.core.customizers;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

/**
 * The type Operation id customizer.
 *
 * @author bnasslahsen
 */
public class OperationIdCustomizer implements GlobalOpenApiCustomizer {

	@Override
	public void customise(OpenAPI openApi) {
		// Map to store operationId counts
		Map<String, Integer> operationIdCount = new HashMap<>();

		// Iterate through all the paths
		for (Map.Entry<String, PathItem> pathEntry : openApi.getPaths().entrySet()) {
			PathItem pathItem = pathEntry.getValue();

			// Process all HTTP methods for the path (GET, POST, PUT, DELETE, etc.)
			processOperation(pathItem.getGet(), operationIdCount);
			processOperation(pathItem.getPost(), operationIdCount);
			processOperation(pathItem.getPut(), operationIdCount);
			processOperation(pathItem.getDelete(), operationIdCount);
			processOperation(pathItem.getPatch(), operationIdCount);
			processOperation(pathItem.getHead(), operationIdCount);
			processOperation(pathItem.getOptions(), operationIdCount);
			processOperation(pathItem.getTrace(), operationIdCount);
		}
	}

	/**
	 * Process operation.
	 *
	 * @param operation        the operation
	 * @param operationIdCount the operation id count
	 */
// Helper method to process each operation and handle duplicate operationId
	private void processOperation(Operation operation, Map<String, Integer> operationIdCount) {
		if (operation != null) {
			String originalOperationId = operation.getOperationId();

			// Check if operationId already exists
			if ( originalOperationId!=null && operationIdCount.containsKey(originalOperationId)) {
				// Get the count for the current operationId and increment
				int count = operationIdCount.get(originalOperationId);
				count++;
				operationIdCount.put(originalOperationId, count);

				// Create new unique operationId by appending _x
				String newOperationId = originalOperationId + "_" + count;
				operation.setOperationId(newOperationId);
			}
			else {
				// First time this operationId is seen, initialize the count
				operationIdCount.put(originalOperationId, 0);
			}
		}
	}

}