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

package org.springdoc.core.data;

import java.util.Locale;

import org.springframework.data.mapping.PersistentEntity;

/**
 * The type Data rest repository.
 *
 * @author bnasslahsen
 */
public class DataRestRepository {

	/**
	 * The Domain type.
	 */
	private Class<?> domainType;

	/**
	 * The Repository type.
	 */
	private Class<?> repositoryType;

	/**
	 * The Relation name.
	 */
	private String relationName;

	/**
	 * The Controller type.
	 */
	private ControllerType controllerType;

	/**
	 * The Is collection like.
	 */
	private boolean isCollectionLike;

	/**
	 * The Is map.
	 */
	private boolean isMap;

	/**
	 * The Property type.
	 */
	private Class<?> propertyType;

	/**
	 * The Persistent entity.
	 */
	private PersistentEntity persistentEntity;

	/**
	 * The Locale.
	 */
	private Locale locale;

	/**
	 * Instantiates a new Data rest repository.
	 *
	 * @param domainType     the domain type
	 * @param repositoryType the repository type
	 * @param locale         the locale
	 */
	public DataRestRepository(Class<?> domainType, Class<?> repositoryType, Locale locale) {
		this.domainType = domainType;
		this.repositoryType = repositoryType;
		this.locale = locale;
	}

	/**
	 * Gets domain type.
	 *
	 * @return the domain type
	 */
	public Class<?> getDomainType() {
		if (!ControllerType.GENERAL.equals(controllerType))
			return domainType;
		return null;
	}

	/**
	 * Sets domain type.
	 *
	 * @param domainType the domain type
	 */
	public void setDomainType(Class<?> domainType) {
		this.domainType = domainType;
	}

	/**
	 * Gets repository type.
	 *
	 * @return the repository type
	 */
	public Class<?> getRepositoryType() {
		return repositoryType;
	}

	/**
	 * Sets repository type.
	 *
	 * @param repositoryType the repository type
	 */
	public void setRepositoryType(Class<?> repositoryType) {
		this.repositoryType = repositoryType;
	}

	/**
	 * Gets relation name.
	 *
	 * @return the relation name
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * Sets relation name.
	 *
	 * @param relationName the relation name
	 */
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	/**
	 * Gets controller type.
	 *
	 * @return the controller type
	 */
	public ControllerType getControllerType() {
		return controllerType;
	}

	/**
	 * Sets controller type.
	 *
	 * @param controllerType the controller type
	 */
	public void setControllerType(ControllerType controllerType) {
		this.controllerType = controllerType;
	}

	/**
	 * Is collection like boolean.
	 *
	 * @return the boolean
	 */
	public boolean isCollectionLike() {
		return isCollectionLike;
	}

	/**
	 * Sets collection like.
	 *
	 * @param collectionLike the collection like
	 */
	public void setCollectionLike(boolean collectionLike) {
		isCollectionLike = collectionLike;
	}

	/**
	 * Is map boolean.
	 *
	 * @return the boolean
	 */
	public boolean isMap() {
		return isMap;
	}

	/**
	 * Sets map.
	 *
	 * @param map the map
	 */
	public void setMap(boolean map) {
		isMap = map;
	}

	/**
	 * Gets property type.
	 *
	 * @return the property type
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * Sets property type.
	 *
	 * @param propertyType the property type
	 */
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	/**
	 * Gets persistent entity.
	 *
	 * @return the persistent entity
	 */
	public PersistentEntity getPersistentEntity() {
		return persistentEntity;
	}

	/**
	 * Sets persistent entity.
	 *
	 * @param persistentEntity the persistent entity
	 */
	public void setPersistentEntity(PersistentEntity persistentEntity) {
		this.persistentEntity = persistentEntity;
	}

	/**
	 * Gets locale.
	 *
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets locale.
	 *
	 * @param locale the locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets return type.
	 *
	 * @return the return type
	 */
	public Class getReturnType() {
		Class returnedEntityType = domainType;

		if (ControllerType.PROPERTY.equals(controllerType))
			returnedEntityType = propertyType;

		else if (ControllerType.GENERAL.equals(controllerType))
			returnedEntityType = null;

		return returnedEntityType;
	}
}
