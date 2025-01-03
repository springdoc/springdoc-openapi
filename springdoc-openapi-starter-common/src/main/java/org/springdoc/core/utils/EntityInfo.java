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

package org.springdoc.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Entity info.
 *
 * @author bnasslashen
 */
public class EntityInfo {

	/**
	 * The Ignored fields.
	 */
	private List<String> ignoredFields = new ArrayList<>();

	/**
	 * The Associations fields.
	 */
	private List<String> associationsFields;

	/**
	 * The Domain type.
	 */
	private Class<?> domainType;

	/**
	 * Gets ignored fields.
	 *
	 * @return the ignored fields
	 */
	public List<String> getIgnoredFields() {
		return ignoredFields;
	}

	/**
	 * Sets ignored fields.
	 *
	 * @param ignoredFields the ignored fields
	 */
	public void setIgnoredFields(List<String> ignoredFields) {
		this.ignoredFields = ignoredFields;
	}

	/**
	 * Gets associations fields.
	 *
	 * @return the associations fields
	 */
	public List<String> getAssociationsFields() {
		return associationsFields;
	}

	/**
	 * Sets associations fields.
	 *
	 * @param associationsFields the associations fields
	 */
	public void setAssociationsFields(List<String> associationsFields) {
		this.associationsFields = associationsFields;
	}

	/**
	 * Gets domain type.
	 *
	 * @return the domain type
	 */
	public Class<?> getDomainType() {
		return domainType;
	}

	/**
	 * Sets domain type.
	 *
	 * @param domainType the domain type
	 */
	public void setDomainType(Class<?> domainType) {
		this.domainType = domainType;
	}
}
