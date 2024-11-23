package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * ClassName: UserMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/18 - 下午11:48
 * @Version: v1.0
 */
@Mapper
public interface UserMapper {
    User getUserByOpenId(String openid);

    void insertUser(User user);

    Long getUserCnt(Map<String, Object> map);

    Integer getUserByCreateTime(Map<String, Object> map);
}
