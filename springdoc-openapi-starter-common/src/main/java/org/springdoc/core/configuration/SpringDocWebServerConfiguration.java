package org.springdoc.core.configuration;

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

            private volatile Supplier<Integer> applicationPortSupplier;
            private volatile Supplier<Integer> actuatorPortSupplier;
            private volatile Supplier<ApplicationContext> managementContextSupplier;

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
                    this.actuatorPortSupplier = webServer::getPort;
                    this.managementContextSupplier = event::getApplicationContext;
                }
                else {
                    this.applicationPortSupplier = webServer::getPort;
                }
            }

            @Override
            public Supplier<Integer> getApplicationPort() {
                return applicationPortSupplier;
            }

            @Override
            public Supplier<Integer> getActuatorPort() {
                return actuatorPortSupplier;
            }

            @Override
            public Supplier<ApplicationContext> getManagementApplicationContext() {
                return this.managementContextSupplier;
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
