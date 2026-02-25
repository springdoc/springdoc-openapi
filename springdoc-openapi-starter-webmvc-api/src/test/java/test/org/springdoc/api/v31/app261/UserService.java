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

package test.org.springdoc.api.v31.app261;

import java.util.List;

import test.org.springdoc.api.v31.app261.user.User;
import test.org.springdoc.api.v31.app261.user.UserRepository;

import org.springframework.stereotype.Service;

/**
 * The type User service. Provides typed return methods for springdoc introspection.
 *
 * @author bnasslahsen
 */
@Service
public class UserService {

	private final UserRepository userRepository;

	/**
	 * Instantiates a new User service.
	 * @param userRepository the user repository
	 */
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Find all users v1.
	 * @return the users
	 */
	public List<User> findAllv1() {
		return userRepository.findAll();
	}

	/**
	 * Find all users v2.
	 * @return the users
	 */
	public List<User> findAllv2() {
		return userRepository.findAll();
	}

}
