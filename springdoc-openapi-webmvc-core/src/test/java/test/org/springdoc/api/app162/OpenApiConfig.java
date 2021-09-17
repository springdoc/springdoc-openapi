package test.org.springdoc.api.app162;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getInfo().version("v1");
            Server server = new Server().url("");
            List<Server> servers=new ArrayList<>();
            servers.add(server);
            openApi.servers(servers);
        };
    }
}
