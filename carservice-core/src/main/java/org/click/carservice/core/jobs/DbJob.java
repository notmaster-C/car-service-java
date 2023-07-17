package org.click.carservice.core.jobs;
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
import org.click.carservice.core.service.ActionLogService;
import org.click.carservice.core.utils.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据库定时备份任务
 * 在backup文件夹中备份最近七日的数据库文件
 * @author click
 */
@Slf4j
@Component
public class DbJob {

    @Autowired
    private Environment environment;
    @Autowired
    private ActionLogService actionLogService;
    /**声明需要格式化的格式(日期加时间)*/
    private final DateTimeFormatter dfDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");

    /**
     * 每个小时备份一次。
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void backup() throws IOException, MessagingException {
        String username = environment.getProperty("spring.datasource.dynamic.datasource.master.username");
        String password = environment.getProperty("spring.datasource.dynamic.datasource.master.password");
        String url = environment.getProperty("spring.datasource.dynamic.datasource.master.url");
        if (url == null || username == null || password == null) {
            return;
        }
        String fileName = dfDateTime.format(LocalDateTime.now()) + ".sql";
        File file = new File("backup/", fileName);
        //判断文件夹是否存在如果不存在则创建
        if (!file.getParentFile().mkdirs()) {
            log.info(file.getPath() + "文件夹已存在");
        }
        if (!file.createNewFile()) {
            log.info(file.getPath() + "文件已存在");
        }

        String dbUrl = url.replace("jdbc:mysql://", "");
        // 获取数据库名称
        String dbName = dbUrl.substring(dbUrl.indexOf("/") + 1, dbUrl.indexOf("?"));
        // 备份今天数据库
        if (DbUtil.backup(file.getPath(), username, password, dbName)) {
            actionLogService.logGeneralSucceed("系统处理定时任务", "数据库备份");
        } else {
            actionLogService.logGeneralFail("系统处理定时任务", "数据库备份");
        }

        // 删除3天前数据库备份文件
        String fileBeforeName = dfDateTime.format(LocalDateTime.now().minusDays(3)) + ".sql";
        File fileBefore = new File("backup", fileBeforeName);
        if (fileBefore.exists()) {
            //删除3天前数据库备份文件
            if (!fileBefore.delete()) {
                actionLogService.logGeneralSucceed("系统处理定时任务", "数据库备份 -> [文件删除失败]");
            }
        }
    }

}
