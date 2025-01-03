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

package org.springdoc.webmvc.ui;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.utils.SpringDocUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springdoc.core.utils.Constants.MVC_SERVLET_PATH;
import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

/**
 * Home redirection to swagger api documentation
 */
@Controller
public class SwaggerUiHome {

	/**
	 * The Swagger ui path.
	 */
	@Value(SWAGGER_UI_PATH)
	private String swaggerUiPath;

	/**
	 * The Mvc servlet path.
	 */
	@Value(MVC_SERVLET_PATH)
	private String mvcServletPath;

	/**
	 * Index string.
	 *
	 * @return the string
	 */
	@GetMapping(DEFAULT_PATH_SEPARATOR)
	@Operation(hidden = true)
	public String index() {
		StringBuilder uiRootPath = new StringBuilder();
		if (SpringDocUtils.isValidPath(mvcServletPath))
			uiRootPath.append(mvcServletPath);

		return REDIRECT_URL_PREFIX + uiRootPath + swaggerUiPath;
	}
}