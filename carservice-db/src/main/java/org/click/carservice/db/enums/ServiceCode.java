package org.click.carservice.db.enums;

/**
 * 服务类型枚举类
 */
public enum ServiceCode {

    SERVICE_TYPE_WASH("100001", "洗车", "1"),

    SERVICE_TYPE_MAINTENANCE("100002", "维修", "2"),

    SERVICE_TYPE_UPKEEP("100003", "保养", "3");


    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String depict;

    /**
     * 优惠券id
     */
    private final String couponId;


    ServiceCode(String code, String depict, String couponId) {
        this.code = code;
        this.depict = depict;
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public String getDepict() {
        return depict;
    }

    public String getCouponId() {
        return couponId;
    }

    /**
     * 根据code获取描述
     * @param code
     * @return
     */
    public String parseValue(String code) {
        if (code != null) {
            for (ServiceCode item : values()) {
                if (item.code.equals(code)) {
                    return item.depict;
                }
            }
        }
        throw new IllegalStateException("code不支持");
    }

    /**
     * 根据描述获取code
     * @param depict
     * @return
     */
    public static String parseCode(String depict) {
        if (depict != null) {
            for (ServiceCode item : values()) {
                if (item.depict.equals(depict)) {
                    return item.code;
                }
            }
        }
        throw new IllegalStateException("depict不支持");
    }

    public static String parseCouponId(String code) {
        if (code != null) {
            for (ServiceCode item : values()) {
                if (item.code.equals(code)) {
                    return item.couponId;
                }
            }
        }
        throw new IllegalStateException("code不支持");
    }
}
