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
package org.springdoc.webflux.core.configuration.hints;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.web.reactive.accept.DefaultApiVersionStrategy;
import org.springframework.web.reactive.accept.HeaderApiVersionResolver;
import org.springframework.web.reactive.accept.MediaTypeParamApiVersionResolver;
import org.springframework.web.reactive.accept.PathApiVersionResolver;
import org.springframework.web.reactive.accept.QueryApiVersionResolver;

/**
 * @author bnasslahsen
 */
public class SpringDocWebFluxHints  implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

		hints.reflection().registerType(
				TypeReference.of(DefaultApiVersionStrategy.class),
				builder -> builder.withField("versionResolvers")
		);

		hints.reflection().registerType(
				TypeReference.of(MediaTypeParamApiVersionResolver.class),
				builder -> builder
						.withField("compatibleMediaType")
						.withField("parameterName")
		);

		hints.reflection().registerType(
				TypeReference.of(PathApiVersionResolver.class),
				builder -> builder.withField("pathSegmentIndex")
		);

		hints.reflection().registerType(
				TypeReference.of(HeaderApiVersionResolver.class),
				builder -> builder.withField("headerName")
		);

		hints.reflection().registerType(
				TypeReference.of(QueryApiVersionResolver.class),
				builder -> builder.withField("queryParamName")
		);
	}
}