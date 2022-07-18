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

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestPart;

import java.lang.reflect.*;
import java.util.*;

/**
 * The type Delegating method parameter utils.
 * @author NaccOll
 */
public class DelegatingMethodParameterProvider {
	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingMethodParameterProvider.class);

	private final boolean defaultParameterObject;

	public DelegatingMethodParameterProvider(boolean defaultParameterObject){
		this.defaultParameterObject = defaultParameterObject;
	}
	/**
	 * Customize method parameter [ ].
	 *
	 * @param pNames the p names
	 * @param parameters the parameters
	 * @param optionalDelegatingMethodParameterCustomizer the optional delegating method parameter customizer
	 * @return the method parameter [ ]
	 */
	public  MethodParameter[] customize(String[] pNames, MethodParameter[] parameters, Optional<DelegatingMethodParameterCustomizer> optionalDelegatingMethodParameterCustomizer) {
		List<MethodParameter> explodedParameters = new ArrayList<>();
		for (int i = 0; i < parameters.length; ++i) {
			MethodParameter p = parameters[i];
			Class<?> paramClass = AdditionalModelsConverter.getParameterObjectReplacement(p.getParameterType());

			if (!MethodParameterPojoExtractor.isSimpleType(paramClass) && (p.hasParameterAnnotation(ParameterObject.class) || AnnotatedElementUtils.isAnnotated(paramClass, ParameterObject.class))) {
				MethodParameterPojoExtractor.extractFrom(paramClass).forEach(methodParameter -> {
					optionalDelegatingMethodParameterCustomizer.ifPresent(customizer -> customizer.customize(p, methodParameter));
					explodedParameters.add(methodParameter);
				});
			}
			else if (defaultParameterObject) {
				boolean isSimpleType = MethodParameterPojoExtractor.isSimpleType(paramClass);
				boolean isRequestBody = p.hasParameterAnnotation(RequestBody.class) || p.hasParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class);
				boolean isRequestPart = p.hasParameterAnnotation(RequestPart.class);
				if(!isSimpleType && !(isRequestBody || isRequestPart)) {
					MethodParameterPojoExtractor.extractFrom(paramClass).forEach(methodParameter -> {
						optionalDelegatingMethodParameterCustomizer
								.ifPresent(customizer -> customizer.customize(p, methodParameter));
						explodedParameters.add(methodParameter);
					});
				}
			}
			else {
				String name = pNames != null ? pNames[i] : p.getParameterName();
				explodedParameters.add(new DelegatingMethodParameter(p, name, null, false, false));
			}
		}
		return explodedParameters.toArray(new MethodParameter[0]);
	}

	/**
	 * Return a variant of this {@code MethodParameter} which refers to the
	 * given containing class.
	 * @param methodParameter the method parameter
	 * @param containingClass a specific containing class (potentially a subclass of the declaring class, e.g. substituting a type variable) A copy of spring withContainingClass, to keep compatibility with older spring versions
	 * @return the method parameter
	 * @see DelegatingMethodParameter#getParameterType()
	 */
	public static MethodParameter changeContainingClass(MethodParameter methodParameter, @Nullable Class<?> containingClass) {
		MethodParameter result = methodParameter.clone();
		try {
			Field containingClassField = FieldUtils.getDeclaredField(result.getClass(), "containingClass", true);
			containingClassField.set(result, containingClass);
			Field parameterTypeField = FieldUtils.getDeclaredField(result.getClass(), "parameterType", true);
			parameterTypeField.set(result, null);
		}
		catch (IllegalAccessException e) {
			LOGGER.warn(e.getMessage());
		}
		return result;
	}

}
