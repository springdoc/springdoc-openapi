package org.springdoc.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConfigurationProperties(prefix = "springdoc")
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
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

        private Groups groups = new Groups();

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

        public Groups getGroups() {
            return groups;
        }

        public void setGroups(Groups groups) {
            this.groups = groups;
        }
    }

    public static class Groups {
        private Boolean enabled = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
