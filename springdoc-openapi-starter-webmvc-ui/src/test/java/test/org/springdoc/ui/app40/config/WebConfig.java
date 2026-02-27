package test.org.springdoc.ui.app40.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .usePathSegment(1)
				.detectSupportedVersions(false)
                .addSupportedVersions("1.0","2.0")
                .setDefaultVersion("1.0")
				.setVersionRequired(false)
                .setVersionParser(new ApiVersionParser());
    }

}
