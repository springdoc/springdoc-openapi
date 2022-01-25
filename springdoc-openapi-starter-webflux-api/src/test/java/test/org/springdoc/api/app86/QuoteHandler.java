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


import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class QuoteHandler {

	private final Flux<Quote> quoteStream;

	public QuoteHandler(QuoteGenerator quoteGenerator) {
		this.quoteStream = quoteGenerator.fetchQuoteStream(Duration.ofMillis(1000)).share();
	}

	public Mono<ServerResponse> hello(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject("Hello Spring!"));
	}

	public Mono<ServerResponse> echo(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
				.body(request.bodyToMono(String.class), String.class);
	}

	public Mono<ServerResponse> streamQuotes(ServerRequest request) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(this.quoteStream, Quote.class);
	}

	public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
		int size = Integer.parseInt(request.queryParam("size").orElse("10"));
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.quoteStream.take(size), Quote.class);
	}
}