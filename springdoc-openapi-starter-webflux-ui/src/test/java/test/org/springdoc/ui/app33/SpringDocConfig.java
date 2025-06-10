package test.org.springdoc.ui.app33;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class SpringDocConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDocConfig.class);


	@Bean
	public WebFilter rewritePathWebFilter(WebFluxProperties webFluxProperties) {
		return (ServerWebExchange exchange, WebFilterChain chain) -> {
			try {
				ServerHttpRequest originalRequest = (ServerHttpRequest) FieldUtils.readDeclaredField(exchange.getRequest(), "originalRequest", true);
				List<String> forwardedPrefixHeaders = originalRequest.getHeaders().get("X-Forwarded-Prefix");
				if (!CollectionUtils.isEmpty(forwardedPrefixHeaders)) {
					String forwardedPrefix = forwardedPrefixHeaders.get(0);
					ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
							.contextPath(forwardedPrefix + webFluxProperties.getBasePath())
							.build();
					ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
					return chain.filter(mutatedExchange);
				}

			}
			catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			return chain.filter(exchange);
		};
	}
}
