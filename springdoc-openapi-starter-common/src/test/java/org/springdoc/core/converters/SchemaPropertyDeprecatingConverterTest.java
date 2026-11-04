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

package org.springdoc.core.converters;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.SpringDocUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the registry of deprecated annotation types can be read while another thread
 * registers a type, and that registering an already known type does not grow it. The registry
 * was a synchronized list streamed without holding its lock, and it gained a duplicate entry on
 * every application context start.
 *
 * @author awinishere
 */
class SchemaPropertyDeprecatingConverterTest {

	private static final List<Class<? extends Annotation>> TEST_TYPES = List.of(
			DeprecatedA.class, DeprecatedB.class, DeprecatedC.class, DeprecatedD.class);

	private final Collection<Class<? extends Annotation>> registry = registry();

	@AfterEach
	void removeTestTypes() {
		registry.removeAll(TEST_TYPES);
	}

	@Test
	void registeringSameTypeDoesNotGrowRegistry() {
		int sizeBefore = registry.size();

		for (int i = 0; i < 100; i++) {
			SpringDocUtils.getConfig().addDeprecatedType(Deprecated.class);
		}

		assertEquals(sizeBefore, registry.size());
	}

	@Test
	void isDeprecatedIsSafeWhileAnotherThreadRegistersTypes() throws Exception {
		Method method = TestEndpoint.class.getMethod("endpoint");
		AtomicReference<Throwable> thrown = new AtomicReference<>();
		Thread reader = new Thread(() -> {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					SchemaPropertyDeprecatingConverter.isDeprecated(method);
				}
			}
			catch (Throwable t) {
				thrown.set(t);
			}
		});

		reader.start();
		try {
			for (int i = 0; i < 20_000; i++) {
				SpringDocUtils.getConfig().addDeprecatedType(TEST_TYPES.get(i % TEST_TYPES.size()));
			}
		}
		finally {
			reader.interrupt();
			reader.join(5000);
		}

		assertNull(thrown.get());
	}

	@Test
	void deprecatedMethodIsStillDetected() throws Exception {
		assertTrue(SchemaPropertyDeprecatingConverter.isDeprecated(TestEndpoint.class.getMethod("endpoint")));
	}

	@SuppressWarnings("unchecked")
	private static Collection<Class<? extends Annotation>> registry() {
		try {
			Field field = SchemaPropertyDeprecatingConverter.class.getDeclaredField("DEPRECATED_ANNOTATIONS");
			field.setAccessible(true);
			return (Collection<Class<? extends Annotation>>) field.get(null);
		}
		catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	static class TestEndpoint {

		@Deprecated
		public void endpoint() {
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface DeprecatedA {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface DeprecatedB {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface DeprecatedC {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface DeprecatedD {
	}
}
