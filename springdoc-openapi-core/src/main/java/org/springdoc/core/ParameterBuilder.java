package org.springdoc.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;

@Component
public class ParameterBuilder extends AbstractParameterBuilder {

	public boolean isFile(ParameterizedType parameterizedType) {
		Type type = parameterizedType.getActualTypeArguments()[0];
		if (MultipartFile.class.getName().equals(type.getTypeName())) {
			return true;
		} else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Type[] upperBounds = wildcardType.getUpperBounds();
			if (MultipartFile.class.getName().equals(upperBounds[0].getTypeName()))
				return true;
		}
		return false;
	}

	public boolean isFile(JavaType ct) {
		return MultipartFile.class.isAssignableFrom(ct.getRawClass());
	}
}
