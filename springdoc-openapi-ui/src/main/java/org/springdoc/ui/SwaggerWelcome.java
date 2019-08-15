package org.springdoc.ui;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.*;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class SwaggerWelcome {

	@Value(API_DOCS_URL)
	private String apiDocsUrl;

	@Value(SWAGGER_UI_PATH)
	private String swaggerPath;

	@Operation(hidden = true)
	@GetMapping(SWAGGER_UI_PATH)
	public String redirectToUi(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf('/'));
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(REDIRECT_URL_PREFIX);
		sbUrl.append(uiRootPath);
		sbUrl.append(SWAGGER_UI_URL);
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