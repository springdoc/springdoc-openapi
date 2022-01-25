/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core.providers;

import java.util.Optional;

import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * The type Web conversion service provider.
 * @author bnasslashen
 */
public class WebConversionServiceProvider {

	/**
	 * The Formatting conversion service.
	 */
	private final FormattingConversionService formattingConversionService;

	/**
	 * Instantiates a new Web conversion service provider.
	 *
	 * @param webConversionServiceOptional the web conversion service optional
	 */
	public WebConversionServiceProvider(Optional<WebConversionService> webConversionServiceOptional) {
		if (webConversionServiceOptional.isPresent())
			this.formattingConversionService = webConversionServiceOptional.get();
		else
			formattingConversionService = new DefaultFormattingConversionService();
	}

	/**
	 * Attempts to convert {@code source} into the target type as described by {@code targetTypeDescriptor}.
	 *
	 * @param source the source
	 * @param targetTypeDescriptor the target type descriptor
	 * @return the converted source
	 */
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor targetTypeDescriptor) {
			return formattingConversionService.convert(source, targetTypeDescriptor);
	}
}
