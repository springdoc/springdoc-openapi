/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import org.springframework.util.ResourceUtils;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

public final class Constants {

	public static final String DEFAULT_API_DOCS_URL = "/v3/api-docs";

	public static final String DEFAULT_SERVER_DESCRIPTION = "Generated server url";

	public static final String API_DOCS_URL = "${springdoc.api-docs.path:#{T(org.springdoc.core.Constants).DEFAULT_API_DOCS_URL}}";

	public static final String SWAGGGER_CONFIG_FILE = "swagger-config";

	public static final String SWAGGER_CONFIG_URL = API_DOCS_URL + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;

	public static final String DEFAULT_API_DOCS_URL_YAML = API_DOCS_URL + ".yaml";

	public static final String SPRINGDOC_ENABLED = "springdoc.api-docs.enabled";

	public static final String SPRINGDOC_CACHE_DISABLED = "springdoc.cache.disabled";

	public static final String SPRINGDOC_SWAGGER_UI_ENABLED = "springdoc.swagger-ui.enabled";

	public static final String SPRINGDOC_SWAGGER_UI_CONFIG_URL = "springdoc.swagger-ui.configUrl";

	public static final String SPRINGDOC_SWAGGER_UI_URL = "springdoc.swagger-ui.url";

	public static final String SPRINGDOC_QDSLPREDICATE_MODE = "springdoc.qdslpredicate.mode";

	public static final String NULL = ":#{null}";

	public static final String MVC_SERVLET_PATH = "${spring.mvc.servlet.path"+ NULL +"}";

	public static final String SPRINGDOC_SWAGGER_UI_URL_VALUE = "${" + SPRINGDOC_SWAGGER_UI_URL + NULL + "}";

	public static final String SPRINGDOC_OAUTH2_REDIRECT_URL = "springdoc.swagger-ui.oauth2RedirectUrl";

	public static final String SPRINGDOC_OAUTH2_REDIRECT_URL_VALUE = "${" + SPRINGDOC_OAUTH2_REDIRECT_URL + NULL + "}";

	public static final String SPRINGDOC_SWAGGER_UI_CONFIG_URL_VALUE = "${" + SPRINGDOC_SWAGGER_UI_CONFIG_URL + NULL + "}";

	public static final String SPRINGDOC_SHOW_ACTUATOR = "springdoc.show-actuator";

	public static final String SPRINGDOC_ACTUATOR_TAG = "Actuator";

	public static final String SPRINGDOC_ACTUATOR_DESCRIPTION = "Monitor and interact";

	public static final String SPRINGDOC_ACTUATOR_DOC_URL = "https://docs.spring.io/spring-boot/docs/current/actuator-api/html/";

	public static final String SPRINGDOC_ACTUATOR_DOC_DESCRIPTION = "Spring Boot Actuator Web API Documentation";

	public static final String DEFAULT_WEB_JARS_PREFIX_URL = "/webjars";

	public static final String WEB_JARS_PREFIX_URL = "${springdoc.webjars.prefix:" + DEFAULT_WEB_JARS_PREFIX_URL + "}";

	public static final String CLASSPATH_RESOURCE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "/META-INF/resources";

	public static final String SWAGGER_UI_URL = "/swagger-ui/index.html";

	public static final String SWAGGER_UI_OAUTH_REDIRECT_URL = "/swagger-ui/oauth2-redirect.html";

	public static final String APPLICATION_OPENAPI_YAML = "application/vnd.oai.openapi";

	public static final String DEFAULT_SWAGGER_UI_PATH = DEFAULT_PATH_SEPARATOR + "swagger-ui.html";

	public static final String SWAGGER_UI_PATH = "${springdoc.swagger-ui.path:#{T(org.springdoc.core.Constants).DEFAULT_SWAGGER_UI_PATH}}";

	public static final String DEFAULT_GROUP_NAME="springdocDefault";

	public static final String GET_METHOD = "get";

	public static final String POST_METHOD = "post";

	public static final String PUT_METHOD = "put";

	public static final String DELETE_METHOD = "delete";

	public static final String PATCH_METHOD = "patch";

	public static final String TRACE_METHOD = "trace";

	public static final String HEAD_METHOD = "head";

	public static final String OPTIONS_METHOD = "options";

	public static final String QUERY_PARAM = "query";

	public static final String DEFAULT_DESCRIPTION = "default response";

	public static final String DEFAULT_TITLE = "OpenAPI definition";

	public static final String DEFAULT_VERSION = "v0";

	public static final String OPENAPI_STRING_TYPE = "string";

	public static final String OPENAPI_ARRAY_TYPE = "array";

	private Constants() {
		super();
	}

}
