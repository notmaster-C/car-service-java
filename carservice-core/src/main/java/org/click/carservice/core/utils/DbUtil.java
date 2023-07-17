package org.click.carservice.core.utils;

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

import java.io.IOException;

/**
 * 数据库备份
 */
public class DbUtil {

    /**数据库连接地址*/
    private static final String MYSQL_PATH = "localhost";
    /**数据库端口*/
    private static final String MYSQL_PORT = "3306";

    /**
     * 备份本地数据库
     * @param filePath  文件地址
     * @param username  用户名
     * @param password  密码
     * @param dbname    数据库名称
     * @return 成功true:失败false
     */
    public static boolean backup(String filePath, String username, String password, String dbname) {
        // 备份命令
        StringBuilder cmd = new StringBuilder()
                .append("mysqldump")
                .append(" -h")
                .append(MYSQL_PATH)
                .append(" -P")
                .append(MYSQL_PORT)
                .append(" -u")
                .append(username)
                .append(" -p")
                .append(password)
                .append(" ")
                .append(dbname)
                .append(" > ")
                .append(filePath);

        try {
            return runtimeExec(cmd);
        } catch (Exception e) {
            throw new RuntimeException("数据库备份失败");
        }
    }


    /**
     * 恢复数据库
     * @param filePath  文件地址
     * @param username  用户名
     * @param password  密码
     * @param dbname    数据库名
     * @return 成功true:失败false
     */
    public static boolean load(String filePath, String username, String password, String dbname) {
        StringBuilder cmd = new StringBuilder()
                .append("mysql ")
                .append(" -h")
                .append(MYSQL_PATH)
                .append(" -P")
                .append(MYSQL_PORT)
                .append(" -u")
                .append(username)
                .append(" -p")
                .append(password)
                .append(" ")
                .append(dbname)
                .append(" < ")
                .append(filePath);

        try {
            return runtimeExec(cmd);
        } catch (Exception e) {
            throw new RuntimeException("数据库恢复失败");
        }
    }

    /**
     * 执行系统命令
     * @param cmd 命令
     * @return 成功or失败
     * @throws IOException  IOException
     * @throws InterruptedException  IOException
     */
    private static boolean runtimeExec(StringBuilder cmd) throws IOException, InterruptedException {
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command;
        if (isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"bash", "-c", String.valueOf(cmd)};
        }
        //获取Runtime实例 执行恢复指令
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    /**
     * 判断操作系统类型、Linux|Windows
     */
    public static boolean isSystem(String osName) {
        Boolean flag = null;
        if (osName.startsWith("windows")) {
            flag = true;
        } else if (osName.startsWith("linux")) {
            flag = false;
        }
        return Boolean.TRUE.equals(flag);
    }

}