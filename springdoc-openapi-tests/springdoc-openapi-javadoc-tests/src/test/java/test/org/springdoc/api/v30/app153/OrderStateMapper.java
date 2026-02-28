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

package test.org.springdoc.api.v30.app153;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The type Order state mapper.
 */
class OrderStateMapper extends PropertyEditorSupport {

	/**
	 * Sets as text.
	 *
	 * @param text the text
	 */
	@Override
	public void setAsText(String text) {
		setValue(
				Arrays.stream(OrderState.class.getEnumConstants())
						.filter(e -> e.getValue().equals(text))
						.findFirst()
						.orElseThrow(() -> new MethodArgumentTypeMismatchException(
								text, OrderState.class, "orderState", null, null)));
	}
}