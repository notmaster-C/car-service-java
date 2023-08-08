package org.click.carservice.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 保险服务项-订单对象 car_service_insurance_service_order
 * 
 * @author huangYan
 * @date 2023-08-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("car_service_insurance_service_order")
public class CarServiceInsuranceServiceOrder {
    private static final long serialVersionUID = 1L;

    /** 保险服务项订单表id */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 保险服务项表id */
    private String insuranceServiceId;

    /** 订单表id */
    private String orderId;

}
