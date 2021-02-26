/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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
 *
 */

package org.springdoc.data.rest.core;

/**
 * The type Data rest repository.
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
	 * Instantiates a new Data rest repository.
	 *
	 * @param domainType the domain type
	 * @param repositoryType the repository type
	 */
	public DataRestRepository(Class<?> domainType, Class<?> repositoryType) {
		this.domainType = domainType;
		this.repositoryType = repositoryType;
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
}
