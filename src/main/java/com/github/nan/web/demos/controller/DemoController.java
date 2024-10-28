package com.github.nan.web.demos.controller;

import com.github.nan.web.core.annotation.AutoWrapResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author NanNan Wang
 */
@Api(tags = "请求规范")
@RestController
@RequestMapping("/demos")
@Slf4j
@AutoWrapResponse
public class DemoController {



    @ApiOperation(value = "向世界问好")
    @GetMapping("/hello")
    public String hello() {
        log.info("first api , use mdc to create trace Id");
        return "hello world!";
    }



    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return Strings.concat("hello ", name);
    }


    @GetMapping("/hey")
    public String hello2(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }


    @PostMapping("/user")
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        return user;
    }

    @PostMapping("/save-user")
    public String saveUser(@Validated @RequestBody User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }


    @PostMapping("/find-user")
    public String findUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }
}
