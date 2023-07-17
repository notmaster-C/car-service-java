package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.wx.model.issue.body.IssueListBody;
import org.click.carservice.wx.service.WxIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 常见问题服务
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebIssueService {

    @Autowired
    private WxIssueService issueService;

    /**
     * 帮助中心
     */
    public Object list(IssueListBody body) {
        return ResponseUtil.okList(issueService.querySelective(body));
    }

}
