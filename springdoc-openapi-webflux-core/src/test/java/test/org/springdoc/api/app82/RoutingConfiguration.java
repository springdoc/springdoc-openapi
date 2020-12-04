package test.org.springdoc.api.app82;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
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
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("getAllUsers"))

				.and(route(GET("/api/user/{id}").and(accept(APPLICATION_JSON)), userHandler::getUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("getUserById")))

				.and(route(POST("/api/user/post").and(accept(APPLICATION_JSON)), userHandler::postUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("saveUser")))

				.and(route(PUT("/api/user/put").and(accept(APPLICATION_JSON)), userHandler::putUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("putUser")))

				.and(route(DELETE("/api/user/delete/{id}").and(accept(APPLICATION_JSON)), userHandler::deleteUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("deleteUser")));
	}

}