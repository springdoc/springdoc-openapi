package org.springdoc.core.customizers;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;

/**
 * Implement and register a bean of type {@link PropertyCustomizer} to customize a schema property
 * based on annotated type
 */
public interface PropertyCustomizer {

    /**
     * @param property to be customized
     * @param type     form the model class
     * @return customized property
     */
    Schema customize(Schema property, AnnotatedType type);
}
