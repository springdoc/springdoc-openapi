package test.org.springdoc.api.app83;

import org.springdoc.core.fn.RouterOperation;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.ROUTER_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfig {
	private final CoffeeService service;

	public RouteConfig(CoffeeService service) {
		this.service = service;
	}

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return route(GET("/coffees"), this::all)
				.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(CoffeeService.class).beanMethod("getAllCoffees"))

				.and(route(GET("/coffees/{id}"), this::byId)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(CoffeeService.class).beanMethod("getCoffeeById")))

				.and(route(GET("/coffees/{id}/orders"), this::orders)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(CoffeeService.class).beanMethod("getOrdersForCoffeeById")));
	}

	private Mono<ServerResponse> all(ServerRequest req) {
		return ServerResponse.ok()
				.body(service.getAllCoffees(), Coffee.class);
	}

	private Mono<ServerResponse> byId(ServerRequest req) {
		return ServerResponse.ok()
				.body(service.getCoffeeById(req.pathVariable("id")), Coffee.class);
	}

	private Mono<ServerResponse> orders(ServerRequest req) {
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(service.getOrdersForCoffeeById(req.pathVariable("id")), CoffeeOrder.class);
	}
}
