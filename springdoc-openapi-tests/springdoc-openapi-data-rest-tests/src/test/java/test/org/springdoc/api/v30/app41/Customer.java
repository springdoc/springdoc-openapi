/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v30.app41;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.springframework.lang.Nullable;

/**
 * Exported entity with associations to entities that have no exported repository.
 *
 * @author bnasslahsen
 */
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToMany
	private Set<CustomerRelationship> relationships;

	@ManyToOne
	private CustomerRegion region;

	@ManyToOne
	@Nullable
	private CustomerRegion billingRegion;

	@ManyToOne
	@Schema(description = "The shipping region")
	private CustomerRegion shippingRegion;

	@ManyToOne
	private Segment segment;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<CustomerRelationship> getRelationships() {
		return relationships;
	}

	public CustomerRegion getRegion() {
		return region;
	}

	public CustomerRegion getBillingRegion() {
		return billingRegion;
	}

	public CustomerRegion getShippingRegion() {
		return shippingRegion;
	}

	public Segment getSegment() {
		return segment;
	}
}
