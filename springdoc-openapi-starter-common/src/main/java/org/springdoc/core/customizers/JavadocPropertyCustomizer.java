/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2023 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
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
import java.util.*;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springframework.util.CollectionUtils;

/**
 * The type Javadoc property customizer.
 * @author bnasslahsen
 */
public record JavadocPropertyCustomizer(JavadocProvider javadocProvider,
										ObjectMapperProvider objectMapperProvider)
		implements ModelConverter {

	/**
	 * Instantiates a new Javadoc property customizer.
	 *
	 * @param javadocProvider the javadoc provider
	 * @param objectMapperProvider the object mapper provider
	 */
	public JavadocPropertyCustomizer {
	}

	/**
	 * Resolve schema.
	 *
	 * @param type the type
	 * @param context the context
	 * @param chain the chain
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
				} catch (IntrospectionException ignored) {}
				if (!CollectionUtils.isEmpty(fields) || !CollectionUtils.isEmpty(clsProperties)) {
					if (!type.isSchemaProperty()) {
						Schema existingSchema = context.resolve(type);
						setJavadocDescription(cls, fields, clsProperties, existingSchema);
					}
					else if (resolvedSchema != null && resolvedSchema.get$ref() != null && resolvedSchema.get$ref().contains(AnnotationsUtils.COMPONENTS_REF)) {
						String schemaName = resolvedSchema.get$ref().substring(21);
						Schema existingSchema = context.getDefinedModels().get(schemaName);
						setJavadocDescription(cls, fields, clsProperties, existingSchema);
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
	 * @param cls the cls
	 * @param fields the fields
	 * @param clsProperties the bean properties of cls
	 * @param existingSchema the existing schema
	 */
	private void setJavadocDescription(Class<?> cls, List<Field> fields, Schema existingSchema) {
		if (existingSchema != null) {
			if (StringUtils.isBlank(existingSchema.getDescription())) {
				existingSchema.setDescription(javadocProvider.getClassJavadoc(cls));
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
							Optional<Field> optionalField = fields.stream().filter(field1 -> field1.getName().equals(stringSchemaEntry.getKey())).findAny();
							optionalField.ifPresent(field -> {
								String fieldJavadoc = javadocProvider.getFieldJavadoc(field);
								if (StringUtils.isNotBlank(fieldJavadoc))
									stringSchemaEntry.getValue().setDescription(fieldJavadoc);
							});
							if (StringUtils.isBlank(stringSchemaEntry.getValue().getDescription())) {
								Optional<PropertyDescriptor> optionalPd = clsProperties.stream().filter(pd -> pd.getName().equals(stringSchemaEntry.getKey())).findAny();
								optionalPd.ifPresent(pd1 -> {
									String fieldJavadoc = javadocProvider.getMethodJavadocDescription(pd1.getReadMethod());
									if (StringUtils.isNotBlank(fieldJavadoc))
										stringSchemaEntry.getValue().setDescription(fieldJavadoc);
								});
							}
						});
			}
		}
	}
}
