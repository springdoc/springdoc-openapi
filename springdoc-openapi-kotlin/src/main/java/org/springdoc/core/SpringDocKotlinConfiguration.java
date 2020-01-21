package org.springdoc.core;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Optional;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocKotlinConfiguration {

    @Bean
    @Primary
    KotlinCoroutinesRequestBuilder kotlinCoroutinesRequestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
                                                                  OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<ParameterCustomizer>> parameterCustomizers) {
        return new KotlinCoroutinesRequestBuilder(parameterBuilder, requestBodyBuilder,
                operationBuilder, operationCustomizers, parameterCustomizers);
    }

    @Bean
    KotlinCoroutinesReturnTypeParser kotlinCoroutinesReturnTypeParser() {
        return new KotlinCoroutinesReturnTypeParser();
    }


}
