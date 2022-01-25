package test.org.springdoc.api.app153;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@Component
public class DefaultWebFluxRegistrations implements WebFluxRegistrations {

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new DefaultRequestMappingHandlerMapping();
	}

}
