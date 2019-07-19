package org.springdoc.ui;

import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL;
import static org.springdoc.core.Constants.DEFAULT_VALIDATOR_URL;
import static org.springdoc.core.Constants.SLASH;
import static org.springdoc.core.Constants.WEB_JARS_URL;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerWelcome {

	@Value("${server.contextPath:#{''}}")
	private String contextPath1;

	@Value("${server.servlet.context-path:#{''}}")
	private String contextPath2;

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(value = "/swagger-ui.html")
	public String redirectToUi() {
		String contextPath = StringUtils.EMPTY;
		// spring-boot 2
		if (contextPath2 != null)
			contextPath = contextPath2;
		// spring-boot 1
		else if (contextPath1 != null)
			contextPath = contextPath1;

		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(REDIRECT_URL_PREFIX);
		sbUrl.append(WEB_JARS_URL);
		if (contextPath.endsWith(SLASH))
			sbUrl.append(contextPath.substring(0, contextPath.length() - 1));
		else
			sbUrl.append(contextPath);
		sbUrl.append(DEFAULT_API_DOCS_URL);
		sbUrl.append(DEFAULT_VALIDATOR_URL);
		return sbUrl.toString();
	}
}