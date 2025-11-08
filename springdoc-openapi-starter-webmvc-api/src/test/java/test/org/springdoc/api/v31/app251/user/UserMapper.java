package test.org.springdoc.api.v31.app251.user;

import org.springframework.stereotype.Component;

/*
 * you could also use a library like MapStruct
 */
@Component
public class UserMapper {

    // User to DTOs
    public UserDTOv1 toV1(User user) {
        return new UserDTOv1(
                user.id(),
                user.name(),
                user.email()
        );
    }

    public UserDTOv2 toV2(User user) {
        String[] nameParts = splitName(user.name());
        return new UserDTOv2(
                user.id(),
                nameParts[0],  // firstName
                nameParts[1],  // lastName
                user.email()
        );
    }

    // DTOs to User
    public User fromV1(UserDTOv1 dto) {
        return new User(
                dto.id(),
                dto.name(),
                dto.email()
        );
    }

    public User fromV2(UserDTOv2 dto) {
        String combinedName = combineName(dto.firstName(), dto.lastName());
        return new User(
                dto.id(),
                combinedName,
                dto.email()
        );
    }

    // Helper methods
    private String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", ""};
        }

        String trimmed = fullName.trim();
        int lastSpaceIndex = trimmed.lastIndexOf(' ');

        if (lastSpaceIndex == -1) {
            // Single word name - put it as firstName
            return new String[]{trimmed, ""};
        }

        // Split at last space (handles middle names better)
        // "John Smith Jr" -> "John Smith" and "Jr"
        // "Mary Ann Smith" -> "Mary Ann" and "Smith"
        return new String[]{
                trimmed.substring(0, lastSpaceIndex),
                trimmed.substring(lastSpaceIndex + 1)
        };
    }

    private String combineName(String firstName, String lastName) {
        firstName = firstName != null ? firstName.trim() : "";
        lastName = lastName != null ? lastName.trim() : "";

        if (firstName.isEmpty()) {
            return lastName;
        }
        if (lastName.isEmpty()) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}
