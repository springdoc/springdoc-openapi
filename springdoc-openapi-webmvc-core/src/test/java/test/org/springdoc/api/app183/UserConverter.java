package test.org.springdoc.api.app183;

import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<String, User> {

	@Override
	public User convert(String userId) {
		// Fetch from repository
		User user = new User();
		user.setId(userId);
		return user;
	}
}