package org.ysling.litemall.core.system;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.ysling.litemall.core.tenant.handler.TenantContextHolder;
import org.ysling.litemall.db.domain.LitemallSystem;
import org.ysling.litemall.db.domain.LitemallTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.ysling.litemall.db.service.ISystemService;
import org.ysling.litemall.db.service.ITenantService;
import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 先注入redisCacheService才注入当前类
 * 系统启动服务，用于设置系统配置信息、检查系统状态及打印系统信息
 * @author Ysling
 */
@Component
@DependsOn("redisCacheService")
public class SystemStartupRunner {

    @Autowired
    private Environment environment;
    @Autowired
    private ITenantService tenantService;
    @Autowired
    private ISystemService systemService;

    @PostConstruct
    public void init() {
        this.initConfig();
        SystemInfoPrinter.printInfo("Litemall-plus 初始化信息", getSystemInfo());
    }

    /**
     * 初始化系统配置信息
     */
    public void initConfig(){
        //系统默认配置
        Map<String, LitemallSystem> defaultConfigs = new HashMap<>();
        Map<String, String> redisConfigs = new HashMap<>();
        //初始化系统默认配置
        for (SystemConstant item : SystemConstant.values()) {
            LitemallSystem system = new LitemallSystem();
            system.setName(item.getName());
            system.setValue(item.getValue());
            system.setDepict(item.getDepict());
            defaultConfigs.put(item.getName(), system);
            redisConfigs.put(item.getName(), item.getValue());
        }

        //读取所有租户配置
        List<LitemallTenant> tenantList = tenantService.list();
        updateConfigs(defaultConfigs ,redisConfigs);

        //切换租户数据库
        for (LitemallTenant tenant :tenantList) {
            TenantContextHolder.setLocalTenantId(tenant.getId());
            updateConfigs(defaultConfigs ,redisConfigs);
        }

        //清除多租户
        TenantContextHolder.removeLocalTenantId();
        //清除多数据源
        DynamicDataSourceContextHolder.clear();
    }

    /**
     * 写入系统配置
     * @param defaultConfigs 数据库默认配置
     * @param redisConfigs  redis配置
     */
    private void updateConfigs(Map<String, LitemallSystem> defaultConfigs, Map<String, String> redisConfigs){
        List<LitemallSystem> systemList = systemService.list();
        HashMap<String, LitemallSystem> systemMap = new HashMap<>(defaultConfigs);
        for (LitemallSystem system :systemList) {
            system.setDepict(SystemConstant.parseDepict(system.getName()));
            systemMap.put(system.getName(), system);
            redisConfigs.put(system.getName(), system.getValue());
        }
        // 写入redis
        SystemConfig.updateConfigs(redisConfigs);
        // 将配置写入数据库
        initConfig(systemMap.values());
    }


    public void initConfig(Collection<LitemallSystem> systemList) {
        for (LitemallSystem system :systemList) {
            if (system.getId() == null){
                systemService.add(system);
                continue;
            }
            QueryWrapper<LitemallSystem> wrapper = new QueryWrapper<>();
            wrapper.eq(LitemallSystem.ID , system.getId());
            wrapper.eq(LitemallSystem.NAME , system.getName());
            if (!systemService.update(system, wrapper)){
                system.setId(null);
                systemService.add(system);
            }
        }
    }

    /**
     * 打印系统配置信息
     */
    private Map<String, String> getSystemInfo() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 0, "系统信息");
        // 测试获取application-db.yml配置信息
        infos.put("cpu核心数-----------", String.valueOf(Runtime.getRuntime().availableProcessors()));
        infos.put("服务器端口-----------", environment.getProperty("server.port"));
        infos.put("数据库USER-----------", environment.getProperty("spring.datasource.druid.username"));
        infos.put("数据库地址-----------", environment.getProperty("spring.datasource.druid.url"));
        infos.put("调试级别-------------", environment.getProperty("logging.level.org.ysling.litemall.wx"));

        // 测试获取application-core.yml配置信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 1, "模块状态");
        infos.put("邮件----------------", environment.getProperty("litemall.notify.mail.enable"));
        infos.put("短信----------------", environment.getProperty("litemall.notify.sms.enable"));
        infos.put("模版消息-------------", environment.getProperty("litemall.notify.wx.enable"));
        infos.put("快递信息-------------", environment.getProperty("litemall.express.enable"));
        infos.put("快递鸟ID-------------", environment.getProperty("litemall.express.appId"));
        infos.put("对象存储-------------", environment.getProperty("litemall.storage.active"));
        infos.put("本地对象存储路径-------", environment.getProperty("litemall.storage.local.storagePath"));
        infos.put("本地对象访问地址-------", environment.getProperty("litemall.storage.local.address"));
        infos.put("本地对象访问端口-------", environment.getProperty("litemall.storage.local.port"));

        // 微信相关信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 2, "微信相关");
        infos.put("微信APP KEY---------", environment.getProperty("litemall.wx.app-id"));
        infos.put("微信APP-SECRET------", environment.getProperty("litemall.wx.app-secret"));
        infos.put("微信支付MCH-ID-------", environment.getProperty("litemall.wx.mch-id"));
        infos.put("微信支付MCH-KEY------", environment.getProperty("litemall.wx.mch-key"));
        infos.put("微信支付通知地址-------", environment.getProperty("litemall.wx.notify-url"));

        //测试获取System表配置信息
        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 3, "系统设置");

        infos.put("自动创建朋友圈分享图---", Boolean.toString(SystemConfig.isAutoCreateShareImage()));
        infos.put("微信支付------------", Boolean.toString(SystemConfig.isAutoPay()));
        infos.put("商场名称------------", SystemConfig.getMallName());
        infos.put("商场地址------------", SystemConfig.getMallAddress());
        infos.put("商场经度------------", SystemConfig.getMallLatitude());
        infos.put("商场纬度------------", SystemConfig.getMallLongitude());
        infos.put("商场电话------------", SystemConfig.getMallPhone());
        infos.put("商场QQ-------------", SystemConfig.getMallQQ());

        //解决druid 日志报错：discard long time none received connection:xxx
        System.setProperty("druid.mysql.usePingMethod", "false");
        //设置http请求超时时间30分钟（单位：毫秒）
        System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(30 * 60 * 1000));
        System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(30 * 60 * 1000));
        return infos;
    }
}
