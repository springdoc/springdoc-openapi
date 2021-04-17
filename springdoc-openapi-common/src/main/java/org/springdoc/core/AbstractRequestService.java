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
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.ParameterCustomizer;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.Constants.OPENAPI_ARRAY_TYPE;
import static org.springdoc.core.Constants.OPENAPI_STRING_TYPE;
import static org.springdoc.core.Constants.QUERY_PARAM;
import static org.springdoc.core.converters.SchemaPropertyDeprecatingConverter.containsDeprecatedAnnotation;

/**
 * The type Abstract request builder.
 * @author bnasslahsen
 */
public abstract class AbstractRequestService {

	/**
	 * The constant PARAM_TYPES_TO_IGNORE.
	 */
	private static final List<Class<?>> PARAM_TYPES_TO_IGNORE = new ArrayList<>();

	/**
	 * The constant ANNOTATIONS_FOR_REQUIRED.
	 */
// using string litterals to support both validation-api v1 and v2
	private static final String[] ANNOTATIONS_FOR_REQUIRED = { "NotNull", "NonNull", "NotBlank", "NotEmpty" };

	/**
	 * The constant POSITIVE_OR_ZERO.
	 */
	private static final String POSITIVE_OR_ZERO = "PositiveOrZero";

	/**
	 * The constant NEGATIVE_OR_ZERO.
	 */
	private static final String NEGATIVE_OR_ZERO = "NegativeOrZero";

	static {
		PARAM_TYPES_TO_IGNORE.add(WebRequest.class);
		PARAM_TYPES_TO_IGNORE.add(NativeWebRequest.class);
		PARAM_TYPES_TO_IGNORE.add(java.security.Principal.class);
		PARAM_TYPES_TO_IGNORE.add(HttpMethod.class);
		PARAM_TYPES_TO_IGNORE.add(java.util.Locale.class);
		PARAM_TYPES_TO_IGNORE.add(java.util.TimeZone.class);
		PARAM_TYPES_TO_IGNORE.add(java.io.InputStream.class);
		PARAM_TYPES_TO_IGNORE.add(java.time.ZoneId.class);
		PARAM_TYPES_TO_IGNORE.add(java.io.Reader.class);
		PARAM_TYPES_TO_IGNORE.add(java.io.OutputStream.class);
		PARAM_TYPES_TO_IGNORE.add(java.io.Writer.class);
		PARAM_TYPES_TO_IGNORE.add(java.util.Map.class);
		PARAM_TYPES_TO_IGNORE.add(org.springframework.ui.Model.class);
		PARAM_TYPES_TO_IGNORE.add(org.springframework.ui.ModelMap.class);
		PARAM_TYPES_TO_IGNORE.add(Errors.class);
		PARAM_TYPES_TO_IGNORE.add(BindingResult.class);
		PARAM_TYPES_TO_IGNORE.add(SessionStatus.class);
		PARAM_TYPES_TO_IGNORE.add(UriComponentsBuilder.class);
		PARAM_TYPES_TO_IGNORE.add(RequestAttribute.class);
	}

	/**
	 * The Parameter builder.
	 */
	private final GenericParameterService parameterBuilder;

	/**
	 * The Request body builder.
	 */
	private final RequestBodyService requestBodyService;

	/**
	 * The Operation builder.
	 */
	private final OperationService operationService;

	/**
	 * The Local spring doc parameter name discoverer.
	 */
	private final LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer;

	/**
	 * The Parameter customizers.
	 */
	private final Optional<List<ParameterCustomizer>> parameterCustomizers;

	/**
	 * Instantiates a new Abstract request builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @param requestBodyService the request body builder
	 * @param operationService the operation builder
	 * @param parameterCustomizers the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 */
	protected AbstractRequestService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			OperationService operationService, Optional<List<ParameterCustomizer>> parameterCustomizers,
			LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyService = requestBodyService;
		this.operationService = operationService;
		parameterCustomizers.ifPresent(customizers -> customizers.removeIf(Objects::isNull));
		this.parameterCustomizers = parameterCustomizers;
		this.localSpringDocParameterNameDiscoverer = localSpringDocParameterNameDiscoverer;
	}

	/**
	 * Add request wrapper to ignore.
	 *
	 * @param classes the classes
	 */
	public static void addRequestWrapperToIgnore(Class<?>... classes) {
		PARAM_TYPES_TO_IGNORE.addAll(Arrays.asList(classes));
	}

	/**
	 * Remove request wrapper to ignore.
	 *
	 * @param classes the classes
	 */
	public static void removeRequestWrapperToIgnore(Class<?>... classes) {
		List<Class<?>> classesToIgnore = Arrays.asList(classes);
		if (PARAM_TYPES_TO_IGNORE.containsAll(classesToIgnore))
			PARAM_TYPES_TO_IGNORE.removeAll(Arrays.asList(classes));
	}

	/**
	 * Is request type to ignore boolean.
	 *
	 * @param rawClass the raw class
	 * @return the boolean
	 */
	public static boolean isRequestTypeToIgnore(Class<?> rawClass) {
		return PARAM_TYPES_TO_IGNORE.stream().anyMatch(clazz -> clazz.isAssignableFrom(rawClass));
	}

	/**
	 * Build operation.
	 *
	 * @param handlerMethod the handler method
	 * @param requestMethod the request method
	 * @param operation the operation
	 * @param methodAttributes the method attributes
	 * @param openAPI the open api
	 * @return the operation
	 */
	public Operation build(HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, MethodAttributes methodAttributes, OpenAPI openAPI) {
		// Documentation
		String operationId = operationService.getOperationId(handlerMethod.getMethod().getName(),
				operation.getOperationId(), openAPI);
		operation.setOperationId(operationId);
		// requests
		String[] pNames = this.localSpringDocParameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		String[] reflectionParametersNames = Arrays.stream(handlerMethod.getMethod().getParameters()).map(java.lang.reflect.Parameter::getName).toArray(String[]::new);
		if (pNames == null || Arrays.stream(pNames).anyMatch(Objects::isNull))
			pNames = reflectionParametersNames;
		parameters = DelegatingMethodParameter.customize(pNames, parameters, parameterBuilder.getDelegatingMethodParameterCustomizer());
		RequestBodyInfo requestBodyInfo = new RequestBodyInfo();
		List<Parameter> operationParameters = (operation.getParameters() != null) ? operation.getParameters() : new ArrayList<>();
		Map<String, io.swagger.v3.oas.annotations.Parameter> parametersDocMap = getApiParameters(handlerMethod.getMethod());
		Components components = openAPI.getComponents();

		for (MethodParameter methodParameter : parameters) {
			// check if query param
			Parameter parameter;
			io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
					AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()),
					io.swagger.v3.oas.annotations.Parameter.class);

			final String pName = methodParameter.getParameterName();
			ParameterInfo parameterInfo = new ParameterInfo(pName, methodParameter, parameterBuilder);

			if (parameterDoc == null)
				parameterDoc = parametersDocMap.get(parameterInfo.getpName());
			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden() || parameterDoc.schema().hidden())
					continue;
				parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, components, methodAttributes.getJsonViewAnnotation());
				parameterInfo.setParameterModel(parameter);
			}

			if (!isParamToIgnore(methodParameter)) {
				parameter = buildParams(parameterInfo, parameters.length, components, requestMethod, methodAttributes.getJsonViewAnnotation());
				// Merge with the operation parameters
				parameter = GenericParameterService.mergeParameter(operationParameters, parameter);
				List<Annotation> parameterAnnotations = Arrays.asList(methodParameter.getParameterAnnotations());
				if (isValidParameter(parameter))
					applyBeanValidatorAnnotations(parameter, parameterAnnotations);
				else if (!RequestMethod.GET.equals(requestMethod)) {
					if (operation.getRequestBody() != null)
						requestBodyInfo.setRequestBody(operation.getRequestBody());
					requestBodyService.calculateRequestBodyInfo(components, methodAttributes,
							parameterInfo, requestBodyInfo);
					applyBeanValidatorAnnotations(requestBodyInfo.getRequestBody(), parameterAnnotations, methodParameter.isOptional());
				}
				customiseParameter(parameter, parameterInfo);
			}
		}

		LinkedHashMap<String, Parameter> map = getParameterLinkedHashMap(components, methodAttributes, operationParameters, parametersDocMap);
		setParams(operation, new ArrayList<>(map.values()), requestBodyInfo);
		return operation;
	}

	/**
	 * Gets parameter linked hash map.
	 *
	 * @param components the components
	 * @param methodAttributes the method attributes
	 * @param operationParameters the operation parameters
	 * @param parametersDocMap the parameters doc map
	 * @return the parameter linked hash map
	 */
	private LinkedHashMap<String, Parameter> getParameterLinkedHashMap(Components components, MethodAttributes methodAttributes, List<Parameter> operationParameters, Map<String, io.swagger.v3.oas.annotations.Parameter> parametersDocMap) {
		LinkedHashMap<String, Parameter> map = operationParameters.stream()
				.collect(Collectors.toMap(
						parameter -> parameter.getName() != null ? parameter.getName() : Integer.toString(parameter.hashCode()),
						parameter -> parameter,
						(u, v) -> {
							throw new IllegalStateException(String.format("Duplicate key %s", u));
						},
						LinkedHashMap::new
				));

		for (Map.Entry<String, io.swagger.v3.oas.annotations.Parameter> entry : parametersDocMap.entrySet()) {
			if (entry.getKey() != null && !map.containsKey(entry.getKey()) && !entry.getValue().hidden()) {
				//Convert
				Parameter parameter = parameterBuilder.buildParameterFromDoc(entry.getValue(), components,
						methodAttributes.getJsonViewAnnotation());
				map.put(entry.getKey(), parameter);
			}
		}

		getHeaders(methodAttributes, map);
		return map;
	}

	/**
	 * Gets headers.
	 *
	 * @param methodAttributes the method attributes
	 * @param map the map
	 * @return the headers
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Parameter> getHeaders(MethodAttributes methodAttributes, Map<String, Parameter> map) {
		for (Map.Entry<String, String> entry : methodAttributes.getHeaders().entrySet()) {
			StringSchema schema = new StringSchema();
			if (StringUtils.isNotEmpty(entry.getValue()))
				schema.addEnumItem(entry.getValue());
			Parameter parameter = new Parameter().in(ParameterIn.HEADER.toString()).name(entry.getKey()).schema(schema);
			if (map.containsKey(entry.getKey())) {
				parameter = map.get(entry.getKey());
				if (StringUtils.isNotEmpty(entry.getValue()))
					parameter.getSchema().addEnumItemObject(entry.getValue());
				parameter.setSchema(parameter.getSchema());
			}
			map.put(entry.getKey(), parameter);
		}
		return map.values();
	}

	/**
	 * Customise parameter parameter.
	 *
	 * @param parameter the parameter
	 * @param parameterInfo the parameter info
	 * @return the parameter
	 */
	protected Parameter customiseParameter(Parameter parameter, ParameterInfo parameterInfo) {
		parameterCustomizers.ifPresent(customizers -> customizers.forEach(customizer -> customizer.customize(parameter, parameterInfo.getMethodParameter())));
		return parameter;
	}

	/**
	 * Is param to ignore boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	public boolean isParamToIgnore(MethodParameter parameter) {
		if (SpringDocAnnotationsUtils.isAnnotationToIgnore(parameter))
			return true;
		if (isRequiredAnnotation(parameter))
			return false;
		return isRequestTypeToIgnore(parameter.getParameterType());
	}

	/**
	 * Is required annotation boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	private boolean isRequiredAnnotation(MethodParameter parameter) {
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		PathVariable pathVariable = parameter.getParameterAnnotation(PathVariable.class);
		org.springframework.web.bind.annotation.RequestBody requestBody = parameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class);
		return (requestParam != null && requestParam.required())
				|| (pathVariable != null && pathVariable.required())
				|| (requestBody != null && requestBody.required());
	}

	/**
	 * Sets params.
	 *
	 * @param operation the operation
	 * @param operationParameters the operation parameters
	 * @param requestBodyInfo the request body info
	 */
	private void setParams(Operation operation, List<Parameter> operationParameters, RequestBodyInfo requestBodyInfo) {
		if (!CollectionUtils.isEmpty(operationParameters))
			operation.setParameters(operationParameters);
		if (requestBodyInfo.getRequestBody() != null)
			operation.setRequestBody(requestBodyInfo.getRequestBody());
	}

	/**
	 * Is valid parameter boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	public boolean isValidParameter(Parameter parameter) {
		return parameter != null && (parameter.getName() != null || parameter.get$ref() != null);
	}

	/**
	 * Build params parameter.
	 *
	 * @param parameterInfo the parameter info
	 * @param length the length
	 * @param components the components
	 * @param requestMethod the request method
	 * @param jsonView the json view
	 * @return the parameter
	 */
	public Parameter buildParams(ParameterInfo parameterInfo, int length, Components components,
			RequestMethod requestMethod, JsonView jsonView) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		if (parameterInfo.getParamType() != null) {
			if (!ValueConstants.DEFAULT_NONE.equals(parameterInfo.getDefaultValue()))
				parameterInfo.setRequired(false);
			else
				parameterInfo.setDefaultValue(null);
			return this.buildParam(parameterInfo, components, jsonView);
		}
		// By default
		if (!isRequestBodyParam(requestMethod, parameterInfo, length)) {
			parameterInfo.setRequired(!methodParameter.isOptional());
			parameterInfo.setParamType(QUERY_PARAM);
			parameterInfo.setDefaultValue(null);
			return this.buildParam(parameterInfo, components, jsonView);
		}
		return null;
	}

	/**
	 * Build param parameter.
	 *
	 * @param parameterInfo the parameter info
	 * @param components the components
	 * @param jsonView the json view
	 * @return the parameter
	 */
	public Parameter buildParam(ParameterInfo parameterInfo, Components components, JsonView jsonView) {
		Parameter parameter = parameterInfo.getParameterModel();
		String name = parameterInfo.getpName();

		if (parameter == null) {
			parameter = new Parameter();
			parameterInfo.setParameterModel(parameter);
		}

		if (StringUtils.isBlank(parameter.getName()))
			parameter.setName(name);

		if (StringUtils.isBlank(parameter.getIn()))
			parameter.setIn(parameterInfo.getParamType());

		if (parameter.getRequired() == null)
			parameter.setRequired(parameterInfo.isRequired());

		if (containsDeprecatedAnnotation(parameterInfo.getMethodParameter().getParameterAnnotations()))
			parameter.setDeprecated(true);

		if (parameter.getSchema() == null && parameter.getContent() == null) {
			Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo, null,
					jsonView);
			if (parameterInfo.getDefaultValue() != null && schema != null) {
				Object defaultValue = parameterInfo.getDefaultValue();
				// Cast default value
				PrimitiveType primitiveType = PrimitiveType.fromTypeAndFormat(schema.getType(), schema.getFormat());
				if (primitiveType != null) {
					Schema<?> primitiveSchema = primitiveType.createProperty();
					primitiveSchema.setDefault(parameterInfo.getDefaultValue());
					defaultValue = primitiveSchema.getDefault();
				}
				schema.setDefault(defaultValue);
			}
			parameter.setSchema(schema);
		}
		return parameter;
	}

	/**
	 * Apply bean validator annotations.
	 *
	 * @param parameter the parameter
	 * @param annotations the annotations
	 */
	public void applyBeanValidatorAnnotations(final Parameter parameter, final List<Annotation> annotations) {
		Map<String, Annotation> annos = new HashMap<>();
		if (annotations != null)
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getSimpleName(), annotation));
		boolean annotationExists = Arrays.stream(ANNOTATIONS_FOR_REQUIRED).anyMatch(annos::containsKey);
		if (annotationExists)
			parameter.setRequired(true);
		Schema<?> schema = parameter.getSchema();
		applyValidationsToSchema(annos, schema);
	}

	/**
	 * Apply bean validator annotations.
	 *
	 * @param requestBody the request body
	 * @param annotations the annotations
	 * @param isOptional the is optional
	 */
	public void applyBeanValidatorAnnotations(final RequestBody requestBody, final List<Annotation> annotations, boolean isOptional) {
		Map<String, Annotation> annos = new HashMap<>();
		boolean requestBodyRequired = false;
		if (!CollectionUtils.isEmpty(annotations)) {
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getSimpleName(), annotation));
			requestBodyRequired = annotations.stream()
					.filter(annotation -> org.springframework.web.bind.annotation.RequestBody.class.equals(annotation.annotationType()))
					.anyMatch(annotation -> ((org.springframework.web.bind.annotation.RequestBody) annotation).required());
		}
		boolean validationExists = Arrays.stream(ANNOTATIONS_FOR_REQUIRED).anyMatch(annos::containsKey);

		if (validationExists || (!isOptional && requestBodyRequired))
			requestBody.setRequired(true);
		Content content = requestBody.getContent();
		for (MediaType mediaType : content.values()) {
			Schema<?> schema = mediaType.getSchema();
			applyValidationsToSchema(annos, schema);
		}
	}

	/**
	 * Calculate size.
	 *
	 * @param annos the annos
	 * @param schema the schema
	 */
	private void calculateSize(Map<String, Annotation> annos, Schema<?> schema) {
		if (annos.containsKey(Size.class.getSimpleName())) {
			Size size = (Size) annos.get(Size.class.getSimpleName());
			if (OPENAPI_ARRAY_TYPE.equals(schema.getType())) {
				schema.setMinItems(size.min());
				schema.setMaxItems(size.max());
			}
			else if (OPENAPI_STRING_TYPE.equals(schema.getType())) {
				schema.setMinLength(size.min());
				schema.setMaxLength(size.max());
			}
		}
	}

	/**
	 * Gets request body builder.
	 *
	 * @return the request body builder
	 */
	public RequestBodyService getRequestBodyBuilder() {
		return requestBodyService;
	}

	/**
	 * Gets api parameters.
	 *
	 * @param method the method
	 * @return the api parameters
	 */
	private Map<String, io.swagger.v3.oas.annotations.Parameter> getApiParameters(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		Set<io.swagger.v3.oas.annotations.Parameters> apiParametersDoc = AnnotatedElementUtils
				.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.Parameters.class);
		LinkedHashMap<String, io.swagger.v3.oas.annotations.Parameter> apiParametersMap = apiParametersDoc.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toMap(io.swagger.v3.oas.annotations.Parameter::name, x -> x, (e1, e2) -> e2,
						LinkedHashMap::new));

		Set<io.swagger.v3.oas.annotations.Parameters> apiParametersDocDeclaringClass = AnnotatedElementUtils
				.findAllMergedAnnotations(declaringClass, io.swagger.v3.oas.annotations.Parameters.class);
		LinkedHashMap<String, io.swagger.v3.oas.annotations.Parameter> apiParametersDocDeclaringClassMap = apiParametersDocDeclaringClass.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toMap(io.swagger.v3.oas.annotations.Parameter::name, x -> x, (e1, e2) -> e2,
						LinkedHashMap::new));
		apiParametersMap.putAll(apiParametersDocDeclaringClassMap);

		Set<io.swagger.v3.oas.annotations.Parameter> apiParameterDoc = AnnotatedElementUtils
				.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.Parameter.class);
		LinkedHashMap<String, io.swagger.v3.oas.annotations.Parameter> apiParameterDocMap = apiParameterDoc.stream()
				.collect(Collectors.toMap(io.swagger.v3.oas.annotations.Parameter::name, x -> x, (e1, e2) -> e2,
						LinkedHashMap::new));
		apiParametersMap.putAll(apiParameterDocMap);

		Set<io.swagger.v3.oas.annotations.Parameter> apiParameterDocDeclaringClass = AnnotatedElementUtils
				.findAllMergedAnnotations(declaringClass, io.swagger.v3.oas.annotations.Parameter.class);
		LinkedHashMap<String, io.swagger.v3.oas.annotations.Parameter> apiParameterDocDeclaringClassMap = apiParameterDocDeclaringClass.stream()
				.collect(Collectors.toMap(io.swagger.v3.oas.annotations.Parameter::name, x -> x, (e1, e2) -> e2,
						LinkedHashMap::new));
		apiParametersMap.putAll(apiParameterDocDeclaringClassMap);

		return apiParametersMap;
	}

	/**
	 * Apply validations to schema.
	 *
	 * @param annos the annos
	 * @param schema the schema
	 */
	private void applyValidationsToSchema(Map<String, Annotation> annos, Schema<?> schema) {
		if (annos.containsKey(Min.class.getSimpleName())) {
			Min min = (Min) annos.get(Min.class.getSimpleName());
			schema.setMinimum(BigDecimal.valueOf(min.value()));
		}
		if (annos.containsKey(Max.class.getSimpleName())) {
			Max max = (Max) annos.get(Max.class.getSimpleName());
			schema.setMaximum(BigDecimal.valueOf(max.value()));
		}
		calculateSize(annos, schema);
		if (annos.containsKey(DecimalMin.class.getSimpleName())) {
			DecimalMin min = (DecimalMin) annos.get(DecimalMin.class.getSimpleName());
			if (min.inclusive())
				schema.setMinimum(BigDecimal.valueOf(Double.parseDouble(min.value())));
			else
				schema.setExclusiveMinimum(true);
		}
		if (annos.containsKey(DecimalMax.class.getSimpleName())) {
			DecimalMax max = (DecimalMax) annos.get(DecimalMax.class.getSimpleName());
			if (max.inclusive())
				schema.setMaximum(BigDecimal.valueOf(Double.parseDouble(max.value())));
			else
				schema.setExclusiveMaximum(true);
		}
		if (annos.containsKey(POSITIVE_OR_ZERO))
			schema.setMinimum(BigDecimal.ZERO);
		if (annos.containsKey(NEGATIVE_OR_ZERO))
			schema.setMaximum(BigDecimal.ZERO);
		if (annos.containsKey(Pattern.class.getSimpleName())) {
			Pattern pattern = (Pattern) annos.get(Pattern.class.getSimpleName());
			schema.setPattern(pattern.regexp());
		}
	}

	/**
	 * Is RequestBody param boolean.
	 *
	 * @param requestMethod the request method
	 * @param parameterInfo the parameter info
	 * @param length the length
	 * @return the boolean
	 */
	private boolean isRequestBodyParam(RequestMethod requestMethod, ParameterInfo parameterInfo, int length) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		DelegatingMethodParameter delegatingMethodParameter = (DelegatingMethodParameter) methodParameter;

		return (!RequestMethod.GET.equals(requestMethod) && (parameterInfo.getParameterModel() == null || parameterInfo.getParameterModel().getIn() == null) && !delegatingMethodParameter.isParameterObject())
				&&
				((methodParameter.getParameterAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class) != null
						|| methodParameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class) != null)
						|| (!ClassUtils.isPrimitiveOrWrapper(methodParameter.getParameter().getType()) && (!ArrayUtils.isEmpty(methodParameter.getParameterAnnotations()) || length == 1)));
	}

}
