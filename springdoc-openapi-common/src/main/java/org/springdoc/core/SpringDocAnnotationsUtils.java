package org.springdoc.core;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

@SuppressWarnings({ "rawtypes" })
public class SpringDocAnnotationsUtils extends AnnotationsUtils {

	private SpringDocAnnotationsUtils() {
		super();
	}

	static final String COMPONENTS_REF = "#/components/schemas/";

	static {
		ModelConverters.getInstance().addConverter(new ObjectNodeConverter());
	}

	public static Schema resolveSchemaFromType(Class<?> schemaImplementation, Components components) {
		Schema schemaObject = null;
		PrimitiveType primitiveType = PrimitiveType.fromType(schemaImplementation);
		if (primitiveType != null) {
			schemaObject = primitiveType.createProperty();
		} else {
			schemaObject = extractSchema(components, schemaImplementation);
		}
		if (schemaObject != null && StringUtils.isBlank(schemaObject.get$ref())
				&& StringUtils.isBlank(schemaObject.getType())) {
			// default to string
			schemaObject.setType("string");
		}
		return schemaObject;
	}

	public static Schema extractSchema(Components components, Type returnType) {
		Schema schemaN = null;
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
		if (resolvedSchema.schema != null) {
			schemaN = resolvedSchema.schema;
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				for (Map.Entry<String, Schema> entry : schemaMap.entrySet()) {
					Map<String, Schema> componentSchemas = components.getSchemas();
					if (componentSchemas == null) {
						componentSchemas = new LinkedHashMap<>();
						componentSchemas.put(entry.getKey(), entry.getValue());
					} else if (!componentSchemas.containsKey(entry.getKey())) {
						componentSchemas.put(entry.getKey(), entry.getValue());
					}
					components.setSchemas(componentSchemas);
				}
			}
		}
		return schemaN;
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
			} else {
				if (mediaType.getSchema() != null)
					applyTypes(classTypes, methodTypes, content, mediaType);
			}
		}

		if (content.size() == 0 && annotationContents.length != 1) {
			return Optional.empty();
		}
		return Optional.of(content);
	}

	private static void addEncodingToMediaType(JsonView jsonViewAnnotation, MediaType mediaType,
			io.swagger.v3.oas.annotations.media.Encoding[] encodings) {
		for (io.swagger.v3.oas.annotations.media.Encoding encoding : encodings) {
			addEncodingToMediaType(mediaType, encoding, jsonViewAnnotation);
		}
	}

	private static void addExtension(io.swagger.v3.oas.annotations.media.Content annotationContent,
			MediaType mediaType) {
		if (annotationContent.extensions() != null && annotationContent.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(annotationContent.extensions());
			if (extensions != null) {
				for (Map.Entry<String, Object> entry : extensions.entrySet()) {
					mediaType.addExtension(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private static void setExamples(MediaType mediaType, ExampleObject[] examples) {
		if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
			getExample(examples[0], true).ifPresent(exampleObject -> mediaType.example(exampleObject.getValue()));
		} else {
			for (ExampleObject example : examples) {
				getExample(example).ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
			}
		}
	}

	private static MediaType getMediaType(Schema schema, Components components, JsonView jsonViewAnnotation,
			io.swagger.v3.oas.annotations.media.Content annotationContent) {
		MediaType mediaType = new MediaType();
		if (!annotationContent.schema().hidden()) {
			if (components != null) {
				getSchema(annotationContent, components, jsonViewAnnotation).ifPresent(mediaType::setSchema);
			} else {
				mediaType.setSchema(schema);
			}
		}
		return mediaType;
	}

}