package org.click.carservice.admin.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.click.carservice.admin.service.AdminCarService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.entity.PageResult;
import org.click.carservice.db.entity.car.UserCarBody;
import org.click.carservice.db.entity.car.UserCarResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: AdminCarController
 * @Author HuangYan
 * @Version 1.0
 * @Description: 管理端-车牌Controller
 */
@RestController
@RequestMapping("/admin/car")
@Api(value = "管理端-车牌Controller", tags = "管理端-车牌Controller")
public class AdminCarController {

    @Autowired
    private AdminCarService carService;

    @GetMapping("/list")
    @ApiOperation(value = "所有车牌信息")
    public ResponseUtil<PageResult<UserCarResult>> list(UserCarBody body){
        return ResponseUtil.okList(carService.selectUserCarPage(body));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "用户id查询用户车辆牌照信息")
    public ResponseUtil<List<CarServiceCar>> list(@PathVariable String userId) {
        CarServiceCar carServiceCar = new CarServiceCar().setUserId(userId);
        return ResponseUtil.ok(carService.selectCarServiceCarList(carServiceCar));
    }

}
