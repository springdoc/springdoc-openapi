package test.org.springdoc.api.v30.app3354.config;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

	@Override
	public Comparable parseVersion(String version) {
		return version;
	}
}
