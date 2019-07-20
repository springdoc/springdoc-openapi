package org.springdoc.ui;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.DEFAULT_VALIDATOR_URL;
import static org.springdoc.core.Constants.SPRING_BOOT_1_CONTEXT_PATH;
import static org.springdoc.core.Constants.SPRING_BOOT_2_CONTEXT_PATH;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.WEB_JARS_URL;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerWelcome {

	@Value(SPRING_BOOT_1_CONTEXT_PATH)
	private String contextPath1;

	@Value(SPRING_BOOT_2_CONTEXT_PATH)
	private String contextPath2;

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@io.swagger.v3.oas.annotations.Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
	public String redirectToUi() {
		String contextPath = StringUtils.EMPTY;
		// spring-boot 2
		if (StringUtils.isNotBlank(contextPath2))
			contextPath = contextPath2;
		// spring-boot 1
		else if (StringUtils.isNotBlank(contextPath1))
			contextPath = contextPath1;

		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(REDIRECT_URL_PREFIX);
		sbUrl.append(WEB_JARS_URL);
		if (contextPath.endsWith(DEFAULT_PATH_SEPARATOR)) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
			sbUrl.append(contextPath + apiDocsUrl);
		} else {
			sbUrl.append(contextPath + apiDocsUrl);
		}
		sbUrl.append(DEFAULT_VALIDATOR_URL);
		return sbUrl.toString();
	}
}