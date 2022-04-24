/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.springframework.util.CollectionUtils;

/**
 * The interface Spring doc properties utils.
 * @author bnasslahsen
 */
public interface SpringDocPropertiesUtils {

	/**
	 * Put.
	 *
	 * @param name the name
	 * @param value the value
	 * @param params the params
	 */
	static void put(String name, List<String> value, Map<String, Object> params) {
		if (!CollectionUtils.isEmpty(value)) {
			params.put(name, value);
		}
	}

	/**
	 * Put.
	 *
	 * @param name the name
	 * @param value the value
	 * @param params the params
	 */
	static void put(final String name, final Integer value, final Map<String, Object> params) {
		if (value != null) {
			params.put(name, value.toString());
		}
	}

	/**
	 * Put.
	 *
	 * @param name the name
	 * @param value the value
	 * @param params the params
	 */
	static void put(final String name, final Boolean value, final Map<String, Object> params) {
		if (value != null) {
			params.put(name, value);
		}
	}

	/**
	 * Put.
	 *
	 * @param name the name
	 * @param value the value
	 * @param params the params
	 */
	static void put(final String name, final String value, final Map<String, Object> params) {
		if (StringUtils.isNotEmpty(value)) {
			params.put(name, value);
		}
	}

	/**
	 * Put.
	 *
	 * @param name the name
	 * @param value the value
	 * @param params the params
	 */
	static void put(String name, Map<String, String> value, Map<String, Object> params) {
		if (!CollectionUtils.isEmpty(value)) {
			params.put(name, value);
		}
	}

}
