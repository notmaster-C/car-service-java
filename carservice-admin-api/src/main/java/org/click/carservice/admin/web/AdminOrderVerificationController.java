package org.click.carservice.admin.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceOrderVerification;
import org.click.carservice.db.entity.PageResult;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 核销Controller
 * 
 * @author ruoyi
 * @date 2023-08-23
 */
@RestController
@RequestMapping("admin/verification")
@Api(value = "管理端-核销", tags = "管理端-核销")
public class AdminOrderVerificationController
{
    @Autowired
    private ICarServiceOrderVerificationService orderVerificationService;

    /**
     * 查询核销列表
     */
    @GetMapping("/list")
    @ApiOperation("查询核销列表")
    public ResponseUtil<PageResult<CarServiceOrderVerification>> list(CarServiceOrderVerification carServiceOrderVerification)
    {
        return ResponseUtil.okList(orderVerificationService.selectCarServiceOrderVerificationList(carServiceOrderVerification));
    }

    /**
     * 获取核销详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取核销详细信息")
    public ResponseUtil getInfo(@PathVariable("id") String id)
    {
        return ResponseUtil.ok(orderVerificationService.findById(id));
    }

    /**
     * 修改核销
     */
    @PutMapping
    @ApiOperation("修改核销")
    public ResponseUtil edit(@RequestBody CarServiceOrderVerification carServiceOrderVerification)
    {
        return ResponseUtil.ok(orderVerificationService.updateSelective(carServiceOrderVerification));
    }

    /**
     * 删除核销
     */
	@DeleteMapping("/{id}")
    @ApiOperation("删除核销")
    public ResponseUtil remove(@PathVariable String id)
    {
        return ResponseUtil.ok(orderVerificationService.deleteById(id));
    }
}
