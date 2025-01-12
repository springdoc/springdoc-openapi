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

package test.org.springdoc.api.v30.app2.config;

import java.time.LocalDate;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import test.org.springdoc.api.v30.app2.entities.Post;
import test.org.springdoc.api.v30.app2.repositories.PostRepository;

import org.springframework.context.annotation.Configuration;

/**
 * @author Davide Pedone
 * 2020
 */
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

	private final PostRepository postRepository;

	@PostConstruct
	private void postConstruct() {
		for (int i = 0; i < 33; i++) {
			Post post = new Post();
			post.setAuthor("name" + i);
			post.setContent("content" + i);
			post.setCreatedAt(LocalDate.now().toEpochDay());
			postRepository.save(post);
		}
	}
}
