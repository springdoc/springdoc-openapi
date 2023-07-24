package test.org.springdoc.api.v30.app210.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.org.springdoc.api.v30.app210.model.domain.User;

@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {

    @PostMapping("abstract-parent")
    public test.org.springdoc.api.v30.app210.model.database.User endpoint(@RequestBody User payload) {
        return new test.org.springdoc.api.v30.app210.model.database.User();
    }
}
