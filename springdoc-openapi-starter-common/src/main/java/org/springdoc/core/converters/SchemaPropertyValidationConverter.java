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

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Iterator;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springdoc.core.properties.SpringDocConfigProperties;

import static org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;

/**
 * The type Schema property validation converter.
 * Applies Jakarta Bean Validation annotations ({@link Positive}, {@link PositiveOrZero},
 * {@link Negative}, {@link NegativeOrZero}) to schema properties.
 * <p>
 * These annotations are not natively handled by swagger-core's ModelResolver for model
 * properties, so this converter fills the gap.
 *
 * @author springdoc
 */
public class SchemaPropertyValidationConverter implements ModelConverter {

	/**
	 * The spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * Instantiates a new Schema property validation converter.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public SchemaPropertyValidationConverter(SpringDocConfigProperties springDocConfigProperties) {
		this.springDocConfigProperties = springDocConfigProperties;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		if (chain.hasNext()) {
			Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
			if (type.isSchemaProperty() && resolvedSchema != null) {
				applyBeanValidationAnnotations(resolvedSchema, type.getCtxAnnotations());
			}
			return resolvedSchema;
		}
		return null;
	}

	/**
	 * Apply bean validation annotations to the schema.
	 *
	 * @param schema      the schema
	 * @param annotations the annotations
	 */
	private void applyBeanValidationAnnotations(Schema<?> schema, Annotation[] annotations) {
		if (annotations == null) {
			return;
		}
		String openapiVersion = springDocConfigProperties.getApiDocs().getVersion().getVersion();
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (annotationType == Positive.class) {
				if (OpenApiVersion.OPENAPI_3_1.getVersion().equals(openapiVersion)) {
					schema.setExclusiveMinimumValue(BigDecimal.ZERO);
				}
				else {
					schema.setMinimum(BigDecimal.ZERO);
					schema.setExclusiveMinimum(true);
				}
			}
			else if (annotationType == PositiveOrZero.class) {
				schema.setMinimum(BigDecimal.ZERO);
			}
			else if (annotationType == NegativeOrZero.class) {
				schema.setMaximum(BigDecimal.ZERO);
			}
			else if (annotationType == Negative.class) {
				if (OpenApiVersion.OPENAPI_3_1.getVersion().equals(openapiVersion)) {
					schema.setExclusiveMaximumValue(BigDecimal.ZERO);
				}
				else {
					schema.setMaximum(BigDecimal.ZERO);
					schema.setExclusiveMaximum(true);
				}
			}
		}
	}
}
