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

package test.org.springdoc.api.v30.app37;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * @author bnasslahsen
 */

@Entity
@Table(name = "PRODUCT_ENTITY")
public class ProductEntity extends BaseEntity implements Comparable<ProductEntity> {

	/**
	 * SVUDI
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 名字.
	 */
	@NotNull
	@Column(nullable = false)
	private String name;

	/**
	 * 单价.
	 */
	@NotNull
	@Column(nullable = false)
	private BigDecimal price;

	/**
	 * 日期.
	 */
	@NotNull
	@Column(nullable = false)
	private LocalDate date;

	@Override
	public boolean isValid() {
		return name != null && price != null && date != null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * 根据日期排序
	 */
	@Override
	public int compareTo(ProductEntity oProduct) {
		return this.getDate().compareTo(oProduct.getDate());
	}

}
