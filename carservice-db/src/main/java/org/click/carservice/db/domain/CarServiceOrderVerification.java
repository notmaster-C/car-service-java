package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.click.carservice.db.annotation.Excel;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.db.validator.order.Order;
import org.click.carservice.db.validator.sort.Sort;

import java.time.LocalDateTime;

/**
 * 核销对象 car_service_order_verification
 *
 * @author ruoyi
 * @date 2023-08-23
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_service_order_verification")
@ApiModel(value = "核销", description = "核销")
public class CarServiceOrderVerification {

    private static final long serialVersionUID=1L;

    /** 核销表自增ID */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /** 用户id */
    @Excel(name = "用户id" )
    @ApiModelProperty("用户id")
    private String userId;

    /** 商品id */
    @Excel(name = "商品id" )
    @ApiModelProperty("商品id")
    private String goodsId;

    /** 订单编号 */
    @Excel(name = "订单编号" )
    @ApiModelProperty("订单编号")
    private String orderSn;

    /** 文件表ID */
    @Excel(name = "文件表ID" )
    @ApiModelProperty("文件表ID")
    private String storageId;

    /** 投保订单号 */
    @Excel(name = "投保订单号" )
    @ApiModelProperty("投保订单号")
    private String toubaoSn;

    /** 核销地址 */
    @Excel(name = "核销地址" )
    @ApiModelProperty("核销地址")
    private String address;

    /** 核销时间 */
    @Excel(name = "核销时间" )
    @ApiModelProperty("核销时间")
    private LocalDateTime verificationTime;

    /** 创建时间 */
    @Excel(name = "创建时间" )
    @ApiModelProperty("创建时间")
    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除" )
    @ApiModelProperty("逻辑删除")
    private Integer deleted;

    /** 租户ID，用于分割多个租户 */
    @Excel(name = "租户ID，用于分割多个租户" )
    @ApiModelProperty("租户ID，用于分割多个租户")
    private String tenantId;

    /** 更新版本号 */
    @Excel(name = "更新版本号" )
    @ApiModelProperty("更新版本号")
    private Long version;

    /**
     * 分页页码  (默认：1)
     */
    @TableField(exist = false)
    private Integer page = 1;

    /**
     * 查询数量  (默认：10) (0:查询全部)
     */
    @TableField(exist = false)
    private Integer limit = 10;

}