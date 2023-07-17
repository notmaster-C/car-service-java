package org.click.carservice.core.baidu.service;


import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.click.carservice.core.baidu.result.AipPicSecCheckResult;
import org.click.carservice.core.baidu.result.AipTextSecCheckResult;
import org.click.carservice.core.utils.JacksonUtil;
import org.click.carservice.core.utils.RegexUtil;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * 百度内容审核
 */
public class BaibuAipSecCheckService {

    /**
     * 是否开启审核
     */
    private static final Boolean isAipContent = false;

    /**
     * 内容安全审核
     */
    private static AipContentCensor aipContentCensor;

    /**
     * 内容安全审核
     */
    public static AipContentCensor aipContentCensor() {
        String APP_ID = "xxxxxxxxx";
        String API_KEY = "xxxxxxxxxx";
        String SECRET_KEY = "xxxxxxxxxxxx";
        if (aipContentCensor == null) {
            aipContentCensor = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        }
        return aipContentCensor;
    }

    /**
     * 百度文本审核
     *
     * @param text 文本信息
     */
    public static void textSecCheck(String text) {
        if (!StringUtils.hasText(text) || text.length() > 2000 || !isAipContent) {
            return;
        }
        JSONObject jsonObject = aipContentCensor().textCensorUserDefined(text);
        String body = jsonObject.toString();
        System.out.println(body);
        AipTextSecCheckResult result = JacksonUtil.parseObject(body, AipTextSecCheckResult.class);
        if (result == null) {
            throw new RuntimeException("接口调用失败");
        }
        if (Objects.equals(2, result.getConclusionType())) {
            for (AipTextSecCheckResult.Data data : result.getData()) {
                throw new RuntimeException(data.getMsg());
            }
        }
        if (result.getErrorMsg() != null) {
            throw new RuntimeException(result.getErrorMsg());
        }
    }


    /**
     * 百度图片审核
     *
     * @param imageUrl 图片地址
     */
    public static void picSecCheck(String imageUrl) {
        if (imageUrl == null || !RegexUtil.isImage(imageUrl) || !isAipContent) {
            return;
        }
        JSONObject jsonObject = aipContentCensor().imageCensorUserDefined(imageUrl, EImgType.URL, new HashMap<>());
        String body = jsonObject.toString();
        AipPicSecCheckResult result = JacksonUtil.parseObject(body, AipPicSecCheckResult.class);
        if (result == null) {
            throw new RuntimeException("接口调用失败");
        }
        if (Objects.equals(2, result.getConclusionType())) {
            for (AipPicSecCheckResult.Data data : result.getData()) {
                throw new RuntimeException(data.getMsg());
            }
        }
        if (result.getErrorMsg() != null) {
            throw new RuntimeException(result.getErrorMsg());
        }
    }

}
