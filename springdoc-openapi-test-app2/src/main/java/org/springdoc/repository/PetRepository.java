package org.springdoc.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springdoc.model.Pet;
import org.springdoc.model.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class PetRepository extends HashMapRepository<Pet, Long> {

    private Long sequenceId = 1L;

    @Override
    <S extends Pet> Long getEntityId(S pet) {
        return pet.getId();
    }

    @Override
    public <S extends Pet> S save(S pet) {
        if (pet.getId() != null && pet.getId() > sequenceId) {
            sequenceId = pet.getId() + 1;
        }
        if (pet.getId() == null) {
            pet.setId(sequenceId);
            sequenceId += 1;
        }
        return super.save(pet);
    }

	@SuppressWarnings("unlikely-arg-type")
	public List<Pet> findPetsByStatus(List<Pet.StatusEnum> statusList) {
        return entities.values().stream()
                .filter(entity -> entity.getStatus() != null)
				.filter(entity -> statusList.contains(entity.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Pet> findPetsByTags(List<String> tags) {
        return entities.values().stream()
                .filter(entity -> entity.getTags() != null)
                .filter(entity -> entity.getTags().stream()
                        .map(Tag::getName)
                        .anyMatch(tags::contains)
                )
                .collect(Collectors.toList());
    }
}
