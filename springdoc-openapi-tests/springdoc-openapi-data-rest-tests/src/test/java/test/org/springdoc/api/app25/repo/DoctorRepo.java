package test.org.springdoc.api.app25.repo;

import java.util.UUID;

import test.org.springdoc.api.app25.model.Clinic;
import test.org.springdoc.api.app25.model.Doctor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface DoctorRepo extends CrudRepository<Doctor, UUID> {

	Iterable<Doctor> findByClinicsContains(Clinic clinic);
	
}
