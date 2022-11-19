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

package org.springdoc.core.providers;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * The type Web conversion service provider.
 * @author bnasslashen
 */
public class WebConversionServiceProvider implements InitializingBean, ApplicationContextAware {

	/**
	 * The constant CONVERTERS.
	 */
	private static final String CONVERTERS = "converters";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebConversionServiceProvider.class);

	/**
	 * The constant SERVLET_APPLICATION_CONTEXT_CLASS.
	 */
	private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

	/**
	 * The constant REACTIVE_APPLICATION_CONTEXT_CLASS.
	 */
	private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

	/**
	 * The Formatting conversion service.
	 */
	private GenericConversionService formattingConversionService;

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() {
		if (isAssignable(SERVLET_APPLICATION_CONTEXT_CLASS, this.applicationContext.getClass())) {
			this.formattingConversionService = applicationContext.getBean("mvcConversionService", FormattingConversionService.class);
		}
		else if (isAssignable(REACTIVE_APPLICATION_CONTEXT_CLASS, this.applicationContext.getClass())) {
			this.formattingConversionService = applicationContext.getBean("webFluxConversionService", FormattingConversionService.class);
		}
		else
			formattingConversionService = new DefaultFormattingConversionService();
	}

	/**
	 * Attempts to convert {@code source} into the target type as described by {@code targetTypeDescriptor}.
	 *
	 * @param source the source
	 * @param targetTypeDescriptor the target type descriptor
	 * @return the converted source
	 */
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor targetTypeDescriptor) {
		return formattingConversionService.convert(source, targetTypeDescriptor);
	}

	/**
	 * Gets spring converted type.
	 *
	 * @param clazz the clazz
	 * @return the spring converted type
	 */
	public Class<?> getSpringConvertedType(Class<?> clazz) {
		Class<?> result = clazz;
		Field convertersField = FieldUtils.getDeclaredField(GenericConversionService.class, CONVERTERS, true);
		if (convertersField != null) {
			Object converters;
			try {
				converters = convertersField.get(formattingConversionService);
				convertersField = FieldUtils.getDeclaredField(converters.getClass(), CONVERTERS, true);
				Map<ConvertiblePair, Object> springConverters = (Map) convertersField.get(converters);
				Optional<ConvertiblePair> convertiblePairOptional = springConverters.keySet().stream().filter(convertiblePair -> convertiblePair.getTargetType().equals(clazz)).findAny();
				if (convertiblePairOptional.isPresent()) {
					ConvertiblePair convertiblePair = convertiblePairOptional.get();
					result = convertiblePair.getSourceType();
				}
			}
			catch (IllegalAccessException e) {
				LOGGER.warn(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Is assignable boolean.
	 *
	 * @param target the target
	 * @param type the type
	 * @return the boolean
	 */
	private boolean isAssignable(String target, Class<?> type) {
		try {
			return ClassUtils.resolveClassName(target, null).isAssignableFrom(type);
		}
		catch (Throwable ex) {
			return false;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
