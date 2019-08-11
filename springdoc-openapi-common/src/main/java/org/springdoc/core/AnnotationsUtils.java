package org.springdoc.core;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

@SuppressWarnings({ "rawtypes" })
public class AnnotationsUtils extends io.swagger.v3.core.util.AnnotationsUtils {

	private AnnotationsUtils() {
		super();
	}

	static final String COMPONENTS_REF = "#/components/schemas/";

	public static Schema resolveSchemaFromType(Class<?> schemaImplementation, Components components,
			JsonView jsonViewAnnotation) {
		Schema schemaObject = null;
		PrimitiveType primitiveType = PrimitiveType.fromType(schemaImplementation);
		if (primitiveType != null) {
			schemaObject = primitiveType.createProperty();
		} else {
			schemaObject = new Schema();
			ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(
					new AnnotatedType().type(schemaImplementation).jsonViewAnnotation(jsonViewAnnotation));
			Map<String, Schema> schemaMap;
			if (resolvedSchema != null) {
				schemaMap = resolvedSchema.referencedSchemas;
				schemaMap.forEach((key, referencedSchema) -> {
					if (components != null) {
						components.addSchemas(key, referencedSchema);
					}
				});
				if (StringUtils.isNotBlank(resolvedSchema.schema.getName())) {
					schemaObject.set$ref(COMPONENTS_REF + resolvedSchema.schema.getName());
				} else {
					schemaObject = resolvedSchema.schema;
				}
			}
		}
		if (StringUtils.isBlank(schemaObject.get$ref()) && StringUtils.isBlank(schemaObject.getType())) {
			// default to string
			schemaObject.setType("string");
		}
		return schemaObject;
	}

	public static Optional<Content> getContent(io.swagger.v3.oas.annotations.media.Content[] annotationContents,
			String[] classTypes, String[] methodTypes, Schema schema, Components components,
			JsonView jsonViewAnnotation) {
		if (annotationContents == null || annotationContents.length == 0) {
			return Optional.empty();
		}

		// Encapsulating Content model
		Content content = new Content();

		for (io.swagger.v3.oas.annotations.media.Content annotationContent : annotationContents) {
			MediaType mediaType = new MediaType();
			if (!annotationContent.schema().hidden()) {
				if (components != null) {
					getSchema(annotationContent, components, jsonViewAnnotation).ifPresent(mediaType::setSchema);
				} else {
					mediaType.setSchema(schema);
				}
			}
			ExampleObject[] examples = annotationContent.examples();
			if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
				getExample(examples[0], true).ifPresent(exampleObject -> mediaType.example(exampleObject.getValue()));
			} else {
				for (ExampleObject example : examples) {
					getExample(example)
							.ifPresent(exampleObject -> mediaType.addExamples(example.name(), exampleObject));
				}
			}
			if (annotationContent.extensions() != null && annotationContent.extensions().length > 0) {
				Map<String, Object> extensions = AnnotationsUtils.getExtensions(annotationContent.extensions());
				if (extensions != null) {
					for (String ext : extensions.keySet()) {
						mediaType.addExtension(ext, extensions.get(ext));
					}
				}
			}

			io.swagger.v3.oas.annotations.media.Encoding[] encodings = annotationContent.encoding();
			for (io.swagger.v3.oas.annotations.media.Encoding encoding : encodings) {
				addEncodingToMediaType(mediaType, encoding, jsonViewAnnotation);
			}
			if (StringUtils.isNotBlank(annotationContent.mediaType())) {
				content.addMediaType(annotationContent.mediaType(), mediaType);
			} else {
				applyTypes(classTypes, methodTypes, content, mediaType);
			}
		}

		if (content.size() == 0) {
			return Optional.empty();
		}
		return Optional.of(content);
	}

}