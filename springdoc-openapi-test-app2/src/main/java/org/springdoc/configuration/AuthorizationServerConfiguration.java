package org.springdoc.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

    public AuthorizationServerConfiguration(BaseClientDetails details,
                                            AuthenticationConfiguration authenticationConfiguration,
                                            ObjectProvider<TokenStore> tokenStore,
                                            ObjectProvider<AccessTokenConverter> tokenConverter,
                                            AuthorizationServerProperties properties) throws Exception {
        super(details, authenticationConfiguration, tokenStore, tokenConverter, properties);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.pathMapping("/oauth/authorize", "/api/oauth/dialog");
    }
}