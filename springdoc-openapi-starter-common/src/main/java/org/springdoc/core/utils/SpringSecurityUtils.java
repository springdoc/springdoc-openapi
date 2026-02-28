/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.core.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.util.pattern.PathPattern;

/**
 * The type Spring security utils.
 *
 * @author bnasslahsen
 */
public final class SpringSecurityUtils {

	/**
	 * Gets path.
	 *
	 * @param pathPatternRequestMatcher the path pattern request matcher
	 * @return the path
	 * @throws IllegalAccessException the illegal access exception
	 */
	public static String getPath(PathPatternRequestMatcher pathPatternRequestMatcher) throws IllegalAccessException {
		PathPattern pathPattern = (PathPattern) FieldUtils.readField(
				pathPatternRequestMatcher,
				"pattern",
				true
		);
		return pathPattern.getPatternString();
	}
}
