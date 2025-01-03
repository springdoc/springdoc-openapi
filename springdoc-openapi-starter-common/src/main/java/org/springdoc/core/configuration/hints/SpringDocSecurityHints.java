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

package org.springdoc.core.configuration.hints;

import java.util.Arrays;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * The type Spring doc DataRest hints.
 *
 * @author bnasslahsen
 */
public class SpringDocSecurityHints implements RuntimeHintsRegistrar {

	/**
	 * The Spring security type names.
	 */
//spring-security
	static String[] springSecurityTypeNames = { "org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter",
			"org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter",
			"org.springframework.security.web.util.matcher.OrRequestMatcher"
	};

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		//spring-security
		Arrays.stream(springSecurityTypeNames).forEach(springDataRestTypeName ->
				hints.reflection()
						.registerTypeIfPresent(classLoader, springDataRestTypeName,
								hint -> hint.withMembers(MemberCategory.DECLARED_FIELDS,
										MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
										MemberCategory.INVOKE_DECLARED_METHODS
								))
		);
	}

}

