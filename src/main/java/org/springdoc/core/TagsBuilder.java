package org.springdoc.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.Operation;

@Component
public class TagsBuilder {

	public Operation build(HandlerMethod handlerMethod, Operation operation) {
		// class tags
		List<io.swagger.v3.oas.annotations.tags.Tag> classTags = ReflectionUtils
				.getRepeatableAnnotations(handlerMethod.getBeanType(), io.swagger.v3.oas.annotations.tags.Tag.class);

		// method tags
		List<io.swagger.v3.oas.annotations.tags.Tag> methodTags = ReflectionUtils
				.getRepeatableAnnotations(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.tags.Tag.class);

		Set<String> tagsStr = new HashSet<String>();
		if (!CollectionUtils.isEmpty(classTags))
			tagsStr.addAll(classTags.stream().map(t -> t.name()).collect(Collectors.toSet()));
		if (!CollectionUtils.isEmpty(methodTags))
			tagsStr.addAll(methodTags.stream().map(t -> t.name()).collect(Collectors.toSet()));

		if (!CollectionUtils.isEmpty(tagsStr))
			operation.setTags(new ArrayList<String>(tagsStr));

		return operation;
	}
}
