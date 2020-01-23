/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("rawtypes")
public class ResponseBuilder extends AbstractResponseBuilder {

	public ResponseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers) {
		super(operationBuilder, returnTypeParsers);
	}

	@Override
	protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType parameterizedType,
			JsonView jsonView) {
		Schema schemaN = null;
		if (Mono.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
			if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && isResponseTypeToIgnore(((ParameterizedType) parameterizedType.getActualTypeArguments()[0])
					.getRawType().getTypeName())) {
				ParameterizedType parameterizedTypeNew = (ParameterizedType) parameterizedType
						.getActualTypeArguments()[0];
				schemaN = calculateSchemaParameterizedType(components, parameterizedTypeNew, jsonView);
			}
			else {
				schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
			}
		}
		else if (Flux.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
			if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && isResponseTypeToIgnore(((ParameterizedType) parameterizedType.getActualTypeArguments()[0])
					.getRawType().getTypeName())) {
				ParameterizedType parameterizedTypeNew = (ParameterizedType) parameterizedType
						.getActualTypeArguments()[0];
				schemaN = calculateFluxSchema(components, parameterizedTypeNew, jsonView);
			}
			else {
				schemaN = calculateFluxSchema(components, parameterizedType, jsonView);
			}
		}
		else if (isResponseTypeToIgnore(parameterizedType.getRawType().getTypeName())) {
			schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
		}
		return schemaN;
	}

	private boolean isResponseTypeToIgnore(String typeName) {
		return ResponseEntity.class.getName().contentEquals(typeName) || HttpEntity.class.getName().contentEquals(typeName);
	}

	private Schema calculateFluxSchema(Components components, ParameterizedType parameterizedType, JsonView jsonView) {
		Schema itemsSchema = SpringDocAnnotationsUtils.extractSchema(components, parameterizedType.getActualTypeArguments()[0], jsonView);
		return new ArraySchema().items(itemsSchema);
	}

}
