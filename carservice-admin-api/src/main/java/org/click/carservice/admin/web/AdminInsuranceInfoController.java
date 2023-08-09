package org.click.carservice.admin.web;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.service.AdminInsuranceInfoService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.entity.PageResult;
import org.click.carservice.db.entity.car.CarServiceInsuranceInfoParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 保单信息Controller
 * 
 * @author huangYan
 * @date 2023-08-04
 */
@RestController
@RequestMapping("/info")
@Api(value = "管理端-保单信息", tags = "管理端-保单信息")
public class AdminInsuranceInfoController
{
    @Autowired
    private AdminInsuranceInfoService carServiceInsuranceInfoService;

    /**
     * 查询保单信息列表
     */
//    @SaCheckPermission("admin:insurance:list")
    @RequiresPermissionsDesc(menu = {"保险管理", "保险信息"}, button = "查询")
    @GetMapping("/list")
    @ApiOperation("查询保单信息列表")
    public ResponseUtil<PageResult<CarServiceInsuranceInfo>> list(CarServiceInsuranceInfoParam carServiceInsuranceInfo)
    {
        return  ResponseUtil.okList(carServiceInsuranceInfoService.selectCarServiceInsuranceInfoList(carServiceInsuranceInfo));
    }

    /**
     * 获取保单信息详细信息
     */
//    @SaCheckPermission("admin:insurance:read")
    @RequiresPermissionsDesc(menu = {"保险管理", "保险信息"}, button = "详情")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取保单信息详细信息")
    public ResponseUtil<CarServiceInsuranceInfo> getInfo(@PathVariable("id") String id)
    {
        return ResponseUtil.ok(carServiceInsuranceInfoService.selectCarServiceInsuranceInfoById(id));
    }

    /**
     * 新增保单信息
     */
//    @SaCheckPermission("admin:insurance:creat")
    @RequiresPermissionsDesc(menu = {"保险管理", "保险信息"}, button = "新增")
    @PostMapping("/add")
    @ApiOperation("新增保单信息")
    public ResponseUtil add(@RequestBody CarServiceInsuranceInfo carServiceInsuranceInfo)
    {
        return ResponseUtil.ok(carServiceInsuranceInfoService.insertCarServiceInsuranceInfo(carServiceInsuranceInfo));
    }

    /**
     * 修改保单信息
     */
    @SaCheckPermission("admin:insurance:update")
    @RequiresPermissionsDesc(menu = {"保险管理", "保险信息"}, button = "修改")
    @PostMapping("/update")
    @ApiOperation("修改保单信息")
    public ResponseUtil edit(@RequestBody CarServiceInsuranceInfo carServiceInsuranceInfo)
    {
        return ResponseUtil.ok(carServiceInsuranceInfoService.updateCarServiceInsuranceInfo(carServiceInsuranceInfo));
    }
}
