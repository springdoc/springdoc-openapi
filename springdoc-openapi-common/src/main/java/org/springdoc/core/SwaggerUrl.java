package org.springdoc.core;

import java.util.Objects;

import static org.springdoc.core.Constants.GROUP_NAME_NOT_NULL;


/**
 * The type Swagger url.
 */
public class SwaggerUrl {
	/**
	 * The Url.
	 */
	private String url;

	/**
	 * The Name.
	 */
	private String name;

	/**
	 * Instantiates a new Swagger url.
	 */
	public SwaggerUrl() {
	}

	/**
	 * Instantiates a new Swagger url.
	 *
	 * @param group the group
	 * @param url the url
	 */
	public SwaggerUrl(String group, String url) {
		Objects.requireNonNull(group, GROUP_NAME_NOT_NULL);
		this.url = url;
		this.name = group;
	}

	/**
	 * Instantiates a new Swagger url.
	 *
	 * @param group the group
	 */
	public SwaggerUrl(String group) {
		Objects.requireNonNull(group, GROUP_NAME_NOT_NULL);
		this.name = group;
	}

	/**
	 * Gets url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets url.
	 *
	 * @param url the url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SwaggerUrl that = (SwaggerUrl) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SwaggerUrl{");
		sb.append("url='").append(url).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}