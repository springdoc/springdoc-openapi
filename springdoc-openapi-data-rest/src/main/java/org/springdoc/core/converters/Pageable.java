/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core.converters;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@NotNull
public class Pageable {

	@Nullable
	@Min(0)
	private Integer page;

	@Nullable
	@Min(1)
	@Max(2000)
	private Integer size;

	@Nullable
	private List<String> sort;

	public Pageable(int page, int size, List<String> sort) {
		this.page = page;
		this.size = size;
		this.sort = sort;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<String> getSort() {
		return sort;
	}

	public void setSort(List<String> sort) {
		if (sort == null)
			this.sort.clear();
		else
			this.sort = sort;
	}

	public void addSort(String sort) {
		this.sort.add(sort);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pageable pageable = (Pageable) o;
		return page == pageable.page &&
				size == pageable.size &&
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