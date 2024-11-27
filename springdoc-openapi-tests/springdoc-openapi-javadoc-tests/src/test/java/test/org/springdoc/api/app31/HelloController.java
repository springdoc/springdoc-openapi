/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.api.app31;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Subscribe subscription response.
	 *
	 * @param token the token 
	 * @param url the url 
	 * @return the subscription response
	 */
	@PostMapping("/test")
	@Callback(callbackUrlExpression = "http://$request.query.url", name = "subscription", operation = {
			@Operation(method = "post", description = "payload data will be sent", parameters = {
					@Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true, schema = @Schema(type = "string", format = "uuid", description = "the generated UUID", accessMode = Schema.AccessMode.READ_ONLY)) }, responses = {
					@ApiResponse(responseCode = "200", description = "Return this code if the callback was received and processed successfully"),
					@ApiResponse(responseCode = "205", description = "Return this code to unsubscribe from future data updates"),
					@ApiResponse(responseCode = "default", description = "All other response codes will disable this callback subscription") }) })
	@Operation(description = "subscribes a client to updates relevant to the requestor's account, as "
			+ "identified by the input token.  The supplied url will be used as the delivery address for response payloads")
	public SubscriptionResponse subscribe(@Schema(required = true, description = "the authentication token "
			+ "provided after initially authenticating to the application") @RequestHeader("x-auth-token") String token,
			@Schema(required = true, description = "the URL to call with response "
					+ "data") @RequestParam("url") String url) {
		return null;
	}

	/**
	 * The type Subscription response.
	 */
	static class SubscriptionResponse {
		/**
		 * The Subscription uuid.
		 */
		private String subscriptionUuid;

		/**
		 * Gets subscription uuid.
		 *
		 * @return the subscription uuid
		 */
		public String getSubscriptionUuid() {
			return subscriptionUuid;
		}

		/**
		 * Sets subscription uuid.
		 *
		 * @param subscriptionUuid the subscription uuid
		 */
		public void setSubscriptionUuid(String subscriptionUuid) {
			this.subscriptionUuid = subscriptionUuid;
		}

	}

}
