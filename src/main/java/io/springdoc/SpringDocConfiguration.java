package io.springdoc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "io.springdoc.core" })
public class SpringDocConfiguration {

}
