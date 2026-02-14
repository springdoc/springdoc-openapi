/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v30.app245;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for validation-to-schema mapping.
 *
 * @author springdoc
 */
@RestController
public class HelloController {

	public record UnloadAmountRequest(
			@NotNull @Positive BigDecimal amount,
			@Nullable String currency,
			@Nullable String description,
			@Nullable @Size(max = 40)
			@Pattern(regexp = "^[a-zA-Z0-9-]+$") String transactionReference,
			@PositiveOrZero BigDecimal fee,
			@NegativeOrZero BigDecimal discount,
			@Negative BigDecimal adjustment
	) {
	}

	@PostMapping(value = "/unload")
	public String unload(@Valid @RequestBody UnloadAmountRequest request) {
		return "OK";
	}

}
