/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app264.user;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

/**
 * The type User repository.
 *
 * @author bnasslahsen
 */
@Repository
public class UserRepository {

	private final List<User> users = new ArrayList<>();

	/**
	 * Find all users.
	 * @return the list of users
	 */
	public List<User> findAll() {
		return users;
	}

	/**
	 * Find user by id.
	 * @param id the id
	 * @return the user
	 */
	public User findById(Integer id) {
		return users.stream().filter(u -> u.id().equals(id)).findFirst().orElse(null);
	}

	@PostConstruct
	private void init() {
		users.add(new User(1, "Dan Vega", "danvega@gmail.com"));
	}

}
