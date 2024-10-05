package org.springdoc.webmvc.ui;

import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver;

import static org.springdoc.ui.SwaggerResourceResolverUtils.findSwaggerResourcePath;

/**
 * The type Web jars version resource resolver.
 *
 * @author bnasslahsen
 */
public class SwaggerResourceResolver extends LiteWebJarsResourceResolver {

	/**
	 * The Swagger ui config properties.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfigProperties;
	
	/**
	 * Instantiates a new Web jars version resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 */
	public SwaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
	}

	/**
	 * Find web jar resource path string.
	 *
	 * @param pathStr the path
	 * @return the string
	 */
	@Nullable
	@Override
	protected String findWebJarResourcePath(String pathStr) {
		String resourcePath = super.findWebJarResourcePath(pathStr);
		if(resourcePath == null)
			return findSwaggerResourcePath(pathStr, swaggerUiConfigProperties.getVersion());
		return resourcePath;
	}

}