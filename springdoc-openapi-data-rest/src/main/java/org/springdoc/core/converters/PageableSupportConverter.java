package org.springdoc.core.converters;


import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public class PageableSupportConverter implements ModelConverter {

    private static final String PAGEABLE_TO_REPLACE = "org.springframework.data.domain.Pageable";
    private static final String PAGE_REQUEST_TO_REPLACE = "org.springframework.data.domain.PageRequest";
    private static final AnnotatedType PAGEABLE = new AnnotatedType(Pageable.class);

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        String typeName = type.getType().getTypeName();
        if (PAGEABLE_TO_REPLACE.equals(typeName) || PAGE_REQUEST_TO_REPLACE.equals(typeName)) {
            type = PAGEABLE;
        }
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }
}