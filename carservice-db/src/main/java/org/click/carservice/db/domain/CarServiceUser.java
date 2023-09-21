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
 * 用户表
 * </p>
 *
 * @author click
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("car_service_user")
@ApiModel("用户表")
public class CarServiceUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户表ID
     */
    @TableId("`id`")
    @ApiModelProperty("用户表ID")
    private String id;
    /**
     * 邀请者
     */
    @TableField("`inviter`")
    @ApiModelProperty("邀请者")
    private String inviter;
    /**
     * 用户名称
     */
    @TableField("`username`")
    @ApiModelProperty("用户名称")
    private String username;
    /**
     * 用户密码
     */
    @TableField("`password`")
    private String password;
    /**
     * 真实姓名
     */
    @TableField("`true_name`")
    @ApiModelProperty("真实姓名")
    private String trueName;

    /**
     * 用户类型 用户类型: 0:普通用户，1:商户
     */
    @ApiModelProperty("用户类型 用户类型: 0:普通用户，1:商户")
    private Integer userType;
    /**
     * 性别：0 未知， 1男， 2女
     */
    @TableField("`gender`")
    @ApiModelProperty("性别：0 未知， 1男， 2女")
    private Byte gender;
    /**
     * 是否网约车车主
     */
    @TableField("`is_wyccz`")
    @ApiModelProperty("是否网约车车主")
    private Byte isWyccz;
    /**
     * 生日
     */
    @TableField("`birthday`")
    @ApiModelProperty("生日")
    private LocalDateTime birthday;
    /**
     * 个人分享图片
     */
    @TableField("`share_url`")
    @ApiModelProperty("个人分享图片")
    private String shareUrl;
    /**
     * 最近一次登录时间
     */
    @TableField("`last_login_time`")
    @ApiModelProperty("最近一次登录时间")
    private LocalDateTime lastLoginTime;
    /**
     * 最近一次登录IP地址
     */
    @TableField("`last_login_ip`")
    @ApiModelProperty("最近一次登录IP地址")
    private String lastLoginIp;
    /**
     * 0 普通用户，1 VIP用户，2 高级VIP用户
     */
    @TableField("`user_level`")
    @ApiModelProperty("0 普通用户，1 VIP用户，2 高级VIP用户")
    private Byte userLevel;
    /**
     * 用户积分
     */
    @TableField("`integral`")
    @ApiModelProperty("用户积分")
    private BigDecimal integral;
    /**
     * 用户昵称或网络名称
     */
    @TableField("`nick_name`")
    @ApiModelProperty("用户昵称或网络名称")
    private String nickName;
    /**
     * 用户手机号码
     */
    @TableField("`mobile`")
    @ApiModelProperty("用户手机号码")
    private String mobile;
    /**
     * 用户头像图片
     */
    @TableField("`avatar_url`")
    @ApiModelProperty("用户头像图片")
    private String avatarUrl;
    /**
     * 微信登录openid
     */
    @TableField("`openid`")
    @ApiModelProperty("微信登录openid")
    private String openid;
    /**
     * 微信登录会话KEY
     */
    @TableField("`session_key`")
    @ApiModelProperty("微信登录会话KEY")
    private String sessionKey;
    /**
     * 0 可用, 1 禁用, 2 注销
     */
    @TableField("`status`")
    @ApiModelProperty("0 可用, 1 禁用, 2 注销")
    private Byte status;
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

    /////////////////////////////////
    // 数据库字段常量
    ////////////////////////////////

    /**
     * 用户表ID
     */
    public static final String ID = "`id`";
    /**
     * 邀请者
     */
    public static final String INVITER = "`inviter`";
    /**
     * 用户名称
     */
    public static final String USERNAME = "`username`";
    /**
     * 用户密码
     */
    public static final String PASSWORD = "`password`";
    /**
     * 真实姓名
     */
    public static final String TRUE_NAME = "`true_name`";
    /**
     * 性别：0 未知， 1男， 2女
     */
    public static final String GENDER = "`gender`";
    /**
     * 生日
     */
    public static final String BIRTHDAY = "`birthday`";
    /**
     * 个人分享图片
     */
    public static final String SHARE_URL = "`share_url`";
    /**
     * 最近一次登录时间
     */
    public static final String LAST_LOGIN_TIME = "`last_login_time`";
    /**
     * 最近一次登录IP地址
     */
    public static final String LAST_LOGIN_IP = "`last_login_ip`";
    /**
     * 0 普通用户，1 VIP用户，2 高级VIP用户
     */
    public static final String USER_LEVEL = "`user_level`";
    /**
     * 用户积分
     */
    public static final String INTEGRAL = "`integral`";
    /**
     * 用户昵称或网络名称
     */
    public static final String NICK_NAME = "`nick_name`";
    /**
     * 用户手机号码
     */
    public static final String MOBILE = "`mobile`";
    /**
     * 用户头像图片
     */
    public static final String AVATAR_URL = "`avatar_url`";
    /**
     * 微信登录openid
     */
    public static final String OPENID = "`openid`";
    /**
     * 微信登录会话KEY
     */
    public static final String SESSION_KEY = "`session_key`";
    /**
     * 0 可用, 1 禁用, 2 注销
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
