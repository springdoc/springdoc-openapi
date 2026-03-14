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

package org.springdoc.ai.dashboard;

/**
 * Response DTO for MCP tool execution.
 *
 * @author bnasslahsen
 */
public class McpToolExecutionResponse {

	/**
	 * Whether the execution was successful.
	 */
	private boolean success;

	/**
	 * The result of the execution.
	 */
	private String result;

	/**
	 * The error message if execution failed.
	 */
	private String error;

	/**
	 * The execution duration in milliseconds.
	 */
	private long durationMs;

	/**
	 * The HTTP response status code from the underlying API call.
	 */
	private int httpStatusCode;

	/**
	 * Whether this response indicates that human approval is required.
	 */
	private boolean requiresApproval;

	/**
	 * Constructs a new McpToolExecutionResponse.
	 * @param success whether the execution was successful
	 * @param result the result
	 * @param error the error message
	 * @param durationMs the duration in milliseconds
	 * @param httpStatusCode the HTTP status code
	 */
	public McpToolExecutionResponse(boolean success, String result, String error, long durationMs, int httpStatusCode) {
		this.success = success;
		this.result = result;
		this.error = error;
		this.durationMs = durationMs;
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * Gets success.
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets success.
	 * @param success the success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Gets result.
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets result.
	 * @param result the result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Gets error.
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets error.
	 * @param error the error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets duration ms.
	 * @return the duration ms
	 */
	public long getDurationMs() {
		return durationMs;
	}

	/**
	 * Sets duration ms.
	 * @param durationMs the duration ms
	 */
	public void setDurationMs(long durationMs) {
		this.durationMs = durationMs;
	}

	/**
	 * Gets http status code.
	 * @return the http status code
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * Sets http status code.
	 * @param httpStatusCode the http status code
	 */
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * Gets requires approval.
	 * @return true if human approval is required before executing this tool
	 */
	public boolean isRequiresApproval() {
		return requiresApproval;
	}

	/**
	 * Sets requires approval.
	 * @param requiresApproval whether human approval is required
	 */
	public void setRequiresApproval(boolean requiresApproval) {
		this.requiresApproval = requiresApproval;
	}

}
