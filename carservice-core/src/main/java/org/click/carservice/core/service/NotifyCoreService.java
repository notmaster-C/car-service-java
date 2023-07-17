package org.click.carservice.core.service;

import org.click.carservice.core.notify.service.NotifyMailService;
import org.click.carservice.core.utils.NotifyMessageUtil;
import org.click.carservice.core.weixin.service.SubscribeMessageService;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.service.IBrandService;
import org.click.carservice.db.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author click
 */
@Service
public class NotifyCoreService {

    @Autowired
    private IUserService userService;
    @Autowired
    private SubscribeMessageService subscribeMessageService;
    @Autowired
    private NotifyMailService mailService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private CommonService commonService;

    /**
     * 订单消息通知
     *
     * @param order 订单
     */
    public void orderNotify(CarServiceOrder order) {
        //获取订单商品
        List<CarServiceOrderGoods> orderGoodsList = commonService.queryByOid(order.getId());
        for (CarServiceOrderGoods orderGoods : orderGoodsList) {
            CarServiceBrand brand = brandService.findById(order.getBrandId());
            //新订单订阅通知
            CarServiceUser user = userService.findById(brand.getUserId());
            if (brand.getUserId() != null && user != null) {
                subscribeMessageService.newOrderSubscribe(user.getOpenid(), order, orderGoods);
            }
            //店铺邮箱通知
            String orderMessage = NotifyMessageUtil.orderMessage(order, orderGoods);
            if (StringUtils.hasText(brand.getMail())) {
                mailService.notifyMail("新订单通知", orderMessage, brand.getMail());
            } else {
                mailService.notifyMail("新订单通知", orderMessage);
            }
        }
    }


    /**
     * 订单退款通知
     *
     * @param order 订单
     */
    public void orderRefundNotify(CarServiceOrder order) {
        List<CarServiceOrderGoods> orderGoodsList = commonService.queryByOid(order.getId());
        for (CarServiceOrderGoods orderGoods : orderGoodsList) {
            String refundMessage = NotifyMessageUtil.refundMessage(order, orderGoods);
            CarServiceBrand brand = brandService.findById(order.getBrandId());
            if (StringUtils.hasText(brand.getMail())) {
                mailService.notifyMail("退款申请", refundMessage, brand.getMail());
            } else {
                mailService.notifyMail("退款申请", refundMessage);
            }
        }
    }

    /**
     * 订单售后通知
     *
     * @param order 订单
     */
    public void aftersaleRefundNotify(CarServiceOrder order, CarServiceAfterSale aftersale) {
        CarServiceBrand brand = brandService.findById(order.getBrandId());
        CarServiceOrderGoods orderGoods = commonService.findByOrderId(order.getId());
        String message = NotifyMessageUtil.aftersaleMessage(order, orderGoods, aftersale);
        if (StringUtils.hasText(brand.getMail())) {
            mailService.notifyMail("售后申请", message, brand.getMail());
        } else {
            mailService.notifyMail("售后申请", message);
        }
    }


}
