package test.org.springdoc.api.app5;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public class CountryPredicate implements QuerydslBinderCustomizer<QCountry> {

	@Override
	public void customize(QuerydslBindings querydslBindings, QCountry qCountry) {
		querydslBindings.bind(qCountry.codeISO3166).as("code").first((path, value) -> path.containsIgnoreCase(value));
		querydslBindings.bind(qCountry.dialingCode).as("postCode").first((path, value) -> path.containsIgnoreCase(value));
	}
}