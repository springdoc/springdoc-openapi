package test.org.springdoc.api.v31.app195.user;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // USING REQUEST HEADER ======================================================

    @GetMapping(value = "/users", version = "1.0")
    public List<UserDTOv1> getUsersV1() {
        log.info("Find All Users using request header: {}", "v1");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV1)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users", version = "2.0")
    public List<UserDTOv2> getUsersV2() {
        log.info("Find All Users using request header: {}", "v2");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toV2)
                .collect(Collectors.toList());
    }
	
}
