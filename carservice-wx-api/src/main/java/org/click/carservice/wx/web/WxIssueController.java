package org.click.carservice.wx.web;
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
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.wx.model.issue.body.IssueListBody;
import org.click.carservice.wx.web.impl.WxWebIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 常见问题服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/issue")
@Validated
public class WxIssueController {

    @Autowired
    private WxWebIssueService issueService;

    /**
     * 帮助中心
     */
    @GetMapping("/list")
    public Object list(IssueListBody body) {
        return issueService.list(body);
    }

}
