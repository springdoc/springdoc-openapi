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

package test.org.springdoc.api.v31.app83;

import java.time.Instant;
import java.util.Objects;

public class CoffeeOrder {
	private String coffeeId;

	private Instant whenOrdered;

	public CoffeeOrder() {
	}

	public CoffeeOrder(String coffeeId, Instant whenOrdered) {
		this.coffeeId = coffeeId;
		this.whenOrdered = whenOrdered;
	}

	public String getCoffeeId() {
		return coffeeId;
	}

	public Instant getWhenOrdered() {
		return whenOrdered;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CoffeeOrder that = (CoffeeOrder) o;
		return Objects.equals(coffeeId, that.coffeeId) &&
				Objects.equals(whenOrdered, that.whenOrdered);
	}

	@Override
	public int hashCode() {
		return Objects.hash(coffeeId, whenOrdered);
	}

	@Override
	public String toString() {
		return "CoffeeOrder{" +
				"coffeeId='" + coffeeId + '\'' +
				", whenOrdered=" + whenOrdered +
				'}';
	}
}
