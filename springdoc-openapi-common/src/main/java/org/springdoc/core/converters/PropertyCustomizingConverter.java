package org.springdoc.core.converters;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizer.PropertyCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

public class PropertyCustomizingConverter implements ModelConverter {

    private final List<PropertyCustomizer> customizers;

    public PropertyCustomizingConverter(List<PropertyCustomizer> customizers) {
        this.customizers = customizers;
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
            if (type.isSchemaProperty() && !CollectionUtils.isEmpty(customizers)) {
                for (PropertyCustomizer customizer : customizers)
                    resolvedSchema = customizer.customize(resolvedSchema, type);
            }
            return resolvedSchema;
        } else {
            return null;
        }
    }
}
