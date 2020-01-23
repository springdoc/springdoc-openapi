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
import java.lang.reflect.WildcardType;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

public class ParameterBuilder extends AbstractParameterBuilder {

	public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
		super(localSpringDocParameterNameDiscoverer, ignoredParameterAnnotations);
	}

	@Override
	protected Schema calculateSchemaFromParameterizedType(Components components, Type paramType, JsonView jsonView) {
		Schema schemaN;
		ParameterizedType parameterizedType = (ParameterizedType) paramType;
		if (Mono.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())
				|| Flux.class.getName().contentEquals(parameterizedType.getRawType().getTypeName()))
			schemaN = SpringDocAnnotationsUtils.extractSchema(components, parameterizedType.getActualTypeArguments()[0],
					jsonView);
		else
			schemaN = SpringDocAnnotationsUtils.extractSchema(components, paramType, jsonView);
		return schemaN;
	}

	public boolean isFile(ParameterizedType parameterizedType) {
		Type type = parameterizedType.getActualTypeArguments()[0];
		if (MultipartFile.class.getName().equals(type.getTypeName())
				|| FilePart.class.getName().equals(type.getTypeName())) {
			return true;
		}
		else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Type[] upperBounds = wildcardType.getUpperBounds();
			return MultipartFile.class.getName().equals(upperBounds[0].getTypeName());
		}
		return false;
	}

	public boolean isFile(JavaType ct) {
		return MultipartFile.class.isAssignableFrom(ct.getRawClass())
				|| FilePart.class.isAssignableFrom(ct.getRawClass());
	}

}
