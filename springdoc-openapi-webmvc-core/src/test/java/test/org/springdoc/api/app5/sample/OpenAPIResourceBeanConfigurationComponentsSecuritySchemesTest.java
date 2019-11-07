package test.org.springdoc.api.app5.sample;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "springdoc.api-docs.path=/api-docs")
@AutoConfigureMockMvc
public class OpenAPIResourceBeanConfigurationComponentsSecuritySchemesTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Given: Bean configuration with security scheme http basic
     * When: Get api-docs
     * Then: Return security definitions http basic
     */
    @Test
    public void shouldDefineComponentsSecuritySchemesForHttpBasic() throws Exception {
        mockMvc
                .perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.security[0].basicScheme", is(new ArrayList<String>())))
                .andExpect(jsonPath("$.components.securitySchemes.basicScheme.type", is("http")))
                .andExpect(jsonPath("$.components.securitySchemes.basicScheme.scheme", is("basic")))
        ;
    }

    /**
     * Given: Bean configuration with security scheme API key
     * When: Get api-docs
     * Then: Return security definitions with API key
     */
    @Test
    public void shouldDefineComponentsSecuritySchemesForApiKey() throws Exception {
        mockMvc
                .perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.security[1].apiKeyScheme", is(new ArrayList<String>())))
                .andExpect(jsonPath("$.components.securitySchemes.apiKeyScheme.type", is("apiKey")))
                .andExpect(jsonPath("$.components.securitySchemes.apiKeyScheme.in", is("header")))
        ;
    }

    /**
     * Given: Bean configuration with security scheme OAuth2
     * When: Get api-docs
     * Then: Return security definitions with OAuth
     */
    @Test
    public void shouldDefineComponentsSecuritySchemesForOAuth2() throws Exception {
        mockMvc
                .perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.security[2].oAuthScheme", is(new ArrayList<String>())))
                .andExpect(jsonPath("$.components.securitySchemes.oAuthScheme.type", is("oauth2")))
                .andExpect(jsonPath("$.components.securitySchemes.oAuthScheme.description", is("This API uses OAuth 2 with the implicit grant flow. [More info](https://api.example.com/docs/auth)")))
                .andExpect(jsonPath("$.components.securitySchemes.oAuthScheme.flows.implicit.authorizationUrl", is("https://api.example.com/oauth2/authorize")))
                .andExpect(jsonPath("$.components.securitySchemes.oAuthScheme.flows.implicit.scopes.read_pets", is("read your pets")))
                .andExpect(jsonPath("$.components.securitySchemes.oAuthScheme.flows.implicit.scopes.write_pets", is("modify pets in your account")))
        ;
    }

    @TestConfiguration
    static class Config {

        @Bean
        public OpenAPI openApi() {
            return new OpenAPI()
                    .components(new Components()

                            //HTTP Basic, see: https://swagger.io/docs/specification/authentication/basic-authentication/
                            .addSecuritySchemes("basicScheme", new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("basic")
                            )

                            //API Key, see: https://swagger.io/docs/specification/authentication/api-keys/
                            .addSecuritySchemes("apiKeyScheme", new SecurityScheme()
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name("X-API-KEY")
                            )

                            //OAuth 2.0, see: https://swagger.io/docs/specification/authentication/oauth2/
                            .addSecuritySchemes("oAuthScheme", new SecurityScheme()
                                    .type(SecurityScheme.Type.OAUTH2)
                                    .description("This API uses OAuth 2 with the implicit grant flow. [More info](https://api.example.com/docs/auth)")
                                    .flows(new OAuthFlows()
                                            .implicit(new OAuthFlow()
                                                    .authorizationUrl("https://api.example.com/oauth2/authorize")
                                                    .scopes(new Scopes()
                                                            .addString("read_pets", "read your pets")
                                                            .addString("write_pets", "modify pets in your account")
                                                    )
                                            )
                                    )
                            )
                    )
                    .addSecurityItem(new SecurityRequirement()
                            .addList("basicScheme")
                    )
                    .addSecurityItem(new SecurityRequirement()
                            .addList("apiKeyScheme")
                    )
                    .addSecurityItem(new SecurityRequirement()
                            .addList("oAuthScheme")
                    )
                    ;
        }
    }

}
