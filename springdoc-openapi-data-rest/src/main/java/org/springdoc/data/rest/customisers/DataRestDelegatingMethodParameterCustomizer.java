package org.springdoc.data.rest.customisers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
import org.springdoc.core.DelegatingMethodParameter;
import org.springdoc.core.converters.models.Pageable;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
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
	 * The Optional spring data web properties.
	 */
	private final Optional<SpringDataWebProperties> optionalSpringDataWebProperties;

	/**
	 * The Optional repository rest configuration.
	 */
	private final Optional<RepositoryRestConfiguration> optionalRepositoryRestConfiguration;

	/**
	 * Instantiates a new Data rest delegating method parameter customizer.
	 *
	 * @param optionalSpringDataWebProperties the optional spring data web properties
	 * @param optionalRepositoryRestConfiguration the optional repository rest configuration
	 */
	public DataRestDelegatingMethodParameterCustomizer(Optional<SpringDataWebProperties> optionalSpringDataWebProperties, Optional<RepositoryRestConfiguration> optionalRepositoryRestConfiguration) {
		this.optionalSpringDataWebProperties = optionalSpringDataWebProperties;
		this.optionalRepositoryRestConfiguration = optionalRepositoryRestConfiguration;
	}

	@Override
	public void customize(MethodParameter originalParameter, MethodParameter methodParameter) {
		PageableDefault pageableDefault = originalParameter.getParameterAnnotation(PageableDefault.class);
		if (pageableDefault != null || (org.springframework.data.domain.Pageable.class.isAssignableFrom(originalParameter.getParameterType()) && (optionalSpringDataWebProperties.isPresent() || optionalRepositoryRestConfiguration.isPresent()))) {
			Field field = FieldUtils.getDeclaredField(DelegatingMethodParameter.class, "additionalParameterAnnotations", true);
			try {
				Annotation[] parameterAnnotations = (Annotation[]) field.get(methodParameter);
				if (ArrayUtils.isNotEmpty(parameterAnnotations))
					for (int i = 0; i < parameterAnnotations.length; i++) {
						if (Parameter.class.equals(parameterAnnotations[i].annotationType()))
							parameterAnnotations[i] = getNewParameterAnnotationForField(methodParameter.getParameterName(), pageableDefault);
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
	 * @param parameterName the parameter name
	 * @param pageableDefault the pageable default
	 * @return the new parameter annotation for field
	 */
	private Annotation getNewParameterAnnotationForField(String parameterName, PageableDefault pageableDefault) {
		Field field;
		Parameter parameterNew = null;
		try {
			field = Pageable.class.getDeclaredField(parameterName);
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
					return parameter.description();
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
									return getArrayDefaultValue(parameterName, pageableDefault, schema.defaultValue());
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
		}
		catch (NoSuchFieldException e) {
			LOGGER.warn(e.getMessage());
		}
		return parameterNew;
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
				if (optionalRepositoryRestConfiguration.isPresent())
					name = optionalRepositoryRestConfiguration.get().getLimitParamName();
				else if (optionalSpringDataWebProperties.isPresent())
					name = optionalSpringDataWebProperties.get().getPageable().getSizeParameter();
				else
					name = originalName;
				break;
			case "sort":
				if (optionalRepositoryRestConfiguration.isPresent())
					name = optionalRepositoryRestConfiguration.get().getSortParamName();
				else if (optionalSpringDataWebProperties.isPresent())
					name = optionalSpringDataWebProperties.get().getSort().getSortParameter();
				else
					name = originalName;
				break;
			case "page":
				if (optionalRepositoryRestConfiguration.isPresent())
					name = optionalRepositoryRestConfiguration.get().getPageParamName();
				else if (optionalSpringDataWebProperties.isPresent())
					name = optionalSpringDataWebProperties.get().getPageable().getPageParameter();
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
				if (pageableDefault != null)
					defaultValue = String.valueOf(pageableDefault.size());
				else if (optionalRepositoryRestConfiguration.isPresent())
					defaultValue = String.valueOf(optionalRepositoryRestConfiguration.get().getDefaultPageSize());
				else if (optionalSpringDataWebProperties.isPresent())
					defaultValue = String.valueOf(optionalSpringDataWebProperties.get().getPageable().getDefaultPageSize());
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

}
