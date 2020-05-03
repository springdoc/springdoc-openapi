/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.CollectionUtils;

@SuppressWarnings({ "rawtypes" })
public class SpringDocAnnotationsUtils extends AnnotationsUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDocAnnotationsUtils.class);

	public static Schema resolveSchemaFromType(Class<?> schemaImplementation, Components components,
			JsonView jsonView, Annotation[] annotations) {
		Schema schemaObject = extractSchema(components, schemaImplementation, jsonView, annotations);
		if (schemaObject != null && StringUtils.isBlank(schemaObject.get$ref())
				&& StringUtils.isBlank(schemaObject.getType())) {
			// default to string
			schemaObject.setType("string");
		}
		return schemaObject;
	}

	public static Schema extractSchema(Components components, Type returnType, JsonView jsonView, Annotation[] annotations) {
		Schema schemaN = null;
		ResolvedSchema resolvedSchema = null;
		try {
			resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(
							new AnnotatedType(returnType).resolveAsRef(true).jsonViewAnnotation(jsonView).ctxAnnotations(annotations));
		}
		catch (Exception e) {
			LOGGER.warn(Constants.GRACEFUL_EXCEPTION_OCCURRED, e);
			return null;
		}
		if (resolvedSchema.schema != null) {
			schemaN = resolvedSchema.schema;
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				for (Map.Entry<String, Schema> entry : schemaMap.entrySet()) {
					Map<String, Schema> componentSchemas = components.getSchemas();
					if (componentSchemas == null) {
						componentSchemas = new LinkedHashMap<>();
						componentSchemas.put(entry.getKey(), entry.getValue());
					}
					else if (!componentSchemas.containsKey(entry.getKey())) {
						componentSchemas.put(entry.getKey(), entry.getValue());
					}
					components.setSchemas(componentSchemas);
				}
			}
		}
		return schemaN;
	}

	public static Schema extractSchema(Components components, Type genericParameterType, JsonView jsonView) {
		return extractSchema(components, genericParameterType, jsonView, null);
	}

	public static Optional<Content> getContent(io.swagger.v3.oas.annotations.media.Content[] annotationContents,
			String[] classTypes, String[] methodTypes, Schema schema, Components components,
			JsonView jsonViewAnnotation) {
		if (ArrayUtils.isEmpty(annotationContents)) {
			return Optional.empty();
		}
		// Encapsulating Content model
		Content content = new Content();

		for (io.swagger.v3.oas.annotations.media.Content annotationContent : annotationContents) {
			MediaType mediaType = getMediaType(schema, components, jsonViewAnnotation, annotationContent);
			ExampleObject[] examples = annotationContent.examples();
			setExamples(mediaType, examples);
			addExtension(annotationContent, mediaType);
			io.swagger.v3.oas.annotations.media.Encoding[] encodings = annotationContent.encoding();
			addEncodingToMediaType(jsonViewAnnotation, mediaType, encodings);
			if (StringUtils.isNotBlank(annotationContent.mediaType())) {
				content.addMediaType(annotationContent.mediaType(), mediaType);
			}
			else {
				if (mediaType.getSchema() != null)
					applyTypes(classTypes, methodTypes, content, mediaType);
			}
		}

		if (content.size() == 0 && annotationContents.length != 1) {
			return Optional.empty();
		}
		return Optional.of(content);
	}

	public static void mergeSchema(Content existingContent, Schema<?> schemaN, String mediaTypeStr) {
		if (existingContent.containsKey(mediaTypeStr)) {
			io.swagger.v3.oas.models.media.MediaType mediaType = existingContent.get(mediaTypeStr);
			if (!schemaN.equals(mediaType.getSchema())) {
				// Merge the two schemas for the same mediaType
				Schema firstSchema = mediaType.getSchema();
				ComposedSchema schemaObject;
				if (firstSchema instanceof ComposedSchema) {
					schemaObject = (ComposedSchema) firstSchema;
					List<Schema> listOneOf = schemaObject.getOneOf();
					if (!CollectionUtils.isEmpty(listOneOf) && !listOneOf.contains(schemaN))
						schemaObject.addOneOfItem(schemaN);
				}
				else {
					schemaObject = new ComposedSchema();
					schemaObject.addOneOfItem(schemaN);
					schemaObject.addOneOfItem(firstSchema);
				}
				mediaType.setSchema(schemaObject);
				existingContent.addMediaType(mediaTypeStr, mediaType);
			}
		}
		else
			// Add the new schema for a different mediaType
			existingContent.addMediaType(mediaTypeStr, new io.swagger.v3.oas.models.media.MediaType().schema(schemaN));
	}

	private static void addEncodingToMediaType(JsonView jsonViewAnnotation, MediaType mediaType,
			io.swagger.v3.oas.annotations.media.Encoding[] encodings) {
		for (io.swagger.v3.oas.annotations.media.Encoding encoding : encodings) {
			addEncodingToMediaType(mediaType, encoding, jsonViewAnnotation);
		}
	}

	private static void addExtension(io.swagger.v3.oas.annotations.media.Content annotationContent,
			MediaType mediaType) {
		if (annotationContent.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(annotationContent.extensions());
			extensions.forEach(mediaType::addExtension);
		}
	}

	private static void setExamples(MediaType mediaType, ExampleObject[] examples) {
		if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
			getExample(examples[0], true).ifPresent(exampleObject -> mediaType.example(exampleObject.getValue()));
		}
		else {
			for (ExampleObject example : examples) {
				getExample(example).ifPresent(exampleObject -> {
							if (exampleObject.get$ref() != null)
								//Ignore description
								exampleObject.setDescription(null);
							mediaType.addExamples(example.name(), exampleObject);
						}
				);
			}
		}
	}

	private static MediaType getMediaType(Schema schema, Components components, JsonView jsonViewAnnotation,
			io.swagger.v3.oas.annotations.media.Content annotationContent) {
		MediaType mediaType = new MediaType();
		if (!annotationContent.schema().hidden()) {
			if (components != null) {
				try {
					getSchema(annotationContent, components, jsonViewAnnotation).ifPresent(mediaType::setSchema);
				}
				catch (Exception e) {
					if (isArray(annotationContent))
						mediaType.setSchema(new ArraySchema().items(new StringSchema()));
					else
						mediaType.setSchema(new StringSchema());
				}
			}
			else {
				mediaType.setSchema(schema);
			}
		}
		return mediaType;
	}

	private static boolean isArray(io.swagger.v3.oas.annotations.media.Content annotationContent) {
		Class<?> schemaImplementation = annotationContent.schema().implementation();
		boolean isArray = false;
		if (schemaImplementation == Void.class) {
			schemaImplementation = annotationContent.array().schema().implementation();
			if (schemaImplementation != Void.class) {
				isArray = true;
			}
		}
		return isArray;
	}

}
