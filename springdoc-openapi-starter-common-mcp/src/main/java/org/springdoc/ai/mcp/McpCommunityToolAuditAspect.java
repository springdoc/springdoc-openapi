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

package org.springdoc.ai.mcp;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * AOP aspect that writes a structured JSON audit event (via {@link McpAuditLogger}) each
 * time an {@code @McpTool}-annotated method from the Spring AI Community library
 * ({@code org.springaicommunity.mcp.annotation.McpTool}) is invoked.
 *
 * <p>
 * This aspect is only registered when {@code org.springaicommunity.mcp.annotation.McpTool}
 * is on the classpath (see
 * {@link org.springdoc.ai.configuration.SpringDocAiAutoConfiguration}). The annotation
 * class is referenced by fully-qualified name in the pointcut expression to avoid a
 * compile-time dependency on the springaicommunity library.
 *
 * @author bnasslahsen
 */
@Aspect
public class McpCommunityToolAuditAspect {

	/**
	 * Fully-qualified name of the {@code @McpTool} annotation from the Spring AI
	 * Community library.
	 */
	private static final String MCP_TOOL_ANNOTATION = "org.springaicommunity.mcp.annotation.McpTool";

	/**
	 * Intercepts any bean method annotated with {@code @McpTool} and emits a JSON audit
	 * event recording the outcome, duration, and security context.
	 * @param pjp the proceeding join point
	 * @return the method's return value
	 * @throws Throwable if the intercepted method throws
	 */
	@Around("@annotation(org.springaicommunity.mcp.annotation.McpTool)")
	public Object audit(ProceedingJoinPoint pjp) throws Throwable {
		String toolName = extractToolName(pjp);
		long startNanos = System.nanoTime();
		try {
			Object result = pjp.proceed();
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(toolName)
				.durationMs(nanosToMillis(System.nanoTime() - startNanos))
				.outcomeStatus("SUCCESS")
				.build());
			return result;
		}
		catch (Throwable ex) {
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(toolName)
				.durationMs(nanosToMillis(System.nanoTime() - startNanos))
				.outcomeStatus("ERROR")
				.errorReason(ex.getClass().getSimpleName() + ": " + ex.getMessage())
				.build());
			throw ex;
		}
	}

	/**
	 * Converts nanoseconds to milliseconds, rounding up so that sub-millisecond
	 * executions report at least {@code 1} instead of {@code 0}.
	 * @param nanos elapsed nanoseconds
	 * @return elapsed milliseconds (minimum 1 when nanos &gt; 0)
	 */
	private static long nanosToMillis(long nanos) {
		return Math.max(1, (nanos + 999_999) / 1_000_000);
	}

	/**
	 * Extracts the tool name from the {@code @McpTool} annotation via reflection,
	 * falling back to the method name if the annotation's {@code name()} is blank.
	 * @param pjp the join point
	 * @return the tool name
	 */
	private String extractToolName(ProceedingJoinPoint pjp) {
		MethodSignature sig = (MethodSignature) pjp.getSignature();
		for (Annotation ann : sig.getMethod().getAnnotations()) {
			if (MCP_TOOL_ANNOTATION.equals(ann.annotationType().getName())) {
				try {
					String name = (String) ann.annotationType().getMethod("name").invoke(ann);
					return (name != null && !name.isBlank()) ? name : sig.getMethod().getName();
				}
				catch (Exception ignored) {
				}
			}
		}
		return sig.getMethod().getName();
	}

}
