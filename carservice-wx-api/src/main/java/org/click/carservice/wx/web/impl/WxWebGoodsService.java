package org.click.carservice.wx.web.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.system.SystemConfig;
import org.click.carservice.core.utils.Inheritable.InheritableCallable;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.entity.GoodsSpecificationVo;
import org.click.carservice.db.enums.CollectType;
import org.click.carservice.db.enums.GoodsStatus;
import org.click.carservice.wx.model.goods.body.GoodsCommentListBody;
import org.click.carservice.wx.model.goods.body.GoodsListBody;
import org.click.carservice.wx.model.goods.result.*;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 商品服务
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebGoodsService {

	@Autowired
	private WxUserService userService;
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
	public Object detail(String userId, String goodId) {
		// 商品信息
		CarServiceGoods info = goodsService.findById(goodId);
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
		FutureTask<List<CarServiceGoodsAttribute>> goodsAttributeListTask = new FutureTask<>(
			new InheritableCallable<List<CarServiceGoodsAttribute>>(){
				@Override
				public List<CarServiceGoodsAttribute> runTask() {
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
		FutureTask<List<CarServiceGoodsProduct>> productListCallableTask = new FutureTask<>(
			new InheritableCallable<List<CarServiceGoodsProduct>>(){
				@Override
				public List<CarServiceGoodsProduct> runTask() {
					return productService.queryByGid(goodId);
				}
			}
		);

		// 商品问题，这里是一些通用问题
		FutureTask<List<CarServiceIssue>> issueCallableTask = new FutureTask<>(
			new InheritableCallable<List<CarServiceIssue>>(){
				@Override
				public List<CarServiceIssue> runTask() {
					return goodsIssueService.getGoodsIssue();
				}
			}
		);

		// 商品品牌商
		FutureTask<CarServiceBrand> brandCallableTask = new FutureTask<>(
			new InheritableCallable<CarServiceBrand>(){
				@Override
				public CarServiceBrand runTask() {
					return brandService.findById(info.getBrandId());
				}
			}
		);

		//团购信息
		FutureTask<List<CarServiceGrouponRules>> grouponRulesCallableTask = new FutureTask<>(
			new InheritableCallable<List<CarServiceGrouponRules>>(){
				@Override
				public List<CarServiceGrouponRules> runTask() {
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
	public Object category(String id) {
		CarServiceCategory currentCategory = categoryService.findById(id);
		CarServiceCategory parentCategory;
		List<CarServiceCategory> brotherCategory;
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
	public Object list(String userId, GoodsListBody body) {
		//添加到搜索历史
		if (userId != null && !Objects.isNull(body.getKeyword())) {
			CarServiceSearchHistory searchHistory = searchHistoryService.findByKeyword(userId, body.getKeyword());
			if (searchHistory != null){
				searchHistoryService.updateVersionSelective(searchHistory);
			} else {
				CarServiceSearchHistory searchHistoryVo = new CarServiceSearchHistory();
				searchHistoryVo.setKeyword(body.getKeyword());
				searchHistoryVo.setUserId(userId);
				searchHistoryVo.setFrom("wx");
				searchHistoryVo.setVersion(1);
				searchHistoryService.save(searchHistoryVo);
			}
		}

		//查询列表数据
		List<CarServiceGoods> goodsList = goodsService.querySelective(body);
		//查询对应店铺信息
		List<String> brandIds = goodsList.stream().map(CarServiceGoods::getBrandId).distinct().collect(Collectors.toList());
		List<CarServiceBrand> brandList = brandService.queryByIds(brandIds);
		// 查询商品所属类目列表。
		List<String> goodsCatIds = goodsService.getCatIds(body.getBrandId(), body.getKeyword(), body.getIsHot(), body.getIsNew());
		List<CarServiceCategory> categoryList;
		if (goodsCatIds.size() != 0) {
			categoryList = categoryService.queryL2ByIds(goodsCatIds);
		} else {
			categoryList = new ArrayList<>(0);
		}

		PageInfo<CarServiceGoods> pagedList = PageInfo.of(goodsList);
		GoodsListResult result = new GoodsListResult();
		result.setList(goodsList);
		result.setTotal(pagedList.getTotal());
		result.setPage(pagedList.getPageNum());
		result.setLimit(pagedList.getPageSize());
		result.setPages(pagedList.getPages());
		result.setFilterCategoryList(categoryList);
		result.setList2(brandList);
		// 因为这里需要返回额外的filterCategoryList参数，因此不能方便使用ResponseUtil.okList
		return ResponseUtil.ok(result);
	}


	/**
	 * 商品详情页面“大家都在看”推荐商品
	 * @param goodId, 商品ID
	 * @return 商品详情页面推荐商品
	 */
	public Object related(String goodId) {
		CarServiceGoods goods = goodsService.findById(goodId);
		if (goods == null) {
			return ResponseUtil.badArgumentValue();
		}
		// 查找六个相关商品,优先级 分类 -> 店铺 -> 新品
		HashMap<String, CarServiceGoods> goodsMap = new HashMap<>();
		int related = 6;
		List<CarServiceGoods> goodsCategoryList = goodsService.queryByCategory(goods.getCategoryId(), related);
		for (CarServiceGoods g :goodsCategoryList) {
			if (goodsMap.size() < related){
				goodsMap.put(g.getId() , g);
			}
		}
		if (goodsMap.size() < related){
			List<CarServiceGoods> goodsBrandList = goodsService.queryByBrand(goods.getBrandId(), 10);
			for (CarServiceGoods g :goodsBrandList) {
				if (goodsMap.size() < related){
					goodsMap.put(g.getId() , g);
				}
			}
		}
		if (goodsMap.size() < related){
			List<CarServiceGoods> goodsNewList = goodsService.queryByNew(10);
			for (CarServiceGoods g :goodsNewList) {
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
	public Object count() {
		return ResponseUtil.ok(goodsService.queryOnSale());
	}


	/**
	 * 评论数量
	 * @param goodsId 商品ID。
	 * @return 评论数量
	 */
	public Object commentCount(String goodsId) {
		GoodsCommentCountResult result = new GoodsCommentCountResult();
		result.setAllCount(goodsCommentService.count(goodsId, false));
		result.setHasPicCount(goodsCommentService.count(goodsId, true));
		return ResponseUtil.ok(result);
	}

	/**
	 * 评论列表
	 */
	public Object commentList(GoodsCommentListBody body) {
		List<CarServiceGoodsComment> commentList = goodsCommentService.querySelective(body);
		List<GoodsCommentListResult> commentVoList = new ArrayList<>(commentList.size());
		for (CarServiceGoodsComment comment : commentList) {
			GoodsCommentListResult result = new GoodsCommentListResult();
			BeanUtil.copyProperties(comment , result);
			result.setUserInfo(userService.findUserVoById(comment.getUserId()));
			commentVoList.add(result);
		}
		return ResponseUtil.okList(commentVoList, commentList);
	}

}
