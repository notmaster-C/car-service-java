//package org.click.carservice.core;
//
//import org.click.carservice.core.notify.model.NotifyType;
//import org.click.carservice.core.notify.service.NotifyMobileService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.task.SyncTaskExecutor;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.concurrent.Executor;
//
///**
// * 测试短信发送服务
// * <p>
// * 注意carserviceNotifyService采用异步线程操作
// * 因此测试的时候需要睡眠一会儿，保证任务执行
// * <p>
// * 开发者需要确保：
// * 1. 在腾讯云短信平台设置短信签名和短信模板notify.properties已经设置正确
// * 2. 在腾讯云短信平台设置短信签名和短信模板
// * 3. 在当前测试类设置好正确的手机号码
// */
//@WebAppConfiguration
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SmsTest {
//
//    @Autowired
//    private NotifyMobileService mobileService;
//
//    @Test
//    public void testCaptcha() {
//        String phone = "xxxxxxxxxxx";
//        String[] params = new String[]{"123456"};
//
//        mobileService.notifySmsTemplate(phone, NotifyType.CAPTCHA, null);
//    }
//
//    @Test
//    public void testPaySucceed() {
//        String phone = "xxxxxxxxxxx";
//        String[] params = new String[]{"123456"};
//
//        mobileService.notifySmsTemplate(phone, NotifyType.PAY_SUCCEED, null);
//    }
//
//    @Test
//    public void testShip() {
//        String phone = "xxxxxxxxxxx";
//        String[] params = new String[]{"123456"};
//
//        mobileService.notifySmsTemplate(phone, NotifyType.SHIP, null);
//    }
//
//    @Test
//    public void testRefund() {
//        String phone = "xxxxxxxxxxx";
//        String[] params = new String[]{"123456"};
//
//        mobileService.notifySmsTemplate(phone, NotifyType.REFUND, params);
//    }
//
//    @Configuration
//    @Import(Application.class)
//    static class ContextConfiguration {
//        @Bean
//        @Primary
//        public Executor executor() {
//            return new SyncTaskExecutor();
//        }
//    }
//}
