package test.org.springdoc.api.v31.app196.config;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

    // allows us to use /api/v2/users instead of /api/2.0/users
    @Override
    public Comparable parseVersion(String version) {
        // Remove "v" prefix if it exists (v1 becomes 1, v2 becomes 2)
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }
		
		if("api-docs".equals(version) || "index.html".equals(version) 
				|| "swagger-ui-bundle.js".equals(version)
				|| "swagger-ui.css".equals(version)
				|| "index.css".equals(version)
				|| "swagger-ui-standalone-preset.js".equals(version)
				|| "favicon-32x32.png".equals(version)
				|| "favicon-16x16.png".equals(version)
				|| "swagger-initializer.js".equals(version)) 
			return null;
        return version;
    }
}
