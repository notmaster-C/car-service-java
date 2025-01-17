package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户浏览足迹表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_footprint")
public class CarServiceFootprint implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户浏览足迹表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 用户表的用户ID
     */
    @TableField("`user_id`")
    private String userId;
    /**
     * 浏览商品ID
     */
    @TableField("`goods_id`")
    private String goodsId;
    /**
     * 商品名称
     */
    @TableField("`name`")
    private String name;
    /**
     * 商品简介
     */
    @TableField("`brief`")
    private String brief;
    /**
     * 商品页面商品图片
     */
    @TableField("`pic_url`")
    private String picUrl;
    /**
     * 零售价格
     */
    @TableField("`retail_price`")
    private BigDecimal retailPrice;
    /**
     * 创建时间
     */
    @TableField(value = "`add_time`", fill = FieldFill.INSERT)
    private LocalDateTime addTime;
    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 逻辑删除
     */
    @TableField("`deleted`")
    @TableLogic
    private Boolean deleted;
    /**
     * 租户ID，用于分割多个租户
     */
    @TableField("`tenant_id`")
    private String tenantId;
    /**
     * 更新版本号
     */
    @TableField("`version`")
    @Version
    private Integer version;

    /////////////////////////////////
    // 数据库字段常量
    ////////////////////////////////

    /**
     * 用户浏览足迹表ID
     */
    public static final String ID = "`id`";
    /**
     * 用户表的用户ID
     */
    public static final String USER_ID = "`user_id`";
    /**
     * 浏览商品ID
     */
    public static final String GOODS_ID = "`goods_id`";
    /**
     * 商品名称
     */
    public static final String NAME = "`name`";
    /**
     * 商品简介
     */
    public static final String BRIEF = "`brief`";
    /**
     * 商品页面商品图片
     */
    public static final String PIC_URL = "`pic_url`";
    /**
     * 零售价格
     */
    public static final String RETAIL_PRICE = "`retail_price`";
    /**
     * 创建时间
     */
    public static final String ADD_TIME = "`add_time`";
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "`update_time`";
    /**
     * 逻辑删除
     */
    public static final String DELETED = "`deleted`";
    /**
     * 租户ID，用于分割多个租户
     */
    public static final String TENANT_ID = "`tenant_id`";
    /**
     * 更新版本号
     */
    public static final String VERSION = "`version`";
}
