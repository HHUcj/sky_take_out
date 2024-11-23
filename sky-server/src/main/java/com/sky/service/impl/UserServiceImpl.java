package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/18 - 下午11:29
 * @Version: v1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String URI = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String GRANT_TYPE = "authorization_code";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String jsonStr = doGet(userLoginDTO);

        String openid = JSONObject.parseObject(jsonStr).getString("openid");
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getUserByOpenId(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insertUser(user);
        }
        return UserLoginVO.builder().
                openid(openid).
                id(user.getId()).
                build();
    }

    private  String doGet(UserLoginDTO userLoginDTO) {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grant_type", GRANT_TYPE);
        String jsonStr = HttpClientUtil.doGet(URI, paramMap);
        return jsonStr;
    }
}
