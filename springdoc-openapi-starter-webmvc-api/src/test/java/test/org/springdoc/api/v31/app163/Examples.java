/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app163;

import java.util.AbstractMap;
import java.util.Map;

import io.swagger.v3.oas.models.examples.Example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class contributes OpenAPI <a href=
 * "https://swagger.io/docs/specification/adding-examples/">examples</a>.
 * Each example is contributed through a bean typed as a map entry
 * (Map.Entry<String, Example>) and referenced through a constant by a resource
 * operation's @ApiResponse.
 */
@Configuration
public class Examples {

	public static final String PREFIX = "#/components/examples/";

	public static final String PUT_COMMISSION_REQUEST_BODY_EXAMPLE_KEY = "httpPutCommissionRequestBodyExample";

	public static final String PUT_COMMISSION_REQUEST_BODY_EXAMPLE = PREFIX + PUT_COMMISSION_REQUEST_BODY_EXAMPLE_KEY;

	public static final String PUT_COMMISSION_RESPONSE_BODY_EXAMPLE_KEY = "httpPutCommissionResponseBodyExample";

	public static final String PUT_COMMISSION_RESPONSE_BODY_EXAMPLE = PREFIX + PUT_COMMISSION_RESPONSE_BODY_EXAMPLE_KEY;

	@Bean
	public Map.Entry<String, Example> httpPutCommissionRequestBodyExample() {
		Example httpPutCommissionRequestBodyExample = new Example();
		Map.Entry<String, Example> entry = new AbstractMap.SimpleEntry<>(PUT_COMMISSION_REQUEST_BODY_EXAMPLE_KEY,
				httpPutCommissionRequestBodyExample);
		httpPutCommissionRequestBodyExample
				.setSummary("HTTP 202 JSON Body request example for updateCommission operation");

		CommissionDto commission = new CommissionDto("esteban@dugueperoux.com", "Esteban",
				"DUGUEPEROUX");

		httpPutCommissionRequestBodyExample.setValue(commission);

		return entry;
	}

	@Bean
	public Map.Entry<String, Example> httpPutCommissionResponseBodyExample() {
		Example httpPutCommissionResponseBodyExample = new Example();
		Map.Entry<String, Example> entry = new AbstractMap.SimpleEntry<>(PUT_COMMISSION_RESPONSE_BODY_EXAMPLE_KEY,
				httpPutCommissionResponseBodyExample);
		httpPutCommissionResponseBodyExample
				.setSummary("HTTP 202 JSON Body response example for updateCommission operation");

		CommissionDto commission = new CommissionDto("esteban@dugueperoux.com", "Esteban",
				"DUGUEPEROUX");

		httpPutCommissionResponseBodyExample.setValue(commission);

		return entry;
	}

}
