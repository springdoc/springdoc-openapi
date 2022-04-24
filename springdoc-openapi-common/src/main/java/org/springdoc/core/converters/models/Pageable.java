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
import java.util.Objects;

import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Pageable.
 * @author bnasslahsen
 */
public class Pageable {

	/**
	 * The Page.
	 */
	@Min(0)
	@Parameter(description = "Zero-based page index (0..N)", schema = @Schema(type = "integer", defaultValue = "0"))
	private Integer page;

	/**
	 * The Size.
	 */
	@Min(1)
	@Parameter(description = "The size of the page to be returned", schema = @Schema(type = "integer", defaultValue = "20"))
	private Integer size;

	/**
	 * The Sort.
	 */
	@Parameter(description = "Sorting criteria in the format: property,(asc|desc). "
			+ "Default sort order is ascending. " + "Multiple sort criteria are supported."
			, name = "sort"
			, array = @ArraySchema(schema = @Schema(type = "string")))
	private List<String> sort;

	/**
	 * Instantiates a new Pageable.
	 *
	 * @param page the page
	 * @param size the size
	 * @param sort the sort
	 */
	public Pageable(int page, int size, List<String> sort) {
		this.page = page;
		this.size = size;
		this.sort = sort;
	}

	/**
	 * Gets page.
	 *
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * Sets page.
	 *
	 * @param page the page
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Sets size.
	 *
	 * @param size the size
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * Gets sort.
	 *
	 * @return the sort
	 */
	public List<String> getSort() {
		return sort;
	}

	/**
	 * Sets sort.
	 *
	 * @param sort the sort
	 */
	public void setSort(List<String> sort) {
		if (sort == null) {
			this.sort.clear();
		}
		else {
			this.sort = sort;
		}
	}

	/**
	 * Add sort.
	 *
	 * @param sort the sort
	 */
	public void addSort(String sort) {
		this.sort.add(sort);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pageable pageable = (Pageable) o;
		return Objects.equals(page, pageable.page) &&
				Objects.equals(size, pageable.size) &&
				Objects.equals(sort, pageable.sort);
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, size, sort);
	}

	@Override
	public String toString() {
		return "Pageable{" +
				"page=" + page +
				", size=" + size +
				", sort=" + sort +
				'}';
	}
}