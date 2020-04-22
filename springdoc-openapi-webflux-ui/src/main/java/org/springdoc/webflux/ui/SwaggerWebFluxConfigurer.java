package org.springdoc.webflux.ui;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

public class SwaggerWebFluxConfigurer implements WebFluxConfigurer {

	private String swaggerPath;

	private String webJarsPrefixUrl;

	private SwaggerIndexTransformer swaggerIndexTransformer;

	public SwaggerWebFluxConfigurer(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerIndexTransformer swaggerIndexTransformer) {
		this.swaggerPath = swaggerUiConfig.getPath();
		this.webJarsPrefixUrl = springDocConfigProperties.getWebjars().getPrefix();
		this.swaggerIndexTransformer = swaggerIndexTransformer;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains("/")) {
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf('/'));
		}
		registry.addResourceHandler(uiRootPath + webJarsPrefixUrl + "/**")
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);
	}

}
