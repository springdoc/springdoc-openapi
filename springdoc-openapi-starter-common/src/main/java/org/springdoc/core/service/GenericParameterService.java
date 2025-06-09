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

package org.springdoc.core.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.DelegatingMethodParameterCustomizer;
import org.springdoc.core.extractor.DelegatingMethodParameter;
import org.springdoc.core.extractor.MethodParameterPojoExtractor;
import org.springdoc.core.models.ParameterInfo;
import org.springdoc.core.models.RequestBodyInfo;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.WebConversionServiceProvider;
import org.springdoc.core.utils.Constants;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
import org.springdoc.core.utils.SpringDocUtils;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import static org.springdoc.core.utils.Constants.DOT;
import static org.springdoc.core.utils.SpringDocUtils.getParameterAnnotations;
import static org.springdoc.core.utils.SpringDocUtils.handleSchemaTypes;

/**
 * The type Generic parameter builder.
 *
 * @author bnasslahsen, coutin
 */
@SuppressWarnings("rawtypes")
public class GenericParameterService {

	/**
	 * The constant FILE_TYPES.
	 */
	private static final List<Class<?>> FILE_TYPES = Collections.synchronizedList(new ArrayList<>());

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericParameterService.class);

	static {
		FILE_TYPES.add(MultipartFile.class);
		FILE_TYPES.add(Resource.class);
		FILE_TYPES.add(MultipartRequest.class);
	}

	/**
	 * The Optional delegating method parameter customizer.
	 */
	private final Optional<List<DelegatingMethodParameterCustomizer>> optionalDelegatingMethodParameterCustomizers;

	/**
	 * The Web conversion service.
	 */
	private final Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider;

	/**
	 * The Property resolver utils.
	 */
	private final PropertyResolverUtils propertyResolverUtils;

	/**
	 * The Expression context.
	 */
	private final BeanExpressionContext expressionContext;

	/**
	 * The Configurable bean factory.
	 */
	private final ConfigurableBeanFactory configurableBeanFactory;

	/**
	 * The Object mapper provider.
	 */
	private final ObjectMapperProvider objectMapperProvider;

	/**
	 * The javadoc provider.
	 */
	private final Optional<JavadocProvider> javadocProviderOptional;

	/**
	 * Instantiates a new Generic parameter builder.
	 *
	 * @param propertyResolverUtils                        the property resolver utils
	 * @param optionalDelegatingMethodParameterCustomizers the optional list delegating method parameter customizer
	 * @param optionalWebConversionServiceProvider         the optional web conversion service provider
	 * @param objectMapperProvider                         the object mapper provider
	 * @param javadocProviderOptional                      the javadoc provider
	 */
	public GenericParameterService(PropertyResolverUtils propertyResolverUtils, Optional<List<DelegatingMethodParameterCustomizer>> optionalDelegatingMethodParameterCustomizers,
			Optional<WebConversionServiceProvider> optionalWebConversionServiceProvider, ObjectMapperProvider objectMapperProvider, Optional<JavadocProvider> javadocProviderOptional) {
		this.propertyResolverUtils = propertyResolverUtils;
		this.optionalDelegatingMethodParameterCustomizers = optionalDelegatingMethodParameterCustomizers;
		this.optionalWebConversionServiceProvider = optionalWebConversionServiceProvider;
		this.configurableBeanFactory = propertyResolverUtils.getFactory();
		this.expressionContext = (configurableBeanFactory != null ? new BeanExpressionContext(configurableBeanFactory, new RequestScope()) : null);
		this.objectMapperProvider = objectMapperProvider;
		this.javadocProviderOptional = javadocProviderOptional;
	}

	/**
	 * Add file type.
	 *
	 * @param classes the classes
	 */
	public static void addFileType(Class<?>... classes) {
		FILE_TYPES.addAll(Arrays.asList(classes));
	}

	/**
	 * Is file boolean.
	 *
	 * @param type the type
	 * @return the boolean
	 */
	public static boolean isFile(Class type) {
		return FILE_TYPES.stream().anyMatch(clazz -> clazz.isAssignableFrom(type));
	}

	/**
	 * Merge parameter parameter.
	 *
	 * @param existingParamDoc the existing param doc
	 * @param paramCalcul      the param calcul
	 * @return the parameter
	 */
	public static Parameter mergeParameter(List<Parameter> existingParamDoc, Parameter paramCalcul) {
		Parameter result = paramCalcul;
		if (paramCalcul != null && paramCalcul.getName() != null) {
			final String name = paramCalcul.getName();
			final String in = paramCalcul.getIn();
			Parameter paramDoc = existingParamDoc.stream().filter(p ->
							name.equals(p.getName())
									&& (StringUtils.isEmpty(in) || StringUtils.isEmpty(p.getIn()) || in.equals(p.getIn()))
					).findAny()
					.orElse(null);
			if (paramDoc != null) {
				mergeParameter(paramCalcul, paramDoc);
				result = paramDoc;
			}
			else
				existingParamDoc.add(result);
		}
		return result;
	}

	/**
	 * Merge parameter.
	 *
	 * @param paramCalcul the param calcul
	 * @param paramDoc    the param doc
	 */
	public static void mergeParameter(Parameter paramCalcul, Parameter paramDoc) {
		if (StringUtils.isBlank(paramDoc.getDescription()))
			paramDoc.setDescription(paramCalcul.getDescription());

		if (StringUtils.isBlank(paramDoc.getIn()))
			paramDoc.setIn(paramCalcul.getIn());

		if (paramDoc.getExample() == null)
			paramDoc.setExample(paramCalcul.getExample());

		if (paramDoc.getDeprecated() == null)
			paramDoc.setDeprecated(paramCalcul.getDeprecated());

		if (paramDoc.getRequired() == null)
			paramDoc.setRequired(paramCalcul.getRequired());

		if (paramDoc.getAllowEmptyValue() == null)
			paramDoc.setAllowEmptyValue(paramCalcul.getAllowEmptyValue());

		if (paramDoc.getAllowReserved() == null)
			paramDoc.setAllowReserved(paramCalcul.getAllowReserved());

		if (StringUtils.isBlank(paramDoc.get$ref()))
			paramDoc.set$ref(paramDoc.get$ref());

		if (paramDoc.getSchema() == null && paramDoc.getContent() == null)
			paramDoc.setSchema(paramCalcul.getSchema());

		if (paramDoc.getExamples() == null)
			paramDoc.setExamples(paramCalcul.getExamples());

		if (paramDoc.getExtensions() == null)
			paramDoc.setExtensions(paramCalcul.getExtensions());

		if (paramDoc.getStyle() == null)
			paramDoc.setStyle(paramCalcul.getStyle());

		if (paramDoc.getExplode() == null)
			paramDoc.setExplode(paramCalcul.getExplode());
	}

	/**
	 * Build parameter from doc parameter.
	 *
	 * @param parameterDoc the parameter doc
	 * @param components   the components
	 * @param jsonView     the json view
	 * @param locale       the locale
	 * @return the parameter
	 */
	public Parameter buildParameterFromDoc(io.swagger.v3.oas.annotations.Parameter parameterDoc,
			Components components, JsonView jsonView, Locale locale) {
		Parameter parameter = new Parameter();
		if (StringUtils.isNotBlank(parameterDoc.description()))
			parameter.setDescription(propertyResolverUtils.resolve(parameterDoc.description(), locale));
		if (StringUtils.isNotBlank(parameterDoc.name()))
			parameter.setName(propertyResolverUtils.resolve(parameterDoc.name(), locale));
		if (StringUtils.isNotBlank(parameterDoc.in().toString()))
			parameter.setIn(parameterDoc.in().toString());
		if (StringUtils.isNotBlank(parameterDoc.example())) {
			try {
				parameter.setExample(objectMapperProvider.jsonMapper().readTree(parameterDoc.example()));
			}
			catch (IOException e) {
				parameter.setExample(parameterDoc.example());
			}
		}
		if (parameterDoc.deprecated())
			parameter.setDeprecated(parameterDoc.deprecated());
		if (parameterDoc.required())
			parameter.setRequired(parameterDoc.required());
		if (parameterDoc.allowEmptyValue())
			parameter.setAllowEmptyValue(parameterDoc.allowEmptyValue());
		if (parameterDoc.allowReserved())
			parameter.setAllowReserved(parameterDoc.allowReserved());

		if (parameterDoc.content().length > 0) {
			Optional<Content> optionalContent = AnnotationsUtils.getContent(parameterDoc.content(), null, null, null, components, jsonView, propertyResolverUtils.isOpenapi31());
			if (propertyResolverUtils.isOpenapi31())
				optionalContent.ifPresent(SpringDocUtils::handleSchemaTypes);
			optionalContent.ifPresent(parameter::setContent);
		}
		else
			setSchema(parameterDoc, components, jsonView, parameter);

		setExamples(parameterDoc, parameter);
		setExtensions(parameterDoc, parameter, locale);
		setParameterStyle(parameter, parameterDoc);
		setParameterExplode(parameter, parameterDoc);

		return parameter;
	}

	/**
	 * Sets schema.
	 *
	 * @param parameterDoc the parameter doc
	 * @param components   the components
	 * @param jsonView     the json view
	 * @param parameter    the parameter
	 */
	private void setSchema(io.swagger.v3.oas.annotations.Parameter parameterDoc, Components components, JsonView jsonView, Parameter parameter) {
		if (StringUtils.isNotBlank(parameterDoc.ref()))
			parameter.$ref(parameterDoc.ref());
		else {
			Schema schema = null;
			try {
				if(StringUtils.isNotEmpty(parameterDoc.schema().type()) || !Void.class.equals(parameterDoc.schema().implementation())){
					schema = AnnotationsUtils.getSchema(parameterDoc.schema(), null, false, parameterDoc.schema().implementation(), components, jsonView, propertyResolverUtils.isOpenapi31()).orElse(null);
				}
				// Cast default value
				if (schema != null && schema.getDefault() != null) {
					PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(schema.getType(), schema.getFormat());
					if (primitiveType != null) {
						Schema<?> primitiveSchema = primitiveType.createProperty();
						primitiveSchema.setDefault(schema.getDefault());
						schema.setDefault(primitiveSchema.getDefault());
					}
				}
			}
			catch (Exception e) {
				LOGGER.warn(Constants.GRACEFUL_EXCEPTION_OCCURRED, e);
			}
			if (schema == null && parameterDoc.array() != null) {
				schema = AnnotationsUtils.getSchema(parameterDoc.schema(), parameterDoc.array(), true, parameterDoc.array().schema().implementation(), components, jsonView, propertyResolverUtils.isOpenapi31()).orElse(null);
				// default value not set by swagger-core for array !
				if (schema != null) {
					Object defaultValue = SpringDocAnnotationsUtils.resolveDefaultValue(parameterDoc.array().arraySchema().defaultValue(), objectMapperProvider.jsonMapper());
					schema.setDefault(defaultValue);
				}
			}
			if (isOpenapi31())
				handleSchemaTypes(schema);
			parameter.setSchema(schema);
		}
	}

	/**
	 * Calculate schema schema.
	 *
	 * @param components      the components
	 * @param parameterInfo   the parameter info
	 * @param requestBodyInfo the request body info
	 * @param jsonView        the json view
	 * @return the schema
	 */
	Schema calculateSchema(Components components, ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo, JsonView jsonView) {
		Schema schemaN;
		String paramName = parameterInfo.getpName();
		MethodParameter methodParameter = parameterInfo.getMethodParameter();

		if (parameterInfo.getParameterModel() == null || parameterInfo.getParameterModel().getSchema() == null) {
			Type type = GenericTypeResolver.resolveType( methodParameter.getGenericParameterType(), methodParameter.getContainingClass());
			if (type instanceof Class && !((Class<?>) type).isEnum() && optionalWebConversionServiceProvider.isPresent()) {
				WebConversionServiceProvider webConversionServiceProvider = optionalWebConversionServiceProvider.get();
				if (!MethodParameterPojoExtractor.isSwaggerPrimitiveType((Class) type) && methodParameter.getParameterType().getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class) == null) {
					Class<?> springConvertedType = webConversionServiceProvider.getSpringConvertedType(methodParameter.getParameterType());
					if (!(String.class.equals(springConvertedType) && ((Class<?>) type).isEnum()) && requestBodyInfo == null)
						type = springConvertedType;
				}
			}
			schemaN = SpringDocAnnotationsUtils.extractSchema(components, type, jsonView, getParameterAnnotations(methodParameter), propertyResolverUtils.getSpecVersion());
		}
		else
			schemaN = parameterInfo.getParameterModel().getSchema();

		if (requestBodyInfo != null) {
			schemaN = calculateRequestBodySchema(components, parameterInfo, requestBodyInfo, schemaN, paramName);
			JavadocProvider javadocProvider = javadocProviderOptional.orElse(null);
			if (schemaN != null && javadocProvider != null && !isRequestBodyPresent(parameterInfo)) {
				String paramJavadocDescription = getParamJavadoc(javadocProvider, methodParameter);
				if (schemaN.getProperties() != null && schemaN.getProperties().containsKey(parameterInfo.getpName())) {
					Map<String, Schema> properties = schemaN.getProperties();
					if (!StringUtils.isBlank(paramJavadocDescription) && StringUtils.isBlank(properties.get(parameterInfo.getpName()).getDescription())) {
						properties.get(parameterInfo.getpName()).setDescription(paramJavadocDescription);
					}
				}
			}
		}

		return schemaN;
	}

	/**
	 * Calculate request body schema schema.
	 *
	 * @param components      the components
	 * @param parameterInfo   the parameter info
	 * @param requestBodyInfo the request body info
	 * @param schemaN         the schema n
	 * @param paramName       the param name
	 * @return the schema
	 */
	private Schema calculateRequestBodySchema(Components components, ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo, Schema schemaN, String paramName) {
		if (schemaN != null && StringUtils.isEmpty(schemaN.getDescription()) && parameterInfo.getParameterModel() != null) {
			String description = parameterInfo.getParameterModel().getDescription();
			if (schemaN.get$ref() != null && schemaN.get$ref().contains(AnnotationsUtils.COMPONENTS_REF)) {
				String key = schemaN.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
				Schema existingSchema = components.getSchemas().get(key);
				if (!StringUtils.isEmpty(description))
					existingSchema.setDescription(description);
			}
			else
				schemaN.setDescription(description);
		}

		if (requestBodyInfo.getMergedSchema() != null) {
			requestBodyInfo.getMergedSchema().addProperty(paramName, schemaN);
			schemaN = requestBodyInfo.getMergedSchema();
		}
		else if (parameterInfo.isRequestPart() || ParameterIn.QUERY.toString().equals(parameterInfo.getParamType()) ||  schemaN instanceof FileSchema || (schemaN!=null && schemaN.getItems() instanceof FileSchema)) {
			schemaN = new ObjectSchema().addProperty(paramName, schemaN);
			requestBodyInfo.setMergedSchema(schemaN);
		}
		else
			requestBodyInfo.addProperties(paramName, schemaN);

		if (requestBodyInfo.getMergedSchema() != null && parameterInfo.isRequired())
			requestBodyInfo.getMergedSchema().addRequiredItem(parameterInfo.getpName());

		return schemaN;
	}

	/**
	 * Sets examples.
	 *
	 * @param parameterDoc the parameter doc
	 * @param parameter    the parameter
	 */
	private void setExamples(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter) {
		Map<String, Example> exampleMap = new LinkedHashMap<>();
		if (parameterDoc.examples().length == 1 && StringUtils.isBlank(parameterDoc.examples()[0].name())) {
			Optional<Example> exampleOptional = AnnotationsUtils.getExample(parameterDoc.examples()[0]);
			exampleOptional.ifPresent(parameter::setExample);
		}
		else {
			for (ExampleObject exampleObject : parameterDoc.examples()) {
				AnnotationsUtils.getExample(exampleObject)
						.ifPresent(example -> exampleMap.put(exampleObject.name(), example));
			}
		}
		if (exampleMap.size() > 0) {
			parameter.setExamples(exampleMap);
		}
	}

	/**
	 * Sets extensions.
	 *
	 * @param parameterDoc the parameter doc
	 * @param parameter    the parameter
	 * @param locale       the locale
	 */
	private void setExtensions(io.swagger.v3.oas.annotations.Parameter parameterDoc, Parameter parameter, Locale locale) {
		if (parameterDoc.extensions().length > 0) {
			Map<String, Object> extensionMap = AnnotationsUtils.getExtensions(propertyResolverUtils.isOpenapi31(), parameterDoc.extensions());
			if (propertyResolverUtils.isResolveExtensionsProperties()) {
				Map<String, Object> extensionsResolved = propertyResolverUtils.resolveExtensions(locale, extensionMap);
				extensionsResolved.forEach(parameter::addExtension);
			}
			else {
				extensionMap.forEach(parameter::addExtension);
			}
		}
	}

	/**
	 * Sets parameter explode.
	 *
	 * @param parameter the parameter
	 * @param p         the p
	 */
	private void setParameterExplode(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
		if (isExplodable(p)) {
			if (Explode.TRUE.equals(p.explode())) {
				parameter.setExplode(Boolean.TRUE);
			}
			else if (Explode.FALSE.equals(p.explode())) {
				parameter.setExplode(Boolean.FALSE);
			}
		}
	}

	/**
	 * Sets parameter style.
	 *
	 * @param parameter the parameter
	 * @param p         the p
	 */
	private void setParameterStyle(Parameter parameter, io.swagger.v3.oas.annotations.Parameter p) {
		if (StringUtils.isNotBlank(p.style().toString())) {
			parameter.setStyle(Parameter.StyleEnum.valueOf(p.style().toString().toUpperCase()));
		}
	}

	/**
	 * Is explodable boolean.
	 *
	 * @param p the p
	 * @return the boolean
	 */
	private boolean isExplodable(io.swagger.v3.oas.annotations.Parameter p) {
		io.swagger.v3.oas.annotations.media.Schema schema = p.schema();
		io.swagger.v3.oas.annotations.media.ArraySchema arraySchema = p.array();

		boolean explode = true;
		Class<?> implementation = schema.implementation();
		if (implementation == Void.class && !schema.type().equals("object") && !schema.type().equals("array") && !AnnotationsUtils.hasArrayAnnotation(arraySchema)) {
			explode = false;
		}
		return explode;
	}

	/**
	 * Is file boolean.
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	public boolean isFile(MethodParameter methodParameter) {
		if (methodParameter.getGenericParameterType() instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getGenericParameterType();
			return isFile(parameterizedType);
		}
		else {
			Class type = methodParameter.getParameterType();
			return isFile(type);
		}
	}

	/**
	 * Gets optional delegating method parameter customizers.
	 *
	 * @return the optional delegating method parameter customizers
	 */
	public Optional<List<DelegatingMethodParameterCustomizer>> getOptionalDelegatingMethodParameterCustomizers() {
		return optionalDelegatingMethodParameterCustomizers;
	}

	/**
	 * Is file boolean.
	 *
	 * @param parameterizedType the parameterized type
	 * @return the boolean
	 */
	private boolean isFile(ParameterizedType parameterizedType) {
		Type type = parameterizedType.getActualTypeArguments()[0];
		Class fileClass = ResolvableType.forType(type).getRawClass();
		if (fileClass != null && isFile(fileClass))
			return true;
		else if (type instanceof WildcardType wildcardType) {
			Type[] upperBounds = wildcardType.getUpperBounds();
			return MultipartFile.class.getName().equals(upperBounds[0].getTypeName());
		}
		return false;
	}

	/**
	 * Gets property resolver utils.
	 *
	 * @return the property resolver utils
	 */
	public PropertyResolverUtils getPropertyResolverUtils() {
		return propertyResolverUtils;
	}

	/**
	 * Gets optional web conversion service provider.
	 *
	 * @return the optional web conversion service provider
	 */
	public Optional<WebConversionServiceProvider> getOptionalWebConversionServiceProvider() {
		return optionalWebConversionServiceProvider;
	}

	/**
	 * Resolve the given annotation-specified value,
	 * potentially containing placeholders and expressions.
	 *
	 * @param value the value
	 * @return the object
	 */
	public Object resolveEmbeddedValuesAndExpressions(String value) {
		if (this.configurableBeanFactory == null || this.expressionContext == null) {
			return value;
		}
		String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(value);
		BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
		if (exprResolver == null) {
			return value;
		}
		return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
	}

	/**
	 * Generate parameter by schema
	 *
	 * @param schema the schema
	 * @return the io.swagger.v3.oas.annotations.Parameter
	 */
	public io.swagger.v3.oas.annotations.Parameter generateParameterBySchema(io.swagger.v3.oas.annotations.media.Schema schema) {
		return new io.swagger.v3.oas.annotations.Parameter() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return io.swagger.v3.oas.annotations.Parameter.class;
			}

			@Override
			public String name() {
				return schema.name();
			}

			@Override
			public ParameterIn in() {
				return ParameterIn.DEFAULT;
			}

			@Override
			public String description() {
				return schema.description();
			}

			@Override
			public boolean required() {
				return schema.requiredMode().equals(RequiredMode.AUTO) ?
						schema.required() :
						schema.requiredMode().equals(RequiredMode.REQUIRED);
			}

			@Override
			public boolean deprecated() {
				return schema.deprecated();
			}

			@Override
			public boolean allowEmptyValue() {
				return false;
			}

			@Override
			public ParameterStyle style() {
				return ParameterStyle.DEFAULT;
			}

			@Override
			public Explode explode() {
				return Explode.DEFAULT;
			}

			@Override
			public boolean allowReserved() {
				return false;
			}

			@Override
			public io.swagger.v3.oas.annotations.media.Schema schema() {
				return schema;
			}

			@Override
			public io.swagger.v3.oas.annotations.media.ArraySchema array() {
				return null;
			}

			@Override
			public io.swagger.v3.oas.annotations.media.Content[] content() {
				return new io.swagger.v3.oas.annotations.media.Content[0];
			}

			@Override
			public boolean hidden() {
				return schema.hidden();
			}

			@Override
			public ExampleObject[] examples() {
				return new ExampleObject[0];
			}

			@Override
			public String example() {
				return schema.example();
			}

			@Override
			public Extension[] extensions() {
				return schema.extensions();
			}

			@Override
			public String ref() {
				return schema.ref();
			}

			@Override
			public Class<?>[] validationGroups() {
				return new Class[0];
			}
		};
	}

	/**
	 * Gets javadoc provider.
	 *
	 * @return the javadoc provider
	 */
	public JavadocProvider getJavadocProvider() {
		return javadocProviderOptional.orElse(null);
	}

	/**
	 * Is request body present boolean.
	 *
	 * @param parameterInfo the parameter info
	 * @return the boolean
	 */
	public boolean isRequestBodyPresent(ParameterInfo parameterInfo) {
		return parameterInfo.getMethodParameter().getParameterAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class) != null
				|| parameterInfo.getMethodParameter().getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class) != null
				|| AnnotatedElementUtils.findMergedAnnotation(Objects.requireNonNull(parameterInfo.getMethodParameter().getMethod()), io.swagger.v3.oas.annotations.parameters.RequestBody.class) != null;
	}

	/**
	 * Gets param javadoc.
	 *
	 * @param javadocProvider the javadoc provider
	 * @param methodParameter the method parameter
	 * @return the param javadoc
	 */
	String getParamJavadoc(JavadocProvider javadocProvider, MethodParameter methodParameter) {
		String pName = methodParameter.getParameterName();
		DelegatingMethodParameter delegatingMethodParameter = (DelegatingMethodParameter) methodParameter;
		if (!delegatingMethodParameter.isParameterObject()) {
			return javadocProvider.getParamJavadoc(methodParameter.getMethod(), pName);
		}
		String fieldName;
		if (StringUtils.isNotEmpty(pName) && pName.contains(DOT))
			fieldName = StringUtils.substringAfterLast(pName, DOT);
		else fieldName = pName;

		String paramJavadocDescription = null;
		Class cls = ((DelegatingMethodParameter) methodParameter).getExecutable().getDeclaringClass();
		if (cls.getSuperclass() != null && cls.isRecord()) {
			Map<String, String> recordParamMap = javadocProvider.getRecordClassParamJavadoc(cls);
			if (recordParamMap.containsKey(fieldName)) {
				paramJavadocDescription = recordParamMap.get(fieldName);
			}
		}

		Field field = FieldUtils.getDeclaredField(cls, fieldName, true);
		String fieldJavadoc = javadocProvider.getFieldJavadoc(field);
		if (StringUtils.isNotBlank(fieldJavadoc)) {
			paramJavadocDescription = fieldJavadoc;
		}
		return paramJavadocDescription;
	}

	/**
	 * Is openapi 31 boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOpenapi31() {
		return propertyResolverUtils.isOpenapi31();
	}
}
