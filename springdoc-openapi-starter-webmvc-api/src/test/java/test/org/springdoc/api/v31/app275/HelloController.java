/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app275;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller exchanging the java.time types without annotations.
 *
 * @author Mattias Sehlstedt
 */
@RestController
public class HelloController {

	@PostMapping("/time1")
	public TimeDtoNoAnnotations postTime1(
			@RequestBody TimeDtoNoAnnotations timeDto) {
		return timeDto;
	}

	public record TimeDtoNoAnnotations(LocalDateTime localDateTime,
									   LocalDate localDate, LocalTime localTime,
									   YearMonth yearMonth, MonthDay monthDay,
									   Year year) {
	}
}
