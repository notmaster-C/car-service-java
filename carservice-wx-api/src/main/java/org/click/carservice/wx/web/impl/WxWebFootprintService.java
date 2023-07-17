package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.wx.service.WxFootprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户访问足迹服务
 * @author click
 */
@Slf4j
@Service
public class WxWebFootprintService {


    @Autowired
    private WxFootprintService footprintService;

    /**
     * 用户足迹列表
     */
    public Object list(String userId, PageBody body) {
        return ResponseUtil.okList(footprintService.queryByAddTime(userId, body));
    }

    /**
     * 删除用户足迹
     * @param userId 用户ID
     * @param id   足迹ID
     * @return 删除操作结果
     */
    public Object delete(String userId, String id) {
        if (footprintService.findById(userId, id) == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (footprintService.deleteById(id) == 0){
            return ResponseUtil.fail("删除失败请重试");
        }
        return ResponseUtil.ok();
    }


}