package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.click.carservice.db.handler.JsonStringArrayTypeHandler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车商品表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_cart")
public class CarServiceCart implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 购物车表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 用户表的用户ID
     */
    @TableField("`user_id`")
    private String userId;
    /**
     * 商品表的商品ID
     */
    @TableField("`goods_id`")
    private String goodsId;
    /**
     * 商品编号
     */
    @TableField("`goods_sn`")
    private String goodsSn;
    /**
     * 商品名称
     */
    @TableField("`goods_name`")
    private String goodsName;
    /**
     * 商品货品表的货品ID
     */
    @TableField("`product_id`")
    private String productId;
    /**
     * 品牌id
     */
    @TableField("`brand_id`")
    private String brandId;
    /**
     * 品牌商名称
     */
    @TableField("`brand_name`")
    private String brandName;
    /**
     * 商品货品的价格
     */
    @TableField("`price`")
    private BigDecimal price;
    /**
     * 商品货品的数量
     */
    @TableField("`number`")
    private Short number;
    /**
     * 商品规格值列表，采用JSON数组格式
     */
    @TableField(value = "`specifications`", typeHandler = JsonStringArrayTypeHandler.class)
    private String[] specifications;
    /**
     * 购物车中商品是否选择状态
     */
    @TableField("`checked`")
    private Boolean checked;
    /**
     * 商品图片或者商品货品图片
     */
    @TableField("`pic_url`")
    private String picUrl;
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
     * 购物车表ID
     */
    public static final String ID = "`id`";
    /**
     * 用户表的用户ID
     */
    public static final String USER_ID = "`user_id`";
    /**
     * 商品表的商品ID
     */
    public static final String GOODS_ID = "`goods_id`";
    /**
     * 商品编号
     */
    public static final String GOODS_SN = "`goods_sn`";
    /**
     * 商品名称
     */
    public static final String GOODS_NAME = "`goods_name`";
    /**
     * 商品货品表的货品ID
     */
    public static final String PRODUCT_ID = "`product_id`";
    /**
     * 品牌id
     */
    public static final String BRAND_ID = "`brand_id`";
    /**
     * 品牌商名称
     */
    public static final String BRAND_NAME = "`brand_name`";
    /**
     * 商品货品的价格
     */
    public static final String PRICE = "`price`";
    /**
     * 商品货品的数量
     */
    public static final String NUMBER = "`number`";
    /**
     * 商品规格值列表，采用JSON数组格式
     */
    public static final String SPECIFICATIONS = "`specifications`";
    /**
     * 购物车中商品是否选择状态
     */
    public static final String CHECKED = "`checked`";
    /**
     * 商品图片或者商品货品图片
     */
    public static final String PIC_URL = "`pic_url`";
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
