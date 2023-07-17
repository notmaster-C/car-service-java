package org.click.carservice.core.system;

import java.io.Serializable;

/**
 * 系统默认配置枚举
 *
 * @author click
 */
public enum SystemConstant implements Serializable {

    /////////////////////////////////
    //       系统配置
    ////////////////////////////////

    MALL_GOODS_MIN_AMOUNT("mall_goods_min_amount", "1", "商品可设置的最小金额"),
    MALL_GOODS_MAX_AMOUNT("mall_goods_max_amount", "1000", "商品可设置的最大金额"),
    MALL_ORDER_MIN_AMOUNT("mall_order_min_amount", "1", "订单支付最小金额"),
    MALL_ORDER_MAX_AMOUNT("mall_order_max_amount", "1000", "订单支付最大金额"),
    MALL_ORDER_BROKERAGE("mall_order_brokerage", "1", "订单服务费比例"),

    /////////////////////////////////
    //       首页配置
    ////////////////////////////////

    WX_SYSTEM_PAY("wx_system_pay", "true", "使用微信支付"),
    WX_SYSTEM_SHARE("wx_system_share", "true", "自动创建朋友圈分享图"),
    WX_INDEX_NEW("wx_index_new", "20", "首页新品条数"),
    WX_INDEX_HOT("wx_index_hot", "20", "首页热卖条数"),
    WX_INDEX_ALL("wx_index_all", "20", "首页分页商品条数"),
    WX_INDEX_COUPON("wx_index_coupon", "3", "首页优惠券展示条数"),
    WX_INDEX_REWARD("wx_index_reward", "4", "首页赏金商品条数"),
    WX_INDEX_GROUPON("wx_index_groupon", "4", "首页团购商品条数"),

    /////////////////////////////////
    //       运费配置
    ////////////////////////////////

    EXPRESS_FREIGHT_VALUE("express_freight_value", "0", "运费金额"),
    EXPRESS_FREIGHT_MIN("express_freight_min", "0", "运费减免最小金额"),

    /////////////////////////////////
    //       订单配置
    ////////////////////////////////

    ORDER_UNPAID("order_unpaid", "30", "支付超时时间"),
    ORDER_UNCONFIRMED("order_unconfirmed", "7", "确认收货超时时间"),
    ORDER_COMMENT("order_comment", "7", "评论超时时间"),

    /////////////////////////////////
    //       商城配置
    ////////////////////////////////

    MALL_NAME("mall_name", "天天云游市场", "商城名称"),
    MALL_ADDRESS("mall_address", "贵阳", "商城地址"),
    MALL_PHONE("mall_phone", "185-8567-5204", "商城联系电话"),
    MALL_QQ("mall_qq", "927069313", "商城QQ交流群"),
    MALL_LONGITUDE("mall_longitude", "121.587839", "商城经度"),
    MALL_LATITUDE("mall_latitude", "31.201900", "商城纬度");

    /**
     * 配置名
     */
    private final String name;
    /**
     * 配置值
     */
    private final String value;
    /**
     * 配置描述
     */
    private final String depict;

    /**
     * 配置名
     */
    public String getName() {
        return name;
    }

    /**
     * 配置值
     */
    public String getValue() {
        return value;
    }

    /**
     * 描述
     */
    public String getDepict() {
        return depict;
    }

    /**
     * 构造方法
     */
    SystemConstant(String name, String value, String depict) {
        this.name = name;
        this.value = value;
        this.depict = depict;
    }

    /**
     * 根据名称获取值
     *
     * @param name 状态
     * @return 返回描述
     */
    public static String parseDepict(String name) {
        if (name != null) {
            for (SystemConstant item : values()) {
                if (item.name.equals(name)) {
                    return item.depict;
                }
            }
        }
        return "";
    }

}
