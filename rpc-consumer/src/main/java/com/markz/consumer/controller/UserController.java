package com.markz.consumer.controller;

import com.markz.common.entity.User;
import com.markz.common.service.UserService;
import com.markz.rpccore.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    private UserService userService;

    @GetMapping
    public User getUserById(Integer id) {
        return null;
    }

    @GetMapping("/test")
    public String test() {
        return userService.test();
    }
}
