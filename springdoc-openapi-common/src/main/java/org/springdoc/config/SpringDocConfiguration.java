package org.springdoc.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"org.springdoc"})
public class SpringDocConfiguration {

    @Bean
    LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
        return new LocalVariableTableParameterNameDiscoverer();
    }
}
