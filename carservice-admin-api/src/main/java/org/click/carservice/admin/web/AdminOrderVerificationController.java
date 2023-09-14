package org.click.carservice.admin.web;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;

import org.click.carservice.db.domain.dto.OrderVerificationExportDto;
import org.click.carservice.db.domain.query.OrderVerificationQuery;
import org.click.carservice.db.poi.ExcelUtil;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceOrderVerification;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 核销Controller
 * 
 * @author ruoyi
 * @date 2023-08-23
 */
@RestController
@RequestMapping("admin/verification")
@Api(value = "核销", tags = "核销")
public class AdminOrderVerificationController
{
    @Autowired
    private ICarServiceOrderVerificationService orderVerificationService;

    /**
     * 查询核销列表
     */
    @GetMapping("/list")
    @ApiOperation("查询核销列表")
    public ResponseUtil list(CarServiceOrderVerification carServiceOrderVerification)
    {
        return ResponseUtil.okList(orderVerificationService.selectCarServiceOrderVerificationList(carServiceOrderVerification));
    }

    /**
     * 导出核销列表
     */
    @PostMapping("/export")
    @ApiOperation("导出核销列表")
    public void export(HttpServletResponse response, OrderVerificationQuery query)
    {
        List<OrderVerificationExportDto> list = orderVerificationService.exportOrderVerification(query);
        ExcelUtil<OrderVerificationExportDto> util = new ExcelUtil<OrderVerificationExportDto>(OrderVerificationExportDto.class);
        util.exportExcel(response, list, "核销数据");
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
