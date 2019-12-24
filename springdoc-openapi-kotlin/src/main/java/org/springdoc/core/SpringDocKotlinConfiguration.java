package org.springdoc.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocKotlinConfiguration {

    @Bean
    @Primary
    KotlinCoroutinesRequestBuilder kotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                                                  OperationBuilder operationBuilder) {
        return new KotlinCoroutinesRequestBuilder(parameterBuilder, requestBodyBuilder,
                operationBuilder);
    }


}
