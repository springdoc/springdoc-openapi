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

package test.org.springdoc.api.v30.app29;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Tracker data.
 */
@Schema(name = "TrackerData")
class TrackerData {

	/**
	 * The Tracker id.
	 */
	@Schema(name = "trackerId", type = "string", required = true, example = "the-tracker-id")
	@JsonProperty("trackerId")
	String trackerId;

	/**
	 * The Timestamp.
	 */
	@Schema(name = "timestamp", type = "string", format = "date-time", required = true, example = "2018-01-01T00:00:00Z")
	@JsonProperty("timestamp")
	Instant timestamp;

	/**
	 * The Value.
	 */
	@Schema(name = "value", type = "number", format = "double", description = "The data value", required = true, example = "19.0")
	@JsonProperty("value")
	Double value;

}
