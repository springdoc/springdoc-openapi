package test.org.springdoc.api.app35;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PropertyRepository extends PagingAndSortingRepository<Property, Long> {
}