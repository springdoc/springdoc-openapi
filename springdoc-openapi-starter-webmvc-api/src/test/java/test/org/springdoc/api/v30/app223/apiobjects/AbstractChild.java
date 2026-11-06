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

package test.org.springdoc.api.v30.app223.apiobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
		@Type(ChildType1.class),
		@Type(ChildType2.class)
})
public abstract class AbstractChild {
	private int id;

	public AbstractChild(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class ChildType1 extends AbstractChild {
	private String childType1Param;

	public ChildType1(int id, String childType1Param) {
		super(id);
		this.childType1Param = childType1Param;
	}

	public String getChildType1Param() {
		return childType1Param;
	}

	public void setChildType1Param(String childType1Param) {
		this.childType1Param = childType1Param;
	}
}

class ChildType2 extends AbstractChild {
	private String childType2Param;

	public ChildType2(int id, String childType2Param) {
		super(id);
		this.childType2Param = childType2Param;
	}

	public String getChildType2Param() {
		return childType2Param;
	}

	public void setChildType2Param(String childType2Param) {
		this.childType2Param = childType2Param;
	}
}