package test.org.springdoc.api.app153;

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
