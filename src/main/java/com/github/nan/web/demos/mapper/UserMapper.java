package com.github.nan.web.demos.mapper;

import com.github.nan.web.demos.mapper.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author NanNan Wang
 */
@Mapper
public interface UserMapper {


    List<UserPO> selectAllUser();
}
