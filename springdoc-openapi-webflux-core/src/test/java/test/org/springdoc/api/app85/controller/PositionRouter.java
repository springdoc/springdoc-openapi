package test.org.springdoc.api.app85.controller;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import test.org.springdoc.api.app85.entity.Position;
import test.org.springdoc.api.app85.handler.PositionHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.Constants.OPERATION_ATTRIBUTE;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PositionRouter {

	@Bean
	public RouterFunction<ServerResponse> positionRoute(PositionHandler handler) {
		return route(GET("/getAllPositions").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().operationId("findAll").description("Get all positions").tags(new String[] { "positions" })
						.response(responseBuilder().responseCode("200").implementationArray(Position.class)))

				.and(route(GET("/getPosition/{id}").and(accept(MediaType.APPLICATION_STREAM_JSON)), handler::findById)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("findById").description("Find all").tags(new String[] { "positions" })
										.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
										.response(responseBuilder().responseCode("200").implementation(Position.class))))

				.and(route(POST("/createPosition").and(accept(MediaType.APPLICATION_JSON)), handler::save)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("save").description("Save position").tags(new String[] { "positions" })
										.requestBody(requestBodyBuilder().implementation(Position.class))
										.response(responseBuilder().responseCode("200").implementation(Position.class))))

				.and(route(DELETE("/deletePosition/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::delete)
						.withAttribute(OPERATION_ATTRIBUTE,
								operationBuilder().operationId("deleteBy").description("Delete By Id").tags(new String[] { "positions" })
										.parameter(parameterBuilder().in(ParameterIn.PATH).name("id"))
										.response(responseBuilder().responseCode("200").content(contentBuilder()))));
	}

}
