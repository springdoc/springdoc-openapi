package org.springdoc.core;

import java.lang.reflect.ParameterizedType;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;

@Component
public class ParameterBuilder extends AbstractParameterBuilder {

	public boolean isFile(ParameterizedType parameterizedType) {
		return MultipartFile.class.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName())
				|| FilePart.class.getName().equals(parameterizedType.getActualTypeArguments()[0].getTypeName());
	}

	public boolean isFile(JavaType ct) {
		return MultipartFile.class.isAssignableFrom(ct.getRawClass())
				|| FilePart.class.isAssignableFrom(ct.getRawClass());
	}

}
