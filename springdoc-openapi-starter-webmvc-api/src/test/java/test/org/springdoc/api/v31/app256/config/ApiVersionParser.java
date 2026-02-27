package test.org.springdoc.api.v31.app256.config;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

	@Override
	public Comparable parseVersion(String version) {
		if (version.startsWith("v") || version.startsWith("V")) {
			version = version.substring(1);
		}

		return version;
	}

}
