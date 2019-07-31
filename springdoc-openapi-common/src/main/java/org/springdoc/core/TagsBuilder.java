package org.springdoc.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

@Component
public class TagsBuilder {

	public Operation build(HandlerMethod handlerMethod, Operation operation, OpenAPI openAPI) {

		// class tags
		List<io.swagger.v3.oas.annotations.tags.Tag> classTags = ReflectionUtils
				.getRepeatableAnnotations(handlerMethod.getBeanType(), io.swagger.v3.oas.annotations.tags.Tag.class);

		// method tags
		List<io.swagger.v3.oas.annotations.tags.Tag> methodTags = ReflectionUtils
				.getRepeatableAnnotations(handlerMethod.getMethod(), io.swagger.v3.oas.annotations.tags.Tag.class);

		List<io.swagger.v3.oas.annotations.tags.Tag> allTags = new ArrayList<>();
		Set<String> tagsStr = new HashSet<>();

		if (!CollectionUtils.isEmpty(methodTags)) {
			tagsStr.addAll(methodTags.stream().map(Tag::name).collect(Collectors.toSet()));
			allTags.addAll(methodTags);
		}

		if (!CollectionUtils.isEmpty(classTags)) {
			tagsStr.addAll(classTags.stream().map(Tag::name).collect(Collectors.toSet()));
			allTags.addAll(classTags);
		}

		Optional<Set<io.swagger.v3.oas.models.tags.Tag>> tags = AnnotationsUtils
				.getTags(allTags.toArray(new Tag[allTags.size()]), true);

		if (tags.isPresent()) {
			Set<io.swagger.v3.oas.models.tags.Tag> tagsSet = tags.get();
			// Existing tags
			List<io.swagger.v3.oas.models.tags.Tag> openApiTags = openAPI.getTags();
			if (!CollectionUtils.isEmpty(openApiTags))
				tagsSet.addAll(openApiTags);
			openAPI.setTags(new ArrayList<>(tagsSet));
		}

		if (!CollectionUtils.isEmpty(tagsStr))
			operation.setTags(new ArrayList<String>(tagsStr));

		return operation;
	}
}
