package org.click.carservice.wx.model.brand.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BrandSaveBody implements Serializable {

    /**
     * 品牌商表ID
     */
    private String id;
    /**
     * 用户表的用户ID
     */
    private String userId;
    /**
     * 品牌商名称
     */
    @NotNull(message = "店铺名称不能为空")
    private String name;
    /**
     * 品牌商简介
     */
    @NotNull(message = "店铺简介不能为空")
    private String depict;
    /**
     * 品牌商邮箱
     */
    @NotNull(message = "店铺邮箱不能为空")
    private String mail;
    /**
     * 品牌商页的品牌商图片
     */
    @NotNull(message = "店铺图片不能为空")
    private String picUrl;
    /**
     * 品牌商的商品低价，仅用于页面展示
     */
    @NotNull(message = "店铺底价不能为空")
    private BigDecimal floorPrice;
    /**
     * 真实姓名
     */
    @NotNull(message = "真实姓名不能为空")
    private String trueName;
    /**
     * 自提地址
     */
    @NotNull(message = "自提地址不能为空")
    private String address;
    /**
     * 更新版本号
     */
    private Integer version;

}
