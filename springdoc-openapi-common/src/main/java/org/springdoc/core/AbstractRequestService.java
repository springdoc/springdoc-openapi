/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.providers.JavadocProvider;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.ClassUtils;
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
import static org.springdoc.core.converters.SchemaPropertyDeprecatingConverter.containsDeprecatedAnnotation;

/**
 * The type Abstract request builder.
 * @author bnasslahsen
 */
public abstract class AbstractRequestService {

	/**
	 * The constant PARAM_TYPES_TO_IGNORE.
	 */
	private static final List<Class<?>> PARAM_TYPES_TO_IGNORE = Collections.synchronizedList(new ArrayList<>());

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
	 * The Default flat param object.
	 */
	private final boolean defaultFlatParamObject;

	/**
	 * The Default support form data.
	 */
	private boolean defaultSupportFormData;


	/**
	 * Instantiates a new Abstract request builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @param requestBodyService the request body builder
	 * @param operationService the operation builder
	 * @param parameterCustomizers the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 */
	protected AbstractRequestService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService, OperationService operationService, Optional<List<ParameterCustomizer>> parameterCustomizers, LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyService = requestBodyService;
		this.operationService = operationService;
		parameterCustomizers.ifPresent(customizers -> customizers.removeIf(Objects::isNull));
		this.parameterCustomizers = parameterCustomizers;
		this.localSpringDocParameterNameDiscoverer = localSpringDocParameterNameDiscoverer;
		this.defaultFlatParamObject = parameterBuilder.getPropertyResolverUtils().getSpringDocConfigProperties().isDefaultFlatParamObject();
		this.defaultSupportFormData = parameterBuilder.getPropertyResolverUtils().getSpringDocConfigProperties().isDefaultSupportFormData();
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
	 * Gets headers.
	 *
	 * @param methodAttributes the method attributes
	 * @param map the map
	 * @return the headers
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Parameter> getHeaders(MethodAttributes methodAttributes, Map<ParameterId, Parameter> map) {
		for (Map.Entry<String, String> entry : methodAttributes.getHeaders().entrySet()) {
			StringSchema schema = new StringSchema();
			if (StringUtils.isNotEmpty(entry.getValue()))
				schema.addEnumItem(entry.getValue());
			Parameter parameter = new Parameter().in(ParameterIn.HEADER.toString()).name(entry.getKey()).schema(schema);
			ParameterId parameterId = new ParameterId(parameter);
			if (map.containsKey(parameterId)) {
				parameter = map.get(parameterId);
				List existingEnum = null;
				if (parameter.getSchema() != null && !CollectionUtils.isEmpty(parameter.getSchema().getEnum()))
					existingEnum = parameter.getSchema().getEnum();
				if (StringUtils.isNotEmpty(entry.getValue()) && (existingEnum == null || !existingEnum.contains(entry.getValue())))
					parameter.getSchema().addEnumItemObject(entry.getValue());
				parameter.setSchema(parameter.getSchema());
			}
			map.put(parameterId, parameter);
		}
		return map.values();
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
	public Operation build(HandlerMethod handlerMethod, RequestMethod requestMethod, Operation operation, MethodAttributes methodAttributes, OpenAPI openAPI) {
		// Documentation
		String operationId = operationService.getOperationId(handlerMethod.getMethod().getName(), operation.getOperationId(), openAPI);
		operation.setOperationId(operationId);
		// requests
		String[] pNames = this.localSpringDocParameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		String[] reflectionParametersNames = Arrays.stream(handlerMethod.getMethod().getParameters()).map(java.lang.reflect.Parameter::getName).toArray(String[]::new);
		if (pNames == null || Arrays.stream(pNames).anyMatch(Objects::isNull))
			pNames = reflectionParametersNames;
		parameters = DelegatingMethodParameter.customize(pNames, parameters, parameterBuilder.getDelegatingMethodParameterCustomizer(), this.defaultFlatParamObject);
		RequestBodyInfo requestBodyInfo = new RequestBodyInfo();
		List<Parameter> operationParameters = (operation.getParameters() != null) ? operation.getParameters() : new ArrayList<>();
		Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> parametersDocMap = getApiParameters(handlerMethod.getMethod());
		Components components = openAPI.getComponents();

		JavadocProvider javadocProvider = parameterBuilder.getJavadocProvider();

		for (MethodParameter methodParameter : parameters) {
			// check if query param
			Parameter parameter;
			io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotatedElementUtils.findMergedAnnotation(AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()), io.swagger.v3.oas.annotations.Parameter.class);

			final String pName = methodParameter.getParameterName();
			ParameterInfo parameterInfo = new ParameterInfo(pName, methodParameter, parameterBuilder, parameterDoc);

			if (parameterDoc == null)
				parameterDoc = parametersDocMap.get(parameterInfo.getParameterId());

			if (parameterDoc == null) {
				io.swagger.v3.oas.annotations.media.Schema schema = AnnotatedElementUtils.findMergedAnnotation(AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()), io.swagger.v3.oas.annotations.media.Schema.class);
				if (schema != null) {
					parameterDoc = parameterBuilder.generateParameterBySchema(schema);
				}
			}

			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden() || parameterDoc.schema().hidden()) continue;

				parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, components, methodAttributes.getJsonViewAnnotation(), methodAttributes.getLocale());
				parameterInfo.setParameterModel(parameter);
			}

			if (!isParamToIgnore(methodParameter)) {
				parameter = buildParams(parameterInfo, components, requestMethod, methodAttributes.getJsonViewAnnotation(), openAPI.getOpenapi());
				// Merge with the operation parameters
				parameter = GenericParameterService.mergeParameter(operationParameters, parameter);
				List<Annotation> parameterAnnotations = Arrays.asList(methodParameter.getParameterAnnotations());
				if (isValidParameter(parameter)) {
					// Add param javadoc
					if (StringUtils.isBlank(parameter.getDescription()) && javadocProvider != null) {
						String paramJavadocDescription = parameterBuilder.getParamJavadoc(javadocProvider, methodParameter);
						if (!StringUtils.isBlank(paramJavadocDescription)) {
							parameter.setDescription(paramJavadocDescription);
						}
					}
					applyBeanValidatorAnnotations(parameter, parameterAnnotations);
				}
				else if (!RequestMethod.GET.equals(requestMethod) || OpenApiVersion.OPENAPI_3_1.getVersion().equals(openAPI.getOpenapi())) {
					if (operation.getRequestBody() != null)
						requestBodyInfo.setRequestBody(operation.getRequestBody());
					requestBodyService.calculateRequestBodyInfo(components, methodAttributes, parameterInfo, requestBodyInfo);
					applyBeanValidatorAnnotations(requestBodyInfo.getRequestBody(), parameterAnnotations, methodParameter.isOptional());
				}
				customiseParameter(parameter, parameterInfo, operationParameters);
			}
		}

		LinkedHashMap<ParameterId, Parameter> map = getParameterLinkedHashMap(components, methodAttributes, operationParameters, parametersDocMap);
		RequestBody requestBody = requestBodyInfo.getRequestBody();
		// support form-data
		if (defaultSupportFormData && requestBody != null && requestBody.getContent() != null && requestBody.getContent().containsKey(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)) {
			Iterator<Entry<ParameterId, Parameter>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ParameterId, Parameter> entry = it.next();
				Parameter parameter = entry.getValue();
				if (!ParameterIn.PATH.toString().equals(parameter.getIn()) && !ParameterIn.HEADER.toString().equals(parameter.getIn()) && !ParameterIn.COOKIE.toString().equals(parameter.getIn())) {
					io.swagger.v3.oas.models.media.Schema<?> itemSchema = new io.swagger.v3.oas.models.media.Schema<>();
					itemSchema.setName(entry.getKey().getpName());
					itemSchema.setDescription(parameter.getDescription());
					itemSchema.setDeprecated(parameter.getDeprecated());
					if (parameter.getExample() != null)
						itemSchema.setExample(parameter.getExample());
					requestBodyInfo.addProperties(entry.getKey().getpName(), itemSchema);
					it.remove();
				}
			}
		}
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
	private LinkedHashMap<ParameterId, Parameter> getParameterLinkedHashMap(Components components, MethodAttributes methodAttributes, List<Parameter> operationParameters, Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> parametersDocMap) {
		LinkedHashMap<ParameterId, Parameter> map = operationParameters.stream().collect(Collectors.toMap(ParameterId::new, parameter -> parameter, (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		}, LinkedHashMap::new));

		for (Map.Entry<ParameterId, io.swagger.v3.oas.annotations.Parameter> entry : parametersDocMap.entrySet()) {
			ParameterId parameterId = entry.getKey();
			if (parameterId != null && !map.containsKey(parameterId) && !entry.getValue().hidden()) {
				Parameter parameter = parameterBuilder.buildParameterFromDoc(entry.getValue(), components, methodAttributes.getJsonViewAnnotation(), methodAttributes.getLocale());
				//proceed with the merge if possible
				if (map.containsKey(parameterId)) {
					GenericParameterService.mergeParameter(map.get(parameterId), parameter);
					map.put(parameterId, parameter);
				}
				else {
					long mumParamsWithName = map.keySet().stream().filter(parameterId1 -> parameterId.getpName().equals(parameterId1.getpName())).count();
					long mumParamsDocWithName = parametersDocMap.keySet().stream().filter(parameterId1 -> parameterId.getpName().equals(parameterId1.getpName())).count();
					if (mumParamsWithName == 1 && mumParamsDocWithName == 1) {
						Optional<ParameterId> parameterIdWithSameNameOptional = map.keySet().stream().filter(parameterId1 -> parameterId.getpName().equals(parameterId1.getpName())).findAny();
						parameterIdWithSameNameOptional.ifPresent(parameterIdWithSameName -> {
							GenericParameterService.mergeParameter(map.get(parameterIdWithSameName), parameter);
							map.put(parameterIdWithSameName, parameter);
						});
					}
					else
						map.put(parameterId, parameter);
				}
			}
		}

		getHeaders(methodAttributes, map);
		map.forEach((parameterId, parameter) -> {
			if (StringUtils.isBlank(parameter.getIn()) && StringUtils.isBlank(parameter.get$ref()))
				parameter.setIn(ParameterIn.QUERY.toString());
		});
		return map;
	}

	/**
	 * Customise parameter parameter.
	 *
	 * @param parameter the parameter
	 * @param parameterInfo the parameter info
	 * @param operationParameters the operation parameters
	 */
	protected void customiseParameter(Parameter parameter, ParameterInfo parameterInfo, List<Parameter> operationParameters) {
		if (parameterCustomizers.isPresent()) {
			List<ParameterCustomizer> parameterCustomizerList = parameterCustomizers.get();
			int index = operationParameters.indexOf(parameter);
			for (ParameterCustomizer parameterCustomizer : parameterCustomizerList)
				parameter = parameterCustomizer.customize(parameter, parameterInfo.getMethodParameter());
			if (index != -1) operationParameters.set(index, parameter);
		}
	}

	/**
	 * Is param to ignore boolean.
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	public boolean isParamToIgnore(MethodParameter parameter) {
		if (SpringDocAnnotationsUtils.isAnnotationToIgnore(parameter)) return true;
		if (isRequiredAnnotation(parameter)) return false;
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
		return (requestParam != null && requestParam.required()) || (pathVariable != null && pathVariable.required()) || (requestBody != null && requestBody.required());
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
	 * @param components the components
	 * @param requestMethod the request method
	 * @param jsonView the json view
	 * @param openApiVersion the open api version
	 * @return the parameter
	 */
	public Parameter buildParams(ParameterInfo parameterInfo, Components components, RequestMethod requestMethod, JsonView jsonView, String openApiVersion) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		if (parameterInfo.getParamType() != null) {
			if (!ValueConstants.DEFAULT_NONE.equals(parameterInfo.getDefaultValue()))
				parameterInfo.setRequired(false);
			else
				parameterInfo.setDefaultValue(null);
			return this.buildParam(parameterInfo, components, jsonView);
		}
		// By default
		if (!isRequestBodyParam(requestMethod, parameterInfo, openApiVersion)) {
			parameterInfo.setRequired(!((DelegatingMethodParameter) methodParameter).isNotRequired() && !methodParameter.isOptional());
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

		if (StringUtils.isBlank(parameter.getName())) parameter.setName(name);

		if (StringUtils.isBlank(parameter.getIn()))
			parameter.setIn(parameterInfo.getParamType());

		if (parameter.getRequired() == null)
			parameter.setRequired(parameterInfo.isRequired());

		if (containsDeprecatedAnnotation(parameterInfo.getMethodParameter().getParameterAnnotations()))
			parameter.setDeprecated(true);

		if (parameter.getSchema() == null && parameter.getContent() == null) {
			Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo, null, jsonView);
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
		if (annotationExists) parameter.setRequired(true);
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
			requestBodyRequired = annotations.stream().filter(annotation -> org.springframework.web.bind.annotation.RequestBody.class.equals(annotation.annotationType())).anyMatch(annotation -> ((org.springframework.web.bind.annotation.RequestBody) annotation).required());
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
	private Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> getApiParameters(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		Set<io.swagger.v3.oas.annotations.Parameters> apiParametersDoc = AnnotatedElementUtils.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.Parameters.class);
		LinkedHashMap<ParameterId, io.swagger.v3.oas.annotations.Parameter> apiParametersMap = apiParametersDoc.stream().flatMap(x -> Stream.of(x.value())).collect(Collectors.toMap(ParameterId::new, x -> x, (e1, e2) -> e2, LinkedHashMap::new));

		Set<io.swagger.v3.oas.annotations.Parameters> apiParametersDocDeclaringClass = AnnotatedElementUtils.findAllMergedAnnotations(declaringClass, io.swagger.v3.oas.annotations.Parameters.class);
		LinkedHashMap<ParameterId, io.swagger.v3.oas.annotations.Parameter> apiParametersDocDeclaringClassMap = apiParametersDocDeclaringClass.stream().flatMap(x -> Stream.of(x.value())).collect(Collectors.toMap(ParameterId::new, x -> x, (e1, e2) -> e2, LinkedHashMap::new));
		apiParametersMap.putAll(apiParametersDocDeclaringClassMap);

		Set<io.swagger.v3.oas.annotations.Parameter> apiParameterDoc = AnnotatedElementUtils.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.Parameter.class);
		LinkedHashMap<ParameterId, io.swagger.v3.oas.annotations.Parameter> apiParameterDocMap = apiParameterDoc.stream().collect(Collectors.toMap(ParameterId::new, x -> x, (e1, e2) -> e2, LinkedHashMap::new));
		apiParametersMap.putAll(apiParameterDocMap);

		Set<io.swagger.v3.oas.annotations.Parameter> apiParameterDocDeclaringClass = AnnotatedElementUtils.findAllMergedAnnotations(declaringClass, io.swagger.v3.oas.annotations.Parameter.class);
		LinkedHashMap<ParameterId, io.swagger.v3.oas.annotations.Parameter> apiParameterDocDeclaringClassMap = apiParameterDocDeclaringClass.stream().collect(Collectors.toMap(ParameterId::new, x -> x, (e1, e2) -> e2, LinkedHashMap::new));
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
			else schema.setExclusiveMinimum(true);
		}
		if (annos.containsKey(DecimalMax.class.getSimpleName())) {
			DecimalMax max = (DecimalMax) annos.get(DecimalMax.class.getSimpleName());
			if (max.inclusive())
				schema.setMaximum(BigDecimal.valueOf(Double.parseDouble(max.value())));
			else schema.setExclusiveMaximum(true);
		}
		if (annos.containsKey(POSITIVE_OR_ZERO)) schema.setMinimum(BigDecimal.ZERO);
		if (annos.containsKey(NEGATIVE_OR_ZERO)) schema.setMaximum(BigDecimal.ZERO);
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
	 * @param openApiVersion the open api version
	 * @return the boolean
	 */
	private boolean isRequestBodyParam(RequestMethod requestMethod, ParameterInfo parameterInfo, String openApiVersion) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		DelegatingMethodParameter delegatingMethodParameter = (DelegatingMethodParameter) methodParameter;
		Boolean isBodyAllowed = !RequestMethod.GET.equals(requestMethod) || OpenApiVersion.OPENAPI_3_1.getVersion().equals(openApiVersion);

		return (isBodyAllowed && (parameterInfo.getParameterModel() == null || parameterInfo.getParameterModel().getIn() == null) && !delegatingMethodParameter.isParameterObject()) && ((methodParameter.getParameterAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class) != null || methodParameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class) != null || methodParameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestPart.class) != null || AnnotatedElementUtils.findMergedAnnotation(Objects.requireNonNull(methodParameter.getMethod()), io.swagger.v3.oas.annotations.parameters.RequestBody.class) != null) || (!ClassUtils.isPrimitiveOrWrapper(methodParameter.getParameterType()) && (!ArrayUtils.isEmpty(methodParameter.getParameterAnnotations()))));
	}

	/**
	 * Is default flat param object boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDefaultFlatParamObject() {
		return defaultFlatParamObject;
	}
}
