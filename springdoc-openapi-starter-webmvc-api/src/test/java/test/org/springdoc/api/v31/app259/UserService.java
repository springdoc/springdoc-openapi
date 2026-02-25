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

package test.org.springdoc.api.v31.app259;

import java.util.List;
import java.util.stream.Collectors;

import test.org.springdoc.api.v31.app259.user.UserDTOv1;
import test.org.springdoc.api.v31.app259.user.UserDTOv2;
import test.org.springdoc.api.v31.app259.user.UserMapper;
import test.org.springdoc.api.v31.app259.user.UserRepository;

import org.springframework.stereotype.Service;

/**
 * The type User service. Provides typed return methods for springdoc introspection.
 *
 * @author bnasslahsen
 */
@Service
public class UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	/**
	 * Instantiates a new User service.
	 * @param userRepository the user repository
	 * @param userMapper the user mapper
	 */
	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	/**
	 * Gets users v1.
	 * @return the users v1
	 */
	public List<UserDTOv1> getUsersV1() {
		return userRepository.findAll().stream().map(userMapper::toV1).collect(Collectors.toList());
	}

	/**
	 * Gets users v2.
	 * @return the users v2
	 */
	public List<UserDTOv2> getUsersV2() {
		return userRepository.findAll().stream().map(userMapper::toV2).collect(Collectors.toList());
	}

}
