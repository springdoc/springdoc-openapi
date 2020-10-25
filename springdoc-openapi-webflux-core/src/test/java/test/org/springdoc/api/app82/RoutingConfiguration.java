package test.org.springdoc.api.app82;

import org.springdoc.core.fn.RouterOperation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.ROUTER_ATTRIBUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutingConfiguration {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
		return route(GET("/api/user/index").and(accept(APPLICATION_JSON)), userHandler::getAll)
				.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(UserRepository.class).beanMethod("getAllUsers").build())

				.and(route(GET("/api/user/{id}").and(accept(APPLICATION_JSON)), userHandler::getUser)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(UserRepository.class).beanMethod("getUserById").build()))

				.and(route(POST("/api/user/post").and(accept(APPLICATION_JSON)), userHandler::postUser)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(UserRepository.class).beanMethod("saveUser").build()))

				.and(route(PUT("/api/user/put/{id}").and(accept(APPLICATION_JSON)), userHandler::putUser)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(UserRepository.class).beanMethod("putUser").build()))

				.and(route(DELETE("/api/user/delete/{id}").and(accept(APPLICATION_JSON)), userHandler::deleteUser)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(UserRepository.class).beanMethod("deleteUser").build()));
	}

}