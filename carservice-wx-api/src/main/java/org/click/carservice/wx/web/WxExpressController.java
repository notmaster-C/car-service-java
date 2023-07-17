package org.click.carservice.wx.web;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.click.carservice.core.express.model.ExpressInfo;
import org.click.carservice.core.express.service.ExpressService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.weixin.service.LogisticsPluginService;
import org.click.carservice.db.domain.carserviceOrder;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 物流查询接口
 *
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/express")
@Validated
public class WxExpressController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    private WxOrderService orderService;
    @Autowired
    private LogisticsPluginService logisticsPluginService;


    /**
     * 通过快递鸟查询物流
     *
     * @param userId  用户id
     * @param orderSn 订单编号
     * @return waybillToken
     */
    @GetMapping("api-track")
    public Object apiTrack(@LoginUser String userId, String orderSn) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        // 订单信息
        carserviceOrder order = orderService.findBySn(userId, orderSn);
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
     * 获取微信物流查询插件token
     *
     * @param userId  用户id
     * @param orderSn 订单编号
     * @return waybillToken
     */
    @GetMapping("logistics")
    public Object logistics(@LoginUser String userId, String orderSn) throws WxErrorException {
        return logisticsPluginService.getWaybillToken(userId, orderSn);
    }


    /**
     * 查询物流公司
     *
     * @return 物流公司
     */
    @GetMapping("channel")
    public Object channel() {
        return ResponseUtil.ok(expressService.getVendors());
    }

}
