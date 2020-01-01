package org.springdoc.core;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GroupedOpenApi {

    private final String group;
    private final List<OpenApiCustomiser> openApiCustomisers;
    private final List<String> pathsToMatch;
    private final List<String> packagesToScan;

    private GroupedOpenApi(Builder builder) {
        this.group = Objects.requireNonNull(builder.group, "group");
        this.pathsToMatch = builder.pathsToMatch;
        this.packagesToScan = builder.packagesToScan;
        this.openApiCustomisers = Objects.requireNonNull(builder.openApiCustomisers);
        SwaggerUiConfigProperties.addGroup(this.group);
        if (CollectionUtils.isEmpty(this.pathsToMatch) && CollectionUtils.isEmpty(this.packagesToScan))
            throw new IllegalStateException("Packages to scan or paths to filter can not be both null for the group:" + this.group);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGroup() {
        return group;
    }

    public List<String> getPathsToMatch() {
        return pathsToMatch;
    }

    public List<String> getPackagesToScan() {
        return packagesToScan;
    }

    public List<OpenApiCustomiser> getOpenApiCustomisers() {
        return openApiCustomisers;
    }

    public static class Builder {
        private final List<OpenApiCustomiser> openApiCustomisers = new ArrayList<>();
        private String group;
        private List<String> pathsToMatch;
        private List<String> packagesToScan;

        private Builder() {
            // use static factory method in parent class
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder pathsToMatch(String... pathsToMatch) {
            this.pathsToMatch = Arrays.asList(pathsToMatch);
            return this;
        }

        public Builder packagesToScan(String... packagesToScan) {
            this.packagesToScan = Arrays.asList(packagesToScan);
            return this;
        }

        public Builder addOpenApiCustomiser(OpenApiCustomiser openApiCustomiser) {
            this.openApiCustomisers.add(openApiCustomiser);
            return this;
        }

        public GroupedOpenApi build() {
            return new GroupedOpenApi(this);
        }
    }
}
