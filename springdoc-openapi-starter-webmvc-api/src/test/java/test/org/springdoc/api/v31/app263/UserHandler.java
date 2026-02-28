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

package test.org.springdoc.api.v31.app263;

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

	private final UserService userService;

	public UserHandler(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Find all users v1.
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse findAllV1(ServerRequest request) {
		return ServerResponse.ok().body(userService.findAllv1());
	}

	/**
	 * Find all users v2.
	 * @param request the request
	 * @return the server response
	 */
	public ServerResponse findAllV2(ServerRequest request) {
		return ServerResponse.ok().body(userService.findAllv2());
	}

}
