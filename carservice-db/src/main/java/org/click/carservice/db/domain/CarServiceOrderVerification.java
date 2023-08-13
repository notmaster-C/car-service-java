package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author click
 * @since 2023-08-13
 */
@Getter
@Setter
@TableName("car_service_order_verification")
public class CarServiceOrderVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 核销表ID
     */
    @TableId("id")
    private String id;

    /**
     * 用户表的用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 商品表的商品ID
     */
    @TableField("goods_id")
    private String goodsId;

    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;

    /**
     * 商户订单号
     */
    @TableField("storage_id")
    private String storageId;

    /**
     * 投保订单号
     */
    @TableField("toubao_sn")
    private String toubaoSn;

    /**
     * 核销具体地址
     */
    @TableField("address")
    private String address;

    /**
     * 核销时间
     */
    @TableField("verification_time")
    private LocalDateTime verificationTime;

    /**
     * 创建时间
     */
    @TableField(value = "add_time", fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 订单状态
     */
    @TableField("order_status")
    private Short orderStatus;

    /**
     * 更新版本号
     */
    @TableField("version")
    @Version
    private Integer version;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String GOODS_ID = "goods_id";

    public static final String ORDER_SN = "order_sn";

    public static final String STORAGE_ID = "storage_id";

    public static final String TOUBAO_SN = "toubao_sn";

    public static final String ADDRESS = "address";

    public static final String VERIFICATION_TIME = "verification_time";

    public static final String ADD_TIME = "add_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String ORDER_STATUS = "order_status";

    public static final String VERSION = "version";
}
