package test.org.springdoc.api.app25.repo;

import java.util.UUID;

import test.org.springdoc.api.app25.model.Clinic;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ClinicRepo extends CrudRepository<Clinic, UUID> {

}
