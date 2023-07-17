package org.click.carservice.wx.web;
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
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.aftersale.body.AftersaleListBody;
import org.click.carservice.wx.web.impl.WxWebAftersaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 售后服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/aftersale")
@Validated
public class WxAftersaleController {


    @Autowired
    private WxWebAftersaleService aftersaleService;


    /**
     * 售后列表
     */
    @GetMapping("list")
    public Object list(@LoginUser String userId, AftersaleListBody body) {
        return aftersaleService.list(userId , body);
    }

    /**
     * 售后详情
     * @param orderId 订单ID
     * @return 售后详情
     */
    @GetMapping("detail")
    public Object detail(@LoginUser String userId, @NotNull String orderId) {
        return aftersaleService.detail(userId , orderId);
    }

    /**
     * 申请售后
     * @param userId   用户ID
     * @param aftersale 用户售后信息
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser String userId, @Valid @RequestBody CarServiceAfterSale aftersale) {
        return aftersaleService.submit(userId , aftersale);
    }

    /**
     * 取消售后
     * @param userId   用户ID
     * @param aftersale 用户售后信息
     * @return 操作结果
     */
    @PostMapping("cancel")
    public Object cancel(@LoginUser String userId, @Valid @RequestBody CarServiceAfterSale aftersale) {
        return aftersaleService.cancel(userId , aftersale);
    }

    /**
     * 审核通过
     */
    @PostMapping("/recept")
    public Object recept(@Valid @RequestBody CarServiceAfterSale aftersale) {
        return aftersaleService.recept(aftersale);
    }


    /**
     * 审核驳回
     */
    @PostMapping("/reject")
    public Object reject(@Valid @RequestBody CarServiceAfterSale aftersale) {
        return aftersaleService.reject(aftersale);
    }

    /**
     * 售后退款
     */
    @PostMapping("/refund")
    public Object refund(@Valid @RequestBody CarServiceAfterSale aftersale) {
        return aftersaleService.refund(aftersale);
    }

}