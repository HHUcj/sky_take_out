package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: ShoppingCartMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午3:38
 * @Version: v1.0
 */
@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCartDTO shoppingCartDTO);

    void updateShoppingCart(ShoppingCart cart);

    void addShoppingCartMapper(ShoppingCart shoppingCart);

    List<ShoppingCart> getShoppingCart(Long userId);

    void delShoppingCart(Long userId);

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    void batchAddShoppingCartMapper(List<ShoppingCart> shoppingCartList);
}
