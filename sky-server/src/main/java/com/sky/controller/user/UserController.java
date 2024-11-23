package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/18 - 下午11:19
 * @Version: v1.0
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {

    private static final String OPEN_ID = "openid";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        if (userLoginDTO.getCode() == null) {
            return Result.error("code为空!");
        }

        UserLoginVO userLoginVO = userService.login(userLoginDTO);

        Map<String, Object> claim = new HashMap<>();
        claim.put(OPEN_ID, userLoginVO.getOpenid());
        claim.put(JwtClaimsConstant.USER_ID, userLoginVO.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claim);

        userLoginVO.setToken(token);

        return Result.success(userLoginVO);
    }
}
