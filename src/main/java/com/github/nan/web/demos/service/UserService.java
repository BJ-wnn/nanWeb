package com.github.nan.web.demos.service;

import com.github.nan.web.demos.controller.User;
import com.github.nan.web.demos.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NanNan Wang
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public List<User> findAllUser() {
        return userMapper.selectAllUser().stream().map( data -> {
            final User user = new User();
            user.setName(data.getName());
            return user;
        }).collect(Collectors.toList());
    }

}
