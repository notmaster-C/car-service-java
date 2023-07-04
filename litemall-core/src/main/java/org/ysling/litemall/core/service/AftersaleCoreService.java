package org.ysling.litemall.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.service.IAftersaleService;
import org.ysling.litemall.db.service.IOrderService;
import org.ysling.litemall.db.domain.LitemallAftersale;
import org.ysling.litemall.db.domain.LitemallOrder;
import org.ysling.litemall.db.enums.AftersaleStatus;
import org.ysling.litemall.db.enums.OrderStatus;
import java.time.LocalDateTime;

/**
 * 售后服务类
 * @author Ysling
 */
@Service
public class AftersaleCoreService {


    @Autowired
    private CommonService commonService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IAftersaleService aftersaleService;


    /**
     * 售后审核通过
     * @param aftersale 售后信息
     * @return 成功
     */
    public Object recept(LitemallAftersale aftersale) {
        //判断售后状态
        if(!aftersale.getStatus().equals(AftersaleStatus.STATUS_REQUEST.getStatus())){
            throw new RuntimeException("当前售后状态不能进行审核通过操作");
        }

        LitemallOrder order = commonService.findOrderById(aftersale.getUserId(), aftersale.getOrderId());
        if (order == null) {
            throw new RuntimeException("未找到售后订单="+aftersale.getOrderId());
        }

        aftersale.setStatus(AftersaleStatus.STATUS_RECEPT.getStatus());
        aftersale.setHandleTime(LocalDateTime.now());
        if (aftersaleService.updateVersionSelective(aftersale) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        //订单也要更新售后状态
        order.setAftersaleStatus(AftersaleStatus.STATUS_RECEPT.getStatus());
        order.setOrderStatus(OrderStatus.STATUS_DISPOSE_AFTERSALE.getStatus());
        if (orderService.updateVersionSelective(order) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }
        return ResponseUtil.ok();
    }

    /**
     * 售后审核拒绝
     * @param aftersale 售后
     * @return 成功
     */
    public Object reject(LitemallAftersale aftersale) {
        //判断售后状态
        if(!aftersale.getStatus().equals(AftersaleStatus.STATUS_REQUEST.getStatus())){
            throw new RuntimeException("当前售后状态不能进行审核拒绝操作");
        }

        LitemallOrder order = commonService.findOrderById(aftersale.getUserId(), aftersale.getOrderId());
        if (order == null) {
            throw new RuntimeException("未找到售后订单="+aftersale.getOrderId());
        }

        aftersale.setStatus(AftersaleStatus.STATUS_REJECT.getStatus());
        aftersale.setHandleTime(LocalDateTime.now());
        if (aftersaleService.updateVersionSelective(aftersale) == 0) {
            throw new RuntimeException("网络繁忙，请刷新重试");
        }

        //订单也要更新售后状态
        order.setAftersaleStatus(AftersaleStatus.STATUS_REJECT.getStatus());
        order.setOrderStatus(OrderStatus.STATUS_REJECT_AFTERSALE.getStatus());
        if (orderService.updateVersionSelective(order) == 0){
            throw new RuntimeException("网络繁忙，请刷新重试");
        }
        return ResponseUtil.ok();
    }





}
