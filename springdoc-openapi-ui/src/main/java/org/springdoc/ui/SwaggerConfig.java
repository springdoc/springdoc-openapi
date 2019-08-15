package org.springdoc.ui;

import static org.springdoc.core.Constants.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Value(SWAGGER_UI_PATH)
	private String swaggerPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf("/"));
		registry.addResourceHandler(uiRootPath + WEB_JARS_PATH + "**").addResourceLocations(WEB_JARS_PATH)
				.resourceChain(false);
	}
}
