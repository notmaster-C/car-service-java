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
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.system.SystemConfig;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.enums.CollectType;
import org.ysling.litemall.db.enums.GoodsStatus;
import org.ysling.litemall.db.entity.GoodsSpecificationVo;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.db.domain.*;
import org.ysling.litemall.wx.service.*;
import org.ysling.litemall.core.utils.Inheritable.InheritableCallable;
import org.ysling.litemall.wx.model.goods.body.GoodsListBody;
import org.ysling.litemall.wx.model.goods.result.GoodsCategoryResult;
import org.ysling.litemall.wx.model.goods.result.GoodsCommentResult;
import org.ysling.litemall.wx.model.goods.result.GoodsDetailResult;
import org.ysling.litemall.wx.model.goods.result.GoodsListResult;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.*;

/**
 * 商品服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/goods")
@Validated
public class WxGoodsController {

	@Autowired
	private WxGoodsService goodsService;
	@Autowired
	private WxGoodsProductService productService;
	@Autowired
	private WxIssueService goodsIssueService;
	@Autowired
	private WxGoodsAttributeService goodsAttributeService;
	@Autowired
	private WxBrandService brandService;
	@Autowired
	private WxCollectService collectService;
	@Autowired
	private WxFootprintService footprintService;
	@Autowired
	private WxCategoryService categoryService;
	@Autowired
	private WxSearchHistoryService searchHistoryService;
	@Autowired
	private WxGoodsSpecificationService goodsSpecificationService;
	@Autowired
	private WxGrouponRulesService grouponRulesService;
	@Autowired
	private WxGoodsCommentService goodsCommentService;
	@Autowired
	private ThreadPoolExecutor executorService;

	/**
	 * 商品详情
	 * <p>
	 * 用户可以不登录。
	 * 如果用户登录，则记录用户足迹以及返回用户收藏信息。
	 *
	 * @param userId 用户ID
	 * @param goodId     商品ID
	 * @return 商品详情
	 */
	@GetMapping("detail")
	public Object detail(@LoginUser(require = false) String userId, @NotNull String goodId) {
		// 商品信息
		LitemallGoods info = goodsService.findById(goodId);
		if(info == null || info.getBrandId() == null || !GoodsStatus.getIsOnSale(info)){
			return ResponseUtil.fail(600,"商品已下架");
		}

		// 评论
		FutureTask<GoodsCommentResult> commentsCallableTsk = new FutureTask<>(
			new InheritableCallable<GoodsCommentResult>(){
				@Override
				public GoodsCommentResult runTask() {
					return goodsCommentService.getComments(goodId, 0, 2);
				}
			}
		);

		// 商品属性
		FutureTask<List<LitemallGoodsAttribute>> goodsAttributeListTask = new FutureTask<>(
			new InheritableCallable<List<LitemallGoodsAttribute>>(){
				@Override
				public List<LitemallGoodsAttribute> runTask() {
					return goodsAttributeService.queryByGid(goodId);
				}
			}
		);

		// 商品规格 返回的是定制的GoodsSpecificationVo
		FutureTask<List<GoodsSpecificationVo>> specificationCallableTask = new FutureTask<>(
			new InheritableCallable<List<GoodsSpecificationVo>>(){
				@Override
				public List<GoodsSpecificationVo> runTask() {
					return goodsSpecificationService.getSpecificationVoList(goodId);
				}
			}
		);

		// 商品规格对应的数量和价格
		FutureTask<List<LitemallGoodsProduct>> productListCallableTask = new FutureTask<>(
			new InheritableCallable<List<LitemallGoodsProduct>>(){
				@Override
				public List<LitemallGoodsProduct> runTask() {
					return productService.queryByGid(goodId);
				}
			}
		);

		// 商品问题，这里是一些通用问题
		FutureTask<List<LitemallIssue>> issueCallableTask = new FutureTask<>(
			new InheritableCallable<List<LitemallIssue>>(){
				@Override
				public List<LitemallIssue> runTask() {
					return goodsIssueService.getGoodsIssue();
				}
			}
		);

		// 商品品牌商
		FutureTask<LitemallBrand> brandCallableTask = new FutureTask<>(
			new InheritableCallable<LitemallBrand>(){
				@Override
				public LitemallBrand runTask() {
					return brandService.findById(info.getBrandId());
				}
			}
		);

		//团购信息
		FutureTask<List<LitemallGrouponRules>> grouponRulesCallableTask = new FutureTask<>(
			new InheritableCallable<List<LitemallGrouponRules>>(){
				@Override
				public List<LitemallGrouponRules> runTask() {
					return grouponRulesService.queryOnByGoodsId(goodId);
				}
			}
		);

		// 用户是否收藏
		FutureTask<Boolean> collectCallableTask = new FutureTask<>(
			new InheritableCallable<Boolean>(){
				@Override
				public Boolean runTask() {
					return collectService.count(userId, CollectType.TYPE_GOODS, goodId);
				}
			}
		);

		// 记录用户的足迹
		footprintService.createFootprint(userId, info);
		executorService.submit(goodsAttributeListTask);
		executorService.submit(specificationCallableTask);
		executorService.submit(productListCallableTask);
		executorService.submit(issueCallableTask);
		executorService.submit(commentsCallableTsk);
		executorService.submit(brandCallableTask);
		executorService.submit(grouponRulesCallableTask);
		executorService.submit(collectCallableTask);


		GoodsDetailResult result = new GoodsDetailResult();
		try {
			result.setInfo(info);
			result.setShareImage(info.getShareUrl());
			result.setUserHasCollect(collectCallableTask.get());
			result.setIssue(issueCallableTask.get());
			result.setComment(commentsCallableTsk.get());
			result.setSpecificationList(specificationCallableTask.get());
			result.setProductList(productListCallableTask.get());
			result.setAttribute(goodsAttributeListTask.get());
			result.setBrand(brandCallableTask.get());
			result.setGroupon(grouponRulesCallableTask.get());
			result.setShare(SystemConfig.isAutoCreateShareImage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtil.ok(result);
	}

	/**
	 * 商品分类类目
	 *
	 * @param id 分类类目ID
	 * @return 商品分类类目
	 */
	@GetMapping("category")
	public Object category(@NotNull String id) {
		LitemallCategory currentCategory = categoryService.findById(id);
		LitemallCategory parentCategory;
		List<LitemallCategory> brotherCategory;
		if (Objects.equals(currentCategory.getPid(),"0")) {
			parentCategory = currentCategory;
			brotherCategory = categoryService.queryByPid(currentCategory.getId());
			currentCategory = brotherCategory.size() > 0 ? brotherCategory.get(0) : currentCategory;
		} else {
			parentCategory = categoryService.findById(currentCategory.getPid());
			brotherCategory = categoryService.queryByPid(currentCategory.getPid());
		}
		GoodsCategoryResult result = new GoodsCategoryResult();
		result.setCurrentCategory(currentCategory);
		result.setParentCategory(parentCategory);
		result.setBrotherCategory(brotherCategory);
		return ResponseUtil.ok(result);
	}

	/**
	 * 根据条件搜素商品
	 * @return 根据条件搜素的商品详情
	 */
	@GetMapping("list")
	public Object list(@LoginUser(require = false) String userId, GoodsListBody body) {
		//添加到搜索历史
		if (userId != null && !Objects.isNull(body.getKeyword())) {
			LitemallSearchHistory searchHistory = searchHistoryService.findByKeyword(userId, body.getKeyword());
			if (searchHistory != null){
				searchHistoryService.updateVersionSelective(searchHistory);
			} else {
				LitemallSearchHistory searchHistoryVo = new LitemallSearchHistory();
				searchHistoryVo.setKeyword(body.getKeyword());
				searchHistoryVo.setUserId(userId);
				searchHistoryVo.setFrom("wx");
				searchHistoryVo.setVersion(1);
				searchHistoryService.save(searchHistoryVo);
			}
		}

		//查询列表数据
		List<LitemallGoods> goodsList = goodsService.querySelective(body);

		// 查询商品所属类目列表。
		List<String> goodsCatIds = goodsService.getCatIds(body.getBrandId(), body.getKeyword(), body.getIsHot(), body.getIsNew());
		List<LitemallCategory> categoryList;
		if (goodsCatIds.size() != 0) {
			categoryList = categoryService.queryL2ByIds(goodsCatIds);
		} else {
			categoryList = new ArrayList<>(0);
		}

		PageInfo<LitemallGoods> pagedList = PageInfo.of(goodsList);
		GoodsListResult result = new GoodsListResult();
		result.setList(goodsList);
		result.setTotal(pagedList.getTotal());
		result.setPage(pagedList.getPageNum());
		result.setLimit(pagedList.getPageSize());
		result.setPages(pagedList.getPages());
		result.setFilterCategoryList(categoryList);
		// 因为这里需要返回额外的filterCategoryList参数，因此不能方便使用ResponseUtil.okList
		return ResponseUtil.ok(result);
	}


	/**
	 * 商品详情页面“大家都在看”推荐商品
	 * @param goodId, 商品ID
	 * @return 商品详情页面推荐商品
	 */
	@GetMapping("related")
	public Object related(@NotNull String goodId) {
		LitemallGoods goods = goodsService.findById(goodId);
		if (goods == null) {
			return ResponseUtil.badArgumentValue();
		}
		// 查找六个相关商品,优先级 分类 -> 店铺 -> 新品
		HashMap<String, LitemallGoods> goodsMap = new HashMap<>();
		int related = 6;
		List<LitemallGoods> goodsCategoryList = goodsService.queryByCategory(goods.getCategoryId(), related);
		for (LitemallGoods g :goodsCategoryList) {
			if (goodsMap.size() < related){
				goodsMap.put(g.getId() , g);
			}
		}
		if (goodsMap.size() < related){
			List<LitemallGoods> goodsBrandList = goodsService.queryByBrand(goods.getBrandId(), 10);
			for (LitemallGoods g :goodsBrandList) {
				if (goodsMap.size() < related){
					goodsMap.put(g.getId() , g);
				}
			}
		}
		if (goodsMap.size() < related){
			List<LitemallGoods> goodsNewList = goodsService.queryByNew(10);
			for (LitemallGoods g :goodsNewList) {
				if (goodsMap.size() < related){
					goodsMap.put(g.getId() , g);
				}
			}
		}
		return ResponseUtil.okList(new ArrayList<>(goodsMap.values()));
	}

	/**
	 * 在售的商品总数
	 * @return 在售的商品总数
	 */
	@GetMapping("count")
	public Object count() {
		return ResponseUtil.ok(goodsService.queryOnSale());
	}

}