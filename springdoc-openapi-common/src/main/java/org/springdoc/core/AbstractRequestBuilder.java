package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;

@SuppressWarnings({ "rawtypes" })
public abstract class AbstractRequestBuilder {

	@Autowired
	protected ParameterBuilder parameterBuilder;

	public abstract Operation build(Components components, HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, String[] allConsumes);

	protected <A extends Annotation> A getParameterAnnotation(HandlerMethod handlerMethod,
			java.lang.reflect.Parameter parameter, int i, Class<A> annotationType) {
		A parameterDoc = AnnotationUtils.getAnnotation(parameter, annotationType);
		if (parameterDoc == null) {
			Set<Method> methods = MethodUtils.getOverrideHierarchy(handlerMethod.getMethod(),
					ClassUtils.Interfaces.INCLUDE);
			for (Method methodOverriden : methods) {
				parameterDoc = AnnotationUtils.getAnnotation(methodOverriden.getParameters()[i], annotationType);
				if (parameterDoc != null)
					break;
			}
		}
		return parameterDoc;
	}

	protected Parameter buildParamDefault(RequestMethod requestMethod, String pNames,
			java.lang.reflect.Parameter parameters, Parameter parameter) {
		if (RequestMethod.GET.equals(requestMethod) && parameter == null) {
			parameter = this.buildParam(QUERY_PARAM, null, parameters, Boolean.FALSE, pNames, null);
		}
		return parameter;
	}

	protected Parameter buildParams(String pName, Components components, java.lang.reflect.Parameter parameters,
			int index, Parameter parameter, HandlerMethod handlerMethod) {
		RequestHeader requestHeader = getParameterAnnotation(handlerMethod, parameters, index, RequestHeader.class);
		RequestParam requestParam = getParameterAnnotation(handlerMethod, parameters, index, RequestParam.class);
		PathVariable pathVar = getParameterAnnotation(handlerMethod, parameters, index, PathVariable.class);

		if (requestHeader != null) {
			String name = StringUtils.isBlank(requestHeader.value()) ? pName : requestHeader.value();
			parameter = this.buildParam(HEADER_PARAM, components, parameters, requestHeader.required(), name,
					parameter);
		} else if (requestParam != null) {
			String name = StringUtils.isBlank(requestParam.value()) ? pName : requestParam.value();
			parameter = this.buildParam(QUERY_PARAM, components, parameters, requestParam.required(), name, parameter);
		} else if (pathVar != null) {
			String name = StringUtils.isBlank(pathVar.value()) ? pName : pathVar.value();
			// check if PATH PARAM
			parameter = this.buildParam(PATH_PARAM, components, parameters, Boolean.TRUE, name, parameter);
		}
		return parameter;
	}

	private Parameter buildParam(String in, Components components, java.lang.reflect.Parameter parameters,
			Boolean required, String name, Parameter parameter) {
		if (parameter == null)
			parameter = new Parameter();
		parameter.setIn(in);
		parameter.setRequired(required);
		parameter.setName(name);
		Schema<?> schema = parameterBuilder.calculateSchema(components, parameters, name);
		parameter.setSchema(schema);
		return parameter;
	}

	protected RequestBody buildRequestBody(Components components, String[] allConsumes,
			java.lang.reflect.Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc,
			String paramName) {
		RequestBody requestBody = new RequestBody();

		Schema<?> schema = parameterBuilder.calculateSchema(components, parameter, paramName);
		io.swagger.v3.oas.models.media.MediaType mediaType = null;
		if (schema != null && schema.getType() != null) {
			mediaType = new io.swagger.v3.oas.models.media.MediaType();
			mediaType.setSchema(schema);
		} else {
			Type returnType = parameter.getType();
			mediaType = calculateSchema(components, returnType);
		}

		Content content1 = new Content();
		if (ArrayUtils.isNotEmpty(allConsumes)) {
			for (String value : allConsumes) {
				setMediaTypeToContent(schema, content1, value);
			}
		} else {
			content1.addMediaType(MediaType.ALL_VALUE, mediaType);
		}
		requestBody.setContent(content1);
		if (parameterDoc != null) {
			if (StringUtils.isNotBlank(parameterDoc.description()))
				requestBody.setDescription(parameterDoc.description());
			requestBody.setRequired(parameterDoc.required());
		}
		return requestBody;
	}



	private io.swagger.v3.oas.models.media.MediaType calculateSchema(Components components, Type returnType) {
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
		io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
		if (resolvedSchema.schema != null) {
			Schema<?> returnTypeSchema = resolvedSchema.schema;
			if (returnTypeSchema != null) {
				mediaType.setSchema(returnTypeSchema);
			}
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach(components::addSchemas);
			}
		}
		return mediaType;
	}


	/**
	 * This is mostly a duplicate of
	 * {@link io.swagger.v3.core.jackson.ModelResolver#applyBeanValidatorAnnotations}.
	 *
	 * @param parameter
	 * @param annotations
	 */
	protected void applyBeanValidatorAnnotations(final Parameter parameter, final List<Annotation> annotations) {
		Map<String, Annotation> annos = new HashMap<>();
		if (annotations != null) {
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getName(), annotation));
		}

		if (annos.containsKey(NotNull.class.getName())) {
			parameter.setRequired(true);
		}

		Schema<?> schema = parameter.getSchema();

		if (annos.containsKey(Min.class.getName())) {
			Min min = (Min) annos.get(Min.class.getName());
			schema.setMinimum(BigDecimal.valueOf(min.value()));
		}
		if (annos.containsKey(Max.class.getName())) {
			Max max = (Max) annos.get(Max.class.getName());
			schema.setMaximum(BigDecimal.valueOf(max.value()));
		}
		if (annos.containsKey(Size.class.getName())) {
			Size size = (Size) annos.get(Size.class.getName());
			if (OPENAPI_ARRAY_TYPE.equals(schema.getType())) {
				schema.setMinItems(size.min());
				schema.setMaxItems(size.max());
			} else if (OPENAPI_STRING_TYPE.equals(schema.getType())) {
				schema.setMinLength(size.min());
				schema.setMaxLength(size.max());
			}
		}
		if (annos.containsKey(DecimalMin.class.getName())) {
			DecimalMin min = (DecimalMin) annos.get(DecimalMin.class.getName());
			if (min.inclusive()) {
				schema.setMinimum(BigDecimal.valueOf(Double.valueOf(min.value())));
			} else {
				schema.setExclusiveMinimum(!min.inclusive());
			}
		}
		if (annos.containsKey(DecimalMax.class.getName())) {
			DecimalMax max = (DecimalMax) annos.get(DecimalMax.class.getName());
			if (max.inclusive()) {
				schema.setMaximum(BigDecimal.valueOf(Double.valueOf(max.value())));
			} else {
				schema.setExclusiveMaximum(!max.inclusive());
			}
		}
		if (annos.containsKey(Pattern.class.getName())) {
			Pattern pattern = (Pattern) annos.get(Pattern.class.getName());
			schema.setPattern(pattern.regexp());
		}
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}
}
