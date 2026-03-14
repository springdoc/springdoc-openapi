/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.ai.customizers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.springdoc.ai.annotations.McpIgnore;
import org.springdoc.ai.annotations.McpToolDescription;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * A built-in {@link McpToolCustomizer} that reads {@link McpToolDescription} annotations
 * from controller handler methods and applies the annotation values to the tool
 * definition. This customizer runs at {@link Ordered#HIGHEST_PRECEDENCE} so that
 * user-registered customizers can further modify the annotation-provided values.
 *
 * @author bnasslahsen
 */
public class McpToolDescriptionCustomizer implements McpToolCustomizer, Ordered {

	/**
	 * Map of operationId to {@link McpToolDescription} annotation.
	 */
	private final Map<String, McpToolDescription> annotationsByOperationId;

	/**
	 * Set of operationIds to exclude from MCP tool exposure.
	 */
	private final Set<String> excludedOperationIds;

	/**
	 * Constructs a new McpToolDescriptionCustomizer from pre-built annotation data.
	 * @param annotationsByOperationId map of operationId to annotation
	 * @param excludedOperationIds set of operationIds to exclude
	 */
	public McpToolDescriptionCustomizer(Map<String, McpToolDescription> annotationsByOperationId,
			Set<String> excludedOperationIds) {
		this.annotationsByOperationId = annotationsByOperationId;
		this.excludedOperationIds = excludedOperationIds;
	}

	/**
	 * Builds a map of operationId to {@link McpToolDescription} from a collection of
	 * handler methods. The operationId is resolved from the
	 * {@link io.swagger.v3.oas.annotations.Operation} annotation if present, otherwise
	 * from the method name.
	 * @param handlerMethods the handler methods to scan
	 * @return the annotation map keyed by operationId
	 */
	public static Map<String, McpToolDescription> buildAnnotationMap(Collection<HandlerMethod> handlerMethods) {
		Map<String, McpToolDescription> map = new HashMap<>();
		for (HandlerMethod hm : handlerMethods) {
			McpToolDescription desc = hm.getMethodAnnotation(McpToolDescription.class);
			if (desc != null) {
				map.put(resolveOperationId(hm), desc);
			}
		}
		return map;
	}

	/**
	 * Builds a set of operationIds that should be excluded from MCP tool exposure based
	 * on the presence of {@link McpIgnore} on the handler method or its declaring class.
	 * @param handlerMethods the handler methods to scan
	 * @return the set of excluded operationIds
	 */
	public static Set<String> buildExcludedOperationIds(Collection<HandlerMethod> handlerMethods) {
		Set<String> excluded = new HashSet<>();
		for (HandlerMethod hm : handlerMethods) {
			boolean ignored = hm.getMethodAnnotation(McpIgnore.class) != null
					|| AnnotatedElementUtils.hasAnnotation(hm.getBeanType(), McpIgnore.class);
			if (ignored) {
				excluded.add(resolveOperationId(hm));
			}
		}
		return excluded;
	}

	private static String resolveOperationId(HandlerMethod hm) {
		io.swagger.v3.oas.annotations.Operation op = hm
			.getMethodAnnotation(io.swagger.v3.oas.annotations.Operation.class);
		if (op != null && !op.operationId().isEmpty()) {
			return op.operationId();
		}
		return hm.getMethod().getName();
	}

	@Override
	public McpToolDefinitionContext customize(McpToolDefinitionContext context, String path,
			PathItem.HttpMethod method, Operation operation) {
		if (excludedOperationIds.contains(operation.getOperationId())) {
			context.setExclude(true);
			return context;
		}
		McpToolDescription ann = annotationsByOperationId.get(operation.getOperationId());
		if (ann != null) {
			context.setDescription(ann.value());
			if (!ann.name().isEmpty()) {
				context.setName(ann.name());
			}
		}
		return context;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
