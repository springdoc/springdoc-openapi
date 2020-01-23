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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("rawtypes")
public class ResponseBuilder extends AbstractResponseBuilder {

	private final List<Class<?>> resultWrappers = Arrays.asList(Callable.class, CompletionStage.class);

	public ResponseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers) {
		super(operationBuilder, returnTypeParsers);
	}

	@Override
	protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType parameterizedType,
														  JsonView jsonView) {
		Schema<?> schemaN = null;
		String parameterizedTypeName = parameterizedType.getRawType().getTypeName();
		if (ResponseEntity.class.getName().contentEquals(parameterizedTypeName) || HttpEntity.class.getName().contentEquals(parameterizedTypeName)) {
			schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
		}
		else if(resultWrappers.stream().anyMatch(clazz -> isParameterizedTypeWrapper(clazz, parameterizedType))) {
			Type type = parameterizedType.getActualTypeArguments()[0];
			if (type instanceof ParameterizedType) {
				schemaN = this.calculateSchemaFromParameterizedType(components, (ParameterizedType) type, jsonView);
			}
			else {
				schemaN = this.calculateSchemaParameterizedType(components, parameterizedType, jsonView);
			}
		}
		return schemaN;
	}

	private boolean isParameterizedTypeWrapper(Class<?> clazz, ParameterizedType type) {
		try {
			Class<?> parameterType = Class.forName(type.getRawType().getTypeName());
			return clazz.isAssignableFrom(parameterType);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
