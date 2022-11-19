package test.org.springdoc.api.app2.controller;

import lombok.RequiredArgsConstructor;
import test.org.springdoc.api.app2.entities.Post;
import test.org.springdoc.api.app2.service.PostService;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Davide Pedone
 * 2020
 */
@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<Post>>> getAll(Pageable pageable) {
		return new ResponseEntity<>(postService.getAll(pageable), HttpStatus.OK);
	}
}
