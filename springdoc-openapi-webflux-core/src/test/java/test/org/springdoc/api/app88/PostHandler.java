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

package test.org.springdoc.api.app88;


import java.net.URI;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
class PostHandler {

	private final PostRepository posts;

	public PostHandler(PostRepository posts) {
		this.posts = posts;
	}

	public Mono<ServerResponse> all(ServerRequest req) {
		return ServerResponse.ok().body(this.posts.findAll(), Post.class);
	}

	public Mono<ServerResponse> create(ServerRequest req) {
		return req.bodyToMono(Post.class)
				.flatMap(post -> this.posts.save(post))
				.flatMap(p -> ServerResponse.created(URI.create("/posts/" + p.getId())).build());
	}

	public Mono<ServerResponse> get(ServerRequest req) {
		return this.posts.findById(req.pathVariable("id"))
				.flatMap(post -> ServerResponse.ok().body(Mono.just(post), Post.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> update(ServerRequest req) {

		return Mono
				.zip(
						(data) -> {
							Post p = (Post) data[0];
							Post p2 = (Post) data[1];
							p.setTitle(p2.getTitle());
							p.setContent(p2.getContent());
							return p;
						},
						this.posts.findById(req.pathVariable("id")),
						req.bodyToMono(Post.class)
				)
				.cast(Post.class)
				.flatMap(post -> this.posts.save(post))
				.flatMap(post -> ServerResponse.noContent().build());

	}

}