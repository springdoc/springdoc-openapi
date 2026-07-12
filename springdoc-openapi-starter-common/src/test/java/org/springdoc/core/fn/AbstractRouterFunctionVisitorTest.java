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
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.fn;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AbstractRouterFunctionVisitor}.
 *
 * @author oss-bot
 */
class AbstractRouterFunctionVisitorTest {

	@Test
	void versionFromNestPredicateAppliesToEveryRouteInTheNest() {
		TestVisitor visitor = new TestVisitor();

		// nest(version("v1"), route(GET "/foo").and(route(GET "/bar")))
		visitor.commonStartNestedPublic();
		visitor.version("v1");
		visitor.routePublic(() -> visitor.path("/foo"));
		visitor.routePublic(() -> visitor.path("/bar"));
		visitor.commonEndNestedPublic();

		List<RouterFunctionData> routes = visitor.getRouterFunctionDatas();
		assertThat(routes).hasSize(2);
		assertThat(routes).extracting(RouterFunctionData::getPath).containsExactly("/foo", "/bar");
		assertThat(routes).extracting(RouterFunctionData::getVersion).containsExactly("v1", "v1");
	}

	@Test
	void versionFromAndNestPredicateAppliesToEveryRouteInItsOwnNest() {
		TestVisitor visitor = new TestVisitor();

		// nest(version("v1"), routes).andNest(version("v2"), routes)
		visitor.commonStartNestedPublic();
		visitor.version("v1");
		visitor.routePublic(() -> visitor.path("/foo"));
		visitor.routePublic(() -> visitor.path("/bar"));
		visitor.commonEndNestedPublic();

		visitor.commonStartNestedPublic();
		visitor.version("v2");
		visitor.routePublic(() -> visitor.path("/foo"));
		visitor.routePublic(() -> visitor.path("/bar"));
		visitor.commonEndNestedPublic();

		List<RouterFunctionData> routes = visitor.getRouterFunctionDatas();
		assertThat(routes).hasSize(4);
		assertThat(routes).extracting(RouterFunctionData::getVersion)
				.containsExactly("v1", "v1", "v2", "v2");
	}

	@Test
	void perRouteVersionRemainsOneShotInsideNest() {
		TestVisitor visitor = new TestVisitor();

		// nest(path("/api"), route().version("v1").GET("/foo").and(route().GET("/bar")))
		visitor.commonStartNestedPublic();
		visitor.path("/api");

		visitor.routePublic(() -> {
			visitor.version("v1");
			visitor.path("/foo");
		});
		visitor.routePublic(() -> visitor.path("/bar"));

		visitor.commonEndNestedPublic();

		List<RouterFunctionData> routes = visitor.getRouterFunctionDatas();
		assertThat(routes).hasSize(2);
		assertThat(routes).extracting(RouterFunctionData::getVersion).containsExactly("v1", null);
	}

	@Test
	void innerNestVersionOverridesOuterNestVersion() {
		TestVisitor visitor = new TestVisitor();

		// nest(version("v1"), nest(version("v2"), route))
		visitor.commonStartNestedPublic();
		visitor.version("v1");

		visitor.commonStartNestedPublic();
		visitor.version("v2");
		visitor.routePublic(() -> visitor.path("/foo"));
		visitor.commonEndNestedPublic();

		// After the inner nest ends, the outer version applies again
		visitor.routePublic(() -> visitor.path("/bar"));
		visitor.commonEndNestedPublic();

		List<RouterFunctionData> routes = visitor.getRouterFunctionDatas();
		assertThat(routes).extracting(RouterFunctionData::getPath).containsExactly("/foo", "/bar");
		assertThat(routes).extracting(RouterFunctionData::getVersion).containsExactly("v2", "v1");
	}

	@Test
	void topLevelPerRouteVersionRemainsOneShot() {
		TestVisitor visitor = new TestVisitor();

		// route().version("v0").GET("/top") followed by route().GET("/plain")
		visitor.routePublic(() -> {
			visitor.version("v0");
			visitor.path("/top");
		});
		visitor.routePublic(() -> visitor.path("/plain"));

		List<RouterFunctionData> routes = visitor.getRouterFunctionDatas();
		assertThat(routes).extracting(RouterFunctionData::getVersion).containsExactly("v0", null);
	}

	/**
	 * Exposes the relevant {@code protected} hooks so tests can simulate the call order a
	 * real {@code RouterFunctions.Visitor} produces.
	 */
	private static class TestVisitor extends AbstractRouterFunctionVisitor {

		void commonStartNestedPublic() {
			commonStartNested();
		}

		void commonEndNestedPublic() {
			commonEndNested();
		}

		void routePublic(Runnable predicateVisits) {
			this.currentRouterFunctionDatas = new ArrayList<>();
			predicateVisits.run();
			commonRoute();
		}
	}
}
