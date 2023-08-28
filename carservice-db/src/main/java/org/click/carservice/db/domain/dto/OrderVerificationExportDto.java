package org.click.carservice.db.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.click.carservice.db.annotation.Excel;
import org.click.carservice.db.enums.OrderStatus;
import org.click.carservice.db.poi.ExcelHandlerAdapter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: OrderVerificationExportDto
 * @Author HuangYan
 * @Date: 2023/8/23 21:23
 * @Version 1.0
 * @Description: 核销单导出
 */
@Data
public class OrderVerificationExportDto {

    /**
     * 商铺名称
     */
    @Excel(name = "商铺名称")
    @ApiModelProperty("商铺名称")
    private String brandName;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    @ApiModelProperty("订单编号")
    private String orderSn;

    /**
     * 商铺/服务名称
     */
    @Excel(name = "服务名称")
    @ApiModelProperty("商铺/服务名称")
    private String goodsName;

    /**
     * 核销用户名
     */
    @Excel(name = "核销用户名")
    @ApiModelProperty("核销用户名")
    private String consignee;

    /**
     * 核销用户名电话
     */
    @Excel(name = "核销用户名电话")
    @ApiModelProperty("核销用户名电话")
    private String mobile;


    /**
     * 核销时间
     */
//    @Excel(name = "核销时间")
    @ApiModelProperty("核销时间")
    private LocalDateTime verificationTime;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态", handler = OrderStatusExcelHandlerAdapter.class)
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @ApiModelProperty("订单状态文本")
    private String orderStatusText;

    /**
     * 商品总费用
     */
    @Excel(name = "商品总费用")
    @ApiModelProperty("商品总费用")
    private BigDecimal goodsPrice;

    /**
     * 优惠券减免
     */
    @Excel(name = "优惠券减免")
    @ApiModelProperty("优惠券减免")
    private BigDecimal couponPrice;

    /**
     * 订单提交时总费用
     */
    @Excel(name = "订单提交时总费用")
    @ApiModelProperty("订单提交时总费用")
    private BigDecimal orderPrice;

    /**
     * 微信付款编号
     */
    @Excel(name = "微信付款编号")
    @ApiModelProperty("微信付款编号")
    private String payId;

    /**
     * 微信付款时间
     */
    @Excel(name = "微信付款时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("微信付款时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    public static class OrderStatusExcelHandlerAdapter implements ExcelHandlerAdapter {

        @Override
        public String format(Object value, String[] args) {
            return OrderStatus.parseValue(Integer.valueOf(String.valueOf(value)));
        }
    }

}

