package org.springdoc.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springdoc.core.Constants.*;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;


@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
@SuppressWarnings("deprecation")
public class SwaggerConfig extends WebMvcConfigurerAdapter { // NOSONAR

    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;

    @Value(WEB_JARS_PREFIX_URL)
    private String webJarsPrefixUrl;

    @Autowired
    private SwaggerIndexTransformer swaggerIndexTransformer;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        StringBuilder uiRootPath = new StringBuilder();
        if (swaggerPath.contains("/")) {
            uiRootPath.append(swaggerPath.substring(0, swaggerPath.lastIndexOf('/')));
        }
        uiRootPath.append("/**");
        String webJarsLocation = webJarsPrefixUrl + DEFAULT_PATH_SEPARATOR;
        registry.addResourceHandler(uiRootPath + "/swagger-ui/**").addResourceLocations(webJarsLocation)
                .resourceChain(false)
                .addTransformer(swaggerIndexTransformer);
    }

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    public SwaggerWelcome swaggerWelcome() {
        return new SwaggerWelcome();
    }

    @Bean
    @ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
    public SwaggerIndexTransformer indexPageTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
        return new SwaggerIndexTransformer(swaggerUiOAuthProperties, objectMapper);
    }
}
