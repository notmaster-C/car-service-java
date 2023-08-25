package org.click.carservice.admin.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.dto.OrderVerificationExportDto;
import org.click.carservice.db.domain.query.OrderVerificationQuery;
import org.click.carservice.db.entity.PageResult;
import org.click.carservice.db.poi.ExcelUtil;
import org.click.carservice.db.service.ICarServiceOrderVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @title: AdminStatementsController
 * @Author HuangYan
 * @Date: 2023/8/25 20:06
 * @Version 1.0
 * @Description: 商铺对账单
 */
@RestController
@RequestMapping("admin/statement")
@Api(value = "管理端-商铺对账单", tags = "管理端-商铺对账单")
public class AdminStatementsController {

    @Autowired
    private ICarServiceOrderVerificationService orderVerificationService;

    /**
     * 对账单列表查询
     * @param query
     * @return
     */
    @PostMapping("/listOrder")
    @ApiOperation("对账单列表查询")
    public ResponseUtil<PageResult<OrderVerificationExportDto>> list(@RequestBody OrderVerificationQuery query) {
        return ResponseUtil.okList(orderVerificationService.exportOrderVerification(query));
    }

    /**
     * 导出对账单
     */
    @PostMapping("/export")
    @ApiOperation("导出对账单")
    public void export(HttpServletResponse response, OrderVerificationQuery query)
    {
        List<OrderVerificationExportDto> list = orderVerificationService.exportOrderVerification(query);
        ExcelUtil<OrderVerificationExportDto> util = new ExcelUtil<OrderVerificationExportDto>(OrderVerificationExportDto.class);
        util.exportExcel(response, list, "核销数据");
    }
}
