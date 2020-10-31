/*
 *
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app90.quotes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static test.org.springdoc.api.AbstractSpringDocTest.HANDLER_FUNCTION;

@Configuration
class QuotesRouter {

	@Bean
	 RouterFunction<ServerResponse> myroute() {
		return route().GET("/hello", accept(TEXT_PLAIN), HANDLER_FUNCTION, ops -> ops.tag("quotes")
				.operationId("hello").response(responseBuilder().responseCode("200"))).build()

				.and(route().POST("/echo", accept(TEXT_PLAIN).and(contentType(TEXT_PLAIN)), HANDLER_FUNCTION, ops -> ops.tag("quotes")
						.operationId("echo")
						.requestBody(requestBodyBuilder().implementation(String.class))
						.response(responseBuilder().responseCode("200").implementation(String.class))).build())

				.and(route().POST("/echo", accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)), HANDLER_FUNCTION, ops -> ops.tag("quotes")
						.operationId("echo")
						.requestBody(requestBodyBuilder().implementation(String.class))
						.response(responseBuilder().responseCode("200").implementation(String.class))).build())

				.and(route().GET("/quotes", accept(APPLICATION_JSON), HANDLER_FUNCTION, ops -> ops.tag("quotes")
						.operationId("fetchQuotes")
						.parameter(parameterBuilder().in(ParameterIn.QUERY).name("size").implementation(String.class))
						.response(responseBuilder().responseCode("200").implementationArray(Quote.class))).build())

				.and(route().GET("/quotes", accept(APPLICATION_STREAM_JSON), HANDLER_FUNCTION, ops -> ops.tag("quotes")
						.operationId("fetchQuotes")
						.response(responseBuilder().responseCode("200").implementation(Quote.class))).build());
	}


	class Quote {

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
			this(ticker, new BigDecimal(price, new MathContext(2)));
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
}
