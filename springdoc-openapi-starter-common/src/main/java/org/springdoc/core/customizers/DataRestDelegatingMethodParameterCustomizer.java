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
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.extractor.DelegatingMethodParameter;
import org.springdoc.core.providers.RepositoryRestConfigurationProvider;
import org.springdoc.core.providers.SpringDataWebPropertiesProvider;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;

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
		SortDefault sortDefault = originalParameter.getParameterAnnotation(SortDefault.class);
		Class<?> parameterType = originalParameter.getParameterType();
		if (pageableDefault != null || sortDefault != null || (
				(Pageable.class.isAssignableFrom(parameterType) || Sort.class.isAssignableFrom(parameterType))
						&& (isSpringDataWebPropertiesPresent() || isRepositoryRestConfigurationPresent())
		)) {
			Field field = FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations", true);
			try {
				Annotation[] parameterAnnotations = (Annotation[]) field.get(methodParameter);
				if (ArrayUtils.isNotEmpty(parameterAnnotations))
					for (int i = 0; i < parameterAnnotations.length; i++) {
						if (Parameter.class.equals(parameterAnnotations[i].annotationType())) {
							Optional<Annotation> annotationForField = getNewParameterAnnotationForField(methodParameter, pageableDefault, sortDefault);
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
	private Optional<Annotation> getNewParameterAnnotationForField(MethodParameter methodParameter, PageableDefault pageableDefault, SortDefault sortDefault) {
		String parameterName = methodParameter.getParameterName();
		Field field;
		Parameter parameterNew;
		try {
			field = methodParameter.getContainingClass().getDeclaredField(parameterName);
			Parameter parameter = field.getAnnotation(Parameter.class);
			parameterNew = new Parameter() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return parameter.annotationType();
				}

				@Override
				public String name() {
					return getName(parameterName, parameter.name());
				}

				@Override
				public ParameterIn in() {
					return parameter.in();
				}

				@Override
				public String description() {
					return getDescription(parameterName, parameter.description());
				}

				@Override
				public boolean required() {
					return parameter.required();
				}

				@Override
				public boolean deprecated() {
					return parameter.deprecated();
				}

				@Override
				public boolean allowEmptyValue() {
					return parameter.allowEmptyValue();
				}

				@Override
				public ParameterStyle style() {
					return parameter.style();
				}

				@Override
				public Explode explode() {
					return parameter.explode();
				}

				@Override
				public boolean allowReserved() {
					return parameter.allowReserved();
				}

				@Override
				public Schema schema() {
					return new Schema() {

						@Override
						public Class<? extends Annotation> annotationType() {
							return parameter.schema().annotationType();
						}

						@Override
						public Class<?> implementation() {
							return parameter.schema().implementation();
						}

						@Override
						public Class<?> not() {
							return parameter.schema().not();
						}

						@Override
						public Class<?>[] oneOf() {
							return parameter.schema().oneOf();
						}

						@Override
						public Class<?>[] anyOf() {
							return parameter.schema().anyOf();
						}

						@Override
						public Class<?>[] allOf() {
							return parameter.schema().allOf();
						}

						@Override
						public String name() {
							return parameter.schema().name();
						}

						@Override
						public String title() {
							return parameter.schema().title();
						}

						@Override
						public double multipleOf() {
							return parameter.schema().multipleOf();
						}

						@Override
						public String maximum() {
							return parameter.schema().maximum();
						}

						@Override
						public boolean exclusiveMaximum() {
							return parameter.schema().exclusiveMaximum();
						}

						@Override
						public String minimum() {
							return parameter.schema().minimum();
						}

						@Override
						public boolean exclusiveMinimum() {
							return parameter.schema().exclusiveMaximum();
						}

						@Override
						public int maxLength() {
							return parameter.schema().maxLength();
						}

						@Override
						public int minLength() {
							return parameter.schema().minLength();
						}

						@Override
						public String pattern() {
							return parameter.schema().pattern();
						}

						@Override
						public int maxProperties() {
							return parameter.schema().maxProperties();
						}

						@Override
						public int minProperties() {
							return parameter.schema().minProperties();
						}

						@Override
						public String[] requiredProperties() {
							return parameter.schema().requiredProperties();
						}

						@Override
						public boolean required() {
							return parameter.schema().required();
						}

						@Override
						public String description() {
							return parameter.schema().description();
						}

						@Override
						public String format() {
							return parameter.schema().format();
						}

						@Override
						public String ref() {
							return parameter.schema().ref();
						}

						@Override
						public boolean nullable() {
							return parameter.schema().nullable();
						}

						@Override
						public boolean readOnly() {
							return AccessMode.READ_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public boolean writeOnly() {
							return AccessMode.WRITE_ONLY.equals(parameter.schema().accessMode());
						}

						@Override
						public AccessMode accessMode() {
							return parameter.schema().accessMode();
						}

						@Override
						public String example() {
							return parameter.schema().example();
						}

						@Override
						public ExternalDocumentation externalDocs() {
							return parameter.schema().externalDocs();
						}

						@Override
						public boolean deprecated() {
							return parameter.schema().deprecated();
						}

						@Override
						public String type() {
							return parameter.schema().type();
						}

						@Override
						public String[] allowableValues() {
							return parameter.schema().allowableValues();
						}

						@Override
						public String defaultValue() {
							return getDefaultValue(parameterName, pageableDefault, parameter.schema().defaultValue());
						}

						@Override
						public String discriminatorProperty() {
							return parameter.schema().discriminatorProperty();
						}

						@Override
						public DiscriminatorMapping[] discriminatorMapping() {
							return parameter.schema().discriminatorMapping();
						}

						@Override
						public boolean hidden() {
							return parameter.schema().hidden();
						}

						@Override
						public boolean enumAsRef() {
							return parameter.schema().enumAsRef();
						}

						@Override
						public Class<?>[] subTypes() {
							return parameter.schema().subTypes();
						}

						@Override
						public Extension[] extensions() {
							return parameter.schema().extensions();
						}

						@Override
						public AdditionalPropertiesValue additionalProperties() {
							return parameter.schema().additionalProperties();
						}
					};
				}

				@Override
				public ArraySchema array() {
					ArraySchema arraySchema = parameter.array();
					return new ArraySchema() {
						@Override
						public Class<? extends Annotation> annotationType() {
							return arraySchema.annotationType();
						}

						@Override
						public Schema schema() {
							return arraySchema.schema();
						}

						@Override
						public Schema arraySchema() {
							Schema schema = arraySchema.arraySchema();
							return new Schema() {

								@Override
								public Class<? extends Annotation> annotationType() {
									return schema.annotationType();
								}

								@Override
								public Class<?> implementation() {
									return schema.implementation();
								}

								@Override
								public Class<?> not() {
									return schema.not();
								}

								@Override
								public Class<?>[] oneOf() {
									return schema.oneOf();
								}

								@Override
								public Class<?>[] anyOf() {
									return schema.anyOf();
								}

								@Override
								public Class<?>[] allOf() {
									return schema.allOf();
								}

								@Override
								public String name() {
									return schema.name();
								}

								@Override
								public String title() {
									return schema.title();
								}

								@Override
								public double multipleOf() {
									return schema.multipleOf();
								}

								@Override
								public String maximum() {
									return schema.maximum();
								}

								@Override
								public boolean exclusiveMaximum() {
									return schema.exclusiveMaximum();
								}

								@Override
								public String minimum() {
									return schema.minimum();
								}

								@Override
								public boolean exclusiveMinimum() {
									return schema.exclusiveMinimum();
								}

								@Override
								public int maxLength() {
									return schema.maxLength();
								}

								@Override
								public int minLength() {
									return schema.minLength();
								}

								@Override
								public String pattern() {
									return schema.pattern();
								}

								@Override
								public int maxProperties() {
									return schema.maxProperties();
								}

								@Override
								public int minProperties() {
									return schema.minProperties();
								}

								@Override
								public String[] requiredProperties() {
									return schema.requiredProperties();
								}

								@Override
								public boolean required() {
									return schema.required();
								}

								@Override
								public String description() {
									return schema.description();
								}

								@Override
								public String format() {
									return schema.format();
								}

								@Override
								public String ref() {
									return schema.ref();
								}

								@Override
								public boolean nullable() {
									return schema.nullable();
								}

								@Override
								public boolean readOnly() {
									return AccessMode.READ_ONLY.equals(schema.accessMode());
								}

								@Override
								public boolean writeOnly() {
									return AccessMode.WRITE_ONLY.equals(schema.accessMode());
								}

								@Override
								public AccessMode accessMode() {
									return schema.accessMode();
								}

								@Override
								public String example() {
									return schema.example();
								}

								@Override
								public ExternalDocumentation externalDocs() {
									return schema.externalDocs();
								}

								@Override
								public boolean deprecated() {
									return schema.deprecated();
								}

								@Override
								public String type() {
									return schema.type();
								}

								@Override
								public String[] allowableValues() {
									return schema.allowableValues();
								}

								@Override
								public String defaultValue() {
									return getArrayDefaultValue(parameterName, pageableDefault, sortDefault, schema.defaultValue());

								}

								@Override
								public String discriminatorProperty() {
									return schema.discriminatorProperty();
								}

								@Override
								public DiscriminatorMapping[] discriminatorMapping() {
									return schema.discriminatorMapping();
								}

								@Override
								public boolean hidden() {
									return schema.hidden();
								}

								@Override
								public boolean enumAsRef() {
									return schema.enumAsRef();
								}

								@Override
								public Class<?>[] subTypes() {
									return schema.subTypes();
								}

								@Override
								public Extension[] extensions() {
									return schema.extensions();
								}

								@Override
								public AdditionalPropertiesValue additionalProperties() {
									return schema.additionalProperties();
								}
							};
						}

						@Override
						public int maxItems() {
							return arraySchema.maxItems();
						}

						@Override
						public int minItems() {
							return arraySchema.minItems();
						}

						@Override
						public boolean uniqueItems() {
							return arraySchema.uniqueItems();
						}

						@Override
						public Extension[] extensions() {
							return arraySchema.extensions();
						}
					};
				}

				@Override
				public Content[] content() {
					return parameter.content();
				}

				@Override
				public boolean hidden() {
					return parameter.hidden();
				}

				@Override
				public ExampleObject[] examples() {
					return parameter.examples();
				}

				@Override
				public String example() {
					return parameter.example();
				}

				@Override
				public Extension[] extensions() {
					return parameter.extensions();
				}

				@Override
				public String ref() {
					return parameter.ref();
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
					name = optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getPrefix() +
							optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getSizeParameter();
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
					name = optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getPrefix() +
							optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().getPageParameter();
				else
					name = originalName;
				break;
			case "direction":
			case "caseSensitive":
				name = originalName;
				break;
			default:
				// Do nothing here
				break;
		}
		return name;
	}

	/**
	 * Gets description.
	 *
	 * @param parameterName the parameter name
	 * @param originalDescription the original description
	 * @return the description
	 */
	private String getDescription(String parameterName, String originalDescription) {
		if ("page".equals(parameterName) && isSpringDataWebPropertiesPresent() &&
				optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().isOneIndexedParameters())
			return "One-based page index (1..N)";
		return originalDescription;
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
				else if (isSpringDataWebPropertiesPresent() && optionalSpringDataWebPropertiesProvider.get().getSpringDataWebProperties().getPageable().isOneIndexedParameters())
					defaultValue = "1";
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
	 * @param sortDefault the sort default
	 * @param defaultSchemaVal the default schema val
	 * @return the default value
	 */
	private String getArrayDefaultValue(String parameterName, PageableDefault pageableDefault, SortDefault sortDefault, String defaultSchemaVal) {
		String defaultValue = defaultSchemaVal;
		if ("sort".equals(parameterName)) {
			DefaultSort defaultSort = getDefaultSort(pageableDefault, sortDefault);
			if (defaultSort != null && ArrayUtils.isNotEmpty(defaultSort.properties)) {
				List<String> sortValues = new ArrayList<>();
				for (String sortValue : defaultSort.properties) {
					String sortStr = String.join(",", sortValue, defaultSort.direction.name());
					sortValues.add(sortStr);
				}
				try {
					defaultValue = ObjectMapperFactory.buildStrictGenericObjectMapper().writeValueAsString(sortValues);
				} catch (JsonProcessingException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		}
		return defaultValue;
	}
	/**
	 * Gets default sort.
	 *
	 * @param pageableDefault the pageable default
	 * @param sortDefault the sort default
	 * @return the default sort
	 */
	private DefaultSort getDefaultSort(PageableDefault pageableDefault, SortDefault sortDefault) {
		if (sortDefault != null) {
			// "sort" is aliased as "value"
			String[] sortProperties = sortDefault.sort();
			Object defaultSort;
			try {
				defaultSort = SortDefault.class.getMethod("sort").getDefaultValue();
			} catch (NoSuchMethodException e) {
				LOGGER.warn(e.getMessage());
				defaultSort = null;
			}
			if (!Objects.deepEquals(sortProperties, defaultSort)) {
				return new DefaultSort(sortDefault.direction(), sortProperties);
			}
			sortProperties = sortDefault.value();
			try {
				defaultSort = SortDefault.class.getMethod("value").getDefaultValue();
			} catch (NoSuchMethodException e) {
				LOGGER.warn(e.getMessage());
				defaultSort = null;
			}
			if (!Objects.deepEquals(sortProperties, defaultSort)) {
				return new DefaultSort(sortDefault.direction(), sortProperties);
			}
		}
		// @SortDefault has higher priority than @PageableDefault
		if (pageableDefault != null) {
			return new DefaultSort(pageableDefault.direction(), pageableDefault.sort());
		}
		return null;
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

	private static class DefaultSort {
		private final Sort.Direction direction;
		private final String[] properties;

		DefaultSort(Sort.Direction direction, String... properties) {
			this.direction = direction;
			this.properties = properties;
		}
	}
}