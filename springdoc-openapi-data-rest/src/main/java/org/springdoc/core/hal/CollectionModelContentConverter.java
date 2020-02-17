package org.springdoc.core.hal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

import java.util.Collection;
import java.util.Iterator;

/**
 * Override resolved schema as there is a custom serializer that converts the data to a map before serializing it.
 *
 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer
 * @see org.springframework.hateoas.mediatype.hal.Jackson2HalModule.HalResourcesSerializer#serialize(Collection, JsonGenerator, SerializerProvider)
 */
public class CollectionModelContentConverter implements ModelConverter {
    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext() && type != null && type.getType() instanceof CollectionType
                && "_embedded".equalsIgnoreCase(type.getPropertyName())) {
            Schema<?> schema = chain.next().resolve(type, context, chain);
            if (schema instanceof ArraySchema) {
                return new MapSchema()
                        .name("_embedded")
                        .additionalProperties(new StringSchema())
                        .additionalProperties(schema);
            }
        }
        return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }
}
