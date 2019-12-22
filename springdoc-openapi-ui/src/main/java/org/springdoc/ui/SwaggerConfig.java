package org.springdoc.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;


@Configuration
@EnableWebMvc
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@SuppressWarnings("deprecation")
public class SwaggerConfig extends WebMvcConfigurerAdapter { // NOSONAR

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Value(WEB_JARS_PREFIX_URL)
    private String webJarsPrefixUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uiRootPath = "";
        if (swaggerPath.contains("/")) {
            uiRootPath = swaggerPath.substring(0, swaggerPath.lastIndexOf('/'));
        }
        uiRootPath += "/**";

        String webJarsLocation = webJarsPrefixUrl + DEFAULT_PATH_SEPARATOR;

        registry.addResourceHandler(uiRootPath + "/swagger-ui/swagger-ui.css").addResourceLocations(webJarsLocation)
                .resourceChain(false);
        registry.addResourceHandler(uiRootPath + "/swagger-ui/swagger-ui-standalone-preset.js").addResourceLocations(webJarsLocation)
                .resourceChain(false);
        registry.addResourceHandler(uiRootPath + "/swagger-ui/swagger-ui-bundle.js").addResourceLocations(webJarsLocation)
                .resourceChain(false);
        registry.addResourceHandler(uiRootPath + "/swagger-ui/index.html").addResourceLocations(webJarsLocation)
                .resourceChain(false);
        registry.addResourceHandler(uiRootPath + "/swagger-ui/favicon-32x32.png").addResourceLocations(webJarsLocation)
                .resourceChain(false);
        registry.addResourceHandler(uiRootPath + "/swagger-ui/favicon-16x16.png").addResourceLocations(webJarsLocation)
                .resourceChain(false);
    }

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    public SwaggerWelcome swaggerWelcome() {
        return new SwaggerWelcome();
    }
}
