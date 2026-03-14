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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.ai.tool.annotation.Tool;

/**
 * AOP aspect that writes a structured JSON audit event (via {@link McpAuditLogger}) each
 * time a Spring AI {@link Tool @Tool}-annotated method is invoked.
 *
 * <p>
 * This aspect is auto-registered by
 * {@link org.springdoc.ai.configuration.SpringDocAiAutoConfiguration} and requires no
 * additional configuration in the application. It produces audit events on the
 * {@code org.springdoc.ai.mcp.audit} logger using the same JSON schema as
 * {@link OpenApiToolCallback}, so both OpenAPI-backed tools and native {@code @Tool}
 * methods feed a single audit trail.
 *
 * @author bnasslahsen
 */
@Aspect
public class McpNativeToolAuditAspect {

	/**
	 * Intercepts any bean method annotated with {@link Tool @Tool} and emits a JSON audit
	 * event recording the outcome, duration, and security context.
	 * @param pjp the proceeding join point
	 * @param tool the annotation present on the intercepted method
	 * @return the method's return value
	 * @throws Throwable if the intercepted method throws
	 */
	@Around("@annotation(tool)")
	public Object audit(ProceedingJoinPoint pjp, Tool tool) throws Throwable {
		String toolName = tool.name().isEmpty() ? pjp.getSignature().getName() : tool.name();
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

}
