package org.springdoc.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SpringDocKotlinConfiguration {

    @Bean
    @Primary
    KotlinCoroutinesRequestBuilder kotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                                                  OperationBuilder operationBuilder) {
        return new KotlinCoroutinesRequestBuilder(parameterBuilder, requestBodyBuilder,
                operationBuilder);
    }


}
