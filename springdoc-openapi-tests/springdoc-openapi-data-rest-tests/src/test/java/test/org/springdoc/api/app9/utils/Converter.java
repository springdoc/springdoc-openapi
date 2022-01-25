package test.org.springdoc.api.app9.utils;

public interface Converter<S, T> {

	T convert(S source);

}
