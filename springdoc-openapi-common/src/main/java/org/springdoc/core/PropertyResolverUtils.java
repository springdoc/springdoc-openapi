package org.springdoc.core;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

public class PropertyResolverUtils {

    private final ConfigurableBeanFactory factory;

    public PropertyResolverUtils(ConfigurableBeanFactory factory) {
        this.factory = factory;
    }

    String resolve(String parameterProperty) {
        return factory.resolveEmbeddedValue(parameterProperty);
    }
}