package test.org.springdoc.api.v30.app200;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FooBarConverter implements Converter<String, FooBar> {

	@Override
	public FooBar convert(String source) {
		return FooBar.fromValue(source);
	}

}