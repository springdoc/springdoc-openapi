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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;

@Component
public class QuoteGenerator {

	private final MathContext mathContext = new MathContext(2);

	private final Random random = new Random();

	private final List<Quote> prices = new ArrayList<>();

	/**
	 * Bootstraps the generator with tickers and initial prices
	 */
	public QuoteGenerator() {
		this.prices.add(new Quote("CTXS", 82.26));
		this.prices.add(new Quote("DELL", 63.74));
		this.prices.add(new Quote("GOOG", 847.24));
		this.prices.add(new Quote("MSFT", 65.11));
		this.prices.add(new Quote("ORCL", 45.71));
		this.prices.add(new Quote("RHT", 84.29));
		this.prices.add(new Quote("VMW", 92.21));
	}


	public Flux<Quote> fetchQuoteStream(Duration period) {

		// We want to emit quotes with a specific period;
		// to do so, we create a Flux.interval
		return Flux.interval(period)
				// In case of back-pressure, drop events
				.onBackpressureDrop()
				// For each tick, generate a list of quotes
				.map(this::generateQuotes)
				// "flatten" that List<Quote> into a Flux<Quote>
				.flatMapIterable(quotes -> quotes)
				.log("io.spring.workshop.stockquotes");
	}

	/*
	 * Create quotes for all tickers at a single instant.
	 */
	private List<Quote> generateQuotes(long interval) {
		final Instant instant = Instant.now();
		return prices.stream()
				.map(baseQuote -> {
					BigDecimal priceChange = baseQuote.getPrice()
							.multiply(new BigDecimal(0.05 * this.random.nextDouble()), this.mathContext);
					Quote result = new Quote(baseQuote.getTicker(), baseQuote.getPrice().add(priceChange));
					result.setInstant(instant);
					return result;
				})
				.collect(Collectors.toList());
	}

}