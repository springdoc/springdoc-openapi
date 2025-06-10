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

package test.org.springdoc.api.v30.app3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.org.springdoc.api.v30.app3.dto.TweetDTO;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rajeevkumarsingh on 08/09/17.
 */
@RestController
public class TweetController {


	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get All Tweets") })
	@GetMapping("/tweets")
	public Flux<TweetDTO> getAllTweets() {
		return null;
	}

	@PostMapping("/tweets")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "create Tweets") })
	public Mono<TweetDTO> createTweets(@Valid @RequestBody TweetDTO tweetDTO) {
		return null;
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get Tweet By Id"),
			@ApiResponse(responseCode = "404", description = "tweet not found") })
	@GetMapping("/tweets/{id}")
	public Mono<ResponseEntity<TweetDTO>> getTweetById(@PathVariable(value = "id") String tweetId) {
		return null;
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "update Tweet"),
			@ApiResponse(responseCode = "404", description = "tweet not found") })
	@PutMapping("/tweets/{id}")
	public Mono<ResponseEntity<TweetDTO>> updateTweet(@PathVariable(value = "id") String tweetId,
			@Valid @RequestBody TweetDTO tweetDTO) {
		return null;
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "delete Tweet"),
			@ApiResponse(responseCode = "404", description = "tweet not found") })
	@DeleteMapping("/tweets/{id}")
	public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String tweetId) {
		return null;
	}

	@Operation(description = "Tweets are Sent to the client as Server Sent Events", responses = {
			@ApiResponse(responseCode = "200", description = "stream All Tweets") })
	@GetMapping(value = "/stream/tweets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<TweetDTO> streamAllTweets() {
		return null;
	}

}
