package test.org.springdoc.api.app2.hateoas;



import test.org.springdoc.api.app2.entities.Post;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * @author Davide Pedone
 * 2020
 */
@Component
public class PostResourceAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {


	@Override
	public EntityModel<Post> toModel(Post entity) {
		return new EntityModel<>(entity);
	}
}
