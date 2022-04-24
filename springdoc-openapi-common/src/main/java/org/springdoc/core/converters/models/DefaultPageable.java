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

package org.springdoc.core.converters.models;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;

/**
 * The type Default pageable.
 * @author bnasslahsen
 */
@ParameterObject
public class DefaultPageable extends Pageable {

	/**
	 * Instantiates a new Default pageable.
	 *
	 * @param page the page
	 * @param size the size
	 * @param sort the sort
	 */
	public DefaultPageable(int page, int size, List<String> sort) {
		super(page, size, sort);
	}
}