package org.click.carservice.wx.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.service.WxCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 微信-车牌控制器
 */
@RestController
@RequestMapping("/wx/car")
@Validated
@Slf4j
@Api(value = "微信-车牌", tags = "微信-车牌")
public class WxCarController {

    @Autowired
    private WxCarService carService;

    @GetMapping("/list")
    @ApiOperation(value = "用户id查询用户所拥有的车辆牌照信息")
    public ResponseUtil<List<CarServiceCar>> list(@LoginUser String userId) {
        return ResponseUtil.ok(carService.queryByUid(userId));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "id查询车牌详情")
    public ResponseUtil<CarServiceCar> detail(@LoginUser String userId, @RequestBody String id) {
        CarServiceCar carServiceCar = carService.detail(userId, id);
        return ResponseUtil.ok(carServiceCar);
    }

    /**
     * 新增车牌信息
     * @param carServiceCar
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "新增车牌信息")
    public ResponseUtil add(@LoginUser String userId, @RequestBody CarServiceCar carServiceCar) {
        carServiceCar.setUserId(userId);
        carService.insertCarServiceCar(carServiceCar);
        return ResponseUtil.ok();
    }

    /**
     * 修改车牌
     * @param userId
     * @param carServiceCar
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "修改车牌信息")
    public ResponseUtil edit(@LoginUser String userId, @RequestBody CarServiceCar carServiceCar) {
        carService.edit(userId, carServiceCar);
        return ResponseUtil.ok();
    }

    /**
     * 删除用户车牌信息
     */
    @PostMapping("/delete")
    @ApiOperation("删除用户车牌信息")
    public ResponseUtil remove(@LoginUser String userId, @RequestBody String id) {
        return ResponseUtil.ok(carService.deleteCarServiceCarById(userId, id));
    }

    /**
     * 设置默认牌照
     * @param userId
     * @param id
     * @return
     */
    @PostMapping("/setDefault")
    @ApiOperation("设置默认牌照")
    public ResponseUtil<Integer> setDefaultCar(@LoginUser String userId, @RequestBody String id){
        return ResponseUtil.ok(carService.setDefaultCar(userId, id));
    }

}
