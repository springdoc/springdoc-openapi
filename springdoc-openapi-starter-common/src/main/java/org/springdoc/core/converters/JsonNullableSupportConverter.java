/*
 * Copyright 2019-2026 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.providers.ObjectMapperProvider;

import org.springdoc.core.utils.SchemaUtils;

/**
 * Describes JsonNullable values without exposing their Java wrapper.
 *
 * @author dpkass
 */
public class JsonNullableSupportConverter implements ModelConverter {

	private static final String JSON_NULLABLE = "org.openapitools.jackson.nullable.JsonNullable";

	private final ObjectMapperProvider mapperProvider;

	/**
	 * @param mapperProvider the OpenAPI object mapper provider
	 */
	public JsonNullableSupportConverter(ObjectMapperProvider mapperProvider) {
		this.mapperProvider = mapperProvider;
	}

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		JavaType javaType = mapperProvider.jsonMapper().constructType(type.getType());
		if (javaType != null && JSON_NULLABLE.equals(javaType.getRawClass().getName()))
			return resolveValue(javaType, type.getCtxAnnotations(), type, context);

		Schema resolved = chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
		if (resolved == null || javaType == null)
			return resolved;
		Schema model = resolved;
		if (model.get$ref() != null) {
			if (!model.get$ref().startsWith(Components.COMPONENTS_SCHEMAS_REF))
				return resolved;
			model = context.getDefinedModels().get(model.get$ref().substring(Components.COMPONENTS_SCHEMAS_REF.length()));
		}
		if (model == null || model.getProperties() == null)
			return resolved;

		ObjectMapper mapper = ModelConverters.getInstance(mapperProvider.isOpenapi31()).getConverters().stream()
				.filter(ModelResolver.class::isInstance).map(ModelResolver.class::cast)
				.map(ModelResolver::objectMapper).findFirst().orElse(null);
		if (mapper == null)
			return resolved;
		var bean = mapper.getSerializationConfig().introspect(javaType);
		var schema = bean.getClassInfo().getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
		List<String> requiredProperties = schema == null ? List.of() : Arrays.asList(schema.requiredProperties());
		for (BeanPropertyDefinition property : bean.findProperties()) {
			if (!JSON_NULLABLE.equals(property.getPrimaryType().getRawClass().getName())
					|| !model.getProperties().containsKey(property.getName()))
				continue;
			List<Annotation> annotations = typeArgumentAnnotations(property);
			if (!annotations.isEmpty()) {
				property.getPrimaryMember().annotations().forEach(annotations::add);
				model.addProperty(property.getName(), resolveValue(property.getPrimaryType(),
						annotations.toArray(Annotation[]::new), type, context));
			}
			// ModelResolver infers presence from validation constraints after resolving
			// a property. JsonNullable instead permits omission unless explicitly required.
			var propertySchema = property.getPrimaryMember().getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
			Boolean required = SchemaUtils.swaggerRequired(propertySchema, null);
			boolean explicitlyRequired = required != null ? required : property.isRequired();
			if (model.getRequired() != null && !explicitlyRequired && !requiredProperties.contains(property.getName()))
				model.getRequired().remove(property.getName());
		}
		if (model.getRequired() != null && model.getRequired().isEmpty())
			model.setRequired(null);
		return resolved;
	}

	/**
	 * Resolve the value normally, then add null only when no non-null constraint applies.
	 * Conflicting annotations retain ModelResolver's behavior.
	 */
	private Schema resolveValue(JavaType wrapper, Annotation[] annotations, AnnotatedType original,
			ModelConverterContext context) {
		Schema value = context.resolve(new AnnotatedType(wrapper.containedTypeOrUnknown(0))
				.ctxAnnotations(annotations).jsonViewAnnotation(original.getJsonViewAnnotation()).resolveAsRef(true));
		if (value == null || (annotations != null && SchemaUtils.annotatedNotNull(Arrays.asList(annotations))))
			return value;

		if (value.get$ref() == null && value.getEnum() == null
				&& value.getAllOf() == null && value.getAnyOf() == null && value.getOneOf() == null
				&& (value.getType() != null || value.getTypes() != null)) {
			// Keep nullability local rather than mutating a cached/shared schema.
			Schema nullable = mapperProvider.jsonMapper().convertValue(value, Schema.class);
			if (mapperProvider.isOpenapi31()) {
				if (nullable.getTypes() == null && nullable.getType() != null)
					nullable.addType(nullable.getType());
				nullable.addType("null");
			}
			else
				nullable.setNullable(true);
			return nullable;
		}
		Schema nullValue = new Schema();
		if (mapperProvider.isOpenapi31())
			nullValue.addType("null");
		else {
			nullValue.setType("object");
			nullValue.setNullable(true);
			nullValue.setEnum(Collections.singletonList(null));
		}
		return new ComposedSchema().addAnyOfItem(value).addAnyOfItem(nullValue);
	}

	/**
	 * JavaType does not retain type-use annotations; retrieve them from the members.
	 */
	private List<Annotation> typeArgumentAnnotations(BeanPropertyDefinition property) {
		List<Annotation> annotations = new ArrayList<>();
		for (var member : Arrays.asList(property.getField(), property.getGetter(), property.getSetter())) {
			if (member == null)
				continue;
			java.lang.reflect.AnnotatedType annotatedType = null;
			if (member.getMember() instanceof Field field)
				annotatedType = field.getAnnotatedType();
			else if (member.getMember() instanceof Method method)
				annotatedType = method.getParameterCount() == 0 ? method.getAnnotatedReturnType()
						: method.getAnnotatedParameterTypes()[0];
			if (annotatedType instanceof AnnotatedParameterizedType parameterized)
				annotations.addAll(Arrays.asList(parameterized.getAnnotatedActualTypeArguments()[0].getAnnotations()));
		}
		return annotations;
	}
}
