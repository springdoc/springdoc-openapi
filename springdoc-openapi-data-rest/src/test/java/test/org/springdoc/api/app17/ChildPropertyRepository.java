package test.org.springdoc.api.app17;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChildPropertyRepository extends PagingAndSortingRepository<ChildProperty, Long> {
}