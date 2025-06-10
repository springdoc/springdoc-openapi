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

package test.org.springdoc.api.v30.app19;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(schema = "application", name = "application")
@Schema(description = "app")
@EntityListeners(AuditingEntityListener.class)
public class Application implements Serializable {
	private static final long serialVersionUID = 6582562282311194139L;

	@Id
	@Column(length = 64)
	@GeneratedValue
	@Schema(description = "id")
	private String id;

	@Column(nullable = false, length = 64)
	@Schema(description = "name")
	private String name;

	@Column(length = 1024)
	@Schema(description = "description")
	private String description;

	@Column(length = 32)
	@Schema(description = "app type")
	@Enumerated(EnumType.STRING)
	private AppType type = AppType.EXTERNAL;

	@Column
	@Schema(description = "icon")
	private String icon;

	@Column(nullable = false)
	@CreatedDate
	@Schema(description = "createTime")
	private LocalDateTime createTime;

	@Column(length = 1024)
	@Schema(description = "rsa-publicKey")
	private String publicKey;

	@Column(length = 16, nullable = false)
	@Enumerated(EnumType.STRING)
	@Schema(description = "status")
	private AuditStatus auditStatus = AuditStatus.UN_SUBMITTED;

	@Column
	@Schema(description = "auditTime")
	private LocalDateTime auditTime;

	public enum AuditStatus {
		UN_SUBMITTED,
		PENDING,
		APPROVED,
		FAILED
	}

	public enum AppType {
		INNER,
		EXTERNAL
	}
}