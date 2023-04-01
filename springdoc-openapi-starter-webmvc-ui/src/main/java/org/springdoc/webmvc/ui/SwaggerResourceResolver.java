package org.springdoc.webmvc.ui;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerResourceResolver;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * The type Web jars version resource resolver.
 *
 * @author bnasslahsen
 */
public class SwaggerResourceResolver extends AbstractSwaggerResourceResolver implements ResourceResolver {

	/**
	 * Instantiates a new Web jars version resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 */
	public SwaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		super(swaggerUiConfigProperties);
	}

	@Override
	public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		Resource resolved = chain.resolveResource(request, requestPath, locations);
		if (resolved == null) {
			String webJarResourcePath = findWebJarResourcePath(requestPath);
			if (webJarResourcePath != null)
				return chain.resolveResource(request, webJarResourcePath, locations);
		}
		return resolved;	}

	@Override
	public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
		String path = chain.resolveUrlPath(resourcePath, locations);
		if (path == null) {
			String webJarResourcePath = findWebJarResourcePath(resourcePath);
			if (webJarResourcePath != null)
				return chain.resolveUrlPath(webJarResourcePath, locations);
		}
		return path;
	}
}