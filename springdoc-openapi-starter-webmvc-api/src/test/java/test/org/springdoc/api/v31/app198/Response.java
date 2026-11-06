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

package test.org.springdoc.api.v31.app198;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author bnasslahsen
 */
public class Response {

	@Schema(deprecated = true)
	@Deprecated
	private BigDecimal val1;

	private BigDecimal val2;

	private BigDecimal val3;

	public Response(BigDecimal val1, BigDecimal val2, BigDecimal val3) {
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
	}

	public BigDecimal getVal1() {
		return val1;
	}

	public void setVal1(BigDecimal val1) {
		this.val1 = val1;
	}

	public BigDecimal getVal2() {
		return val2;
	}

	public void setVal2(BigDecimal val2) {
		this.val2 = val2;
	}

	public BigDecimal getVal3() {
		return val3;
	}

	public void setVal3(BigDecimal val3) {
		this.val3 = val3;
	}
}
