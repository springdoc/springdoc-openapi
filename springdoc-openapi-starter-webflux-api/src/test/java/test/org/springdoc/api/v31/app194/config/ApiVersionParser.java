package test.org.springdoc.api.v31.app194.config;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

	private static final String DEFAULT_VERSION = "1.0";

	// allows us to use /api/v2/users instead of /api/2.0/users
	@Override
	public Comparable parseVersion(String version) {
		// Remove "v" prefix if it exists (v1 becomes 1, v2 becomes 2)
		if (version.startsWith("v") || version.startsWith("V")) {
			version = version.substring(1);
		}
		// For non-numeric path segments (e.g. "api-docs", "swagger-ui.css"),
		// fall back to the default version to avoid NPE from returning null
		// in the reactive ApiVersionStrategy pipeline
		try {
			Double.parseDouble(version);
		}
		catch (NumberFormatException ex) {
			return DEFAULT_VERSION;
		}
		return version;
	}

}
