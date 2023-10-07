package org.click.carservice.wx.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.web.impl.WxWebCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 微信-车牌控制器
 */
@RestController
@RequestMapping("/wx/car")
@Validated
@Slf4j
@Api(value = "微信-车牌", tags = "微信-车牌")
public class WxCarController extends CarServiceCarServiceImpl {

    @Autowired
    private WxWebCarService carService;

    @GetMapping("/list")
    @ApiOperation(value = "用户id查询用户所拥有的车辆牌照信息")
    public Object list(@LoginUser String userId) {
        return carService.list(userId);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "id查询车牌详情")
    public Object detail(@LoginUser String userId, @RequestBody String id) {
        return carService.detail(userId, id);
    }
    /**
     * 添加或更新收货地址
     * @param userId  用户ID
     * @param car 车辆信息
     * @return 添加或更新操作结果
     */
    @PostMapping("/save")
    public Object save(@LoginUser String userId, @Valid @RequestBody CarServiceCar car) {
        return carService.save(userId , car);
    }

    /**
     * 删除用户车牌信息
     */
    @PostMapping("/delete")
    @ApiOperation("删除用户车牌信息")
    public ResponseUtil remove(@LoginUser String userId, @RequestBody String id) {
        return ResponseUtil.ok(carService.delete(userId, id));
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
