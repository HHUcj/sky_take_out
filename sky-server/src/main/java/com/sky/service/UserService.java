package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

/**
 * ClassName: UserService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/18 - 下午11:29
 * @Version: v1.0
 */
public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
