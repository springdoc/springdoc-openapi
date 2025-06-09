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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
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
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.utils.PropertyResolverUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import static org.springdoc.core.utils.Constants.DEFAULT_SERVER_DESCRIPTION;
import static org.springdoc.core.utils.Constants.DEFAULT_TITLE;
import static org.springdoc.core.utils.Constants.DEFAULT_VERSION;
import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Open api builder.
 *
 * @author bnasslahsen
 */
public class OpenAPIService implements ApplicationContextAware {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIService.class);

	/**
	 * The Basic error controller.
	 */
	private static Class<?> basicErrorController;

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
	 * The server base URL customisers.
	 */
	private final Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Cached open api map.
	 */
	private final Map<String, OpenAPI> cachedOpenAPI = new HashMap<>();

	/**
	 * The Property resolver utils.
	 */
	private final PropertyResolverUtils propertyResolverUtils;

	/**
	 * The javadoc provider.
	 */
	private final Optional<JavadocProvider> javadocProvider;

	/**
	 * The Context.
	 */
	private ApplicationContext context;

	/**
	 * The Open api.
	 */
	private OpenAPI openAPI;

	/**
	 * The Is servers present.
	 */
	private boolean isServersPresent;

	/**
	 * Instantiates a new Open api builder.
	 *
	 * @param openAPI                   the open api
	 * @param securityParser            the security parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param propertyResolverUtils     the property resolver utils
	 * @param openApiBuilderCustomizers the open api builder customisers
	 * @param serverBaseUrlCustomizers  the server base url customizers
	 * @param javadocProvider           the javadoc provider
	 */
	public OpenAPIService(Optional<OpenAPI> openAPI, SecurityService securityParser,
			SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils,
			Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
			Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
			Optional<JavadocProvider> javadocProvider) {
		if (openAPI.isPresent()) {
			this.openAPI = openAPI.get();
			if (this.openAPI.getComponents() == null)
				this.openAPI.setComponents(new Components());
			if (this.openAPI.getPaths() == null)
				this.openAPI.setPaths(new Paths());
			if (!CollectionUtils.isEmpty(this.openAPI.getServers()))
				this.isServersPresent = true;
		}
		this.propertyResolverUtils = propertyResolverUtils;
		this.securityParser = securityParser;
		this.springDocConfigProperties = springDocConfigProperties;
		this.openApiBuilderCustomisers = openApiBuilderCustomizers;
		this.serverBaseUrlCustomizers = serverBaseUrlCustomizers;
		this.javadocProvider = javadocProvider;
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
	 *
	 * @param locale the locale
	 * @return the open api
	 */
	public OpenAPI build(Locale locale) {
		Optional<OpenAPIDefinition> apiDef = getOpenAPIDefinition();
		OpenAPI calculatedOpenAPI = null;

		if (openAPI == null) {
			calculatedOpenAPI = new OpenAPI(springDocConfigProperties.getSpecVersion());
			calculatedOpenAPI.setComponents(new Components());
			calculatedOpenAPI.setPaths(new Paths());
		}
		else {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				calculatedOpenAPI = objectMapper.readValue(objectMapper.writeValueAsString(openAPI), OpenAPI.class);
			}
			catch (JsonProcessingException e) {
				LOGGER.warn("Json Processing Exception occurred: {}", e.getMessage());
				calculatedOpenAPI = openAPI;
			}
		}

		if (apiDef.isPresent()) {
			buildOpenAPIWithOpenAPIDefinition(calculatedOpenAPI, apiDef.get(), locale);
		}
		// Set default info
		else if (calculatedOpenAPI != null && calculatedOpenAPI.getInfo() == null) {
			Info infos = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
			calculatedOpenAPI.setInfo(infos);
		}
		// Set default mappings
		this.mappingsMap.putAll(context.getBeansWithAnnotation(RestController.class));
		this.mappingsMap.putAll(context.getBeansWithAnnotation(RequestMapping.class));
		this.mappingsMap.putAll(context.getBeansWithAnnotation(Controller.class));

		initializeHiddenRestController();

		// add security schemes
		if (calculatedOpenAPI != null)
			this.calculateSecuritySchemes(calculatedOpenAPI.getComponents(), locale);
		openApiBuilderCustomisers.ifPresent(customizers -> customizers.forEach(customiser -> customiser.customise(this)));
		return calculatedOpenAPI;
	}

	/**
	 * Initialize hidden rest controller.
	 */
	private void initializeHiddenRestController() {
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
	 * @param serverBaseUrl the server base url
	 * @param openAPI       the open api
	 */
	public void updateServers(String serverBaseUrl, OpenAPI openAPI) {
		if (!isServersPresent && serverBaseUrl != null)        // default server value
		{
			Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
			List<Server> servers = new ArrayList<>();
			servers.add(server);
			openAPI.setServers(servers);
		}
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
	 * @param operation     the operation
	 * @param openAPI       the open api
	 * @param locale        the locale
	 * @return the operation
	 */
	public Operation buildTags(HandlerMethod handlerMethod, Operation operation, OpenAPI openAPI, Locale locale) {

		Set<io.swagger.v3.oas.models.tags.Tag> tags = new HashSet<>();
		Set<String> tagsStr = new HashSet<>();

		buildTagsFromMethod(handlerMethod.getMethod(), tags, tagsStr, locale);
		buildTagsFromClass(handlerMethod.getBeanType(), tags, tagsStr, locale);

		if (!CollectionUtils.isEmpty(tagsStr))
			tagsStr = tagsStr.stream()
					.map(str -> propertyResolverUtils.resolve(str, locale))
					.collect(Collectors.toCollection(LinkedHashSet::new));

		if (springdocTags.containsKey(handlerMethod)) {
			io.swagger.v3.oas.models.tags.Tag tag = springdocTags.get(handlerMethod);
			tagsStr.add(tag.getName());
			if (openAPI.getTags() == null || !openAPI.getTags().contains(tag)) {
				openAPI.addTagsItem(tag);
			}
		}

		if (!CollectionUtils.isEmpty(tagsStr)) {
			if (CollectionUtils.isEmpty(operation.getTags()))
				operation.setTags(new ArrayList<>(tagsStr));
			else {
				Set<String> operationTagsSet = new HashSet<>(operation.getTags());
				operationTagsSet.addAll(tagsStr);
				operation.getTags().clear();
				operation.getTags().addAll(operationTagsSet);
			}
		}

		if (isAutoTagClasses(operation)) {
			String tagAutoName = splitCamelCase(handlerMethod.getBeanType().getSimpleName());
			operation.addTagsItem(tagAutoName);
			if (javadocProvider.isPresent()) {
				String description = javadocProvider.get().getClassJavadoc(handlerMethod.getBeanType());
				if (StringUtils.isNotBlank(description)) {
					io.swagger.v3.oas.models.tags.Tag tag = new io.swagger.v3.oas.models.tags.Tag();
					tag.setName(tagAutoName);
					tag.setDescription(description);
					if (openAPI.getTags() == null || !openAPI.getTags().contains(tag)) {
						openAPI.addTagsItem(tag);
					}
				}
			}
		}

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
	 * @param method  the method
	 * @param tags    the tags
	 * @param tagsStr the tags str
	 * @param locale  the locale
	 */
	private void buildTagsFromMethod(Method method, Set<io.swagger.v3.oas.models.tags.Tag> tags, Set<String> tagsStr, Locale locale) {
		// method tags
		Set<Tags> tagsSet = AnnotatedElementUtils
				.findAllMergedAnnotations(method, Tags.class);
		Set<Tag> methodTags = tagsSet.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toCollection(LinkedHashSet::new));
		methodTags.addAll(AnnotatedElementUtils.findAllMergedAnnotations(method, Tag.class));
		if (!CollectionUtils.isEmpty(methodTags)) {
			tagsStr.addAll(methodTags.stream().map(tag -> propertyResolverUtils.resolve(tag.name(), locale)).collect(Collectors.toCollection(LinkedHashSet::new)));
			List<Tag> allTags = new ArrayList<>(methodTags);
			addTags(allTags, tags, locale);
		}
	}

	/**
	 * Add tags.
	 *
	 * @param sourceTags the source tags
	 * @param tags       the tags
	 * @param locale     the locale
	 */
	private void addTags(List<Tag> sourceTags, Set<io.swagger.v3.oas.models.tags.Tag> tags, Locale locale) {
		Optional<Set<io.swagger.v3.oas.models.tags.Tag>> optionalTagSet = AnnotationsUtils
				.getTags(sourceTags.toArray(new Tag[0]), true);
		optionalTagSet.ifPresent(tagsSet -> {
			tagsSet.forEach(tag -> {
				tag.name(propertyResolverUtils.resolve(tag.getName(), locale));
				tag.description(propertyResolverUtils.resolve(tag.getDescription(), locale));
				if (tags.stream().noneMatch(t -> t.getName().equals(tag.getName())))
					tags.add(tag);
			});
		});
	}

	/**
	 * Build tags from class.
	 *
	 * @param beanType the bean type
	 * @param tags     the tags
	 * @param tagsStr  the tags str
	 * @param locale   the locale
	 */
	public void buildTagsFromClass(Class<?> beanType, Set<io.swagger.v3.oas.models.tags.Tag> tags, Set<String> tagsStr, Locale locale) {
		List<Tag> allTags = new ArrayList<>();
		// class tags
		Set<Tags> tagsSet = AnnotatedElementUtils
				.findAllMergedAnnotations(beanType, Tags.class);
		Set<Tag> classTags = tagsSet.stream()
				.flatMap(x -> Stream.of(x.value())).collect(Collectors.toCollection(LinkedHashSet::new));
		classTags.addAll(AnnotatedElementUtils.findAllMergedAnnotations(beanType, Tag.class));
		if (!CollectionUtils.isEmpty(classTags)) {
			tagsStr.addAll(classTags.stream().map(tag -> propertyResolverUtils.resolve(tag.name(), locale)).collect(Collectors.toCollection(LinkedHashSet::new)));
			allTags.addAll(classTags);
			addTags(allTags, tags, locale);
		}
	}

	/**
	 * Resolve properties schema.
	 *
	 * @param schema the schema
	 * @param locale the locale
	 * @return the schema
	 */
	@SuppressWarnings("unchecked")
	public Schema resolveProperties(Schema schema, Locale locale) {
		resolveProperty(schema::getName, schema::name, propertyResolverUtils, locale);
		resolveProperty(schema::getTitle, schema::title, propertyResolverUtils, locale);
		resolveProperty(schema::getDescription, schema::description, propertyResolverUtils, locale);

		Map<String, Schema> properties = schema.getProperties();
		if (!CollectionUtils.isEmpty(properties)) {
			LinkedHashMap<String, Schema> resolvedSchemas = properties.entrySet().stream().map(es -> {
				es.setValue(resolveProperties(es.getValue(), locale));
				if (es.getValue().getItems() !=null ) {
					resolveProperties(es.getValue().getItems(), locale);
				}
				return es;
			}).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
					LinkedHashMap::new));
			schema.setProperties(resolvedSchemas);
		}
		return schema;
	}

	/**
	 * Sets server base url.
	 *
	 * @param serverBaseUrl the server base url
	 * @param httpRequest   the http request
	 * @return the string
	 */
	public String calculateServerBaseUrl(String serverBaseUrl, HttpRequest httpRequest) {
		String customServerBaseUrl = serverBaseUrl;
		if (serverBaseUrlCustomizers.isPresent()) {
			for (ServerBaseUrlCustomizer customizer : serverBaseUrlCustomizers.get()) {
				customServerBaseUrl = customizer.customize(customServerBaseUrl, httpRequest);
			}
		}

		return customServerBaseUrl;
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
	 * Get webhooks webhooks [ ].
	 *
	 * @return the webhooks [ ]
	 */
	public Webhooks[] getWebhooks() {
		List<Webhooks> allWebhooks = new ArrayList<>();

		// First: scan Spring-managed beans
		Map<String, Object> beans = context.getBeansWithAnnotation(Webhooks.class);

		for (Object bean : beans.values()) {
			Class<?> beanClass = bean.getClass();

			// Collect @Webhooks or @Webhook on class level
			collectWebhooksFromElement(beanClass, allWebhooks);

			// Collect from methods
			for (Method method : beanClass.getDeclaredMethods()) {
				collectWebhooksFromElement(method, allWebhooks);
			}
		}

		// Fallback: classpath scanning if nothing found
		if (allWebhooks.isEmpty()) {
			ClassPathScanningCandidateComponentProvider scanner =
					new ClassPathScanningCandidateComponentProvider(false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Webhooks.class));
			scanner.addIncludeFilter(new AnnotationTypeFilter(Webhook.class));

			if (AutoConfigurationPackages.has(context)) {
				for (String basePackage : AutoConfigurationPackages.get(context)) {
					Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);

					for (BeanDefinition bd : candidates) {
						try {
							Class<?> clazz = Class.forName(bd.getBeanClassName());

							// Class-level annotations
							collectWebhooksFromElement(clazz, allWebhooks);

							// Method-level annotations
							for (Method method : clazz.getDeclaredMethods()) {
								collectWebhooksFromElement(method, allWebhooks);
							}

						} catch (ClassNotFoundException e) {
							LOGGER.error("Class not found in classpath: {}", e.getMessage());
						}
					}
				}
			}
		}

		return allWebhooks.toArray(new Webhooks[0]);
	}

	/**
	 * Collect webhooks from element.
	 *
	 * @param element   the element
	 * @param collector the collector
	 */
	private void collectWebhooksFromElement(AnnotatedElement element, List<Webhooks> collector) {
		// If @Webhooks is present (container)
		Webhooks container = element.getAnnotation(Webhooks.class);
		if (container != null) {
			collector.add(container);
		}

		// If individual @Webhook annotations are present
		Webhook[] individualWebhooks = element.getAnnotationsByType(Webhook.class);
		if (individualWebhooks.length > 0) {
			collector.add(new Webhooks() {
				@Override
				public Webhook[] value() {
					return individualWebhooks;
				}

				@Override
				public Class<? extends Annotation> annotationType() {
					return Webhooks.class;
				}
			});
		}
	}



	/**
	 * Build open api with open api definition.
	 *
	 * @param openAPI the open api
	 * @param apiDef  the api def
	 * @param locale  the locale
	 */
	private void buildOpenAPIWithOpenAPIDefinition(OpenAPI openAPI, OpenAPIDefinition apiDef, Locale locale) {
		boolean isOpenapi3 = propertyResolverUtils.isOpenapi31();
		Map<String, Object> extensions = AnnotationsUtils.getExtensions(isOpenapi3, apiDef.info().extensions());
		// info
		AnnotationsUtils.getInfo(apiDef.info(),true).map(info -> resolveProperties(info, extensions, locale)).ifPresent(openAPI::setInfo);
		// OpenApiDefinition security requirements
		securityParser.getSecurityRequirements(apiDef.security()).ifPresent(openAPI::setSecurity);
		// OpenApiDefinition external docs
		AnnotationsUtils.getExternalDocumentation(apiDef.externalDocs(), isOpenapi3).ifPresent(openAPI::setExternalDocs);
		// OpenApiDefinition tags
		AnnotationsUtils.getTags(apiDef.tags(), false).ifPresent(tags -> openAPI.setTags(new ArrayList<>(tags)));
		// OpenApiDefinition servers
		Optional<List<Server>> optionalServers = AnnotationsUtils.getServers(apiDef.servers());
		optionalServers.map(servers -> resolveProperties(servers, locale)).ifPresent(servers -> {
					this.isServersPresent = true;
					openAPI.servers(servers);
				}
		);
		// OpenApiDefinition extensions
		if (apiDef.extensions().length > 0) {
			openAPI.setExtensions(AnnotationsUtils.getExtensions(isOpenapi3, apiDef.extensions()));
		}
	}

	/**
	 * Resolve properties info.
	 *
	 * @param servers the servers
	 * @param locale  the locale
	 * @return the servers
	 */
	private List<Server> resolveProperties(List<Server> servers, Locale locale) {
		servers.forEach(server -> {
			resolveProperty(server::getUrl, server::url, propertyResolverUtils, locale);
			resolveProperty(server::getDescription, server::description, propertyResolverUtils, locale);
			if (CollectionUtils.isEmpty(server.getVariables()))
				server.setVariables(null);
		});
		return servers;
	}

	/**
	 * Resolve properties info.
	 *
	 * @param info       the info
	 * @param extensions
	 * @param locale     the locale
	 * @return the info
	 */
	private Info resolveProperties(Info info, Map<String, Object> extensions, Locale locale) {
		resolveProperty(info::getTitle, info::title, propertyResolverUtils, locale);
		resolveProperty(info::getDescription, info::description, propertyResolverUtils, locale);
		resolveProperty(info::getVersion, info::version, propertyResolverUtils, locale);
		resolveProperty(info::getTermsOfService, info::termsOfService, propertyResolverUtils, locale);

		License license = info.getLicense();
		if (license != null) {
			resolveProperty(license::getName, license::name, propertyResolverUtils, locale);
			resolveProperty(license::getUrl, license::url, propertyResolverUtils, locale);
		}

		Contact contact = info.getContact();
		if (contact != null) {
			resolveProperty(contact::getName, contact::name, propertyResolverUtils, locale);
			resolveProperty(contact::getEmail, contact::email, propertyResolverUtils, locale);
			resolveProperty(contact::getUrl, contact::url, propertyResolverUtils, locale);
		}

		if (propertyResolverUtils.isResolveExtensionsProperties() && extensions != null)  {
			Map<String, Object> extensionsResolved = propertyResolverUtils.resolveExtensions(locale, extensions);
			if(propertyResolverUtils.isOpenapi31())
				extensionsResolved.forEach(info::addExtension31);
			else
				info.setExtensions(extensionsResolved);
		}

		return info;
	}


	/**
	 * Resolve property.
	 *
	 * @param getProperty           the get property
	 * @param setProperty           the set property
	 * @param propertyResolverUtils the property resolver utils
	 * @param locale                the locale
	 */
	private void resolveProperty(Supplier<String> getProperty, Consumer<String> setProperty,
			PropertyResolverUtils propertyResolverUtils, Locale locale) {
		String value = getProperty.get();
		if (StringUtils.isNotBlank(value)) {
			setProperty.accept(propertyResolverUtils.resolve(value, locale));
		}
	}

	/**
	 * Calculate security schemes.
	 *
	 * @param components the components
	 * @param locale     the locale
	 */
	private void calculateSecuritySchemes(Components components, Locale locale) {
		// Look for SecurityScheme in a spring managed bean
		Map<String, Object> securitySchemeBeans = context
				.getBeansWithAnnotation(io.swagger.v3.oas.annotations.security.SecurityScheme.class);
		if (securitySchemeBeans.size() > 0) {
			for (Map.Entry<String, Object> entry : securitySchemeBeans.entrySet()) {
				Class<?> objClz = entry.getValue().getClass();
				Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = AnnotatedElementUtils.findMergedRepeatableAnnotations(objClz, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
				this.addSecurityScheme(apiSecurityScheme, components, locale);
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
				this.addSecurityScheme(apiSecurityScheme, components, locale);
			}

		}
	}

	/**
	 * Add security scheme.
	 *
	 * @param apiSecurityScheme the api security scheme
	 * @param components        the components
	 * @param locale            the locale
	 */
	private void addSecurityScheme(Set<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme,
			Components components, Locale locale) {
		for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation : apiSecurityScheme) {
			Optional<SecuritySchemePair> securityScheme = securityParser.getSecurityScheme(securitySchemeAnnotation, locale);
			if (securityScheme.isPresent()) {
				Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
				if (StringUtils.isNotBlank(securityScheme.get().key())) {
					securitySchemeMap.put(securityScheme.get().key(), securityScheme.get().securityScheme());
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
	 * @param scanner        the scanner
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
	 * @param scanner        the scanner
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
					if (apiSecuritySchemes != null && !ArrayUtils.isEmpty(apiSecuritySchemes.value()))
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
	 * @param tag            the tag
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
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1, LinkedHashMap::new));
	}

	/**
	 * Gets cached open api.
	 *
	 * @param locale associated the cache entry
	 * @return the cached open api
	 */
	public OpenAPI getCachedOpenAPI(Locale locale) {
		return cachedOpenAPI.get(locale.toLanguageTag());
	}

	/**
	 * Sets cached open api.
	 *
	 * @param cachedOpenAPI the cached open api
	 * @param locale        associated the cache entry
	 */
	public void setCachedOpenAPI(OpenAPI cachedOpenAPI, Locale locale) {
		this.cachedOpenAPI.put(locale.toLanguageTag(), cachedOpenAPI);
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
