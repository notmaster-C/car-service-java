package org.click.carservice.db.enums;
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

import java.io.Serializable;

/**
 * 优惠券过期类型
 *
 * @author click
 */
public enum CouponTimeType implements Serializable {

    /////////////////////////////////
    //       优惠券过期类型
    ////////////////////////////////

    /**
     * 过期类型基于领取时间的有效天数days
     */
    TIME_TYPE_DAYS((short) 0, "过期类型基于领取时间的有效天数"),
    /**
     * 过期类型基于start_time和end_time
     */
    TIME_TYPE_TIME((short) 1, "过期类型基于start_time和end_time");


    /**
     * 状态
     */
    private final Short status;
    /**
     * 描述
     */
    private final String depict;

    /**
     * 状态
     */
    public Short getStatus() {
        return status;
    }

    /**
     * 描述
     */
    public String getDepict() {
        return depict;
    }


    CouponTimeType(Short status, String depict) {
        this.status = status;
        this.depict = depict;
    }

    /**
     * 状态判断
     */
    public Boolean equals(Short status) {
        return this.getStatus().equals(status);
    }


    /**
     * 根据状态获取描述
     *
     * @param status 状态
     * @return 返回描述
     */
    public static String parseValue(Short status) {
        if (status != null) {
            for (CouponTimeType item : values()) {
                if (item.status.equals(status)) {
                    return item.depict;
                }
            }
        }
        throw new IllegalStateException("status不支持");
    }

}
