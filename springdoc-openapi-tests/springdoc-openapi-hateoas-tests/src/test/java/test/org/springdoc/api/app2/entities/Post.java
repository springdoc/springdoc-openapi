package test.org.springdoc.api.app2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Davide Pedone
 * 2020
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue
    private Long id;
    private String author;
    private String content;
    private Long createdAt;

}
