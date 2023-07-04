package org.ysling.litemall.wx.web;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.utils.RegexUtil;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallAddress;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.ysling.litemall.wx.service.WxAddressService;
import org.ysling.litemall.wx.service.WxTenantService;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.wx.service.WxUserService;
import org.ysling.litemall.wx.service.GetRegionService;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 用户收货地址服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/address")
@Validated
public class WxAddressController extends GetRegionService {

	@Autowired
	private WxAddressService addressService;
	@Autowired
    private WxTenantService tenantService;
	@Autowired
	private WxUserService userService;


	/**
	 * 用户收货地址列表
	 *
	 * @param userId 用户ID
	 * @return 收货地址列表
	 */
	@GetMapping("list")
	public Object list(@LoginUser String userId) {
		return ResponseUtil.okList(addressService.queryByUid(userId));
	}

	/**
	 * 获取租户地址
	 *
	 * @param userId 用户ID
	 * @return 收货地址列表
	 */
	@GetMapping("tenant")
	public Object getAddress(@LoginUser String userId) {
		LitemallUser user = userService.findById(userId);
		if (user == null){
			return ResponseUtil.fail("用户获取失败，请重新登陆");
		}
		return ResponseUtil.ok(tenantService.findById(user.getTenantId()));
	}

	/**
	 * 收货地址详情
	 *
	 * @param userId 用户ID
	 * @param id     收货地址ID
	 * @return 收货地址详情
	 */
	@GetMapping("detail")
	public Object detail(@LoginUser String userId, @JsonBody String id) {
		if (Objects.isNull(userId)) {
			return ResponseUtil.unlogin();
		}
		LitemallAddress address = addressService.query(userId, id);
		if (address == null) {
			return ResponseUtil.fail("地址获取失败");
		}
		return ResponseUtil.ok(address);
	}

	/**
	 * 添加或更新收货地址
	 *
	 * @param userId  用户ID
	 * @param address 用户收货地址
	 * @return 添加或更新操作结果
	 */
	@PostMapping("save")
	public Object save(@LoginUser String userId, @Valid @RequestBody LitemallAddress address) {
		if (Objects.isNull(userId)) {
			return ResponseUtil.unlogin();
		}
		Object error = validate(address);
		if (error != null) {
			return error;
		}
		if (address.getId() == null || "0".equals(address.getId())) {
			if (address.getIsDefault()) {
				// 重置其他收货地址的默认选项
				addressService.resetDefault(userId);
			}
			address.setId(null);
			address.setUserId(userId);
			addressService.add(address);
		} else {
			LitemallAddress litemallAddress = addressService.query(userId, address.getId());
			if (litemallAddress == null) {
				return ResponseUtil.badArgumentValue();
			}
			if (address.getIsDefault()) {
				// 重置其他收货地址的默认选项
				addressService.resetDefault(userId);
			}
			address.setUserId(userId);
			if (addressService.updateSelective(address) <= 0){
				throw new RuntimeException("网络繁忙，请刷新重试");
			}
		}
		return ResponseUtil.ok(address.getId());
	}

	/**
	 * 删除收货地址
	 *
	 * @param userId  用户ID
	 * @param id 	  用户收货地址ID
	 */
	@PostMapping("delete")
	public Object delete(@LoginUser String userId, @JsonBody String id) {
		if (Objects.isNull(userId)) {
			return ResponseUtil.unlogin();
		}
		addressService.deleteByUser(userId, id);
		return ResponseUtil.ok();
	}


	private Object validate(LitemallAddress address) {
		String name = address.getName();
		if (!StringUtils.hasText(name)) {
			return ResponseUtil.badArgument();
		}

		// 测试收货手机号码是否正确
		String mobile = address.getMobile();
		if (!StringUtils.hasText(mobile) || !RegexUtil.isMobileSimple(mobile)) {
			return ResponseUtil.badArgument();
		}

		String province = address.getProvince();
		if (Objects.isNull(province)) {
			return ResponseUtil.badArgument();
		}

		String city = address.getCity();
		if (Objects.isNull(city)) {
			return ResponseUtil.badArgument();
		}

		String county = address.getCounty();
		if (Objects.isNull(county)) {
			return ResponseUtil.badArgument();
		}

		String areaCode = address.getAreaCode();
		if (Objects.isNull(areaCode)) {
			return ResponseUtil.badArgument();
		}

		String addressDetail = address.getAddressDetail();
		if (!StringUtils.hasText(addressDetail)) {
			return ResponseUtil.badArgument();
		}

		String addressAll = address.getAddressAll();
		if (!StringUtils.hasText(addressAll)) {
			return ResponseUtil.badArgument();
		}

		Boolean isDefault = address.getIsDefault();
		if (Objects.isNull(isDefault)) {
			return ResponseUtil.badArgument();
		}
		return null;
	}


}