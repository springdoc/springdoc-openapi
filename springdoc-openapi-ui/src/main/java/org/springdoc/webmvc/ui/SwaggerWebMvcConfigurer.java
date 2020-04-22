package org.springdoc.webmvc.ui;

import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

@SuppressWarnings("deprecation")
public class SwaggerWebMvcConfigurer extends WebMvcConfigurerAdapter { // NOSONAR

	private String swaggerPath;

	private SwaggerIndexTransformer swaggerIndexTransformer;

	public SwaggerWebMvcConfigurer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerIndexTransformer swaggerIndexTransformer) {
		this.swaggerPath = swaggerUiConfig.getPath();
		this.swaggerIndexTransformer = swaggerIndexTransformer;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains("/"))
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf('/'));
		uiRootPath.append("/**");
		registry.addResourceHandler(uiRootPath + "/swagger-ui/**")
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);
	}

}
