/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v30.app118;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * The type Abstract parent.
 */
@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildOfAbstract1.class),
		@Type(ChildOfAbstract2.class)
})
public abstract class AbstractParent {
	/**
	 * The Id.
	 */
	private int id;

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
	}
}

/**
 * The type Child of abstract 1.
 */
class ChildOfAbstract1 extends AbstractParent {
	/**
	 * The Abstrach child 1 param.
	 */
	private String abstrachChild1Param;

	/**
	 * Gets abstrach child 1 param.
	 *
	 * @return the abstrach child 1 param
	 */
	public String getAbstrachChild1Param() {
		return abstrachChild1Param;
	}

	/**
	 * Sets abstrach child 1 param.
	 *
	 * @param abstrachChild1Param the abstrach child 1 param
	 */
	public void setAbstrachChild1Param(String abstrachChild1Param) {
		this.abstrachChild1Param = abstrachChild1Param;
	}
}

/**
 * The type Child of abstract 2.
 */
class ChildOfAbstract2 extends AbstractParent {
	/**
	 * The Abstract child 2 param.
	 */
	private String abstractChild2Param;

	/**
	 * Gets abstract child 2 param.
	 *
	 * @return the abstract child 2 param
	 */
	public String getAbstractChild2Param() {
		return abstractChild2Param;
	}

	/**
	 * Sets abstract child 2 param.
	 *
	 * @param abstractChild2Param the abstract child 2 param
	 */
	public void setAbstractChild2Param(String abstractChild2Param) {
		this.abstractChild2Param = abstractChild2Param;
	}
}
