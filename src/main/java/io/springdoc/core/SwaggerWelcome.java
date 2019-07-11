package io.springdoc.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Redirect to access swagger UI via short URL from "/swagger-ui" to
// "/swagger-ui/index.html?url=/api/swagger&validatorUrl="
@Controller
public class SwaggerWelcome {

	@Value("${server.contextPath:#{''}}")
	private String contextPath1;

	@Value("${server.servlet.context-path:#{''}}")
	private String contextPath2;
	
	@RequestMapping(value = "/swagger-ui.html", method = RequestMethod.GET)
	public String redirectToUi() {
		// return "redirect:/index.html?url=/openapi.json";
		String contextPath = "";
		// spring-boot 2
		if (contextPath2 != null)
			contextPath = contextPath2;
		// spring-boot 1
		else if (contextPath1 != null)
			contextPath = contextPath1;

		return "redirect:/webjars/swagger-ui/index.html?url=" + contextPath + "/openapi.json";
	}
}