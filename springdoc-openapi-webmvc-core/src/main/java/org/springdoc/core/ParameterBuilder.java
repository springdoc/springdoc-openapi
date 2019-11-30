package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

@Component
public class ParameterBuilder extends AbstractParameterBuilder {

    public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
        super(localSpringDocParameterNameDiscoverer, ignoredParameterAnnotations);
    }

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, Type paramType, JsonView jsonView) {
        return SpringDocAnnotationsUtils.extractSchema(components, paramType, jsonView);
    }

    public boolean isFile(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (MultipartFile.class.getName().equals(type.getTypeName())) {
            return true;
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            return MultipartFile.class.getName().equals(upperBounds[0].getTypeName());
        }
        return false;
    }

    public boolean isFile(JavaType ct) {
        return MultipartFile.class.isAssignableFrom(ct.getRawClass());
    }
}
