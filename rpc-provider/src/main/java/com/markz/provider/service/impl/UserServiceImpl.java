package com.markz.provider.service.impl;

import com.markz.common.entity.User;
import com.markz.common.service.UserService;
import com.markz.rpccore.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService(serviceInterface = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Integer id) {
        User user = new User();
        user.setId(1);
        user.setUsername("markz");
        user.setPassword("1234");
        return user;
    }

    @Override
    public String test() {
        return "hello";
    }
}