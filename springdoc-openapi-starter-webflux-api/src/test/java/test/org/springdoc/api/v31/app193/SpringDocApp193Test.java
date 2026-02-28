/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app193;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springdoc.core.annotations.RouterOperation;
import reactor.core.publisher.Mono;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpringDocApp193Test extends AbstractSpringDocTest {

    @Test
    void getIconResource() {
        webTestClient.get().uri("/icons/icon.svg").exchange()
                .expectStatus().isOk()
                .expectHeader().cacheControl(CacheControl.maxAge(Duration.ofHours(24)))
                .expectBody().consumeWith(response -> {
                    byte[] body = response.getResponseBody();
                    assertNotNull(body);
                    String bodyValue = new String(body, StandardCharsets.UTF_8);
                    assertThat(bodyValue).contains("<rect width=\"1\" height=\"1\" fill=\"black\"/>");
                });
    }

    @Test
    void getUnknownResource() {
        webTestClient.get().uri("/icons/unknown.svg").exchange()
                .expectStatus().isNotFound();
    }

    @SpringBootApplication
    @ComponentScan(basePackages = { "org.springdoc" })
    static class SpringDocTestApp {

        @Bean
        @RouterOperation(
                method = RequestMethod.GET,
                operation = @Operation(
                        operationId = "getIcon",
                        summary = "Get icons resource.",
                        responses = @ApiResponse(
                                responseCode = "200", content = @Content(mediaType = "image/svg+xml"), description = "icon resource",
                                headers = @Header(name = HttpHeaders.CACHE_CONTROL, required = true, description = "Cache-Control for icons", schema = @Schema(type = "string"))
                        )
                )
        )
        public RouterFunction<ServerResponse> getIconsResourceWithCacheControl() {
            return RouterFunctions
                    .resources("/icons/**", new ClassPathResource("icons/"))
                    .filter(HandlerFilterFunction.ofResponseProcessor(this::injectCacheControlHeader));
        }

        private Mono<ServerResponse> injectCacheControlHeader(ServerResponse serverResponse) {
            if (serverResponse instanceof EntityResponse<?> entityResponse) {
                return EntityResponse.fromObject(entityResponse.entity())
                        .header(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(24, TimeUnit.HOURS).getHeaderValue())
                        .build().cast(ServerResponse.class);
            }
            return Mono.just(serverResponse);
        }

    }

}