/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app222;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

public class SpringDocApp222Test extends AbstractSpringDocV30Test {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@JsonTypeInfo(use = Id.NAME, property = "@type")
	@JsonSubTypes(@Type(CommonImplementor.class))
	interface FirstHierarchy {}

	@JsonTypeInfo(use = Id.NAME, property = "@type")
	@JsonSubTypes(@Type(CommonImplementor.class))
	interface SecondHierarchy {}

	class CommonImplementor implements FirstHierarchy, SecondHierarchy {}

	record CommonImplementorUser(FirstHierarchy firstHierarchy, SecondHierarchy secondHierarchy) {}

	record FirstHierarchyUser(FirstHierarchy firstHierarchy) {}

	record SecondHierarchyUser(SecondHierarchy secondHierarchy) {}
}
