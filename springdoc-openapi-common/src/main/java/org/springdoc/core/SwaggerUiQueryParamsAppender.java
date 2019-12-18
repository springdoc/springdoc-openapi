package org.springdoc.core;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.springdoc.core.Constants.DEFAULT_VALIDATOR_URL;
import static org.springdoc.core.SwaggerUiConfigProperties.*;

final public class SwaggerUiQueryParamsAppender {

    private SwaggerUiQueryParamsAppender() {
    }

    /**
     * Appends swagger-ui query-params to the provided builder.
     * <p>
     * Since url is derived from springdoc-endpoint it can be provided independently of swaggerUiParams.
     * If configUrl is used, config has to be provided manually including `url`, `validatorUrl`
     *
     * @param uriBuilder      the UriComponentsBuilder to which queryParams get appended
     * @param swaggerUiParams a map of the swaggerUiParams to use
     * @param url             the url to use as queryParameter (gets derived from springdoc properties
     * @return UriComponentsBuilder with appended swagger-ui query-parameters
     */
    public static UriComponentsBuilder appendSwaggerUiQueryParams(final UriComponentsBuilder uriBuilder,
                                                                  final Map<String, String> swaggerUiParams,
                                                                  final String url) {
        if (swaggerUiParams.get(CONFIG_URL_PROPERTY) == null) {
            uriBuilder.queryParam(URL_PROPERTY, url);

            if (swaggerUiParams.get(VALIDATOR_URL_PROPERTY) == null) {
                uriBuilder.queryParam(VALIDATOR_URL_PROPERTY, DEFAULT_VALIDATOR_URL);
            }
        }
        return appendQueryParameter(uriBuilder, swaggerUiParams);
    }

    private static UriComponentsBuilder appendQueryParameter(UriComponentsBuilder uriBuilder,
                                                             Map<String, String> swaggerUiParams) {

        return swaggerUiParams.entrySet().stream()
                .reduce(uriBuilder,
                        (b, e) -> b.queryParam(e.getKey(), e.getValue()),
                        (left, right) -> left);
    }


}
