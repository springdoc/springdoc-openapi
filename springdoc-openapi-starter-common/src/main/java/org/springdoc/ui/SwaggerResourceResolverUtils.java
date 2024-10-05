package org.springdoc.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The interface Swagger resource resolver utils.
 *
 * @author bnasslahsen
 */
public interface SwaggerResourceResolverUtils {

	/**
	 * Find swagger resource path string.
	 *
	 * @param pathStr the path
	 * @param version the version
	 * @return the string
	 */
	static String findSwaggerResourcePath(String pathStr, String version) {
		Path path = Paths.get(pathStr);
		if (path.getNameCount() < 2) return null;
		if (version == null) return null;
		Path first = path.getName(0);
		Path rest = path.subpath(1, path.getNameCount());
		return first.resolve(version).resolve(rest).toString();
	}
	
}
