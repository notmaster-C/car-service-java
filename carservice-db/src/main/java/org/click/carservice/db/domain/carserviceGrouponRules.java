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
 * 团购规则表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("carservice_groupon_rules")
public class carserviceGrouponRules implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 团购规则表ID
     */
    @TableId("`id`")
    private String id;
    /**
     * 商品表的商品ID
     */
    @TableField("`goods_id`")
    private String goodsId;
    /**
     * 商品名称
     */
    @TableField("`goods_name`")
    private String goodsName;
    /**
     * 商品图片或者商品货品图片
     */
    @TableField("`pic_url`")
    private String picUrl;
    /**
     * 优惠金额
     */
    @TableField("`discount`")
    private BigDecimal discount;
    /**
     * 团购人数
     */
    @TableField("`discount_member`")
    private Integer discountMember;
    /**
     * 团购过期时间
     */
    @TableField("`expire_time`")
    private LocalDateTime expireTime;
    /**
     * 团购规则状态，正常上线则0，到期自动下线则1，管理手动下线则2
     */
    @TableField("`status`")
    private Short status;
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
     * 团购规则表ID
     */
    public static final String ID = "`id`";
    /**
     * 商品表的商品ID
     */
    public static final String GOODS_ID = "`goods_id`";
    /**
     * 商品名称
     */
    public static final String GOODS_NAME = "`goods_name`";
    /**
     * 商品图片或者商品货品图片
     */
    public static final String PIC_URL = "`pic_url`";
    /**
     * 优惠金额
     */
    public static final String DISCOUNT = "`discount`";
    /**
     * 团购人数
     */
    public static final String DISCOUNT_MEMBER = "`discount_member`";
    /**
     * 团购过期时间
     */
    public static final String EXPIRE_TIME = "`expire_time`";
    /**
     * 团购规则状态，正常上线则0，到期自动下线则1，管理手动下线则2
     */
    public static final String STATUS = "`status`";
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
