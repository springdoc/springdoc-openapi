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

package org.springdoc.core.converters;


import java.util.Iterator;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

public class PageableSupportConverter implements ModelConverter {

	private static final String PAGEABLE_TO_REPLACE = "org.springframework.data.domain.Pageable";

	private static final String PAGE_REQUEST_TO_REPLACE = "org.springframework.data.domain.PageRequest";

	private static final AnnotatedType PAGEABLE = new AnnotatedType(Pageable.class);

	@Override
	public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
		String typeName = type.getType().getTypeName();
		if (PAGEABLE_TO_REPLACE.equals(typeName) || PAGE_REQUEST_TO_REPLACE.equals(typeName)) {
			type = PAGEABLE;
		}
		if (chain.hasNext()) {
			return chain.next().resolve(type, context, chain);
		}
		else {
			return null;
		}
	}
}