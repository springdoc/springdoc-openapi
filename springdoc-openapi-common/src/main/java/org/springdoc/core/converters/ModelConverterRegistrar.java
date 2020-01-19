package org.springdoc.core.converters;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;

import java.util.List;

/**
 * Wrapper for model converters to only register converters once
 */
public class ModelConverterRegistrar {

    /**
     * @param modelConverters spring registered model converter beans which have to be
     *                        registered in {@link ModelConverters} instance
     */
    public ModelConverterRegistrar(List<ModelConverter> modelConverters) {
        modelConverters.forEach(ModelConverters.getInstance()::addConverter);
    }
}
