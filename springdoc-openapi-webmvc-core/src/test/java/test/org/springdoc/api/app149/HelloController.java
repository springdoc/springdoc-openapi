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

package test.org.springdoc.api.app149;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * To test the case a user does not use @RestController but puts @Operation on handler methods
 * and wants these methods to be exposed.
 *
 * @author Azige
 */
@Controller
public class HelloController {

	@GetMapping("/hello")
	@Operation(responses = @ApiResponse(
			responseCode = "200",
			description = "OK",
			content = @Content(schema = @Schema(implementation = HelloMessage.class))
	))
	public String hello() {
		return "forward:/message";
	}

	@GetMapping("/message")
	@Operation
	@ResponseBody
	public HelloMessage message() {
		return new HelloMessage("Lucky numbers!", 777);
	}

	@GetMapping("/helloModelAndView")
	@Operation(responses = @ApiResponse(
			responseCode = "200",
			description = "OK",
			content = @Content(schema = @Schema(implementation = HelloMessage.class))
	))
	public ModelAndView helloModelAndView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("forward:/message");
		return mav;
	}
}
