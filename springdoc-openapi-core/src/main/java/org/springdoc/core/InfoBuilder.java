package org.springdoc.core;

import static org.springdoc.core.Constants.DEFAULT_TITLE;
import static org.springdoc.core.Constants.DEFAULT_VERSION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Component
public class InfoBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfoBuilder.class);

	@Autowired
	private ApplicationContext context;

	private InfoBuilder() {
		super();
	}

	public void build(OpenAPI openAPI) {
		// Look for OpenAPIDefinition in a spring managed bean
		Map<String, Object> openAPIDefinitionMap = context.getBeansWithAnnotation(OpenAPIDefinition.class);
		OpenAPIDefinition apiDef = null;
		if (openAPIDefinitionMap.size() > 1)
			LOGGER.warn(
					"found more than one OpenAPIDefinition class. springdoc-openapi will be using the first one found.");
		if (openAPIDefinitionMap.size() > 0) {
			Map.Entry<String, Object> entry = openAPIDefinitionMap.entrySet().iterator().next();
			Class<?> objClz = (Class<?>) entry.getValue().getClass();
			apiDef = ReflectionUtils.getAnnotation(objClz, OpenAPIDefinition.class);
		}

		// Look for OpenAPIDefinition in the spring classpath
		else {
			ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
					false);
			scanner.addIncludeFilter(new AnnotationTypeFilter(OpenAPIDefinition.class));
			List<String> packagesToScan = AutoConfigurationPackages.get(context);
			apiDef = getApiDefClass(scanner, packagesToScan);
		}

		if (apiDef != null) {
			// info
			AnnotationsUtils.getInfo(apiDef.info()).ifPresent(info -> openAPI.setInfo(info));
			// OpenApiDefinition security requirements
			SecurityParser.getSecurityRequirements(apiDef.security()).ifPresent(s -> openAPI.setSecurity(s));
			// OpenApiDefinition external docs
			AnnotationsUtils.getExternalDocumentation(apiDef.externalDocs())
					.ifPresent(docs -> openAPI.setExternalDocs(docs));
			// OpenApiDefinition tags
			AnnotationsUtils.getTags(apiDef.tags(), false).ifPresent(tags -> openAPI.setTags(new ArrayList<>(tags)));
			// OpenApiDefinition servers
			AnnotationsUtils.getServers(apiDef.servers()).ifPresent(servers -> openAPI.setServers(servers));
			// OpenApiDefinition extensions
			if (apiDef.extensions().length > 0) {
				openAPI.setExtensions(AnnotationsUtils.getExtensions(apiDef.extensions()));
			}
		} else {
			Info infos = new Info().title(DEFAULT_TITLE).version(DEFAULT_VERSION);
			openAPI.setInfo(infos);
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
					LOGGER.error("Class Not Found in classpath: " + e.getMessage());
				}
			}
		}
		return null;
	}

}
