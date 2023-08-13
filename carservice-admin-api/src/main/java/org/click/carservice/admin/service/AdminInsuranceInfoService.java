package org.click.carservice.admin.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.poi.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.db.domain.CarServiceCar;
import org.click.carservice.db.domain.CarServiceInsuranceInfo;
import org.click.carservice.db.domain.CarServiceInsuranceService;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.enums.CarPropertiesEnum;
import org.click.carservice.db.enums.CarTypeEnum;
import org.click.carservice.db.enums.EngineTypeEnum;
import org.click.carservice.db.enums.UserGender;
import org.click.carservice.db.service.impl.CarServiceInsuranceInfoServiceImpl;
import org.click.carservice.db.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 保单信息Service业务层处理
 * 
 * @author huangYan
 * @date 2023-08-04
 */
@Service
@Slf4j
public class AdminInsuranceInfoService extends CarServiceInsuranceInfoServiceImpl {

    private final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final ExecutorService EXECUTOR = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AdminCarService carService;

    @Autowired
    private AdminInsuranceService insuranceService;

    /**
     * 解析导入保单信息
     * @param file
     * @return
     * @throws Exception
     */
    public String importInsuranceData(MultipartFile file) throws Exception {
        HashSet<String> msg = new HashSet<>();
        HashMap<String, CarServiceInsuranceInfo> dataMap = new HashMap<>();
        ExcelReader reader = cn.hutool.poi.excel.ExcelUtil.getReader(file.getInputStream());
        int rowCount = reader.getRowCount();
        int startRowIndex = 2;
        for (int i = startRowIndex; i < rowCount; i++) {
            List<Object> readRow = reader.readRow(i);
            String key = String.valueOf(readRow.get(0)) + readRow.get(1);
            // 判断缓存是否存在某一保单信息，不存在解析后新增，存在则解析保单服务项存入保单信息
            if (!dataMap.containsKey(key)) {
                // 创建保单信息
                CarServiceInsuranceInfo carServiceInsuranceInfo = new CarServiceInsuranceInfo();
                // 创建保单服务项
                CarServiceInsuranceService insuranceServiceImportBody = new CarServiceInsuranceService();
                // 解析保单信息
                // 手机号获取用户id
                String mobil = String.valueOf(readRow.get(0));
                CarServiceUser carServiceUser = userService.selectUserByMobil(mobil);
                // 不存在用户则新增
                if (ObjectUtil.isNull(carServiceUser)) {
                    carServiceUser = new CarServiceUser();
                    carServiceUser.setUsername(mobil);
                    carServiceUser.setMobile(mobil);
                    carServiceUser.setTrueName(String.valueOf(readRow.get(1)));
                    carServiceUser.setGender(UserGender.parseCode(String.valueOf(readRow.get(2))));
                    carServiceUser.setBirthday(LocalDateTimeUtil.of((Date) readRow.get(3)));
                    String nickName = "用户" + RandomUtil.randomString(4);
                    carServiceUser.setNickName(nickName);
                    userService.save(carServiceUser);
                    msg.add("新增用户：" + nickName + "；");
                }
                carServiceInsuranceInfo.setUserId(carServiceUser.getId());
                // 车牌号获取车牌id
                String carNumber = String.valueOf(readRow.get(4));
                CarServiceCar car = carService.selectCarByCarNumber(carNumber);
                // 无车牌信息则新增
                if (ObjectUtil.isNull(car)) {
                    car = new CarServiceCar();
                    car.setUserId(carServiceUser.getId());
                    car.setCarNumber(carNumber);
                    car.setCarType(CarTypeEnum.parseCode(String.valueOf(readRow.get(5))));
                    car.setEngineType(EngineTypeEnum.parseCode(String.valueOf(readRow.get(6))));
                    car.setCarModel(String.valueOf(readRow.get(7)));
                    car.setCarProperties(CarPropertiesEnum.parseCode(String.valueOf(readRow.get(8))));
                    car.setCarAge(Long.valueOf(String.valueOf(readRow.get(9))));
                    carService.save(car);
                    msg.add("新增车牌号：" + carNumber + "；");
                }
                carServiceInsuranceInfo.setCarId(car.getId());
                carServiceInsuranceInfo.setInsureUser(String.valueOf(readRow.get(10)));
                carServiceInsuranceInfo.setInsureUserPhone(String.valueOf(readRow.get(11)));
                carServiceInsuranceInfo.setInsureNum(String.valueOf(readRow.get(12)));
                carServiceInsuranceInfo.setInsureCompany(String.valueOf(readRow.get(13)));
                carServiceInsuranceInfo.setInsureTime(DATEFORMAT.parse(String.valueOf(readRow.get(14))));
                carServiceInsuranceInfo.setInsureEndTime(DATEFORMAT.parse(String.valueOf(readRow.get(15))));
                // 解析保单服务项
                insuranceServiceImportBody.setServiceName(String.valueOf(readRow.get(16)));
                insuranceServiceImportBody.setServiceCode(String.valueOf(readRow.get(17)));
                insuranceServiceImportBody.setServiceTotal(Long.valueOf(String.valueOf(readRow.get(18))));
                // 构建数据
                List<CarServiceInsuranceService> carServiceInsuranceServiceList = new ArrayList<>();
                carServiceInsuranceServiceList.add(insuranceServiceImportBody);
                carServiceInsuranceInfo.setCarServiceInsuranceServiceList(carServiceInsuranceServiceList);
                // 存入缓存
                dataMap.put(key, carServiceInsuranceInfo);
            } else {
                CarServiceInsuranceInfo carServiceInsuranceInfo = dataMap.get(key);
                CarServiceInsuranceService carServiceInsuranceService = new CarServiceInsuranceService();
                carServiceInsuranceService.setServiceName(String.valueOf(readRow.get(16)));
                carServiceInsuranceService.setServiceCode(String.valueOf(readRow.get(17)));
                carServiceInsuranceService.setServiceTotal(Long.valueOf(String.valueOf(readRow.get(18))));
                carServiceInsuranceInfo.getCarServiceInsuranceServiceList().add(carServiceInsuranceService);
            }
        }
        // 解析完成后，获取所有的保单信息进行入库
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);
        StringBuffer sb = new StringBuffer();
        List<CarServiceInsuranceInfo> finalData = new ArrayList<>(dataMap.values());
        finalData.forEach(data -> {
            try {
                // 保存保单信息和保单服务项
                insertCarServiceInsuranceInfo(data);
                // 通过保单服务项生成优惠券，并保存
                EXECUTOR.submit(() -> insuranceService.insertCouponByInsuranceInfo(data));
                success.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
                String em = "已存在保单信息，投保单业务流水号: " + data.getInsureNum() + ";";
                sb.append(em);
                fail.getAndIncrement();
            }
        });
        String info = msg.stream()
                .collect(Collectors.joining(System.lineSeparator()));
        log.info("导入保险信息成功： {}", info );
        return "导入成功：" + success + "条, 失败：" + fail + "条，失败原因：" + sb;
    }
}
