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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DependentRequired;
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

						private Schema parameterSchema = parameter.schema();

						@Override
						public Class<? extends Annotation> annotationType() {
							return parameterSchema.annotationType();
						}

						@Override
						public Class<?> implementation() {
							return parameterSchema.implementation();
						}

						@Override
						public Class<?> not() {
							return parameterSchema.not();
						}

						@Override
						public Class<?>[] oneOf() {
							return parameterSchema.oneOf();
						}

						@Override
						public Class<?>[] anyOf() {
							return parameterSchema.anyOf();
						}

						@Override
						public Class<?>[] allOf() {
							return parameterSchema.allOf();
						}

						@Override
						public String name() {
							return parameterSchema.name();
						}

						@Override
						public String title() {
							return parameterSchema.title();
						}

						@Override
						public double multipleOf() {
							return parameterSchema.multipleOf();
						}

						@Override
						public String maximum() {
							return parameterSchema.maximum();
						}

						@Override
						public boolean exclusiveMaximum() {
							return parameterSchema.exclusiveMaximum();
						}

						@Override
						public String minimum() {
							return parameterSchema.minimum();
						}

						@Override
						public boolean exclusiveMinimum() {
							return parameterSchema.exclusiveMaximum();
						}

						@Override
						public int maxLength() {
							return parameterSchema.maxLength();
						}

						@Override
						public int minLength() {
							return parameterSchema.minLength();
						}

						@Override
						public String pattern() {
							return parameterSchema.pattern();
						}

						@Override
						public int maxProperties() {
							return parameterSchema.maxProperties();
						}

						@Override
						public int minProperties() {
							return parameterSchema.minProperties();
						}

						@Override
						public String[] requiredProperties() {
							return parameterSchema.requiredProperties();
						}

						@Override
						public boolean required() {
							return parameterSchema.required();
						}

						@Override
						public RequiredMode requiredMode() {
							return parameterSchema.requiredMode();
						}

						@Override
						public String description() {
							return parameterSchema.description();
						}

						@Override
						public String format() {
							return parameterSchema.format();
						}

						@Override
						public String ref() {
							return parameterSchema.ref();
						}

						@Override
						public boolean nullable() {
							return parameterSchema.nullable();
						}

						@Override
						public boolean readOnly() {
							return AccessMode.READ_ONLY.equals(parameterSchema.accessMode());
						}

						@Override
						public boolean writeOnly() {
							return AccessMode.WRITE_ONLY.equals(parameterSchema.accessMode());
						}

						@Override
						public AccessMode accessMode() {
							return parameterSchema.accessMode();
						}

						@Override
						public String example() {
							return parameterSchema.example();
						}

						@Override
						public ExternalDocumentation externalDocs() {
							return parameterSchema.externalDocs();
						}

						@Override
						public boolean deprecated() {
							return parameterSchema.deprecated();
						}

						@Override
						public String type() {
							return parameterSchema.type();
						}

						@Override
						public String[] allowableValues() {
							return parameterSchema.allowableValues();
						}

						@Override
						public String defaultValue() {
							return getDefaultValue(parameterName, pageableDefault, parameterSchema.defaultValue());
						}

						@Override
						public String discriminatorProperty() {
							return parameterSchema.discriminatorProperty();
						}

						@Override
						public DiscriminatorMapping[] discriminatorMapping() {
							return parameterSchema.discriminatorMapping();
						}

						@Override
						public boolean hidden() {
							return parameterSchema.hidden();
						}

						@Override
						public boolean enumAsRef() {
							return parameterSchema.enumAsRef();
						}

						@Override
						public Class<?>[] subTypes() {
							return parameterSchema.subTypes();
						}

						@Override
						public Extension[] extensions() {
							return parameterSchema.extensions();
						}

						@Override
						public Class<?>[] prefixItems() {
							return parameterSchema.prefixItems();
						}

						@Override
						public String[] types() {
							return parameterSchema.types();
						}

						@Override
						public int exclusiveMaximumValue() {
							return parameterSchema.exclusiveMaximumValue();
						}

						@Override
						public int exclusiveMinimumValue() {
							return parameterSchema.exclusiveMinimumValue();
						}

						@Override
						public Class<?> contains() {
							return parameterSchema.contains();
						}

						@Override
						public String $id() {
							return parameterSchema.$id();
						}

						@Override
						public String $schema() {
							return parameterSchema.$schema();
						}

						@Override
						public String $anchor() {
							return parameterSchema.$anchor();
						}

						@Override
						public String $vocabulary() {
							return parameterSchema.$vocabulary();
						}

						@Override
						public String $dynamicAnchor() {
							return parameterSchema.$dynamicAnchor();
						}

						@Override
						public String contentEncoding() {
							return parameterSchema.contentEncoding();
						}

						@Override
						public String contentMediaType() {
							return parameterSchema.contentMediaType();
						}

						@Override
						public Class<?> contentSchema() {
							return parameterSchema.contentSchema();
						}

						@Override
						public Class<?> propertyNames() {
							return parameterSchema.propertyNames();
						}

						@Override
						public int maxContains() {
							return parameterSchema.maxContains();
						}

						@Override
						public int minContains() {
							return parameterSchema.minContains();
						}

						@Override
						public Class<?> additionalItems() {
							return parameterSchema.additionalItems();
						}

						@Override
						public Class<?> unevaluatedItems() {
							return parameterSchema.unevaluatedItems();
						}

						@Override
						public Class<?> _if() {
							return parameterSchema._if();
						}

						@Override
						public Class<?> _else() {
							return parameterSchema._else();
						}

						@Override
						public Class<?> then() {
							return parameterSchema.then();
						}

						@Override
						public String $comment() {
							return parameterSchema.$comment();
						}

						@Override
						public Class<?>[] exampleClasses() {
							return parameterSchema.exampleClasses();
						}

						@Override
						public AdditionalPropertiesValue additionalProperties() {
							return parameterSchema.additionalProperties();
						}

						@Override
						public DependentRequired[] dependentRequiredMap() {
							return parameterSchema.dependentRequiredMap();
						}

						@Override
						public StringToClassMapItem[] dependentSchemas() {
							return parameterSchema.dependentSchemas();
						}

						@Override
						public StringToClassMapItem[] patternProperties() {
							return parameterSchema.patternProperties();
						}

						@Override
						public StringToClassMapItem[] properties() {
							return parameterSchema.properties();
						}

						@Override
						public Class<?> unevaluatedProperties() {
							return parameterSchema.unevaluatedProperties();
						}

						@Override
						public Class<?> additionalPropertiesSchema() {
							return parameterSchema.additionalPropertiesSchema();
						}

						@Override
						public String[] examples() {
							return parameterSchema.examples();
						}

						@Override
						public String _const() {
							return parameterSchema._const();
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
						public Schema items() {
							return arraySchema.items();
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
								public RequiredMode requiredMode() {
									return schema.requiredMode();
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
								public Class<?>[] prefixItems() {
									return schema.prefixItems();
								}

								@Override
								public String[] types() {
									return schema.types();
								}

								@Override
								public int exclusiveMaximumValue() {
									return schema.exclusiveMaximumValue();
								}

								@Override
								public int exclusiveMinimumValue() {
									return schema.exclusiveMinimumValue();
								}

								@Override
								public Class<?> contains() {
									return schema.contains();
								}

								@Override
								public String $id() {
									return schema.$id();
								}

								@Override
								public String $schema() {
									return schema.$schema();
								}

								@Override
								public String $anchor() {
									return schema.$anchor();
								}

								@Override
								public String $vocabulary() {
									return schema.$vocabulary();
								}

								@Override
								public String $dynamicAnchor() {
									return schema.$dynamicAnchor();
								}

								@Override
								public String contentEncoding() {
									return schema.contentEncoding();
								}

								@Override
								public String contentMediaType() {
									return schema.contentMediaType();
								}

								@Override
								public Class<?> contentSchema() {
									return schema.contentSchema();
								}

								@Override
								public Class<?> propertyNames() {
									return schema.propertyNames();
								}

								@Override
								public int maxContains() {
									return schema.maxContains();
								}

								@Override
								public int minContains() {
									return schema.minContains();
								}

								@Override
								public Class<?> additionalItems() {
									return schema.additionalItems();
								}

								@Override
								public Class<?> unevaluatedItems() {
									return schema.unevaluatedItems();
								}

								@Override
								public Class<?> _if() {
									return schema._if();
								}

								@Override
								public Class<?> _else() {
									return schema._else();
								}

								@Override
								public Class<?> then() {
									return schema.then();
								}

								@Override
								public String $comment() {
									return schema.$comment();
								}

								@Override
								public Class<?>[] exampleClasses() {
									return schema.exampleClasses();
								}

								@Override
								public AdditionalPropertiesValue additionalProperties() {
									return schema.additionalProperties();
								}

								@Override
								public DependentRequired[] dependentRequiredMap() {
									return schema.dependentRequiredMap();
								}

								@Override
								public StringToClassMapItem[] dependentSchemas() {
									return schema.dependentSchemas();
								}

								@Override
								public StringToClassMapItem[] patternProperties() {
									return schema.patternProperties();
								}

								@Override
								public StringToClassMapItem[] properties() {
									return schema.properties();
								}

								@Override
								public Class<?> unevaluatedProperties() {
									return schema.unevaluatedProperties();
								}

								@Override
								public Class<?> additionalPropertiesSchema() {
									return schema.additionalPropertiesSchema();
								}

								@Override
								public String[] examples() {
									return schema.examples();
								}

								@Override
								public String _const() {
									return schema._const();
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

						@Override
						public Schema contains() {
							return arraySchema.contains();
						}

						@Override
						public int maxContains() {
							return arraySchema.maxContains();
						}

						@Override
						public int minContains() {
							return arraySchema.minContains();
						}

						@Override
						public Schema unevaluatedItems() {
							return arraySchema.unevaluatedItems();
						}

						@Override
						public Schema[] prefixItems() {
							return arraySchema.prefixItems();
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
					}
					catch (NoSuchMethodException e) {
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
				final List<String> sortValues = defaultSort.getEffectiveProperties();
				try {
					defaultValue = ObjectMapperFactory.buildStrictGenericObjectMapper().writeValueAsString(sortValues);
				}
				catch (JsonProcessingException e) {
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
			}
			catch (NoSuchMethodException e) {
				LOGGER.warn(e.getMessage());
				defaultSort = null;
			}
			if (!Objects.deepEquals(sortProperties, defaultSort)) {
				return new DefaultSort(sortDefault.direction(), sortProperties);
			}
			sortProperties = sortDefault.value();
			try {
				defaultSort = SortDefault.class.getMethod("value").getDefaultValue();
			}
			catch (NoSuchMethodException e) {
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

		private static final String DIRECTION_GROUP = Arrays.stream(Sort.Direction.values()).map(Enum::name).collect(Collectors.joining("|"));

		private static final String DIRECTED_REGEXP = "\\w+(\\.\\w+)*,\\s*(" + DIRECTION_GROUP + ')';

		private static final Pattern DIRECTED_PATTERN = Pattern.compile(DIRECTED_REGEXP, Pattern.CASE_INSENSITIVE);

		private final Sort.Direction direction;

		private final String[] properties;

		DefaultSort(Sort.Direction direction, String... properties) {
			this.direction = direction;
			this.properties = properties;
		}

		List<String> getEffectiveProperties() {
			return Arrays.stream(properties)
					.map(p -> {
						if (DIRECTED_PATTERN.matcher(p).matches()) {
							return p;
						}
						return p + ',' + direction.name();
					})
					.collect(Collectors.toList());
		}
	}
}
