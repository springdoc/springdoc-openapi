package test.org.springdoc.api.v31.app1

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Person {

	@Id
	var id: Long? = null

	@Embedded
	var name: Name? = null
}

@Embeddable
class Name {

	var firstName: String? = null

	var lastName: String? = null
}
