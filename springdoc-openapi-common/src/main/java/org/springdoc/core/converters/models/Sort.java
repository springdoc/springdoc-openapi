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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The Sort type.
 * @author daniel-shuy
 */
public class Sort {

	/**
	 * The Sort.
	 */
	@Parameter(description = "Sorting criteria in the format: property,(asc|desc). "
			+ "Default sort order is ascending. " + "Multiple sort criteria are supported."
			, name = "sort"
			, array = @ArraySchema(schema = @Schema(type = "string")))
	private List<String> sort;

	/**
	 * Instantiates a new Sort.
	 *
	 * @param sort the sort
	 */
	public Sort(List<String> sort) {
		this.sort = sort;
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
		Sort sort = (Sort) o;
		return Objects.equals(this.sort, sort.sort);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sort);
	}

	@Override
	public String toString() {
		return "Sort{" +
				"sort=" + sort +
				'}';
	}
}