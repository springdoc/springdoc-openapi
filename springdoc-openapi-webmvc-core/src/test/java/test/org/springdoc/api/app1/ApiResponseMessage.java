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

package test.org.springdoc.api.app1;

import javax.xml.bind.annotation.XmlTransient;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
@javax.xml.bind.annotation.XmlRootElement
public class ApiResponseMessage {
	public static final int ERROR = 1;

	public static final int WARNING = 2;

	public static final int INFO = 3;

	public static final int OK = 4;

	public static final int TOO_BUSY = 5;

	int code;

	String type;

	String message;

	public ApiResponseMessage() {
	}

	public ApiResponseMessage(int code, String message) {
		this.code = code;
		switch (code) {
			case ERROR:
				setType("error");
				break;
			case WARNING:
				setType("warning");
				break;
			case INFO:
				setType("info");
				break;
			case OK:
				setType("ok");
				break;
			case TOO_BUSY:
				setType("too busy");
				break;
			default:
				setType("unknown");
				break;
		}
		this.message = message;
	}

	@XmlTransient
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
