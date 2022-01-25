package test.org.springdoc.api.app25.repo;

import java.util.UUID;

import test.org.springdoc.api.app25.model.Owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface OwnerRepo extends JpaRepository<Owner, UUID> {

}
