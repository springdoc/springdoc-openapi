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

package org.springdoc.core.customizers;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.extractor.DelegatingMethodParameter;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.util.CollectionUtils;

/**
 * The type Javadoc property customizer.
 *
 * @author bnasslahsen
 */
public record JavadocPropertyCustomizer(JavadocProvider javadocProvider,
										ObjectMapperProvider objectMapperProvider)
		implements ModelConverter {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingMethodParameter.class);

	/**
	 * Resolve schema.
	 *
	 * @param type    the type
	 * @param context the context
	 * @param chain   the chain
	 * @return the schema
	 */
	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			JavaType javaType = objectMapperProvider.jsonMapper().constructType(type.getType());
			if (javaType != null) {
				Class<?> cls = javaType.getRawClass();
				Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
				List<Field> fields = FieldUtils.getAllFieldsList(cls);
				List<PropertyDescriptor> clsProperties = new ArrayList<>();
				try {
					clsProperties = Arrays.asList(Introspector.getBeanInfo(cls).getPropertyDescriptors());
				}
				catch (IntrospectionException ignored) {
					LOGGER.warn(ignored.getMessage());
				}
				if (!CollectionUtils.isEmpty(fields) || !CollectionUtils.isEmpty(clsProperties)) {
					if (!type.isSchemaProperty()) {
						Schema existingSchema = context.resolve(type);
						setJavadocDescription(cls, fields, clsProperties, existingSchema, false);
					}
					else if (resolvedSchema != null && resolvedSchema.get$ref() != null && resolvedSchema.get$ref().contains(AnnotationsUtils.COMPONENTS_REF)) {
						String schemaName = resolvedSchema.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length());
						Schema existingSchema = context.getDefinedModels().get(schemaName);
						setJavadocDescription(cls, fields, clsProperties, existingSchema, false);
					}
					else {
						try {
							Field processedTypesField = FieldUtils.getDeclaredField(ModelConverterContextImpl.class, "processedTypes", true);
							Set<AnnotatedType> processedType = (Set<AnnotatedType>) processedTypesField.get(context);
							if(processedType.contains(type))
								setJavadocDescription(cls, fields, clsProperties, resolvedSchema, true);
						}
						catch (IllegalAccessException e) {
							LOGGER.warn(e.getMessage());
						}
					}
				}
				return resolvedSchema;
			}
		}
		return null;
	}

	/**
	 * Sets javadoc description.
	 *
	 * @param cls             the cls
	 * @param fields          the fields
	 * @param clsProperties   the bean properties of cls
	 * @param existingSchema  the existing schema
	 * @param isProcessedType the is processed type
	 */
	public void setJavadocDescription(Class<?> cls, List<Field> fields, List<PropertyDescriptor> clsProperties, Schema existingSchema, boolean isProcessedType) {
		if (existingSchema != null) {
			if (StringUtils.isBlank(existingSchema.getDescription()) && !isProcessedType) {
				String classJavadoc = javadocProvider.getClassJavadoc(cls);
				if (StringUtils.isNotBlank(classJavadoc)) {
					existingSchema.setDescription(classJavadoc);
				}
			}
			Map<String, Schema> properties = existingSchema.getProperties();
			if (!CollectionUtils.isEmpty(properties)) {
				if (cls.getSuperclass() != null && cls.isRecord()) {
					Map<String, String> recordParamMap = javadocProvider.getRecordClassParamJavadoc(cls);
					properties.entrySet().stream()
							.filter(stringSchemaEntry -> StringUtils.isBlank(stringSchemaEntry.getValue().getDescription()))
							.forEach(stringSchemaEntry -> {
								if (recordParamMap.containsKey(stringSchemaEntry.getKey()))
									stringSchemaEntry.getValue().setDescription(recordParamMap.get(stringSchemaEntry.getKey()));
							});
				}
				properties.entrySet().stream()
						.filter(stringSchemaEntry -> StringUtils.isBlank(stringSchemaEntry.getValue().getDescription()))
						.forEach(stringSchemaEntry -> {
							Optional<Field> optionalField = fields.stream().filter(field1 -> findFields(stringSchemaEntry, field1)).findAny();
							optionalField.ifPresent(field -> {
								String fieldJavadoc = javadocProvider.getFieldJavadoc(field);
								if (StringUtils.isNotBlank(fieldJavadoc))
									stringSchemaEntry.getValue().setDescription(fieldJavadoc);
							});
							if (StringUtils.isBlank(stringSchemaEntry.getValue().getDescription())) {
								Optional<PropertyDescriptor> optionalPd = clsProperties.stream().filter(pd -> pd.getName().equals(stringSchemaEntry.getKey())).findAny();
								optionalPd.ifPresent(pd1 -> {
									if(pd1.getReadMethod() != null) {
										String fieldJavadoc = javadocProvider.getMethodJavadocDescription(pd1.getReadMethod());
										if (StringUtils.isNotBlank(fieldJavadoc))
											stringSchemaEntry.getValue().setDescription(fieldJavadoc);
									}
								});
							}
						});
			}
		}
	}

	/**
	 * Find fields boolean.
	 *
	 * @param stringSchemaEntry the string schema entry
	 * @param field             the field
	 * @return the boolean
	 */
	private boolean findFields(Entry<String, Schema> stringSchemaEntry, Field field) {
		if (field.getName().equals(stringSchemaEntry.getKey())) {
			return true;
		}
		else {
			JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
			if (jsonPropertyAnnotation != null) {
				String jsonPropertyName = jsonPropertyAnnotation.value();
				if (jsonPropertyName.equals(stringSchemaEntry.getKey())) {
					return true;
				}
			}
			else if (field.getName().equalsIgnoreCase(stringSchemaEntry.getKey().replace("_", ""))) {
				return true;
			}
			return false;
		}
	}
}
