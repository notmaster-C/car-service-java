package org.click.carservice.core.service;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.IOrderGoodsService;
import org.click.carservice.db.service.IOrderService;
import org.click.carservice.db.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 统计服务
 * @author click
 */
@Service
public class StatCoreService {


    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderGoodsService orderGoodsService;

    /**
     * 统计用户数据
     */
    public List<Map<String, Object>> statUser() {
        QueryWrapper<CarServiceUser> wrapper = new QueryWrapper<>();
        wrapper.select("SUBSTR(add_time, 1, 10) AS day", "COUNT(DISTINCT id) AS users");
        wrapper.groupBy("SUBSTR(add_time, 1, 10)");
        return userService.listMaps(wrapper);
    }

    /**
     * 统计订单数据
     */
    public List<Map<String, Object>> statOrder() {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.select("SUBSTR(add_time, 1, 10) AS day"
                , "COUNT(DISTINCT id) AS orders"
                , "COUNT(DISTINCT user_id) AS customers"
                , "SUM(actual_price) AS amount"
                , "ROUND(SUM(actual_price)/COUNT(DISTINCT user_id),2) AS pcr"
        );
        wrapper.groupBy("SUBSTR(add_time, 1, 10)");
        ArrayList<Short> status = new ArrayList<>();
        status.add(OrderStatus.STATUS_SHIP.getStatus());
        status.add(OrderStatus.STATUS_CONFIRM.getStatus());
        wrapper.in(CarServiceOrder.ORDER_STATUS, status);
        return orderService.listMaps(wrapper);
    }

    /**
     * 统计订单商品数据
     */
    public List<Map<String, Object>> statGoods() {
        QueryWrapper<CarServiceOrderGoods> wrapper = new QueryWrapper<>();
        wrapper.select("SUBSTR(add_time, 1, 10) AS day"
                , "COUNT(DISTINCT order_id) AS orders"
                , "SUM(number) AS products"
                , "SUM(number*price) AS amount"
        );
        wrapper.groupBy("SUBSTR(add_time, 1, 10)");
        return orderGoodsService.listMaps(wrapper);
    }


}
