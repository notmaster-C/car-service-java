package org.click.carservice.admin.web;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.click.carservice.admin.service.AdminCarService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.entity.car.UserCarBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: AdminCarController
 * @Author HuangYan
 * @Version 1.0
 * @Description: 管理端-车牌Controller
 */
@RestController
@RequestMapping("/admin/car")
@Api(value = "管理端-车牌Controller", tags = "管理端-车牌Controller")
public class AdminCarController  {

    @Autowired
    private AdminCarService carService;

    @GetMapping("/list")
    @ApiOperation(value = "所有车牌信息")
    public Object list(UserCarBody body){
        return ResponseUtil.okList(carService.querySelective(body));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "用户id查询用户车辆牌照信息")
    public Object queryByUid(@PathVariable String userId) {
        if(ObjectUtils.isEmpty(userId)){
            return ResponseUtil.fail("error id");
        }
        return ResponseUtil.ok(carService.queryByUid(userId));
    }

}
