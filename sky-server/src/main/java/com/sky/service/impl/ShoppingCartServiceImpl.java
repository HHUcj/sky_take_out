package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ShoppingCartServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午3:36
 * @Version: v1.0
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setMealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCartDTO);

        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateShoppingCart(cart);
        } else {
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            Long userId = BaseContext.getCurrentId();

            if (dishId != null) {
                Dish dish = dishMapper.getDishById(dishId);
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .number(1)
                        .createTime(LocalDateTime.now())
                        .image(dish.getImage())
                        .name(dish.getName())
                        .dishId(dishId)
                        .amount(dish.getPrice())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .build();
                shoppingCartMapper.addShoppingCartMapper(shoppingCart);
            } else {
                Setmeal setmeal = setMealMapper.getById(setmealId);
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .userId(userId)
                        .number(1)
                        .createTime(LocalDateTime.now())
                        .image(setmeal.getImage())
                        .name(setmeal.getName())
                        .setmealId(setmealId)
                        .amount(setmeal.getPrice())
                        .build();
                shoppingCartMapper.addShoppingCartMapper(shoppingCart);
            }
        }
    }

    @Override
    public List<ShoppingCart> getShoppingCart() {
        return shoppingCartMapper.getShoppingCart(BaseContext.getCurrentId());
    }

    @Override
    public void delShoppingCart() {
        shoppingCartMapper.delShoppingCart(BaseContext.getCurrentId());
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCartDTO);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            return;
        }

        ShoppingCart shoppingCart = shoppingCartList.get(0);
        int number = shoppingCart.getNumber();

        if (number == 1) {
            shoppingCartMapper.subShoppingCart(shoppingCartDTO);
        } else if (number > 1) {
            shoppingCart.setNumber(number-1);
            shoppingCartMapper.updateShoppingCart(shoppingCart);
        }
    }
}
