package test.org.springdoc.api.app2.api;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.*;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Home redirection to swagger api documentation
 */
@Controller
public class HomeController {

	@Value(SWAGGER_UI_PATH)
	private String swaggerUiPath;

	@GetMapping(DEFAULT_PATH_SEPARATOR)
	public String index() {
		return REDIRECT_URL_PREFIX + swaggerUiPath;
	}
}
