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

package test.org.springdoc.api.v31.app301;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springdoc.core.providers.ObjectMapperProvider;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "springdoc.enable-data-rest=false")
public class SpringDocApp301Test extends AbstractSpringDocTest {

	private final Map<ClassKey, Class<?>> springMixins = new HashMap<>();

	@Autowired
	ObjectMapperProvider objectMapperProvider;

	@BeforeEach
	void init() throws IllegalAccessException {
		SimpleMixInResolver _mixIns = (SimpleMixInResolver) FieldUtils.readDeclaredField(objectMapperProvider.jsonMapper(), "_mixIns", true);
		Map<ClassKey, Class<?>> _localMixIns = (Map<ClassKey, Class<?>>) FieldUtils.readDeclaredField(_mixIns, "_localMixIns", true);
		Iterator<Entry<ClassKey, Class<?>>> it = _localMixIns.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ClassKey, Class<?>> entry = it.next();
			if (entry.getKey().toString().startsWith("org.springframework")) {
				springMixins.put(entry.getKey(), entry.getValue());
				it.remove();
			}
		}

	}

	@AfterEach
	void clean() throws IllegalAccessException {
		SimpleMixInResolver _mixIns = (SimpleMixInResolver) FieldUtils.readDeclaredField(objectMapperProvider.jsonMapper(), "_mixIns", true);
		Map<ClassKey, Class<?>> _localMixIns = (Map<ClassKey, Class<?>>) FieldUtils.readDeclaredField(_mixIns, "_localMixIns", true);
		_localMixIns.putAll(springMixins);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}