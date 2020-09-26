package test.org.springdoc.api.app19;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
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