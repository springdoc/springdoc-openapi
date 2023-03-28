package org.springdoc.ui;

import java.io.File;

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
	 * @param path the path
	 * @return the string
	 */
	@Nullable
	protected String findWebJarResourcePath(String path) {
		String webjar = webjar(path);
		if (webjar.length() > 0) {
			String version = swaggerUiConfigProperties.getVersion();
			if (version != null) {
				String partialPath = path(webjar, path);
				return webjar + File.separator + version + File.separator + partialPath;
			}
		}
		return null;
	}

	/**
	 * Webjar string.
	 *
	 * @param path the path
	 * @return the string
	 */
	private String webjar(String path) {
		int startOffset = (path.startsWith("/") ? 1 : 0);
		int endOffset = path.indexOf('/', 1);
		return endOffset != -1 ? path.substring(startOffset, endOffset) : path;
	}


	/**
	 * Path string.
	 *
	 * @param webjar the webjar
	 * @param path the path
	 * @return the string
	 */
	private String path(String webjar, String path) {
		if (path.startsWith(webjar)) {
			path = path.substring(webjar.length() + 1);
		}
		return path;
	}
}