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

package test.org.springdoc.api.v31.app9.component.dto.converter;


import test.org.springdoc.api.v31.app9.component.dto.DemoComponentDto;
import test.org.springdoc.api.v31.app9.component.model.DemoComponent;
import test.org.springdoc.api.v31.app9.utils.Converter;

import org.springframework.stereotype.Component;

@Component
public class DemoComponentConverter implements Converter<DemoComponent, DemoComponentDto> {

	@Override
	public DemoComponentDto convert(DemoComponent source) {
		if (source == null) {
			return null;
		}

		return DemoComponentDto.builder() //
				.id(source.getId()) //
				.payload(source.getPayload()) //
				.build();
	}

}
