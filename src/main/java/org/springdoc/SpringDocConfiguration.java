package org.springdoc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.springdoc.core" })
public class SpringDocConfiguration {

}
