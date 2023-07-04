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

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author Ysling
 */
@Slf4j
public class SystemInfoPrinter {

    public static final String CREATE_PART_COPPER = "XOXOXOXOX";

    private static int maxSize = 0;

    public static void printInfo(String title, Map<String, String> infos) {
        setMaxSize(infos);
        printHeader(title);
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            printLine(entry.getKey(), entry.getValue());
        }
        printEnd(title);
    }

    private static void setMaxSize(Map<String, String> infos) {
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            int size = entry.getKey().length() + entry.getValue().length();
            if (size > maxSize) {
                maxSize = size;
            }
        }

        maxSize = maxSize + 30;
    }

    private static void printHeader(String title) {
        log.info("====================" + title + "-开始" + "====================");
    }

    private static void printEnd(String title) {
        log.info("  ");
        log.info("====================" + title + "-结束" + "====================");
        log.info("  ");
    }

    private static String getLineCopper() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxSize; i++) {
            sb.append("=");
        }

        return sb.toString();
    }

    private static void printLine(String head, String line) {
        if (line == null) {
            return;
        }
        if (head.startsWith(CREATE_PART_COPPER)) {
            log.info("");
            log.info("    [[  " + line + "  ]]");
            log.info("");
        } else {
            log.info(String.format("系统配置初始化 -> [ %s -> %s ]" , head , line));
        }
    }
}
