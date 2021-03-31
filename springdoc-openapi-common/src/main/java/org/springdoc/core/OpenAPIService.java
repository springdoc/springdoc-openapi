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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.Constants.DEFAULT_SERVER_DESCRIPTION;
import static org.springdoc.core.Constants.DEFAULT_TITLE;
import static org.springdoc.core.Constants.DEFAULT_VERSION;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Open api builder.
 * @author bnasslahsen
 */
public class OpenAPIService {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIService.class);

	/**
	 * The Context.
	 */
	private final ApplicationContext context;

	/**
	 * The Security parser.
	 */
	private final SecurityService securityParser;

	/**
	 * The Mappings map.
	 */
	private final Map<String, Object> mappingsMap = new HashMap<>();

	/**
	 * The Springdoc tags.
	 */
	private final Map<HandlerMethod, io.swagger.v3.oas.models.tags.Tag> springdocTags = new HashMap<>();

	/**
	 * The Open api builder customisers.
	 */
	private final Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Open api.
	 */
	private OpenAPI openAPI;

	/**
	 * The Cached open api.
	 */
	private OpenAPI cachedOpenAPI;

	/**
	 * The Calculated open api.
	 */
	private OpenAPI calculatedOpenAPI;

	/**
	 * The Is servers present.
	 */
	private boolean isServersPresent;

	/**
	 * The Server base url.
	 */
	private String serverBaseUrl;

	/**
	 * Instantiates a new Open api builder.
	 *
	 * @param openAPI the open api
	 * @param context the context
	 * @param securityParser the security parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param openApiBuilderCustomisers the open api builder customisers
	 */
	OpenAPIService(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityService securityParser,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers) {
		if (openAPI.isPresent()) {
			this.openAPI = openAPI.get();
			if (this.openAPI.getComponents() == null)
				this.openAPI.setComponents(new Components());
			if (this.openAPI.getPaths() == null)
				this.openAPI.setPaths(new Paths());
			if (!CollectionUtils.isEmpty(this.openAPI.getServers()))
				this.isServersPresent = true;
		}
		this.context = context;
		this.securityParser = securityParser;
		this.springDocConfigProperties = springDocConfigProperties;
		this.openApiBuilderCustomisers = openApiBuilderCustomisers;
		if (springDocConfigProperties.isUseFqn())
			TypeNameResolver.std.setUseFqn(true);
	}

	/**
	 * Split camel case string.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String splitCamelCase(String str) {
		return str.replaceAll(
				String.format(
						"%s|%s|%s",
						"(?<=[A-Z])(?=[A-Z][a-z])",
						"(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"),
				"-")
				.toLowerCase(Locale.ROOT);
	}

	/**
	 * Build.
	 */
	public void build() {
		Optional<OpenAPIDefinition> apiDef = getOpenAPIDefinition();

		if (openAPI == null) {
			this.calculatedOpenAPI = new OpenAPI();
			this.calculatedOpenAPI.setComponents(new Components());
			this.calculatedOpenAPI.setPaths(new Paths());
		}
		else
			this.calculatedOpenAPI = openAPI;

		if (apiDef.isPresent()) {
			buildOpenAPIWithOpenAPIDefinition(calculatedOpenAPI, apiDef.get());
		}
		// Set default info
		else if (calculatedOpenAPI.getInfo() == null) {
			Info infos = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
			calculatedOpenAPI.setInfo(infos);
		}
		// Set default mappings
		this.mappingsMap.putAll(context.getBeansWithAnnotation(RestController.class));
		this.mappingsMap.putAll(context.getBeansWithAnnotation(RequestMapping.class));
		this.mappingsMap.putAll(context.getBeansWithAnnotation(Controller.class));

		initializeHiddenRestController();

		// add security schemes
		this.calculateSecuritySchemes(calculatedOpenAPI.getComponents());
		openApiBuilderCustomisers.ifPresent(customisers -> customisers.forEach(customiser -> customiser.customise(this)));
	}

	private void initializeHiddenRestController() {
		Class basicErrorController = null;
		try {
			//spring-boot 2
			basicErrorController = Class.forName("org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController");
		}
		catch (ClassNotFoundException e) {
			//spring-boot 1
			try {
				basicErrorController = Class.forName("org.springframework.boot.autoconfigure.web.BasicErrorController");
			}
			catch (ClassNotFoundException classNotFoundException) {
				//Basic error controller class not found
				LOGGER.warn(classNotFoundException.getMessage());
			}
		}
		if (basicErrorController != null)
			getConfig().addHiddenRestControllers(basicErrorController);
		List<Class<?>> hiddenRestControllers = this.mappingsMap.entrySet().parallelStream()
				.filter(controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(),
						Hidden.class) != null)).map(controller -> controller.getValue().getClass())
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(hiddenRestControllers))
			getConfig().addHiddenRestControllers(hiddenRestControllers.toArray(new Class<?>[hiddenRestControllers.size()]));
	}

	/**
	 * Update servers open api.
	 *
	 * @param openAPI the open api
	 * @return the open api
	 */
	public OpenAPI updateServers(OpenAPI openAPI) {
		if (!isServersPresent && serverBaseUrl != null)        // default server value
		{
			Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
			List<Server> servers = new ArrayList<>();
			servers.add(server);
			openAPI.setServers(servers);
		}
		return openAPI;
	}

	/**
	 * Sets servers present.
	 *
	 * @param serversPresent the servers present
	 */
	public void setServersPresent(boolean serversPresent) {
		isServersPresent = serversPresent;
	}

	/**
	 * Build tags operation.
	 *
	 * @param handlerMethod the handler method
	 * @param operation the operation
	 * @param openAPI the open api
	 * @return the operation
	 */
	public Operation buildTags(HandlerMethod handlerMethod, Operation operation, OpenAPI openAPI) {

		Set<io.swagger.v3.oas.models.tags.Tag> tags = new HashSet<>();
		Set<String> tagsStr = new HashSet<>();

		buildTagsFromClass(handlerMethod.getBeanType(), tags, tagsStr);
		buildTagsFromMethod(handlerMethod.getMethod(), tags, tagsStr);

		if (springdocTags.containsKey(handlerMethod)) {
			io.swagger.v3.oas.models.tags.Tag tag = springdocTags.get(handlerMethod);
			tagsStr.add(tag.getName());
			if (openAPI.getTags() == null || !openAPI.getTags().contains(tag)) {
				openAPI.addTagsItem(tag);
			}
		}

		if (!CollectionUtils.isEmpty(tagsStr))
			operation.setTags(new ArrayList<>(tagsStr));

		if (isAutoTagClasses(operation))
			operation.addTagsItem(splitCamelCase(handlerMethod.getBeanType().getSimpleName()));

		if (!CollectionUtils.isEmpty(tags)) {
			// Existing tags
			List<io.swagger.v3.oas.models.tags.Tag> openApiTags = openAPI.getTags();
			if (!CollectionUtils.isEmpty(openApiTags))
				tags.addAll(openApiTags);
			openAPI.setTags(new ArrayList<>(tags));
		}

		// Handle SecurityRequirement at operation level
		io.swagger.v3.oas.annotations.security.SecurityRequirement[] securityRequirements = securityParser
				.getSecurityRequirements(handlerMethod);
		if (securityRequirements != null) {
			if (securityRequirements.length == 0)
				operation.setSecurity(Collections.emptyList());
			else
				securityParser.buildSecurityRequirement(securityRequirements, operation);
		}

		return operation;
	}

	/**
	 * Build tags from method.
	 *
	 * @param method the method
	 * @param tags the tags
	 * @param tagsStr the tags str
	 */
	private void buildTagsFromMethod(Method method, Set<io.swagger.v3.oas.models.tags.Tag> tags, Set<String> tagsStr) {
		// method tags
		Set<Tags> tagsSet = AnnotatedElementUtils
				.findAllMergedAnnotations(method, Tags.class);
		Set<Tag> methodTags = tagsSet.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toSet());
		methodTags.addAll(AnnotatedElementUtils.findAllMergedAnnotations(method, Tag.class));
		if (!CollectionUtils.isEmpty(methodTags)) {
			tagsStr.addAll(methodTags.stream().map(Tag::name).collect(Collectors.toSet()));
			List<Tag> allTags = new ArrayList<>(methodTags);
			AnnotationsUtils
					.getTags(allTags.toArray(new Tag[0]), true).ifPresent(tags::addAll);
		}
	}

	/**
	 * Build tags from class.
	 *
	 * @param beanType the bean type
	 * @param tags the tags
	 * @param tagsStr the tags str
	 */
	public void buildTagsFromClass(Class<?> beanType, Set<io.swagger.v3.oas.models.tags.Tag> tags, Set<String> tagsStr) {
		List<Tag> allTags = new ArrayList<>();
		// class tags
		Set<Tags> tagsSet = AnnotatedElementUtils
				.findAllMergedAnnotations(beanType, Tags.class);
		Set<Tag> classTags = tagsSet.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toSet());
		classTags.addAll(AnnotatedElementUtils.findAllMergedAnnotations(beanType, Tag.class));
		if (!CollectionUtils.isEmpty(classTags)) {
			tagsStr.addAll(classTags.stream().map(Tag::name).collect(Collectors.toSet()));
			allTags.addAll(classTags);
			AnnotationsUtils
					.getTags(allTags.toArray(new Tag[0]), true).ifPresent(tags::addAll);
		}
	}

	/**
	 * Resolve properties schema.
	 *
	 * @param schema the schema
	 * @param propertyResolverUtils the property resolver utils
	 * @return the schema
	 */
	@SuppressWarnings("unchecked")
	public Schema resolveProperties(Schema schema, PropertyResolverUtils propertyResolverUtils) {
		resolveProperty(schema::getName, schema::name, propertyResolverUtils);
		resolveProperty(schema::getTitle, schema::title, propertyResolverUtils);
		resolveProperty(schema::getDescription, schema::description, propertyResolverUtils);

		Map<String, Schema> properties = schema.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			Map<String, Schema> resolvedSchemas = properties.entrySet().stream().map(es -> {
				es.setValue(resolveProperties(es.getValue(), propertyResolverUtils));
				return es;
			}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			schema.setProperties(resolvedSchemas);
		}

		return schema;
	}

	/**
	 * Sets server base url.
	 *
	 * @param serverBaseUrl the server base url
	 */
	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	/**
	 * Gets open api definition.
	 *
	 * @return the open api definition
	 */
	private Optional<OpenAPIDefinition> getOpenAPIDefinition() {
		// Look for OpenAPIDefinition in a spring managed bean
		Map<String, Object> openAPIDefinitionMap = context.getBeansWithAnnotation(OpenAPIDefinition.class);
		OpenAPIDefinition apiDef = null;
		if (openAPIDefinitionMap.size() > 1)
			LOGGER.warn(
					"found more than one OpenAPIDefinition class. springdoc-openapi will be using the first one found.");
		if (openAPIDefinitionMap.size() > 0) {
			Map.Entry<String, Object> entry = openAPIDefinitionMap.entrySet().iterator().next();
			Class<?> objClz = entry.getValue().getClass();
			apiDef = AnnotatedElementUtils.findMergedAnnotation(objClz, OpenAPIDefinition.class);
		}

		// Look for OpenAPIDefinition in the spring classpath
		else {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(OpenAPIDefinition.class));
			if (AutoConfigurationPackages.has(context)) {
				List<String> packagesToScan = AutoConfigurationPackages.get(context);
				apiDef = getApiDefClass(scanner, packagesToScan);
			}

		}
		return Optional.ofNullable(apiDef);
	}

	/**
	 * Build open api with open api definition.
	 *
	 * @param openAPI the open api
	 * @param apiDef the api def
	 */
	private void buildOpenAPIWithOpenAPIDefinition(OpenAPI openAPI, OpenAPIDefinition apiDef) {
		// info
		AnnotationsUtils.getInfo(apiDef.info()).map(this::resolveProperties).ifPresent(openAPI::setInfo);
		// OpenApiDefinition security requirements
		securityParser.getSecurityRequirements(apiDef.security()).ifPresent(openAPI::setSecurity);
		// OpenApiDefinition external docs
		AnnotationsUtils.getExternalDocumentation(apiDef.externalDocs()).ifPresent(openAPI::setExternalDocs);
		// OpenApiDefinition tags
		AnnotationsUtils.getTags(apiDef.tags(), false).ifPresent(tags -> openAPI.setTags(new ArrayList<>(tags)));
		// OpenApiDefinition servers
		Optional<List<Server>> optionalServers = AnnotationsUtils.getServers(apiDef.servers());
		if (optionalServers.isPresent()) {
			openAPI.setServers(optionalServers.get());
			this.isServersPresent = true;
		}
		// OpenApiDefinition extensions
		if (apiDef.extensions().length > 0) {
			openAPI.setExtensions(AnnotationsUtils.getExtensions(apiDef.extensions()));
		}
	}

	/**
	 * Resolve properties info.
	 *
	 * @param info the info
	 * @return the info
	 */
	private Info resolveProperties(Info info) {
		PropertyResolverUtils propertyResolverUtils = context.getBean(PropertyResolverUtils.class);
		resolveProperty(info::getTitle, info::title, propertyResolverUtils);
		resolveProperty(info::getDescription, info::description, propertyResolverUtils);
		resolveProperty(info::getVersion, info::version, propertyResolverUtils);
		resolveProperty(info::getTermsOfService, info::termsOfService, propertyResolverUtils);

		License license = info.getLicense();
		if (license != null) {
			resolveProperty(license::getName, license::name, propertyResolverUtils);
			resolveProperty(license::getUrl, license::url, propertyResolverUtils);
		}

		Contact contact = info.getContact();
		if (contact != null) {
			resolveProperty(contact::getName, contact::name, propertyResolverUtils);
			resolveProperty(contact::getEmail, contact::email, propertyResolverUtils);
			resolveProperty(contact::getUrl, contact::url, propertyResolverUtils);
		}
		return info;
	}

	/**
	 * Resolve property.
	 *
	 * @param getProperty the get property
	 * @param setProperty the set property
	 * @param propertyResolverUtils the property resolver utils
	 */
	private void resolveProperty(Supplier<String> getProperty, Consumer<String> setProperty,
			PropertyResolverUtils propertyResolverUtils) {
		String value = getProperty.get();
		if (StringUtils.isNotBlank(value)) {
			setProperty.accept(propertyResolverUtils.resolve(value));
		}
	}

	/**
	 * Calculate security schemes.
	 *
	 * @param components the components
	 */
	private void calculateSecuritySchemes(Components components) {
		// Look for SecurityScheme in a spring managed bean
		Map<String, Object> securitySchemeBeans = context
				.getBeansWithAnnotation(io.swagger.v3.oas.annotations.security.SecurityScheme.class);
		if (securitySchemeBeans.size() > 0) {
			for (Map.Entry<String, Object> entry : securitySchemeBeans.entrySet()) {
				Class<?> objClz = entry.getValue().getClass();
				Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = AnnotatedElementUtils.findMergedRepeatableAnnotations(objClz, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
				this.addSecurityScheme(apiSecurityScheme, components);
			}
		}

		// Look for SecurityScheme in the spring classpath
		else {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(io.swagger.v3.oas.annotations.security.SecuritySchemes.class));
			scanner.addIncludeFilter(
					new AnnotationTypeFilter(io.swagger.v3.oas.annotations.security.SecurityScheme.class));
			if (AutoConfigurationPackages.has(context)) {
				List<String> packagesToScan = AutoConfigurationPackages.get(context);
				Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = getSecuritySchemesClasses(
						scanner, packagesToScan);
				this.addSecurityScheme(apiSecurityScheme, components);
			}

		}
	}

	/**
	 * Add security scheme.
	 *
	 * @param apiSecurityScheme the api security scheme
	 * @param components the components
	 */
	private void addSecurityScheme(Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme,
			Components components) {
		for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation : apiSecurityScheme) {
			Optional<SecuritySchemePair> securityScheme = securityParser.getSecurityScheme(securitySchemeAnnotation);
			if (securityScheme.isPresent()) {
				Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
				if (StringUtils.isNotBlank(securityScheme.get().getKey())) {
					securitySchemeMap.put(securityScheme.get().getKey(), securityScheme.get().getSecurityScheme());
					if (!CollectionUtils.isEmpty(components.getSecuritySchemes())) {
						components.getSecuritySchemes().putAll(securitySchemeMap);
					}
					else {
						components.setSecuritySchemes(securitySchemeMap);
					}
				}
			}
		}
	}

	/**
	 * Gets api def class.
	 *
	 * @param scanner the scanner
	 * @param packagesToScan the packages to scan
	 * @return the api def class
	 */
	private OpenAPIDefinition getApiDefClass(ClassPathScanningCandidateComponentProvider scanner,
			List<String> packagesToScan) {
		for (String pack : packagesToScan) {
			for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
				// first one found is ok
				try {
					return AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
							OpenAPIDefinition.class);
				}
				catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * Is auto tag classes boolean.
	 *
	 * @param operation the operation
	 * @return the boolean
	 */
	public boolean isAutoTagClasses(Operation operation) {
		return CollectionUtils.isEmpty(operation.getTags()) && springDocConfigProperties.isAutoTagClasses();
	}

	/**
	 * Gets security schemes classes.
	 *
	 * @param scanner the scanner
	 * @param packagesToScan the packages to scan
	 * @return the security schemes classes
	 */
	private Set<io.swagger.v3.oas.annotations.security.SecurityScheme> getSecuritySchemesClasses(
			ClassPathScanningCandidateComponentProvider scanner, List<String> packagesToScan) {
		Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = new HashSet<>();
		for (String pack : packagesToScan) {
			for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
				try {
					apiSecurityScheme.add(AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
							io.swagger.v3.oas.annotations.security.SecurityScheme.class));
					SecuritySchemes apiSecuritySchemes
							= AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()), io.swagger.v3.oas.annotations.security.SecuritySchemes.class);
					if (apiSecuritySchemes!=null && !ArrayUtils.isEmpty(apiSecuritySchemes.value()))
						Arrays.stream(apiSecuritySchemes.value()).forEach(apiSecurityScheme::add);
				}
				catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
				}
			}
		}
		return apiSecurityScheme;
	}

	/**
	 * Add tag.
	 *
	 * @param handlerMethods the handler methods
	 * @param tag the tag
	 */
	public void addTag(Set<HandlerMethod> handlerMethods, io.swagger.v3.oas.models.tags.Tag tag) {
		handlerMethods.forEach(handlerMethod -> springdocTags.put(handlerMethod, tag));
	}

	/**
	 * Gets mappings map.
	 *
	 * @return the mappings map
	 */
	public Map<String, Object> getMappingsMap() {
		return this.mappingsMap;
	}

	/**
	 * Add mappings.
	 *
	 * @param mappings the mappings
	 */
	public void addMappings(Map<String, Object> mappings) {
		this.mappingsMap.putAll(mappings);
	}

	/**
	 * Gets controller advice map.
	 *
	 * @return the controller advice map
	 */
	public Map<String, Object> getControllerAdviceMap() {
		Map<String, Object> controllerAdviceMap = context.getBeansWithAnnotation(ControllerAdvice.class);
		return Stream.of(controllerAdviceMap).flatMap(mapEl -> mapEl.entrySet().stream()).filter(
				controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(), Hidden.class) == null))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
	}

	/**
	 * Gets cached open api.
	 *
	 * @return the cached open api
	 */
	public OpenAPI getCachedOpenAPI() {
		return cachedOpenAPI;
	}

	/**
	 * Sets cached open api.
	 *
	 * @param cachedOpenAPI the cached open api
	 */
	public void setCachedOpenAPI(OpenAPI cachedOpenAPI) {
		this.cachedOpenAPI = cachedOpenAPI;
	}

	/**
	 * Gets calculated open api.
	 *
	 * @return the calculated open api
	 */
	public OpenAPI getCalculatedOpenAPI() {
		return calculatedOpenAPI;
	}

	/**
	 * Reset calculated open api.
	 */
	public void resetCalculatedOpenAPI() {
		this.calculatedOpenAPI = null;
	}

	/**
	 * Gets context.
	 *
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * Gets security parser.
	 *
	 * @return the security parser
	 */
	public SecurityService getSecurityParser() {
		return securityParser;
	}
}
