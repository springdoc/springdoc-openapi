package org.springdoc.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocConfigProperties {

    private Boolean showActuator = false;
    private Webjars webjars = new Webjars();
    private ApiDocs apiDocs = new ApiDocs();
    private List<String> packagesToScan;
    private List<String> pathsToMatch;

    public List<String> getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public Boolean getShowActuator() {
        return showActuator;
    }

    public void setShowActuator(Boolean showActuator) {
        this.showActuator = showActuator;
    }

    public Webjars getWebjars() {
        return webjars;
    }

    public void setWebjars(Webjars webjars) {
        this.webjars = webjars;
    }

    public ApiDocs getApiDocs() {
        return apiDocs;
    }

    public void setApiDocs(ApiDocs apiDocs) {
        this.apiDocs = apiDocs;
    }

    public List<String> getPathsToMatch() {
        return pathsToMatch;
    }

    public void setPathsToMatch(List<String> pathsToMatch) {
        this.pathsToMatch = pathsToMatch;
    }

    public static class Webjars {
        private String prefix = "/webjars";

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    public static class ApiDocs {
        /**
         * Path to the generated OpenAPI documentation. For a yaml file, append ".yaml" to the path.
         */
        private String path = Constants.DEFAULT_API_DOCS_URL;
        /**
         * Weather to generate and serve a OpenAPI document.
         */
        private Boolean enabled = true;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
