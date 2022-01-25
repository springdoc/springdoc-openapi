package test.org.springdoc.api.app20;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "banks")
public interface BankRepository extends JpaRepository<Bank, Long>, CodeLookupRepository<Bank, String> {
    // moved to CodeLookupRepository
    //Bank findOneByCode(String code);
}
