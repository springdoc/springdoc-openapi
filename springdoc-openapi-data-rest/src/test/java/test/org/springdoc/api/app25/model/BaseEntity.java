package test.org.springdoc.api.app25.model;

import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@Type(type = "uuid-char")
	private UUID id;

	@PrePersist
	private void generateId() {
		if (id == null)
			id = UUID.randomUUID();
	}

}