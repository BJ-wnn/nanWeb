package com.github.nan.web.core.function;

import java.util.function.Function;

/**
 * 使用函数式方法，统一规范类型转换。
 *
 * <br>
 * 使用场景：
 * <blockquote><pre>
 *  命令式编程
 *  public void demo(UserVO userVO) {
 *      UserDO do = new UserDO();
 *      do.setUserName(userVO.getName());
 *      do.setUserAge(userVO.getAge());
 *      ....
 *  }
 *  函数式编程
 *  public void demo(UserVO userVO) {
 *      UserDO do = TypeConverter.convert(userVO, UserDO::new);
 *  }
 * </pre></blockquote>
 *
 * @author NanNan Wang
 */
public class TypeConverter {
    public static <T, R> R convert(T t, Function<T, R> function) {
        if (t == null || function == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        return function.apply(t);
    }

}
