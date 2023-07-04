package org.ysling.litemall.core.system;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import org.ysling.litemall.core.redis.cache.RedisCacheService;
import org.ysling.litemall.core.tenant.handler.TenantContextHolder;
import org.ysling.litemall.core.utils.BeanUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置
 * @author Ysling
 */
public class SystemConfig implements Serializable {

    private static final String CONFIG_PREFIX = "SYSTEM_CONFIG:";

    /**将系统配置根据租户写入redis*/
    public static void updateConfigs(Map<String, String> data) {
        HashMap<Object, Object> hashMap = new HashMap<>(data);
        String tenantId = TenantContextHolder.getLocalTenantId();
        RedisCacheService.putAllHash(CONFIG_PREFIX + tenantId ,hashMap);
    }

    /**从redis中获取租户配置*/
    private static String getConfig(String keyName) {
        String tenantId = TenantContextHolder.getLocalTenantId();
         String hash = (String) RedisCacheService.getHash(CONFIG_PREFIX + tenantId, keyName);
         if (hash == null){
             BeanUtil.getBean(SystemStartupRunner.class).initConfig();
         }
        return (String) RedisCacheService.getHash(CONFIG_PREFIX + tenantId , keyName);
    }

    /**将从redis获取的对象转为Integer*/
    private static Integer getConfigInt(String keyName) {
        return Integer.parseInt(getConfig(keyName));
    }

    /**将从redis获取的对象转为Boolean*/
    private static Boolean getConfigBoolean(String keyName) {
        return Boolean.valueOf(getConfig(keyName));
    }

    /**将从redis获取的对象转为BigDecimal*/
    private static BigDecimal getConfigBigDec(String keyName) {
        return new BigDecimal(getConfig(keyName));
    }

    /**订单服务费比例 % */
    public static BigDecimal getOrderBrokerage() {
        return getConfigBigDec(SystemConstant.MALL_ORDER_BROKERAGE.getName());
    }

    /**商品可设置的最小金额 单位元*/
    public static Integer getGoodsMinAmount() {
        return getConfigInt(SystemConstant.MALL_GOODS_MIN_AMOUNT.getName());
    }

    /**商品可设置的最大金额 单位元*/
    public static Integer getGoodsMaxAmount() {
        return getConfigInt(SystemConstant.MALL_GOODS_MAX_AMOUNT.getName());
    }

    /**订单支付最小金额 单位元*/
    public static Integer getOrderMinAmount() {
        return getConfigInt(SystemConstant.MALL_ORDER_MIN_AMOUNT.getName());
    }

    /**订单支付最大金额 单位元*/
    public static Integer getOrderMaxAmount() {
        return getConfigInt(SystemConstant.MALL_ORDER_MAX_AMOUNT.getName());
    }

    /**首页新品商品展示数量*/
    public static Integer getNewLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_NEW.getName());
    }

    /**首页热卖商品展示数量*/
    public static Integer getHotLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_HOT.getName());
    }

    /**首页懒加载商品展示数量*/
    public static Integer getAllLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_ALL.getName());
    }

    /**首页优惠券展示数量*/
    public static Integer getCouponLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_COUPON.getName());
    }

    /**首页赏金商品展示数量*/
    public static Integer getRewardLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_REWARD.getName());
    }

    /**首页赏金商品展示数量*/
    public static Integer getGrouponLimit() {
        return getConfigInt(SystemConstant.WX_INDEX_GROUPON.getName());
    }

    /**使用微信支付*/
    public static boolean isAutoPay() {
        return getConfigBoolean(SystemConstant.WX_SYSTEM_PAY.getName());
    }

    /**自动创建朋友圈分享图*/
    public static boolean isAutoCreateShareImage() {
        return getConfigBoolean(SystemConstant.WX_SYSTEM_SHARE.getName());
    }

    /**运费金额 单位元*/
    public static BigDecimal getFreightValue() {
        return getConfigBigDec(SystemConstant.EXPRESS_FREIGHT_VALUE.getName());
    }

    /**运费减免最小金额 单位元*/
    public static BigDecimal getFreightMin() {
        return getConfigBigDec(SystemConstant.EXPRESS_FREIGHT_MIN.getName());
    }

    /**支付超时时间 单位分钟*/
    public static Integer getOrderUnpaid() {
        return getConfigInt(SystemConstant.ORDER_UNPAID.getName());
    }

    /**确认收货超时时间 单位天*/
    public static Integer getOrderUnconfirmed() {
        return getConfigInt(SystemConstant.ORDER_UNCONFIRMED.getName());
    }

    /**评论超时时间 单位天*/
    public static Integer getOrderComment() {
        return getConfigInt(SystemConstant.ORDER_COMMENT.getName());
    }

    /**商城名称*/
    public static String getMallName() {
        return getConfig(SystemConstant.MALL_NAME.getName());
    }

    /**商城地址*/
    public static String getMallAddress() {
        return getConfig(SystemConstant.MALL_ADDRESS.getName());
    }

    /**商城联系电话*/
    public static String getMallPhone() {
        return getConfig(SystemConstant.MALL_PHONE.getName());
    }

    /**商城QQ交流群*/
    public static String getMallQQ() {
        return getConfig(SystemConstant.MALL_QQ.getName());
    }

    /**商城经度*/
    public static String getMallLongitude() {
        return getConfig(SystemConstant.MALL_LONGITUDE.getName());
    }

    /**商城纬度*/
    public static String getMallLatitude() {
        return getConfig(SystemConstant.MALL_LATITUDE.getName());
    }


}