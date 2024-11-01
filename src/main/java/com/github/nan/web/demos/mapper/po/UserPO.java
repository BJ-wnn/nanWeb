package com.github.nan.web.demos.mapper.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author NanNan Wang
 */
@Data
public class UserPO {

    private long id;
    private String name;
    private String password;
    private String email;
    private LocalDateTime createAt;
}
