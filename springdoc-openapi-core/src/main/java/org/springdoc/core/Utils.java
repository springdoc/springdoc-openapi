package org.springdoc.core;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Utils {

	public static String evaluateResponseStatus(Method method, Class<?> beanType) {
		HttpStatus responseStatus = null;
		ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(method, ResponseStatus.class);
		if (annotation == null && beanType != null) {
			annotation = AnnotatedElementUtils.findMergedAnnotation(beanType, ResponseStatus.class);
		}
		if (annotation != null) {
			responseStatus = annotation.code();
		}
		if (annotation == null) {
				responseStatus = HttpStatus.OK;
		}
		return responseStatus.toString();
	}
}
