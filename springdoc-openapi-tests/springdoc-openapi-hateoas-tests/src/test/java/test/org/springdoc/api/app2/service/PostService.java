package test.org.springdoc.api.app2.service;


import lombok.RequiredArgsConstructor;
import test.org.springdoc.api.app2.entities.Post;
import test.org.springdoc.api.app2.hateoas.PostResourceAssembler;
import test.org.springdoc.api.app2.repositories.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

/**
 * @author Davide Pedone
 * 2020
 */
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PagedResourcesAssembler<Post> pagedResourcesAssembler;
    private final PostResourceAssembler postResourceAssembler;

    public PagedModel<EntityModel<Post>> getAll(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        return pagedResourcesAssembler.toModel(postPage, postResourceAssembler);
    }

}
