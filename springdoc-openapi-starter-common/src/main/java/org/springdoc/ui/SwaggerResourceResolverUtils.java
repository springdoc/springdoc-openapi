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
