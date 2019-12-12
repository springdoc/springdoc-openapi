package org.springdoc.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class PropertyResolverUtils {

    @Autowired
    ConfigurableBeanFactory factory;

    String resolve(String parameterProperty) {
        return factory.resolveEmbeddedValue(parameterProperty);
    }
}