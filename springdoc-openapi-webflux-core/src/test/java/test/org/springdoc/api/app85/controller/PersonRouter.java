package test.org.springdoc.api.app85.controller;

import org.springdoc.core.fn.RouterOperation;
import test.org.springdoc.api.app85.handler.PersonHandler;
import test.org.springdoc.api.app85.service.PersonService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.ROUTER_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class PersonRouter {

	@Bean
	public RouterFunction<ServerResponse> personRoute(PersonHandler handler) {
		return route(GET("/getAllPersons").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
				.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(PersonService.class).beanMethod("getAll").build())

				.and(route(GET("/getPerson/{id}").and(accept(MediaType.APPLICATION_STREAM_JSON)), handler::findById)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(PersonService.class).beanMethod("getById").build()))

				.and(route(POST("/createPerson").and(accept(MediaType.APPLICATION_JSON)), handler::save)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(PersonService.class).beanMethod("save").build()))

				.and(route(DELETE("/deletePerson/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete)
						.withAttribute(ROUTER_ATTRIBUTE, RouterOperation.builder().beanClass(PersonService.class).beanMethod("delete").build()));
	}

}
