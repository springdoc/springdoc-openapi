package org.springdoc.core;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

public final class Constants {

    public static final String DEFAULT_API_DOCS_URL = "/v3/api-docs";
    public static final String DEFAULT_SERVER_DESCRIPTION = "Generated server url";
    public static final String API_DOCS_URL = "${springdoc.api-docs.path:#{T(org.springdoc.core.Constants).DEFAULT_API_DOCS_URL}}";
    public static final String SWAGGGER_CONFIG_FILE = "swagger-config";
    public static final String SWAGGER_CONFIG_URL = API_DOCS_URL + DEFAULT_PATH_SEPARATOR + SWAGGGER_CONFIG_FILE;
    public static final String DEFAULT_API_DOCS_URL_YAML = API_DOCS_URL + ".yaml";
    public static final String SPRINGDOC_ENABLED = "springdoc.api-docs.enabled";
    public static final String SPRINGDOC_GROUPS_ENABLED = "springdoc.api-docs.groups.enabled";
    public static final String SPRINGDOC_GROUPS_ENABLED_VALUE = "${" + SPRINGDOC_GROUPS_ENABLED + ":false}";
    public static final String SPRINGDOC_SWAGGER_UI_ENABLED = "springdoc.swagger-ui.enabled";
    public static final String SPRINGDOC_SHOW_ACTUATOR = "springdoc.show-actuator";
    public static final String SPRINGDOC_SHOW_ACTUATOR_VALUE = "${" + SPRINGDOC_SHOW_ACTUATOR + ":false}";
    public static final String SPRINGDOC_PACKAGES_TO_SCAN = "${springdoc.packagesToScan:#{null}}";
    public static final String SPRINGDOC_PATHS_TO_MATCH = "${springdoc.pathsToMatch:#{null}}";
    public static final String SPRINGDOC_ACTUATOR_TAG = "Actuator";
    public static final String DEFAULT_WEB_JARS_PREFIX_URL = "/webjars";
    public static final String WEB_JARS_PREFIX_URL = "${springdoc.webjars.prefix:#{T(org.springdoc.core.Constants).DEFAULT_WEB_JARS_PREFIX_URL}}";
    public static final String SWAGGER_UI_URL = "/swagger-ui/index.html";
    public static final String APPLICATION_OPENAPI_YAML = "application/vnd.oai.openapi";
    public static final String DEFAULT_SWAGGER_UI_PATH = DEFAULT_PATH_SEPARATOR + "swagger-ui.html";
    public static final String SWAGGER_UI_PATH = "${springdoc.swagger-ui.path:#{T(org.springdoc.core.Constants).DEFAULT_SWAGGER_UI_PATH}}";
    public static final String MVC_SERVLET_PATH = "${spring.mvc.servlet.path:#{null}}";
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
