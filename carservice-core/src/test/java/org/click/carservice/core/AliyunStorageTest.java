package org.click.carservice.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.click.carservice.core.storage.storage.AliyunStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class AliyunStorageTest {

    private final Log logger = LogFactory.getLog(AliyunStorageTest.class);
    @Autowired
    private AliyunStorage aliyunStorage;

    @Test
    public void test() throws IOException {
        String test = getClass().getClassLoader().getResource("carservice.png").getFile();
        File testFile = new File(test);
        aliyunStorage.store(new FileInputStream(test), testFile.length(), "image/png", "carservice.png");
        Resource resource = aliyunStorage.loadAsResource("carservice.png");
        String url = aliyunStorage.generateUrl("carservice.png");
        logger.info("test file " + test);
        logger.info("store file " + resource.getURI());
        logger.info("generate url " + url);
    }

}