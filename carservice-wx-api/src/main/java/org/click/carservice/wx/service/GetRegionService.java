package org.click.carservice.wx.service;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import org.click.carservice.db.domain.carserviceRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市区查询
 * @author click
 */
@Service
public class GetRegionService {

    @Autowired
    private WxRegionService regionService;

    private static List<carserviceRegion> regionList;

    protected List<carserviceRegion> getRegions() {
        if (regionList == null) {
            createRegion();
        }
        return regionList;
    }

    private synchronized void createRegion() {
        if (regionList == null) {
            regionList = regionService.getAll();
        }
    }
}
