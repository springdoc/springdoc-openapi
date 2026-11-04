/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springdoc.core.configuration;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SpringDocWebServerConfiguration {

    /**
     * WebServer context
     */
    public interface SpringDocWebServerContext {
        Supplier<Integer> getApplicationPort();

        Supplier<Integer> getActuatorPort();

        Supplier<ApplicationContext> getManagementApplicationContext();

        String getContextPath();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({WebServerInitializedEvent.class, ServerProperties.class})
    static class EmbeddedWebServerConfiguration {

        @Bean
        SpringDocWebServerContext webServerPortProvider(ObjectProvider<ServerProperties> serverPropertiesProvider) {
            return new SpringDocWebServerPortListener(serverPropertiesProvider);
        }

        static final class SpringDocWebServerPortListener
                implements ApplicationListener<WebServerInitializedEvent>,
                SpringDocWebServerContext {

            private final String contextPath;

            private final AtomicReference<Supplier<Integer>> applicationPortSupplier = new AtomicReference<>();
            private final AtomicReference<Supplier<Integer>> actuatorPortSupplier = new AtomicReference<>();
            private final AtomicReference<Supplier<ApplicationContext>> managementContextSupplier = new AtomicReference<>();

            public SpringDocWebServerPortListener(ObjectProvider<ServerProperties> serverPropertiesProvider) {
                ServerProperties serverProperties = serverPropertiesProvider.getIfAvailable();
                contextPath = serverProperties != null
                        ? StringUtils.defaultIfEmpty(serverProperties.getServlet().getContextPath(), EMPTY)
                        : EMPTY;
            }

            @Override
            public void onApplicationEvent(WebServerInitializedEvent event) {
                final WebServer webServer = event.getWebServer();
                if (WebServerApplicationContext.hasServerNamespace(event.getApplicationContext(), "management")) {
                    this.actuatorPortSupplier.set(webServer::getPort);
                    this.managementContextSupplier.set(event::getApplicationContext);
                }
                else {
                    this.applicationPortSupplier.set(webServer::getPort);
                }
            }

            @Override
            public Supplier<Integer> getApplicationPort() {
                return applicationPortSupplier.get();
            }

            @Override
            public Supplier<Integer> getActuatorPort() {
                return actuatorPortSupplier.get();
            }

            @Override
            public Supplier<ApplicationContext> getManagementApplicationContext() {
                return this.managementContextSupplier.get();
            }

            @Override
            public String getContextPath() {
                return contextPath;
            }

        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingClass({"org.springframework.boot.web.server.context.WebServerInitializedEvent", "org.springframework.boot.web.server.autoconfigure.ServerProperties"})
    static class DeployedWebServerConfiguration  {

        @Bean
        @ConditionalOnMissingBean(SpringDocWebServerContext.class)
        SpringDocWebServerContext webServerPortProvider() {
            return new SpringDocWebServerContext() {
                @Override
                public Supplier<Integer> getApplicationPort() {
                    return () -> -1;
                }

                @Override
                public Supplier<Integer> getActuatorPort() {
                    return () -> -1;
                }

                @Override
                public Supplier<ApplicationContext> getManagementApplicationContext() {
                    return () -> null;
                }

                @Override
                public String getContextPath() {
                    return EMPTY;
                }
            };
        }
    }
}
