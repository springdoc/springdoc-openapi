package org.springdoc.core;

import static org.springframework.util.AntPathMatcher.*;

public final class Constants {

	public static final String DEFAULT_API_DOCS_URL = "/v3/api-docs";
	public static final String DEFAULT_SERVER_DESCRIPTION = "Generated server url";
	public static final String API_DOCS_URL = "${springdoc.api-docs.path:#{T(org.springdoc.core.Constants).DEFAULT_API_DOCS_URL}}";
	public static final String DEFAULT_API_DOCS_URL_YAML = API_DOCS_URL + ".yaml";
	public static final String WEB_JARS_URL = "/webjars/swagger-ui/index.html?url=";
	public static final String DEFAULT_VALIDATOR_URL = "&validatorUrl=";
	public static final String APPLICATION_OPENAPI_YAML = "application/vnd.oai.openapi";
	public static final String DEFAULT_SWAGGER_UI_PATH = DEFAULT_PATH_SEPARATOR + "swagger-ui.html";
	public static final String SWAGGER_UI_PATH = "${springdoc.swagger-ui.path:#{T(org.springdoc.core.Constants).DEFAULT_SWAGGER_UI_PATH}}";

	public static final String GET_METHOD = "get";
	public static final String POST_METHOD = "post";
	public static final String PUT_METHOD = "put";
	public static final String DELETE_METHOD = "delete";
	public static final String PATCH_METHOD = "patch";
	public static final String TRACE_METHOD = "trace";
	public static final String HEAD_METHOD = "head";
	public static final String OPTIONS_METHOD = "options";

	public static final String QUERY_PARAM = "query";
	public static final String HEADER_PARAM = "header";
	public static final String COOKIE_PARAM = "cookie";
	public static final String PATH_PARAM = "path";
	public static final String FORM_PARAM = "form";

	public static final String DEFAULT_DESCRIPTION = "default response";
	public static final String DEFAULT_TITLE = "OpenAPI definition";
	public static final String DEFAULT_VERSION = "v0";
	public static final String DEFAULT_PROPS_LOCATION = "/swagger.properties";
	public static final String DEFAULT_LICENSE_VALUE = "Apache 2.0 License";
	public static final String DEFAULT_LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0.html";
	public static final String OPENAPI_STRING_TYPE = "string";
	public static final String OPENAPI_ARRAY_TYPE = "array";

	private Constants() {
		super();
	}

}
