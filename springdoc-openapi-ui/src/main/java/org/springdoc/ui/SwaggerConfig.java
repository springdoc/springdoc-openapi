package org.springdoc.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springdoc.core.Constants.WEB_JARS_PREFIX_URL;

@Configuration
@EnableWebMvc
@SuppressWarnings("deprecation")
 class SwaggerConfig extends WebMvcConfigurerAdapter { // NOSONAR

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uiRootPath = "";
        if (swaggerPath.contains("/")) {
            uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf('/'));
        }
        registry.addResourceHandler(uiRootPath + "/**").addResourceLocations(WEB_JARS_PREFIX_URL + "/")
                .resourceChain(false);
    }
}
