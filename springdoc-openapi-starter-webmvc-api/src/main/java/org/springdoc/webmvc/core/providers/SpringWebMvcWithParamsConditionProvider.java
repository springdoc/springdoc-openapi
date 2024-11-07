package org.springdoc.webmvc.core.providers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * The type Spring web mvc provider with params condition.
 *
 * @author Jelicho
 */
public class SpringWebMvcWithParamsConditionProvider extends SpringWebMvcProvider {
	@Override
	public Set<String> getActivePatterns(Object requestMapping) {
		Set<String> activePatterns = super.getActivePatterns(requestMapping);
		RequestMappingInfo requestMappingInfo = (RequestMappingInfo) requestMapping;
		String queryStringByParamsCondition = getQueryStringByParamsCondition(requestMappingInfo.getParamsCondition());

		if (queryStringByParamsCondition.isEmpty()) {
			return activePatterns;
		}

		return activePatterns.stream()
				.map(activePattern -> activePattern + "?" + queryStringByParamsCondition)
				.collect(Collectors.toSet());
	}

	private String getQueryStringByParamsCondition(ParamsRequestCondition paramsRequestCondition) {
		return paramsRequestCondition
				.getExpressions()
				.stream()
				.map(this::mapExpression)
				.collect(Collectors.joining("&"));
	}

	private String mapExpression(NameValueExpression<String> expression) {
		return expression.getName() + "=" + (expression.getValue() != null ? expression.getValue() : "");
	}
}
