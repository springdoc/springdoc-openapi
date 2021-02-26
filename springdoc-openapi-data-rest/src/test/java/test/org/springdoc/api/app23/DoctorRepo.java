package test.org.springdoc.api.app23;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface DoctorRepo extends CrudRepository<Doctor, Long> {
}