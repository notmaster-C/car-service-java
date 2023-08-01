package org.click.carservice.admin.service;

import org.click.carservice.db.service.impl.CarServiceCarServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "car_service_car")
public class AdminCarService extends CarServiceCarServiceImpl {
}
