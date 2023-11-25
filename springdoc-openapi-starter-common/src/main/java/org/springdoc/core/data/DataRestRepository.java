/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.data;

import java.util.Locale;

import org.springframework.data.mapping.PersistentEntity;


/**
 * Base class for DataRestRepository.
 */
class BaseDataRestRepository {
	private Class<?> domainType;
	private Class<?> repositoryType;
	private String relationName;
	private ControllerType controllerType;
	private boolean isCollectionLike;
	private boolean isMap;
	private Class<?> propertyType;
	private PersistentEntity persistentEntity;
	private Locale locale;

	/**
	 * BaseDataRestRepository constructor.
	 *
	 * @param domainType    The domain type.
	 * @param repositoryType The repository type.
	 * @param locale        The locale.
	 */
	public BaseDataRestRepository(Class<?> domainType, Class<?> repositoryType, Locale locale) {
		this.domainType = domainType;
		this.repositoryType = repositoryType;
		this.locale = locale;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public void setDomainType(Class<?> domainType) {
		this.domainType = domainType;
	}

	public Class<?> getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(Class<?> repositoryType) {
		this.repositoryType = repositoryType;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public ControllerType getControllerType() {
		return controllerType;
	}

	public void setControllerType(ControllerType controllerType) {
		this.controllerType = controllerType;
	}

	public boolean isCollectionLike() {
		return isCollectionLike;
	}

	public void setCollectionLike(boolean collectionLike) {
		isCollectionLike = collectionLike;
	}

	public boolean isMap() {
		return isMap;
	}

	public void setMap(boolean map) {
		isMap = map;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	public PersistentEntity getPersistentEntity() {
		return persistentEntity;
	}

	public void setPersistentEntity(PersistentEntity persistentEntity) {
		this.persistentEntity = persistentEntity;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}

/**
 * DataRestRepository class extending BaseDataRestRepository.
 */
public class DataRestRepository extends BaseDataRestRepository {

	/**
	 * Constructor for DataRestRepository.
	 *
	 * @param domainType    The domain type.
	 * @param repositoryType The repository type.
	 * @param locale        The locale.
	 */
	public DataRestRepository(Class<?> domainType, Class<?> repositoryType, Locale locale) {
		super(domainType, repositoryType, locale);
	}

	/**
	 * Gets the return type based on the controller type.
	 *
	 * @return The return type.
	 */
	public Class<?> getReturnType() {
		if (ControllerType.PROPERTY.equals(getControllerType())) {
			return getPropertyType();
		} else if (ControllerType.GENERAL.equals(getControllerType())) {
			return null;
		}
		return getDomainType();
	}
}
