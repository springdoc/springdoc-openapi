package org.springdoc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.models.Components;

public interface OpenAPIExtension {

	ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
			Components components, String[] classConsumes, String[] methodConsumes,
			boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain);
}
