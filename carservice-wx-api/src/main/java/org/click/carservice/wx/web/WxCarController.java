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

    @GetMapping("/user")
    @ApiOperation(value = "用户id查询用户所拥有的车辆牌照信息")
    public ResponseUtil<List<CarServiceCar>> list(@LoginUser String userId) {
        return ResponseUtil.ok(carService.queryByUid(userId));
    }

    /**
     * 新增车牌信息
     * @param carServiceCar
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "新增车牌信息")
    public ResponseUtil add(@LoginUser String userId, @RequestBody CarServiceCar carServiceCar) {
        carServiceCar.setUserId(userId);
        carService.insertCarServiceCar(carServiceCar);
        return ResponseUtil.ok();
    }

    /**
     * 删除用户车牌信息
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户车牌信息")
    public ResponseUtil remove(@LoginUser String userId, @PathVariable String id) {
        return ResponseUtil.ok(carService.deleteCarServiceCarById(userId, id));
    }

    /**
     * 设置默认牌照
     * @param userId
     * @param id
     * @return
     */
    @PostMapping("/setDefault/{id}")
    @ApiOperation("设置默认牌照")
    public ResponseUtil<Integer> setDefaultCar(@LoginUser String userId, @PathVariable String id){
        return ResponseUtil.ok(carService.setDefaultCar(userId, id));
    }

}
