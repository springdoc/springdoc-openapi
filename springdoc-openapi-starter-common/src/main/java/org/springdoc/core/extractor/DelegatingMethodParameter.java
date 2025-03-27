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
package org.springdoc.core.extractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springdoc.core.service.AbstractRequestService;

import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

/**
 * The type Delegating method parameter.
 *
 * @author zarebski.m
 */
public class DelegatingMethodParameter extends MethodParameter {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingMethodParameter.class);

	/**
	 * The Delegate.
	 */
	private final MethodParameter delegate;

	/**
	 * The Additional parameter annotations.
	 */
	private final Annotation[] additionalParameterAnnotations;

	/**
	 * The Parameter name.
	 */
	private final String parameterName;

	/**
	 * The Is parameter object.
	 */
	private final boolean isParameterObject;

	/**
	 * If Is parameter object. then The Field should be not null
	 */
	private final Field field;

	/**
	 * The Method annotations.
	 */
	private final Annotation[] methodAnnotations;

	/**
	 * The Is not required.
	 */
	private boolean isNotRequired;

	/**
	 * Instantiates a new Delegating method parameter.
	 *
	 * @param delegate                       the delegate
	 * @param parameterName                  the parameter name
	 * @param additionalParameterAnnotations the additional parameter annotations
	 * @param methodAnnotations              the method annotations
	 * @param isParameterObject              the is parameter object
	 * @param isNotRequired                  the is required
	 */
	DelegatingMethodParameter(MethodParameter delegate, String parameterName, Annotation[] additionalParameterAnnotations, Annotation[] methodAnnotations, boolean isParameterObject, Field field, boolean isNotRequired) {
		super(delegate);
		this.delegate = delegate;
		this.field = field;
		this.additionalParameterAnnotations = additionalParameterAnnotations;
		this.parameterName = parameterName;
		this.isParameterObject = isParameterObject;
		this.isNotRequired = isNotRequired;
		this.methodAnnotations =methodAnnotations;
	}

	/**
	 * Customize method parameter [ ].
	 *
	 * @param pNames                                       the p names
	 * @param parameters                                   the parameters
	 * @param optionalDelegatingMethodParameterCustomizers the optional list delegating method parameter customizer
	 * @param defaultFlatParamObject                       the default flat param object
	 * @return the method parameter [ ]
	 */
	public static MethodParameter[] customize(String[] pNames, MethodParameter[] parameters,
			Optional<List<DelegatingMethodParameterCustomizer>> optionalDelegatingMethodParameterCustomizers, boolean defaultFlatParamObject) {
		List<MethodParameter> explodedParameters = new ArrayList<>();
		for (int i = 0; i < parameters.length; ++i) {
			MethodParameter p = parameters[i];
			Class<?> paramClass = AdditionalModelsConverter.getParameterObjectReplacement(p.getParameterType());

			boolean hasFlatAnnotation = p.hasParameterAnnotation(ParameterObject.class) || AnnotatedElementUtils.isAnnotated(paramClass, ParameterObject.class);
			boolean hasNotFlatAnnotation = Arrays.stream(p.getParameterAnnotations())
					.anyMatch(annotation -> Arrays.asList(RequestBody.class, RequestPart.class).contains(annotation.annotationType()));
			if (!MethodParameterPojoExtractor.isSimpleType(paramClass)
					&& (hasFlatAnnotation || (defaultFlatParamObject && !hasNotFlatAnnotation && !AbstractRequestService.isRequestTypeToIgnore(paramClass)))) {
				List<MethodParameter> flatParams = new CopyOnWriteArrayList<>();
				MethodParameterPojoExtractor.extractFrom(paramClass).forEach(flatParams::add);
				optionalDelegatingMethodParameterCustomizers.orElseGet(ArrayList::new).forEach(cz -> cz.customizeList(p, flatParams));
				explodedParameters.addAll(flatParams);
			}
			else {
				String name = pNames != null ? pNames[i] : p.getParameterName();
				explodedParameters.add(new DelegatingMethodParameter(p, name, null, null, false, null, false));
			}
		}
		return explodedParameters.toArray(new MethodParameter[0]);
	}

	/**
	 * Return a variant of this {@code MethodParameter} which refers to the
	 * given containing class.
	 *
	 * @param methodParameter the method parameter
	 * @param containingClass a specific containing class (potentially a subclass of the declaring class, e.g. substituting a type variable) A copy of spring withContainingClass, to keep compatibility with older spring versions
	 * @return the method parameter
	 * @see #getParameterType() #getParameterType()#getParameterType()
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

	@Override
	@NonNull
	public Annotation[] getParameterAnnotations() {
		Annotation[] allMethodAnnotations = ArrayUtils.addAll(delegate.getParameterAnnotations(), this.methodAnnotations);
		return ArrayUtils.addAll(allMethodAnnotations, additionalParameterAnnotations);
	}

	@Override
	public String getParameterName() {
		return parameterName;
	}

	@Override
	public Method getMethod() {
		return delegate.getMethod();
	}

	@Override
	public Constructor<?> getConstructor() {
		return delegate.getConstructor();
	}

	@Override
	public Class<?> getDeclaringClass() {
		return delegate.getDeclaringClass();
	}

	@Override
	public Member getMember() {
		return delegate.getMember();
	}

	@Override
	public AnnotatedElement getAnnotatedElement() {
		return delegate.getAnnotatedElement();
	}

	@Override
	public Executable getExecutable() {
		return delegate.getExecutable();
	}

	@Override
	public MethodParameter withContainingClass(Class<?> containingClass) {
		return delegate.withContainingClass(containingClass);
	}

	@Override
	public Class<?> getContainingClass() {
		return delegate.getContainingClass();
	}

	@Override
	public Class<?> getParameterType() {
		return delegate.getParameterType();
	}

	@Override
	public Type getGenericParameterType() {
		return delegate.getGenericParameterType();
	}

	@Override
	public Class<?> getNestedParameterType() {
		return delegate.getNestedParameterType();
	}

	@Override
	public Type getNestedGenericParameterType() {
		return delegate.getNestedGenericParameterType();
	}

	@Override
	public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
		delegate.initParameterNameDiscovery(parameterNameDiscoverer);
	}

	/**
	 * Is not required boolean.
	 *
	 * @return the boolean
	 */
	public boolean isNotRequired() {
		return isNotRequired;
	}

	/**
	 * Sets not required.
	 *
	 * @param notRequired the not required
	 */
	public void setNotRequired(boolean notRequired) {
		isNotRequired = notRequired;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		DelegatingMethodParameter that = (DelegatingMethodParameter) o;
		return Objects.equals(delegate, that.delegate) &&
				Arrays.equals(additionalParameterAnnotations, that.additionalParameterAnnotations) &&
				Objects.equals(parameterName, that.parameterName);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(super.hashCode(), delegate, parameterName);
		result = 31 * result + Arrays.hashCode(additionalParameterAnnotations);
		return result;
	}

	/**
	 * Is parameter object boolean.
	 *
	 * @return the boolean
	 */
	public boolean isParameterObject() {
		return isParameterObject;
	}

	/**
	 * Gets field. If Is parameter object. then The Field should be not null
	 * @return the field
	 * @see #isParameterObject
	 */
	@Nullable
	public Field getField() {
		return field;
	}
}
