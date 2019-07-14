package org.springdoc.core;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

@SuppressWarnings({ "rawtypes" })
public abstract class AnnotationsUtils {

	public static final String COMPONENTS_REF = "#/components/schemas/";

	public static Schema resolveSchemaFromType(Class<?> schemaImplementation, Components components,
			JsonView jsonViewAnnotation) {
		Schema schemaObject;
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

}