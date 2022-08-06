package test.org.springdoc.api.app25.model;

import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@JdbcTypeCode(SqlTypes.CHAR)
	private UUID id;

	@PrePersist
	private void generateId() {
		if (id == null)
			id = UUID.randomUUID();
	}

}