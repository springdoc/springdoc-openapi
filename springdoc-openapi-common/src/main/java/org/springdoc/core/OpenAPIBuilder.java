/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.util.ArrayList;
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

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiBuilderCustomiser;
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

public class OpenAPIBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIBuilder.class);

	private OpenAPI openAPI;

	private OpenAPI cachedOpenAPI;

	private OpenAPI calculatedOpenAPI;

	private final ApplicationContext context;

	private final SecurityParser securityParser;

	private final Map<String, Object> mappingsMap = new HashMap<>();

	private final Map<HandlerMethod, io.swagger.v3.oas.models.tags.Tag> springdocTags = new HashMap<>();

	private final Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider;

	private final Optional<List<OpenApiBuilderCustomiser>> openApiBuilderCustomisers;

	private boolean isServersPresent;

	private String serverBaseUrl;

	private final SpringDocConfigProperties springDocConfigProperties;

	@SuppressWarnings("WeakerAccess")
	OpenAPIBuilder(Optional<OpenAPI> openAPI, ApplicationContext context, SecurityParser securityParser,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider, SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiBuilderCustomiser>> openApiBuilderCustomisers) {
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
		this.springSecurityOAuth2Provider = springSecurityOAuth2Provider;
		this.springDocConfigProperties = springDocConfigProperties;
		this.openApiBuilderCustomisers = openApiBuilderCustomisers;
	}

	private static String splitCamelCase(String str) {
		return str.replaceAll(
				String.format(
						"%s|%s|%s",
						"(?<=[A-Z])(?=[A-Z][a-z])",
						"(?<=[^A-Z])(?=[A-Z])",
						"(?<=[A-Za-z])(?=[^A-Za-z])"),
				"-")
				.toLowerCase(Locale.ROOT);
	}

	public Components getComponents() {
		return calculatedOpenAPI.getComponents();
	}

	public Paths getPaths() {
		return calculatedOpenAPI.getPaths();
	}

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

		// default server value
		if (CollectionUtils.isEmpty(calculatedOpenAPI.getServers()) || !isServersPresent) {
			this.updateServers(calculatedOpenAPI);
		}
		// add security schemes
		this.calculateSecuritySchemes(calculatedOpenAPI.getComponents());
		openApiBuilderCustomisers.ifPresent(customisers -> customisers.forEach(customiser -> customiser.customise(this)));
	}

	public void updateServers(OpenAPI openAPI) {
		Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
		List<Server> servers = new ArrayList();
		servers.add(server);
		openAPI.setServers(servers);
	}

	public boolean isServersPresent() {
		return isServersPresent;
	}

	public Operation buildTags(HandlerMethod handlerMethod, Operation operation, OpenAPI openAPI) {
		// class tags
		Set<Tag> classTags =
				AnnotatedElementUtils.findAllMergedAnnotations(handlerMethod.getBeanType(), Tag.class);

		// method tags
		Set<Tag> methodTags =
				AnnotatedElementUtils.findAllMergedAnnotations(handlerMethod.getMethod(), Tag.class);

		List<Tag> allTags = new ArrayList<>();
		Set<String> tagsStr = new HashSet<>();

		if (!CollectionUtils.isEmpty(methodTags)) {
			tagsStr.addAll(methodTags.stream().map(Tag::name).collect(Collectors.toSet()));
			allTags.addAll(methodTags);
		}

		if (!CollectionUtils.isEmpty(classTags)) {
			tagsStr.addAll(classTags.stream().map(Tag::name).collect(Collectors.toSet()));
			allTags.addAll(classTags);
		}

		if (springdocTags.containsKey(handlerMethod)) {
			io.swagger.v3.oas.models.tags.Tag tag = springdocTags.get(handlerMethod);
			tagsStr.add(tag.getName());
			if (openAPI.getTags() == null || !openAPI.getTags().contains(tag)) {
				openAPI.addTagsItem(tag);
			}
		}

		Optional<Set<io.swagger.v3.oas.models.tags.Tag>> tags = AnnotationsUtils
				.getTags(allTags.toArray(new Tag[0]), true);

		if (tags.isPresent()) {
			Set<io.swagger.v3.oas.models.tags.Tag> tagsSet = tags.get();
			// Existing tags
			List<io.swagger.v3.oas.models.tags.Tag> openApiTags = openAPI.getTags();
			if (!CollectionUtils.isEmpty(openApiTags))
				tagsSet.addAll(openApiTags);
			openAPI.setTags(new ArrayList<>(tagsSet));
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
		if (!CollectionUtils.isEmpty(tagsStr))
			operation.setTags(new ArrayList<>(tagsStr));


		if (isAutoTagClasses(operation))
			operation.addTagsItem(splitCamelCase(handlerMethod.getBeanType().getSimpleName()));

		return operation;
	}

	private boolean isAutoTagClasses(Operation operation) {
		return CollectionUtils.isEmpty(operation.getTags()) && springDocConfigProperties.isAutoTagClasses();
	}

	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

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

	private void resolveProperty(Supplier<String> getProperty, Consumer<String> setProperty,
			PropertyResolverUtils propertyResolverUtils) {
		String value = getProperty.get();
		if (StringUtils.isNotBlank(value)) {
			setProperty.accept(propertyResolverUtils.resolve(value));
		}
	}

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

	private Set<io.swagger.v3.oas.annotations.security.SecurityScheme> getSecuritySchemesClasses(
			ClassPathScanningCandidateComponentProvider scanner, List<String> packagesToScan) {
		Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = new HashSet<>();
		for (String pack : packagesToScan) {
			for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
				try {
					apiSecurityScheme.add(AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
							io.swagger.v3.oas.annotations.security.SecurityScheme.class));
				}
				catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
				}
			}
		}
		return apiSecurityScheme;
	}

	public void addTag(Set<HandlerMethod> handlerMethods, io.swagger.v3.oas.models.tags.Tag tag) {
		handlerMethods.forEach(handlerMethod -> springdocTags.put(handlerMethod, tag));
	}

	public Map<String, Object> getMappingsMap() {
		return this.mappingsMap;
	}

	public void addMappings(Map<String, Object> mappings) {
		this.mappingsMap.putAll(mappings);
	}

	public Map<String, Object> getControllerAdviceMap() {
		Map<String, Object> controllerAdviceMap = context.getBeansWithAnnotation(ControllerAdvice.class);
		return Stream.of(controllerAdviceMap).flatMap(mapEl -> mapEl.entrySet().stream()).filter(
				controller -> (AnnotationUtils.findAnnotation(controller.getValue().getClass(), Hidden.class) == null))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1));
	}

	public Optional<SecurityOAuth2Provider> getSpringSecurityOAuth2Provider() {
		return springSecurityOAuth2Provider;
	}

	public OpenAPI getCachedOpenAPI() {
		return cachedOpenAPI;
	}

	public void setCachedOpenAPI(OpenAPI cachedOpenAPI) {
		this.cachedOpenAPI = cachedOpenAPI;
	}

	public OpenAPI getCalculatedOpenAPI() {
		return calculatedOpenAPI;
	}

	public void resetCalculatedOpenAPI() {
		this.calculatedOpenAPI = null;
	}
}
