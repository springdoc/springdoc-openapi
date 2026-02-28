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

import java.util.HashSet;
import java.util.Set;

/**
 * The type Entity info.
 *
 * @author bnasslashen
 */
public class EntityInfo {

	/**
	 * The Ignored fields.
	 */
	private Set<String> ignoredFields = new HashSet<>();

	/**
	 * The Associations fields.
	 */
	private Set<String> associationsFields;

	/**
	 * The Domain type.
	 */
	private Class<?> domainType;

	/**
	 * Gets ignored fields.
	 *
	 * @return the ignored fields
	 */
	public Set<String> getIgnoredFields() {
		return ignoredFields;
	}

	/**
	 * Sets ignored fields.
	 *
	 * @param ignoredFields the ignored fields
	 */
	public void setIgnoredFields(Set<String> ignoredFields) {
		this.ignoredFields = ignoredFields;
	}

	/**
	 * Gets associations fields.
	 *
	 * @return the associations fields
	 */
	public Set<String> getAssociationsFields() {
		return associationsFields;
	}

	/**
	 * Sets associations fields.
	 *
	 * @param associationsFields the associations fields
	 */
	public void setAssociationsFields(Set<String> associationsFields) {
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
