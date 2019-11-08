package org.springdoc.core;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

@Component
public class ParameterBuilder extends AbstractParameterBuilder {

    public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
        super(localSpringDocParameterNameDiscoverer);
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
