package org.springdoc.ui;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.DEFAULT_VALIDATOR_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.WEB_JARS_URL;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Controller
public class SwaggerWelcome {

	@Value(API_DOCS_URL)
	private String apiDocsUrl;
	@Value(SWAGGER_UI_PATH)
	private String uiPath;


	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(WEB_JARS_URL);
		sbUrl.append(apiDocsUrl);
		sbUrl.append(DEFAULT_VALIDATOR_URL);
		return route(GET(uiPath),
				req -> ServerResponse.temporaryRedirect(URI.create(sbUrl.toString())).build());
	}

}