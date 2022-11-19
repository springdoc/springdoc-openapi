package test.org.springdoc.api.app2.config;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import test.org.springdoc.api.app2.entities.Post;
import test.org.springdoc.api.app2.repositories.PostRepository;

import org.springframework.context.annotation.Configuration;

/**
 * @author Davide Pedone
 * 2020
 */
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

	private final PostRepository postRepository;

	@PostConstruct
	private void postConstruct() {
		for (int i = 0; i < 33; i++) {
			Post post = new Post();
			post.setAuthor("name" + i);
			post.setContent("content" + i);
			post.setCreatedAt(LocalDate.now().toEpochDay());
			postRepository.save(post);
		}
	}
}
