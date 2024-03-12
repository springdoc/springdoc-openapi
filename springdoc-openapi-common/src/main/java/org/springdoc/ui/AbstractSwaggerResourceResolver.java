package org.springdoc.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springdoc.core.SwaggerUiConfigProperties;

import org.springframework.lang.Nullable;

/**
 * The type Web jars version resource resolver.
 *
 * @author bnasslahsen
 */
public class AbstractSwaggerResourceResolver {

	/**
	 * The Swagger ui config properties.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	/**
	 * Instantiates a new Web jars version resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 */
	public AbstractSwaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
	}

	/**
	 * Find web jar resource path string.
	 *
	 * @param pathStr the path
	 * @return the string
	 */
	@Nullable
	protected String findWebJarResourcePath(String pathStr) {
		Path path = Paths.get(pathStr);
		if (path.getNameCount() < 2) return null;
		String version = swaggerUiConfigProperties.getVersion();
		if (version == null) return null;
		Path first = path.getName(0);
		Path rest = path.subpath(1, path.getNameCount());
		return first.resolve(version).resolve(rest).toString();
	}
	
}
