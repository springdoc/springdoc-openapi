package test.org.springdoc.api.app19;

import com.querydsl.core.types.dsl.StringExpression;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public class ApplicationPredicate implements QuerydslBinderCustomizer<QApplication> {

	@Override
	public void customize(QuerydslBindings bindings, QApplication root) {
		bindings.excludeUnlistedProperties(true);
		bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
		bindings.including(root.icon);
		bindings.including(root.name);
	}

}
