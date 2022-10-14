package test.org.springdoc.api.app35;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.data.repository.PagingAndSortingRepository;

@Hidden
public interface PropertyRepository extends PagingAndSortingRepository<Property, Long> {
}