package org.springdoc.api;

import static org.springdoc.core.Constants.DEFAULT_SERVER_DESCRIPTION;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springdoc.core.Constants;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Component
public class OpenAPICloner {

	public OpenAPI cloneOpenApi(OpenAPI from, String serverBaseUrl) {
		OpenAPI to = new OpenAPI();
		to.components(from.getComponents()).extensions(from.getExtensions()).externalDocs(from.getExternalDocs())
				.info(from.getInfo()).openapi(from.getOpenapi()).paths(from.getPaths()).security(from.getSecurity())
				.tags(from.getTags());
		List<Server> fromServers = from.getServers();
		List<Server> toServers = fromServers == null ? new ArrayList<>() : new ArrayList<>(from.getServers());
		to.servers(toServers);
		Iterator<Server> it = toServers.iterator();
		while (it.hasNext()) {
			Server server = it.next();
			if (Constants.DEFAULT_SERVER_DESCRIPTION.equals(server.getDescription())) {
				it.remove();
			}
		}
		Server server = new Server().url(serverBaseUrl).description(DEFAULT_SERVER_DESCRIPTION);
		to.addServersItem(server);
		return to;
	}
}