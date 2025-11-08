package test.org.springdoc.api.v31.app250.user;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return users;
    }

    public User findById(Integer id) {
        return users.stream().filter(u -> u.id().equals(id)).findFirst().orElse(null);
    }

    @PostConstruct
    private void init() {
        users.add(new User(1,"Dan Vega","danvega@gmail.com"));
    }
}
