package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.express.model.ExpressInfo;
import org.click.carservice.core.express.service.ExpressService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 物流查询接口
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebExpressService {

    @Autowired
    private ExpressService expressService;
    @Autowired
    private WxOrderService orderService;


    /**
     * 通过快递鸟查询物流
     * @param userId 用户id
     * @param orderSn 订单编号
     * @return waybillToken
     */
    public Object apiTrack(@LoginUser String userId, String orderSn){
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        // 订单信息
        CarServiceOrder order = orderService.findBySn(userId, orderSn);
        if (order == null) {
            return ResponseUtil.fail("订单不存在");
        }

        Map<String, Object> result = new HashMap<>();
        // 订单状态为已发货且物流信息不为空
        // "YTO", "800669400640887922"
        if (StringUtils.hasText(order.getShipSn())) {
            ExpressInfo ei = expressService.getExpressInfo(order.getShipChannel(), order.getShipSn());
            result.put("expressInfo", ei);
        } else {
            return ResponseUtil.fail("暂无物流信息");
        }
        return ResponseUtil.ok(result);
    }

    /**
     * 查询物流公司
     * @return 物流公司
     */
    public Object channel() {
        return ResponseUtil.ok(expressService.getVendors());
    }

}
