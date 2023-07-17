package org.click.carservice.core.weixin.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.db.domain.carserviceTenant;
import org.click.carservice.db.service.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author click
 */
@Slf4j
@Component
public class WxStartupRunner {

    @Autowired
    private WxMaConfig wxMaConfig;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private ITenantService tenantService;


    @PostConstruct
    public void init() {
        log.info("初始化 -> [初始化多租户微信配置]");
        //添加默认小程序配置
        WxMaConfigHolder.remove();
        wxMaService.addConfig(WxMaConfigHolder.get(), wxMaConfig);
        List<carserviceTenant> tenantList = tenantService.list();
        //初始化多租户微信配置
        for (carserviceTenant tenant : tenantList) {
            this.addWxConfig(tenant);
        }
    }

    /**
     * 添加微信配置
     *
     * @param tenant 租户
     */
    public void addWxConfig(carserviceTenant tenant) {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(tenant.getAppId());
        config.setSecret(tenant.getAppSecret());
        config.setToken(tenant.getToken());
        config.setAesKey(tenant.getAesKey());
        config.setMsgDataFormat("XML");
        //添加小程序配置
        wxMaService.addConfig(tenant.getAppId(), config);
    }

    /**
     * 根据appid删除wx配置
     *
     * @param appid 小程序appid
     */
    public void remove(String appid) {
        wxMaService.removeConfig(appid);
    }

}
