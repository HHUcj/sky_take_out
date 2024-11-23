package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * ClassName: ShoppingCartService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午3:36
 * @Version: v1.0
 */
public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getShoppingCart();

    void delShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
