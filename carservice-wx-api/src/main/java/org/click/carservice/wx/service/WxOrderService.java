package org.click.carservice.wx.service;
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

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.service.impl.OrderServiceImpl;
import org.click.carservice.wx.model.brand.body.BrandOrderListBody;
import org.click.carservice.wx.model.order.body.OrderListBody;
import org.click.carservice.wx.model.order.result.UserOrderInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_order")
public class WxOrderService extends OrderServiceImpl {


    @Cacheable(sync = true)
    public CarServiceOrder findById(String userId, String orderId) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.ID, orderId);
        return getOne(wrapper);
    }

    @Cacheable(sync = true)
    public Integer count(String userId) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public Integer count(List<Short> orderStatus) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(CarServiceOrder.ORDER_STATUS, orderStatus);
        }
        return Math.toIntExact(count(wrapper));
    }

    /**
     * 判断订单号是否存在
     */
    @Cacheable(sync = true)
    public Boolean countByOrderSn(String userId, String orderSn) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.ORDER_SN, orderSn);
        return exists(wrapper);
    }

    /**
     * 判断订单号是否存在
     */
    @Cacheable(sync = true)
    public CarServiceOrder findBySn(String userId, String orderSn) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.ORDER_SN, orderSn);
        return getOne(wrapper);
    }


    @Cacheable(sync = true)
    public CarServiceOrder findByBrandId(String brandId, String orderId) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.ID, orderId);
        wrapper.eq(CarServiceOrder.BRAND_ID, brandId);
        return getOne(wrapper);
    }

    /**
     * 判断商户订单号是否存在
     */
    public Boolean countByOutTradeNo(String userId, String outTradeNo) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        wrapper.eq(CarServiceOrder.OUT_TRADE_NO, outTradeNo);
        return exists(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceOrder> queryByOrderStatus(String userId, List<Short> orderStatus, OrderListBody body) {
        QueryWrapper<CarServiceOrder> wrapper = startPage(body);
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(CarServiceOrder.ORDER_STATUS, orderStatus);
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public List<CarServiceOrder> queryByBrandOrderStatus(String brandId, List<Short> orderStatus, BrandOrderListBody body) {
        QueryWrapper<CarServiceOrder> wrapper = startPage(body);
        wrapper.eq(CarServiceOrder.BRAND_ID, brandId);
        if (orderStatus != null && orderStatus.size() > 0) {
            wrapper.in(CarServiceOrder.ORDER_STATUS, orderStatus);
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(CarServiceOrder.MOBILE, body.getMobile());
        }
        return queryAll(wrapper);
    }
    @Cacheable(sync = true)
    public boolean createQrcode(String orderId) {

        UpdateWrapper<CarServiceOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq(CarServiceOrder.ID, orderId);
        // 生成字节数组输出流
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        QrCodeUtil.generate(orderId,
                QrConfig.create().setWidth(300).setHeight(300),"png",byteOut);
        // 生成字节数组输入流
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        // 从流中读取图片,返回图片文件
        BufferedImage img = ImgUtil.read(byteIn);
        // 将图片对象转换为Base64形式
        String base64 = ImgUtil.toBase64(img, "png");
        wrapper.set("QrCode",base64);
        return update(wrapper);
    }

    @Cacheable(sync = true)
    public UserOrderInfo orderInfo(String userId) {
        QueryWrapper<CarServiceOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceOrder.USER_ID, userId);
        List<CarServiceOrder> orders = queryAll(wrapper);
        Integer unpaid = 0;
        Integer unship = 0;
        Integer unrecv = 0;
        Integer uncomment = 0;
        for (CarServiceOrder order : orders) {
            if (OrderStatus.isCreateStatus(order) || OrderStatus.isGrouponNoneStatus(order)) {
                unpaid++;
            } else if (OrderStatus.isPayStatus(order) || OrderStatus.isBtlPayStatus(order) || OrderStatus.isGrouponOnStatus(order) || OrderStatus.isGrouponSucceedStatus(order)) {
                unship++;
            } else if (OrderStatus.isShipStatus(order)) {
                unrecv++;
            } else if (OrderStatus.isConfirmStatus(order) || OrderStatus.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            }
        }
        UserOrderInfo orderInfo = new UserOrderInfo();
        orderInfo.setUnpaid(unpaid);
        orderInfo.setUnship(unship);
        orderInfo.setUnrecv(unrecv);
        orderInfo.setUncomment(uncomment);
        return orderInfo;
    }



    @CacheEvict(allEntries = true)
    public void updateAftersaleStatus(String orderId, Short statusReject) {
        CarServiceOrder order = new CarServiceOrder();
        order.setId(orderId);
        order.setAftersaleStatus(statusReject);
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);
    }


}
