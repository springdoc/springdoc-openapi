/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package test.org.springdoc.api.v31.app264;

import java.util.stream.Collectors;

import test.org.springdoc.api.v31.app264.user.UserMapper;
import test.org.springdoc.api.v31.app264.user.UserRepository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * The type User handler.
 *
 * @author bnasslahsen
 */
@Component
public class UserHandler {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	/**
	 * Instantiates a new User handler.
	 * @param userRepository the user repository
	 * @param userMapper the user mapper
	 */
	public UserHandler(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	/**
	 * Find all users v1 server response.
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse findAllV1(ServerRequest request) {
		return ServerResponse.ok()
			.body(userRepository.findAll().stream().map(userMapper::toV1).collect(Collectors.toList()));
	}

	/**
	 * Find all users v2 server response.
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse findAllV2(ServerRequest request) {
		return ServerResponse.ok()
			.body(userRepository.findAll().stream().map(userMapper::toV2).collect(Collectors.toList()));
	}

}
