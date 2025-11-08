package test.org.springdoc.api.v31.app248.user;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // USING MEDIA TYPE (Content Negotiation) =======================================

    @GetMapping(value = "/users/media", version = "1.0", produces = "application/json")
    public List<UserDTOv1> getUsersMediaV1() {
        log.info("Find All Users using media type versioning: {}", "v1");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV1)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users/media", version = "2.0", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTOv2> getUsersMediaV2() {
        log.info("Find All Users using media type versioning: {}", "v2");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV2)
                .collect(Collectors.toList());
    }

}
