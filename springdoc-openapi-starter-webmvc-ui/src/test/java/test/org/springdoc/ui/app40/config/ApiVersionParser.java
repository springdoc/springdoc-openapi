package test.org.springdoc.ui.app40.config;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

    // allows us to use /api/v2/users instead of /api/2.0/users
    @Override
    public Comparable parseVersion(String version) {
        // Remove "v" prefix if it exists (v1 becomes 1, v2 becomes 2)
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }
		
        return version;
    }
}
