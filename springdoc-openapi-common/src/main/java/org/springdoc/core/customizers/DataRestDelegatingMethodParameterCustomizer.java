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

package org.springdoc.core.customizers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.DelegatingMethodParameter;
import org.springdoc.core.delegates.ArraySchemaDelegate;
import org.springdoc.core.delegates.ParameterDelegate;
import org.springdoc.core.delegates.SchemaDelegate;
import org.springdoc.core.providers.RepositoryRestConfigurationProvider;
import org.springdoc.core.providers.SpringDataWebPropertiesProvider;

import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableDefault;

/**
 * The type Data rest delegating method parameter customizer.
 */
public class DataRestDelegatingMethodParameterCustomizer implements DelegatingMethodParameterCustomizer {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataRestDelegatingMethodParameterCustomizer.class);


	/**
	 * The Optional spring data web properties provider.
	 */
	private final Optional<SpringDataWebPropertiesProvider> optionalSpringDataWebPropertiesProvider;


	/**
	 * The Optional repository rest configuration provider.
	 */
	private final Optional<RepositoryRestConfigurationProvider> optionalRepositoryRestConfigurationProvider;


	/**
	 * Instantiates a new Data rest delegating method parameter customizer.
	 *
	 * @param optionalSpringDataWebPropertiesProvider the optional spring data web properties provider
	 * @param optionalRepositoryRestConfigurationProvider the optional repository rest configuration provider
	 */
	public DataRestDelegatingMethodParameterCustomizer(Optional<SpringDataWebPropertiesProvider> optionalSpringDataWebPropertiesProvider, Optional<RepositoryRestConfigurationProvider> optionalRepositoryRestConfigurationProvider) {
		this.optionalSpringDataWebPropertiesProvider = optionalSpringDataWebPropertiesProvider;
		this.optionalRepositoryRestConfigurationProvider = optionalRepositoryRestConfigurationProvider;
	}

	@Override
	public void customize(MethodParameter originalParameter, MethodParameter methodParameter) {
		PageableDefault pageableDefault = originalParameter.getParameterAnnotation(PageableDefault.class);
		if (pageableDefault != null || (org.springframework.data.domain.Pageable.class.isAssignableFrom(originalParameter.getParameterType()) && (isSpringDataWebPropertiesPresent() || isRepositoryRestConfigurationPresent()))) {
			Field field = FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations", true);
			try {
				Annotation[] parameterAnnotations = (Annotation[]) field.get(methodParameter);
				if (ArrayUtils.isNotEmpty(parameterAnnotations))
					for (int i = 0; i < parameterAnnotations.length; i++) {
						if (Parameter.class.equals(parameterAnnotations[i].annotationType())) {
							Optional<Annotation> annotationForField = getNewParameterAnnotationForField(methodParameter, pageableDefault);
							if (annotationForField.isPresent())
								parameterAnnotations[i] = annotationForField.get();
						}
					}
			}
			catch (IllegalAccessException e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}

	/**
	 * Gets new parameter annotation for field.
	 *
	 * @param methodParameter the method parameter
	 * @param pageableDefault the pageable default
	 * @return the new parameter annotation for field
	 */
	private Optional<Annotation> getNewParameterAnnotationForField(MethodParameter methodParameter, PageableDefault pageableDefault) {
		String parameterName = methodParameter.getParameterName();
		Field field;
		Parameter parameterNew;
		try {
			field = methodParameter.getContainingClass().getDeclaredField(parameterName);
			Parameter parameter = field.getAnnotation(Parameter.class);
			parameterNew = new ParameterDelegate(parameter) {
				@Override
				public String name() {
					return getName(parameterName, parameter.name());
				}

				@Override
				public Schema schema() {
					return new SchemaDelegate(parameter.schema()) {

						@Override
						public boolean readOnly() {
							return AccessMode.READ_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public boolean writeOnly() {
							return AccessMode.WRITE_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public String defaultValue() {
							return getDefaultValue(parameterName, pageableDefault, parameter.schema().defaultValue());
						}
					};
				}

				@Override
				public ArraySchema array() {
					ArraySchema arraySchema = parameter.array();
					return new ArraySchemaDelegate(arraySchema) {
						@Override
						public Schema arraySchema() {
							Schema schema = arraySchema.arraySchema();
							return new SchemaDelegate(schema) {

								@Override
								public boolean readOnly() {
									return AccessMode.READ_ONLY.equals(schema.accessMode());
								}

								@Override
								public boolean writeOnly() {
									return AccessMode.WRITE_ONLY.equals(schema.accessMode());
								}

								@Override
								public String defaultValue() {
									return getArrayDefaultValue(parameterName, pageableDefault, schema.defaultValue());
								}
							};
						}
					};
				}
			};
			return Optional.of(parameterNew);
		}
		catch (NoSuchFieldException e) {
			LOGGER.warn(e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Gets name.
	 *
	 * @param parameterName the parameter name
	 * @param originalName the original name
	 * @return the name
	 */
	private String getName(String parameterName, String originalName) {
		String name = null;
		switch (parameterName) {
			case "size":
				if (isRepositoryRestConfigurationPresent())
					name = optionalRepositoryRestConfigurationProvider.get().getRepositoryRestConfiguration().getLimitParamName();
				else if (isSpringDataWebPropertiesPresent())
					name = optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getSizeParameter();
				else
					name = originalName;
				break;
			case "sort":
				if (isRepositoryRestConfigurationPresent())
					name = optionalRepositoryRestConfigurationProvider.get().getRepositoryRestConfiguration().getSortParamName();
				else if (isSpringDataWebPropertiesPresent())
					name = optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getSort().getSortParameter();
				else
					name = originalName;
				break;
			case "page":
				if (isRepositoryRestConfigurationPresent())
					name = optionalRepositoryRestConfigurationProvider.get().getRepositoryRestConfiguration().getPageParamName();
				else if (isSpringDataWebPropertiesPresent())
					name = optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getPageParameter();
				else
					name = originalName;
				break;
			case "direction":
				name = originalName;
				break;
			default:
				// Do nothing here
				break;
		}
		return name;
	}

	/**
	 * Gets default value.
	 *
	 * @param parameterName the parameter name
	 * @param pageableDefault the pageable default
	 * @param defaultSchemaVal the default schema val
	 * @return the default value
	 */
	private String getDefaultValue(String parameterName, PageableDefault pageableDefault, String defaultSchemaVal) {
		String defaultValue = null;
		switch (parameterName) {
			case "size":
				if (pageableDefault != null) {
					// "size" is aliased as "value"
					int size = pageableDefault.size();
					Object defaultSize;
					try {
						defaultSize = PageableDefault.class.getMethod("size").getDefaultValue();
					} catch (NoSuchMethodException e) {
						LOGGER.warn(e.getMessage());
						defaultSize = null;
					}
					if (Objects.deepEquals(size, defaultSize)) {
						size = pageableDefault.value();
					}
					defaultValue = String.valueOf(size);
				}
				else if (isRepositoryRestConfigurationPresent())
					defaultValue = String.valueOf(optionalRepositoryRestConfigurationProvider.get().getRepositoryRestConfiguration().getDefaultPageSize());
				else if (isSpringDataWebPropertiesPresent())
					defaultValue = String.valueOf(optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getDefaultPageSize());
				else
					defaultValue = defaultSchemaVal;
				break;
			case "page":
				if (pageableDefault != null)
					defaultValue = String.valueOf(pageableDefault.page());
				else
					defaultValue = defaultSchemaVal;
				break;
			default:
				// Do nothing here
				break;
		}
		return defaultValue;
	}

	/**
	 * Gets default value.
	 *
	 * @param parameterName the parameter name
	 * @param pageableDefault the pageable default
	 * @param defaultSchemaVal the default schema val
	 * @return the default value
	 */
	private String getArrayDefaultValue(String parameterName, PageableDefault pageableDefault, String defaultSchemaVal) {
		String defaultValue = defaultSchemaVal;
		if ("sort".equals(parameterName) && pageableDefault != null && ArrayUtils.isNotEmpty(pageableDefault.sort())) {
			List<String> sortValues = new ArrayList<>();
			for (String sortValue : pageableDefault.sort()) {
				String sortStr = String.join(",", sortValue, pageableDefault.direction().name());
				sortValues.add(sortStr);
			}
			try {
				defaultValue = ObjectMapperFactory.buildStrictGenericObjectMapper().writeValueAsString(sortValues);
			}
			catch (JsonProcessingException e) {
				LOGGER.warn(e.getMessage());
			}
		}
		return defaultValue;
	}

	/**
	 * Spring data web properties is present boolean.
	 *
	 * @return the boolean
	 */
	private boolean isSpringDataWebPropertiesPresent() {
		return optionalSpringDataWebPropertiesProvider.isPresent() && optionalSpringDataWebPropertiesProvider.get().isSpringDataWebPropertiesPresent();
	}

	/**
	 * Repository rest configuration is present boolean.
	 *
	 * @return the boolean
	 */
	private boolean isRepositoryRestConfigurationPresent() {
		return optionalRepositoryRestConfigurationProvider.isPresent() && optionalRepositoryRestConfigurationProvider.get().isRepositoryRestConfigurationPresent();
	}
}
