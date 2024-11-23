package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ShopController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/18 - 下午6:14
 * @Version: v1.0
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {
    private static final String STATUS = "status";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(STATUS);
        return Result.success(status);
    }
}
