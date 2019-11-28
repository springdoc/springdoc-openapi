package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

@Component
public class ParameterBuilder extends AbstractParameterBuilder {

    public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
        super(localSpringDocParameterNameDiscoverer);
    }

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, Type paramType, JsonView jsonView) {
        Schema schemaN;
        ParameterizedType parameterizedType = (ParameterizedType) paramType;
        if (Mono.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())
                || Flux.class.getName().contentEquals(parameterizedType.getRawType().getTypeName()))
            schemaN = SpringDocAnnotationsUtils.extractSchema(components, parameterizedType.getActualTypeArguments()[0],
                    jsonView);
        else
            schemaN = SpringDocAnnotationsUtils.extractSchema(components, paramType, jsonView);
        return schemaN;
    }

    public boolean isFile(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (MultipartFile.class.getName().equals(type.getTypeName())
                || FilePart.class.getName().equals(type.getTypeName())) {
            return true;
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            return MultipartFile.class.getName().equals(upperBounds[0].getTypeName());
        }
        return false;
    }

    public boolean isFile(JavaType ct) {
        return MultipartFile.class.isAssignableFrom(ct.getRawClass())
                || FilePart.class.isAssignableFrom(ct.getRawClass());
    }

}
