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

package test.org.springdoc.api.v30.app153;

import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringValueResolver;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

public class DefaultRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private static final SpelExpressionParser PARSER = new SpelExpressionParser();

	private static ThreadLocal<Object> handlerHolder = new ThreadLocal<>();

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		super.setEmbeddedValueResolver(new StringValueResolver() {
			@Override
			public String resolveStringValue(String strVal) {
				Object handler = handlerHolder.get();
				if (handler != null) {
					strVal = String.valueOf(PARSER.parseExpression(strVal, ParserContext.TEMPLATE_EXPRESSION)
							.getValue(new StandardEvaluationContext(handler)));
				}
				if (resolver != null) {
					strVal = resolver.resolveStringValue(strVal);
				}
				return strVal;
			}
		});
	}

	@Override
	protected void detectHandlerMethods(Object handler) {
		Object handlerObject = (handler instanceof String ? obtainApplicationContext().getBean((String) handler)
				: handler);
		handlerHolder.set(handlerObject);
		super.detectHandlerMethods(handler);
		handlerHolder.remove();
	}

}
