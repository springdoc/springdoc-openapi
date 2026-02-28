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

package test.org.springdoc.api.v30.app20;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "bank")
public class Bank implements EntityDefinition {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bnk_seq")
	@SequenceGenerator(name = "bnk_seq", sequenceName = "bnk_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
	@NaturalId
	private String code;

	@Column(unique = true, nullable = false)
	private String name;

	public Long getId() {
		return this.id;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCode());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Bank)) {
			return false;
		}
		Bank bank = (Bank) o;
		return Objects.equals(getCode(), bank.getCode());
	}

	@Override
	public String toString() {
		return "Bank{" +
				"id=" + id +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public String getKey() {
		return code;
	}

	@Override
	public String getDescription() {
		return name;
	}
}
