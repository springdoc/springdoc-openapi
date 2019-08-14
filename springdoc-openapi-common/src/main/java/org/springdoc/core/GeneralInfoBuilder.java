package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Component
public class GeneralInfoBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralInfoBuilder.class);
	private ApplicationContext context;
	private String serverBaseUrl;

	public GeneralInfoBuilder(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void build(OpenAPI openAPI) {
		Optional<OpenAPIDefinition> apiDef = getOpenAPIDefinition();
		if (apiDef.isPresent()) {
			buildOpenAPIWithOpenAPIDefinition(openAPI, apiDef.get());
		}
		// Set default info
		else if (openAPI.getInfo() == null) {
			Info infos = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
			openAPI.setInfo(infos);
		}

		// default server value
		if (CollectionUtils.isEmpty(openAPI.getServers())) {
			Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
			openAPI.addServersItem(server);
		}

		// add security schemes
		this.calculateSecuritySchemes(openAPI.getComponents());
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
			apiDef = ReflectionUtils.getAnnotation(objClz, OpenAPIDefinition.class);
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
		AnnotationsUtils.getInfo(apiDef.info()).ifPresent(openAPI::setInfo);
		// OpenApiDefinition security requirements
		SecurityParser.getSecurityRequirements(apiDef.security()).ifPresent(openAPI::setSecurity);
		// OpenApiDefinition external docs
		AnnotationsUtils.getExternalDocumentation(apiDef.externalDocs()).ifPresent(openAPI::setExternalDocs);
		// OpenApiDefinition tags
		AnnotationsUtils.getTags(apiDef.tags(), false).ifPresent(tags -> openAPI.setTags(new ArrayList<>(tags)));
		// OpenApiDefinition servers
		openAPI.setServers(AnnotationsUtils.getServers(apiDef.servers()).orElse(null));
		// OpenApiDefinition extensions
		if (apiDef.extensions().length > 0) {
			openAPI.setExtensions(AnnotationsUtils.getExtensions(apiDef.extensions()));
		}
	}

	private void calculateSecuritySchemes(Components components) {
		// Look for OpenAPIDefinition in a spring managed bean
		Map<String, Object> securitySchemeBeans = context
				.getBeansWithAnnotation(io.swagger.v3.oas.annotations.security.SecurityScheme.class);
		if (securitySchemeBeans.size() > 0) {
			for (Map.Entry<String, Object> entry : securitySchemeBeans.entrySet()) {
				Class<?> objClz = entry.getValue().getClass();
				List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = ReflectionUtils
						.getRepeatableAnnotations(objClz, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
				this.addSecurityScheme(apiSecurityScheme, components);
			}
		}

		// Look for OpenAPIDefinition in the spring classpath
		else {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(
					new AnnotationTypeFilter(io.swagger.v3.oas.annotations.security.SecurityScheme.class));
			if (AutoConfigurationPackages.has(context)) {
				List<String> packagesToScan = AutoConfigurationPackages.get(context);
				List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = getSecuritySchemesClasses(
						scanner, packagesToScan);
				this.addSecurityScheme(apiSecurityScheme, components);
			}

		}
	}

	private void addSecurityScheme(List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme,
			Components components) {
		if (!CollectionUtils.isEmpty(apiSecurityScheme)) {
			for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation : apiSecurityScheme) {
				Optional<SecurityParser.SecuritySchemePair> securityScheme = SecurityParser
						.getSecurityScheme(securitySchemeAnnotation);
				if (securityScheme.isPresent()) {
					Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
					if (StringUtils.isNotBlank(securityScheme.get().key)) {
						securitySchemeMap.put(securityScheme.get().key, securityScheme.get().securityScheme);
						if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() != 0) {
							components.getSecuritySchemes().putAll(securitySchemeMap);
						} else {
							components.setSecuritySchemes(securitySchemeMap);
						}
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
				} catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
				}
			}
		}
		return null;
	}

	private List<io.swagger.v3.oas.annotations.security.SecurityScheme> getSecuritySchemesClasses(
			ClassPathScanningCandidateComponentProvider scanner, List<String> packagesToScan) {
		List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = new ArrayList<>();
		for (String pack : packagesToScan) {
			for (BeanDefinition bd : scanner.findCandidateComponents(pack)) {
				try {
					apiSecurityScheme.add(AnnotationUtils.findAnnotation(Class.forName(bd.getBeanClassName()),
							io.swagger.v3.oas.annotations.security.SecurityScheme.class));
				} catch (ClassNotFoundException e) {
					LOGGER.error("Class Not Found in classpath : {}", e.getMessage());
				}
			}
		}
		return apiSecurityScheme;
	}

}
