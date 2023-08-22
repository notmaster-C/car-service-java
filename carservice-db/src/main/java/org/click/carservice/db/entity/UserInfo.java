package org.click.carservice.db.entity;
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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息
 *
 * @author click
 */
@Data
@ApiModel(value = "微信用户信息", description = "微信用户信息")
public class UserInfo implements Serializable {

    /**
     * 微信openid
     */
    @ApiModelProperty("微信openid")
    private String openId;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 用户类型: 0:普通用户，1:商户
     */
    @ApiModelProperty("用户类型: 0:普通用户，1:商户")
    private Integer userType;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatarUrl;
    /**
     * 用户手机号码
     */
    @ApiModelProperty("用户手机号码")
    private String mobile;
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String trueName;
    /**
     * 国家
     */
    @ApiModelProperty("国家")
    private String country;
    /**
     * 省份
     */
    @ApiModelProperty("省份")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty("城市")
    private String city;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private Byte gender;
    /**
     * 语言
     */
    @ApiModelProperty("语言")
    private String language;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private LocalDate birthday;
    /**
     * uni登陆ID
     */
    @ApiModelProperty("uni登陆ID")
    private String unionId;
    /**
     * 个人分享图片
     */
    @ApiModelProperty("个人分享图片")
    private String shareUrl;


}
