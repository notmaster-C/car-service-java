package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 品牌商表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_brand")
@ApiModel("品牌商表")
public class CarServiceBrand implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 品牌商表ID
     */
    @TableId("`id`")
    @ApiModelProperty("品牌商表ID")
    private String id;
    /**
     * 用户表的用户ID
     */
    @TableField("`user_id`")
    @ApiModelProperty("用户表的用户ID")
    private String userId;
    /**
     * 品牌商名称
     */
    @TableField("`name`")
    @ApiModelProperty("品牌商名称")
    private String name;
    /**
     * 品牌商简介
     */
    @TableField("`depict`")
    @ApiModelProperty("品牌商简介")
    private String depict;
    /**
     * 品牌商邮箱
     */
    @TableField("`mail`")
    @ApiModelProperty("邮箱")
    private String mail;
    /**
     * 品牌商页的品牌商图片
     */
    @TableField("`pic_url`")
    @ApiModelProperty("图片地址")
    private String picUrl;
    /**
     * 自提地址
     */
    @TableField("`address`")
    @ApiModelProperty("自提地址")
    private String address;
    /**
     * 品牌商的商品低价，仅用于页面展示
     */
    @TableField("`floor_price`")
    @ApiModelProperty("品牌商的商品低价，仅用于页面展示")
    private BigDecimal floorPrice;
    /**
     * 访问量
     */
    @TableField("`look_count`")
    @ApiModelProperty("访问量")
    private Long lookCount;
    /**
     * 点赞量
     */
    @TableField("`like_count`")
    @ApiModelProperty("点赞量")
    private Long likeCount;
    /**
     * 评论数量
     */
    @TableField("`comment_count`")
    @ApiModelProperty("评论数量")
    private Long commentCount;
    /**
     * 0 可用, 1 禁用, 2 注销,3 待审核
     */
    @TableField("`status`")
    @ApiModelProperty("0 可用, 1 禁用, 2 注销, 3待审核")
    private Byte status;
    /**
     * 权重用于排序
     */
    @TableField("`weight`")
    @ApiModelProperty("权重用于排序")
    private Integer weight;
    /**
     * 创建时间
     */
    @TableField(value = "`add_time`", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime addTime;
    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    /**
     * 逻辑删除
     */
    @TableField("`deleted`")
    @TableLogic
    @ApiModelProperty("逻辑删除")
    private Boolean deleted;
    /**
     * 租户ID，用于分割多个租户
     */
    @TableField("`tenant_id`")
    @ApiModelProperty("租户ID，用于分割多个租户")
    private String tenantId;
    /**
     * 更新版本号
     */
    @TableField("`version`")
    @Version
    @ApiModelProperty("更新版本号")
    private Integer version;
    /**
     * 店铺经度
     */
    @TableField("`latitude`")
    @ApiModelProperty("店铺经度")
    private Float latitude;
    /**
     * 店铺纬度
     */
    @TableField("`longitude`")
    @ApiModelProperty("店铺纬度")
    private Float longitude;

    /////////////////////////////////
    // 数据库字段常量
    ////////////////////////////////

    /**
     * 品牌商表ID
     */
    public static final String ID = "`id`";
    /**
     * 用户表的用户ID
     */
    public static final String USER_ID = "`user_id`";
    /**
     * 品牌商名称
     */
    public static final String NAME = "`name`";
    /**
     * 品牌商简介
     */
    public static final String DEPICT = "`depict`";
    /**
     * 品牌商邮箱
     */
    public static final String MAIL = "`mail`";
    /**
     * 品牌商页的品牌商图片
     */
    public static final String PIC_URL = "`pic_url`";
    /**
     * 自提地址
     */
    public static final String ADDRESS = "`address`";
    /**
     * 品牌商的商品低价，仅用于页面展示
     */
    public static final String FLOOR_PRICE = "`floor_price`";
    /**
     * 访问量
     */
    public static final String LOOK_COUNT = "`look_count`";
    /**
     * 点赞量
     */
    public static final String LIKE_COUNT = "`like_count`";
    /**
     * 评论数量
     */
    public static final String COMMENT_COUNT = "`comment_count`";
    /**
     * 0 可用, 1 禁用, 2 注销
     */
    public static final String STATUS = "`status`";
    /**
     * 权重用于排序
     */
    public static final String WEIGHT = "`weight`";
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
