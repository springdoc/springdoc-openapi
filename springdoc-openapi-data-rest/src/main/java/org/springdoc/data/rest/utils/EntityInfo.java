package org.springdoc.data.rest.utils;

import java.util.List;

/**
 * The type Entity info.
 * @author bnasslashen
 */
public class EntityInfo {

	/**
	 * The Ignored fields.
	 */
	private List<String> ignoredFields;

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
