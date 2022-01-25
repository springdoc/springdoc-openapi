package test.org.springdoc.api.app2.repositories;


import test.org.springdoc.api.app2.entities.Post;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Davide Pedone
 * 2020
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
