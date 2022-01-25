package test.org.springdoc.api.app24;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public class UserPredicate implements QuerydslBinderCustomizer<QUser> {


	@Override
	public void customize(QuerydslBindings bindings, QUser user) {
		bindings.excludeUnlistedProperties(true);
		bindings.including(user.email);
		bindings.bind(user.firstName).as("name").withDefaultBinding();
	}
}
