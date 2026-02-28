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

package test.org.springdoc.api.v31.app262;

import java.util.stream.Collectors;

import test.org.springdoc.api.v31.app262.user.UserMapper;
import test.org.springdoc.api.v31.app262.user.UserRepository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * The type User handler.
 *
 * @author  bnasslahsen
 */
@Component
public class UserHandler {

	/**
	 * The User service.
	 */
	private final UserService userService;

	/**
	 * Instantiates a new User handler.
	 *
	 * @param userService the user service
	 */
	public UserHandler(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get users media v1.
	 * @param request the request 
	 * @return  the server response
	 */
	public ServerResponse getUsersMediaV1(ServerRequest request) {
		return ServerResponse.ok().body(userService.getUsersMediaV1());
	}

	/**
	 * Get users media v2.
	 * @param request the request 
	 * @return  the server response
	 */
	public ServerResponse getUsersMediaV2(ServerRequest request) {
		return ServerResponse.ok().body(userService.getUsersMediaV2());
	}

}
