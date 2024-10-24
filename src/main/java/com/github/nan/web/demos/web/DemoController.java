package com.github.nan.web.demos.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NanNan Wang
 */
@Api(tags = "请求规范")
@RestController
@RequestMapping("/demos")
public class DemoController {


    @ApiOperation(value = "向世界问好")
    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }



    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return Strings.concat("hello ", name);
    }



}
