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

package org.springdoc.core.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Principal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.extractor.DelegatingMethodParameter;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.models.ParameterId;
import org.springdoc.core.models.ParameterInfo;
import org.springdoc.core.models.RequestBodyInfo;
import org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.utils.SchemaUtils;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springdoc.core.converters.SchemaPropertyDeprecatingConverter.containsDeprecatedAnnotation;
import static org.springdoc.core.service.GenericParameterService.isFile;
import static org.springdoc.core.utils.SpringDocUtils.getParameterAnnotations;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * The type Abstract request builder.
 *
 * @author bnasslahsen
 */
public abstract class AbstractRequestService {

	/**
	 * The constant PARAM_TYPES_TO_IGNORE.
	 */
	private static final List<Class<?>> PARAM_TYPES_TO_IGNORE = Collections.synchronizedList(new ArrayList<>());

	static {
		PARAM_TYPES_TO_IGNORE.add(WebRequest.class);
		PARAM_TYPES_TO_IGNORE.add(NativeWebRequest.class);
		PARAM_TYPES_TO_IGNORE.add(Principal.class);
		PARAM_TYPES_TO_IGNORE.add(HttpMethod.class);
		PARAM_TYPES_TO_IGNORE.add(Locale.class);
		PARAM_TYPES_TO_IGNORE.add(TimeZone.class);
		PARAM_TYPES_TO_IGNORE.add(InputStream.class);
		PARAM_TYPES_TO_IGNORE.add(ZoneId.class);
		PARAM_TYPES_TO_IGNORE.add(Reader.class);
		PARAM_TYPES_TO_IGNORE.add(OutputStream.class);
		PARAM_TYPES_TO_IGNORE.add(Writer.class);
		PARAM_TYPES_TO_IGNORE.add(Map.class);
		PARAM_TYPES_TO_IGNORE.add(Model.class);
		PARAM_TYPES_TO_IGNORE.add(ModelMap.class);
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
	 * The Local spring doc parameter name discoverer.
	 */
	private final SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer;

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
	 * @param parameterBuilder                      the parameter builder
	 * @param requestBodyService                    the request body builder
	 * @param parameterCustomizers                  the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 */
	protected AbstractRequestService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			Optional<List<ParameterCustomizer>> parameterCustomizers,
			SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		super();
		this.parameterBuilder = parameterBuilder;
		this.requestBodyService = requestBodyService;
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
	 * @param map              the map
	 * @return the headers
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Parameter> getHeaders(MethodAttributes methodAttributes, Map<ParameterId, Parameter> map) {
		for (Entry<String, String> entry : methodAttributes.getHeaders().entrySet()) {
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
	 * @param handlerMethod    the handler method
	 * @param requestMethod    the request method
	 * @param operation        the operation
	 * @param methodAttributes the method attributes
	 * @param openAPI          the open api
	 * @return the operation
	 * @see org.springdoc.core.customizers.DelegatingMethodParameterCustomizer#customizeList(MethodParameter, List)
	 * @see ParameterCustomizer#customize(Parameter, MethodParameter)
	 * @see org.springdoc.core.customizers.PropertyCustomizer#customize(Schema, AnnotatedType)
	 */
	public Operation build(HandlerMethod handlerMethod, RequestMethod requestMethod,
			Operation operation, MethodAttributes methodAttributes, OpenAPI openAPI) {
		// Documentation
		String operationId = operation.getOperationId() != null ? operation.getOperationId() : handlerMethod.getMethod().getName();
		operation.setOperationId(operationId);
		// requests
		String[] pNames = this.localSpringDocParameterNameDiscoverer.getParameterNames(handlerMethod.getMethod());
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		String[] reflectionParametersNames = Arrays.stream(handlerMethod.getMethod().getParameters()).map(java.lang.reflect.Parameter::getName).toArray(String[]::new);
		if (pNames == null || Arrays.stream(pNames).anyMatch(Objects::isNull))
			pNames = reflectionParametersNames;
		// Process: DelegatingMethodParameterCustomizer
		parameters = DelegatingMethodParameter.customize(pNames, parameters, parameterBuilder.getOptionalDelegatingMethodParameterCustomizers(), this.defaultFlatParamObject);
		RequestBodyInfo requestBodyInfo = new RequestBodyInfo();
		List<Parameter> operationParameters = (operation.getParameters() != null) ? operation.getParameters() : new ArrayList<>();
		Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> parametersDocMap = getApiParameters(handlerMethod.getMethod());
		Components components = openAPI.getComponents();

		JavadocProvider javadocProvider = parameterBuilder.getJavadocProvider();

		for (MethodParameter methodParameter : parameters) {
			// check if query param
			Parameter parameter;
			io.swagger.v3.oas.annotations.Parameter parameterDoc = AnnotatedElementUtils.findMergedAnnotation(
					AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()),
					io.swagger.v3.oas.annotations.Parameter.class);

			final String pName = methodParameter.getParameterName();
			ParameterInfo parameterInfo = new ParameterInfo(pName, methodParameter, parameterBuilder, parameterDoc);

			if (parameterDoc == null)
				parameterDoc = parametersDocMap.get(parameterInfo.getParameterId());

			if (parameterDoc == null) {
				io.swagger.v3.oas.annotations.media.Schema schema = AnnotatedElementUtils.findMergedAnnotation(
						AnnotatedElementUtils.forAnnotations(methodParameter.getParameterAnnotations()), io.swagger.v3.oas.annotations.media.Schema.class);
				if (schema != null) {
					parameterDoc = parameterBuilder.generateParameterBySchema(schema);
				}
			}

			// use documentation as reference
			if (parameterDoc != null) {
				if (parameterDoc.hidden() || parameterDoc.schema().hidden())
					continue;

				parameter = parameterBuilder.buildParameterFromDoc(parameterDoc, components, methodAttributes.getJsonViewAnnotation(), methodAttributes.getLocale());
				parameterInfo.setParameterModel(parameter);
			}

			if (!isParamToIgnore(methodParameter)) {
				parameter = buildParams(parameterInfo, components, requestMethod, methodAttributes, openAPI.getOpenapi());
				List<Annotation> parameterAnnotations = List.of(getParameterAnnotations(methodParameter));
				if (isValidParameter(parameter, methodAttributes)) {
					// Merge with the operation parameters
					parameter = GenericParameterService.mergeParameter(operationParameters, parameter);
					// Add param javadoc
					if (StringUtils.isBlank(parameter.getDescription()) && javadocProvider != null) {
						String paramJavadocDescription = parameterBuilder.getParamJavadoc(javadocProvider, methodParameter);
						if (!StringUtils.isBlank(paramJavadocDescription)) {
							parameter.setDescription(paramJavadocDescription);
						}
					}
					// Process: applyValidationsToSchema
					applyBeanValidatorAnnotations(methodParameter, parameter, parameterAnnotations, parameterInfo.isParameterObject(), openAPI.getOpenapi());
				}
				else if (!RequestMethod.GET.equals(requestMethod) || OpenApiVersion.OPENAPI_3_1.getVersion().equals(openAPI.getOpenapi())) {
					if (operation.getRequestBody() != null)
						requestBodyInfo.setRequestBody(operation.getRequestBody());
					// Process: PropertyCustomizer
					requestBodyService.calculateRequestBodyInfo(components, methodAttributes,
							parameterInfo, requestBodyInfo);
					applyBeanValidatorAnnotations(requestBodyInfo.getRequestBody(), parameterAnnotations, methodParameter.isOptional());
				}
				// Process: ParameterCustomizer
				customiseParameter(parameter, parameterInfo, operationParameters);
			}
		}

		LinkedHashMap<ParameterId, Parameter> map = getParameterLinkedHashMap(components, methodAttributes, operationParameters, parametersDocMap);
		RequestBody requestBody = requestBodyInfo.getRequestBody();
		// support form-data
		if (defaultSupportFormData && requestBody != null
				&& requestBody.getContent() != null
				&& requestBody.getContent().containsKey(MULTIPART_FORM_DATA_VALUE)) {
			Iterator<Entry<ParameterId, Parameter>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<ParameterId, Parameter> entry = it.next();
				Parameter parameter = entry.getValue();
				if (!ParameterIn.PATH.toString().equals(parameter.getIn()) && !ParameterIn.HEADER.toString().equals(parameter.getIn())
						&& !ParameterIn.COOKIE.toString().equals(parameter.getIn())) {
					Schema<?> itemSchema;
					if (parameter.getSchema() != null) {
						itemSchema = parameter.getSchema();
					}
					else {
						itemSchema = new Schema<>();
						itemSchema.setName(entry.getKey().getpName());
					}
					if (StringUtils.isNotEmpty(parameter.getDescription()))
						itemSchema.setDescription(parameter.getDescription());
					if (parameter.getExample() != null)
						itemSchema.setExample(parameter.getExample());
					if (parameter.getDeprecated() != null)
						itemSchema.setDeprecated(parameter.getDeprecated());
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
	 * @param components          the components
	 * @param methodAttributes    the method attributes
	 * @param operationParameters the operation parameters
	 * @param parametersDocMap    the parameters doc map
	 * @return the parameter linked hash map
	 */
	private LinkedHashMap<ParameterId, Parameter> getParameterLinkedHashMap(Components components, MethodAttributes methodAttributes, List<Parameter> operationParameters, Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> parametersDocMap) {
		LinkedHashMap<ParameterId, Parameter> map = operationParameters.stream().collect(Collectors.toMap(ParameterId::new, parameter -> parameter, (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		}, LinkedHashMap::new));

		for (Entry<ParameterId, io.swagger.v3.oas.annotations.Parameter> entry : parametersDocMap.entrySet()) {
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
	 * @param parameter           the parameter
	 * @param parameterInfo       the parameter info
	 * @param operationParameters the operation parameters
	 */
	protected void customiseParameter(Parameter parameter, ParameterInfo parameterInfo, List<Parameter> operationParameters) {
		if (parameterCustomizers.isPresent()) {
			List<ParameterCustomizer> parameterCustomizerList = parameterCustomizers.get();
			int index = operationParameters.indexOf(parameter);
			for (ParameterCustomizer parameterCustomizer : parameterCustomizerList)
				parameter = parameterCustomizer.customize(parameter, parameterInfo.getMethodParameter());
			if (index != -1) {
				if (parameter == null)
					operationParameters.remove(index);
				else
					operationParameters.set(index, parameter);
			}
		}
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
		if (isRequestBodyWithMapType(parameter))
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
	 * Is request body with map type
	 *
	 * @param parameter the parameter
	 * @return the boolean
	 */
	private boolean isRequestBodyWithMapType(MethodParameter parameter) {
		// Exclude parameters from the Actuator package
		if (parameter.getContainingClass().getPackageName().startsWith("org.springframework.boot.actuate")) {
			return false;
		}
		// Check for @RequestBody annotation
		org.springframework.web.bind.annotation.RequestBody requestBody = parameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class);
		if (requestBody == null) {
			return false;
		}
		// Check if the parameter type is assignable to Map
		Class<?> parameterType = parameter.getParameterType();
		return Map.class.isAssignableFrom(parameterType);
	}

	/**
	 * Sets params.
	 *
	 * @param operation           the operation
	 * @param operationParameters the operation parameters
	 * @param requestBodyInfo     the request body info
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
	 * @param parameter        the parameter
	 * @param methodAttributes the method attributes
	 * @return the boolean
	 */
	public boolean isValidParameter(Parameter parameter, MethodAttributes methodAttributes) {
		return parameter != null && (parameter.getName() != null || parameter.get$ref() != null) && !(Arrays.asList(methodAttributes.getMethodConsumes()).contains(APPLICATION_FORM_URLENCODED_VALUE) && ParameterIn.QUERY.toString().equals(parameter.getIn()));
	}

	/**
	 * Build params parameter.
	 *
	 * @param parameterInfo    the parameter info
	 * @param components       the components
	 * @param requestMethod    the request method
	 * @param methodAttributes the method attributes
	 * @param openApiVersion   the open api version
	 * @return the parameter
	 */
	public Parameter buildParams(ParameterInfo parameterInfo, Components components,
			RequestMethod requestMethod, MethodAttributes methodAttributes, String openApiVersion) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		if (parameterInfo.getParamType() != null) {
			if (!ValueConstants.DEFAULT_NONE.equals(parameterInfo.getDefaultValue()))
				parameterInfo.setRequired(false);
			else
				parameterInfo.setDefaultValue(null);
			return this.buildParam(parameterInfo, components, methodAttributes.getJsonViewAnnotation());
		}
		// By default
		if (!isRequestBodyParam(requestMethod, parameterInfo, openApiVersion, methodAttributes)) {
			parameterInfo.setRequired(!((DelegatingMethodParameter) methodParameter).isNotRequired() && !methodParameter.isOptional());
			parameterInfo.setDefaultValue(null);
			return this.buildParam(parameterInfo, components, methodAttributes.getJsonViewAnnotation());
		}
		return null;
	}

	/**
	 * Build param parameter.
	 *
	 * @param parameterInfo the parameter info
	 * @param components    the components
	 * @param jsonView      the json view
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
	 * @param methodParameter   the method parameter
	 * @param parameter         the parameter
	 * @param annotations       the annotations
	 * @param isParameterObject the is parameter object
	 * @param openapiVersion    the openapi version
	 */
	public void applyBeanValidatorAnnotations(final MethodParameter methodParameter, final Parameter parameter, final List<Annotation> annotations, final boolean isParameterObject, String openapiVersion) {
		boolean annotatedNotNull = annotations != null && SchemaUtils.annotatedNotNull(annotations);
		if (annotatedNotNull && !isParameterObject) {
			parameter.setRequired(true);
		}
		if (annotations != null) {
			Schema<?> schema = parameter.getSchema();
			SchemaUtils.applyValidationsToSchema(schema, annotations, openapiVersion);
			if (schema instanceof ArraySchema && methodParameter instanceof DelegatingMethodParameter mp) {
				java.lang.reflect.AnnotatedType annotatedType = null;
				if (isParameterObject) {
					Field field = mp.getField();
					if (field != null) {
						annotatedType = field.getAnnotatedType();
					}
				} else {
					java.lang.reflect.Parameter param = mp.getParameter();
					annotatedType = param.getAnnotatedType();
				}
				if (annotatedType instanceof AnnotatedParameterizedType paramType) {
					java.lang.reflect.AnnotatedType[] typeArgs = paramType.getAnnotatedActualTypeArguments();
					for (java.lang.reflect.AnnotatedType typeArg : typeArgs) {
						List<Annotation> genericAnnotations = Arrays.stream(typeArg.getAnnotations()).toList();
						SchemaUtils.applyValidationsToSchema(schema.getItems(), genericAnnotations, openapiVersion);
					}
				}
			}
		}
	}

	/**
	 * Apply bean validator annotations.
	 *
	 * @param requestBody the request body
	 * @param annotations the annotations
	 * @param isOptional  the is optional
	 */
	public void applyBeanValidatorAnnotations(final RequestBody requestBody, final List<Annotation> annotations, boolean isOptional) {
		Map<String, Annotation> annos = new HashMap<>();
		boolean springRequestBodyRequired = false;
		boolean swaggerRequestBodyRequired = false;
		if (!CollectionUtils.isEmpty(annotations)) {
			annotations.forEach(annotation -> annos.put(annotation.annotationType().getSimpleName(), annotation));
			springRequestBodyRequired = annotations.stream()
					.filter(annotation -> org.springframework.web.bind.annotation.RequestBody.class.equals(annotation.annotationType()))
					.anyMatch(annotation -> ((org.springframework.web.bind.annotation.RequestBody) annotation).required());
			swaggerRequestBodyRequired = annotations.stream()
					.filter(annotation -> io.swagger.v3.oas.annotations.parameters.RequestBody.class.equals(annotation.annotationType()))
					.anyMatch(annotation -> ((io.swagger.v3.oas.annotations.parameters.RequestBody) annotation).required());
		}
		boolean validationExists = SchemaUtils.annotatedNotNull(annos.values().stream().toList());

		if (validationExists || (!isOptional && (springRequestBodyRequired || swaggerRequestBodyRequired)))
			requestBody.setRequired(true);
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
	 * Is default flat param object boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDefaultFlatParamObject() {
		return defaultFlatParamObject;
	}

	/**
	 * Gets parameter builder.
	 *
	 * @return the parameter builder
	 */
	public GenericParameterService getParameterBuilder() {
		return parameterBuilder;
	}

	/**
	 * Gets api parameters.
	 *
	 * @param method the method
	 * @return the api parameters
	 */
	private Map<ParameterId, io.swagger.v3.oas.annotations.Parameter> getApiParameters(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		Set<Parameters> apiParametersDoc = AnnotatedElementUtils.findAllMergedAnnotations(method, Parameters.class);
		LinkedHashMap<ParameterId, io.swagger.v3.oas.annotations.Parameter> apiParametersMap = apiParametersDoc.stream().flatMap(x -> Stream.of(x.value())).collect(Collectors.toMap(ParameterId::new, x -> x, (e1, e2) -> e2, LinkedHashMap::new));

		Set<Parameters> apiParametersDocDeclaringClass = AnnotatedElementUtils.findAllMergedAnnotations(declaringClass, Parameters.class);
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
	 * Is RequestBody param boolean.
	 *
	 * @param requestMethod    the request method
	 * @param parameterInfo    the parameter info
	 * @param openApiVersion   the open api version
	 * @param methodAttributes the method attributes
	 * @return the boolean
	 */
	private boolean isRequestBodyParam(RequestMethod requestMethod, ParameterInfo parameterInfo, String openApiVersion, MethodAttributes methodAttributes) {
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		DelegatingMethodParameter delegatingMethodParameter = (DelegatingMethodParameter) methodParameter;
		boolean isBodyAllowed = !RequestMethod.GET.equals(requestMethod) || OpenApiVersion.OPENAPI_3_1.getVersion().equals(openApiVersion);

		return (isBodyAllowed && (parameterInfo.getParameterModel() == null || parameterInfo.getParameterModel().getIn() == null) && !delegatingMethodParameter.isParameterObject())
				&&
				(checkRequestBodyAnnotation(methodParameter)
						|| checkOperationRequestBody(methodParameter)
						|| checkFile(methodParameter)
						|| Arrays.asList(methodAttributes.getMethodConsumes()).contains(MULTIPART_FORM_DATA_VALUE));
	}

	/**
	 * Checks whether Swagger's or Spring's RequestBody annotation is present on a parameter or method
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean checkRequestBodyAnnotation(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class)
				|| methodParameter.hasParameterAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class)
				|| AnnotatedElementUtils.isAnnotated(Objects.requireNonNull(methodParameter.getParameter()), io.swagger.v3.oas.annotations.parameters.RequestBody.class)
				|| AnnotatedElementUtils.isAnnotated(Objects.requireNonNull(methodParameter.getMethod()), io.swagger.v3.oas.annotations.parameters.RequestBody.class);
	}

	/**
	 * Check file boolean.
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean checkFile(MethodParameter methodParameter) {
		if (methodParameter.getParameterAnnotation(RequestPart.class) != null)
			return true;
		else if (methodParameter.getParameterAnnotation(RequestParam.class) != null) {
			return isFile(methodParameter.getParameterType());
		}
		return false;
	}

	/**
	 * Check operation request body boolean.
	 *
	 * @param methodParameter the method parameter
	 * @return the boolean
	 */
	private boolean checkOperationRequestBody(MethodParameter methodParameter) {
		if (AnnotatedElementUtils.findMergedAnnotation(Objects.requireNonNull(methodParameter.getMethod()), io.swagger.v3.oas.annotations.Operation.class) != null) {
			io.swagger.v3.oas.annotations.Operation operation = AnnotatedElementUtils.findMergedAnnotation(Objects.requireNonNull(methodParameter.getMethod()), io.swagger.v3.oas.annotations.Operation.class);
			if (operation != null) {
				io.swagger.v3.oas.annotations.parameters.RequestBody requestBody = operation.requestBody();
				if (StringUtils.isNotBlank(requestBody.description()))
					return true;
				else if (StringUtils.isNotBlank(requestBody.ref()))
					return true;
				else if (requestBody.required())
					return true;
				else if (requestBody.useParameterTypeSchema())
					return true;
				else if (requestBody.content().length > 0)
					return true;
				else
					return requestBody.extensions().length > 0;
			}
		}
		return false;
	}

	/**
	 * deprecated use {@link SchemaUtils#hasNotNullAnnotation(Collection)}
	 * @param annotationSimpleNames the annotation simple names
	 * @return boolean
	 */
	@Deprecated(forRemoval = true)
	public static boolean hasNotNullAnnotation(Collection<String> annotationSimpleNames) {
		return SchemaUtils.hasNotNullAnnotation(annotationSimpleNames);
	}

}
