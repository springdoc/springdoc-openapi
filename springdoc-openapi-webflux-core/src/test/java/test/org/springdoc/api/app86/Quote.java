/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package test.org.springdoc.api.app86;


import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

public class Quote {

	private static final MathContext MATH_CONTEXT = new MathContext(2);

	private String ticker;

	private BigDecimal price;

	private Instant instant;

	public Quote() {
	}

	public Quote(String ticker, BigDecimal price) {
		this.ticker = ticker;
		this.price = price;
	}

	public Quote(String ticker, Double price) {
		this(ticker, new BigDecimal(price, MATH_CONTEXT));
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}

	@Override
	public String toString() {
		return "Quote{" +
				"ticker='" + ticker + '\'' +
				", price=" + price +
				", instant=" + instant +
				'}';
	}
}