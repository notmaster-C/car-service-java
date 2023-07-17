package org.click.carservice.core.service;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.storage.service.StorageService;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.db.domain.carserviceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 获取网络资源并存人本地对象存储
 *
 * @author click
 */
@Slf4j
@Service
public class StorageCoreService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private ThreadPoolExecutor executorService;

    /**
     * 上传并文本中所有未存储图片
     *
     * @param text 文本
     * @return 替换后文本
     */
    public String uploadTextFile(String text) {
        //获取文本中src中的资源url
        List<String> urlList = RegexUtil.getTextUrlList(text);
        //异步任务map
        HashMap<String, FutureTask<String>> taskMap = new HashMap<>();
        //遍历资源url
        for (String urlPath : urlList) {
            if (urlPath.contains(storageService.getStoragePath())) {
                continue;
            }
            FutureTask<String> urlTask = new FutureTask<>(() -> getStorageUrl(urlPath));
            executorService.submit(urlTask);
            taskMap.put(urlPath, urlTask);
        }

        //获取异步任务返回值并替换原url
        for (String urlPath : taskMap.keySet()) {
            try {
                //本地对象存储替换原url
                String storageUrl = taskMap.get(urlPath).get();
                if (storageUrl != null) {
                    text = text.replace(urlPath, storageUrl);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.info("网络资源获取失败:" + urlPath);
            }
        }
        return text;
    }


    public String uploadStorageUrl(String urlPath) {
        FutureTask<String> urlTask = new FutureTask<>(() -> getStorageUrl(urlPath));
        executorService.submit(urlTask);
        try {
            return urlTask.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将网络资源存入本地对象存储
     *
     * @param urlPath 网络资源
     * @return 本地对象存储地址
     */
    public String getStorageUrl(String urlPath) {
        try {
            URL url = new URL(urlPath);
            //url地址，不带域名，不带参数
            String path = url.getPath();
            if (path.contains("&")) {
                path = path.substring(0, path.indexOf("&"));
                String protocol = url.getProtocol();
                String host = url.getHost();
                String port = url.getPort() == -1 ? "" : ":" + url.getPort();
                url = new URL(protocol + "://" + host + port + path);
            }

            //利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();

            //文件名称
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            //网络输入流
            InputStream inputStream = conn.getInputStream();
            //输入流大小
            int contentLength = conn.getContentLength();
            //文件类型
            String contentType = path.substring(path.lastIndexOf(".") + 1);
            carserviceStorage storage = storageService.store(inputStream, contentLength, contentType, fileName);
            return storage.getUrl();
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
