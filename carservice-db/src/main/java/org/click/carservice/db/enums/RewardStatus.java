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
 * 赏金状态
 *
 * @author click
 */
public enum RewardStatus implements Serializable {

    /////////////////////////////////
    //       赏金参与状态
    ////////////////////////////////

    /**
     * 赏金待加入
     */
    STATUS_NONE((short) 0, "待加入"),
    /**
     * 赏金已加入
     */
    STATUS_SUCCEED((short) 1, "已加入"),
    /**
     * 赏金已取消
     */
    STATUS_CANCEL((short) 2, "已取消");


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


    RewardStatus(Short status, String depict) {
        this.status = status;
        this.depict = depict;
    }

    /**
     * 根据状态获取描述
     *
     * @param status 状态
     * @return 返回描述
     */
    public static String parseValue(Short status) {
        if (status != null) {
            for (RewardStatus item : values()) {
                if (item.status.equals(status)) {
                    return item.depict;
                }
            }
        }
        throw new IllegalStateException("status不支持");
    }
}
