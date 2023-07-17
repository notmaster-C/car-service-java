package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.*;
import org.click.carservice.db.enums.LikeType;
import org.click.carservice.wx.model.like.body.LikeSubmitBody;
import org.click.carservice.wx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户点赞服务
 * @author click
 */
@Slf4j
@Service
public class WxWebLikeService {

    @Autowired
    private WxTopicService topicService;
    @Autowired
    private WxLikeService likeService;
    @Autowired
    private WxBrandService brandService;
    @Autowired
    private WxDynamicService dynamicService;
    @Autowired
    private WxCommentService commentService;


    /**
     * 点赞
     * @param userId 用户ID
     * @param body   评论信息
     * @return 提交订单操作结果
     */
    public Object submit(String userId, LikeSubmitBody body) {
        Short likeType = body.getLikeType();
        String valueId = body.getValueId();

        if (LikeType.parseValue(likeType) == null){
            return ResponseUtil.fail("类型不支持");
        }

        CarServiceLike like = likeService.query(likeType, valueId, userId);
        if (like != null){
            like.setCancel(!like.getCancel());
            likeService.updateSelective(like);
        }else {
            like = new CarServiceLike();
            like.setUserId(userId);
            like.setType(likeType);
            like.setValueId(valueId);
            like.setCancel(false);
            likeService.add(like);
        }

        if (LikeType.TYPE_TOPIC.getStatus().equals(likeType)){
            CarServiceTopic topic = topicService.findById(valueId);
            if (like.getCancel()){
                topic.setLikeCount(topic.getLikeCount() > 0 ? topic.getLikeCount() - 1 : 0);
            }else {
                topic.setLikeCount(topic.getLikeCount() + 1);
            }
            if (topicService.updateVersionSelective(topic) == 0){
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_BRAND.getStatus().equals(likeType)){
            CarServiceBrand brand = brandService.findById(valueId);
            if (like.getCancel()){
                brand.setLikeCount(brand.getLikeCount() > 0 ? brand.getLikeCount() - 1 : 0);
            }else {
                brand.setLikeCount(brand.getLikeCount() + 1);
            }
            if (brandService.updateVersionSelective(brand) == 0){
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_TIMELINE.getStatus().equals(likeType)){
            CarServiceDynamic dynamic = dynamicService.findById(valueId);
            if (like.getCancel()){
                dynamic.setLikeCount(dynamic.getLikeCount() > 0 ? dynamic.getLikeCount() - 1 : 0);
            }else {
                dynamic.setLikeCount(dynamic.getLikeCount() + 1);
            }
            if (dynamicService.updateVersionSelective(dynamic) == 0){
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        if (LikeType.TYPE_COMMENT.getStatus().equals(likeType)){
            CarServiceComment comment = commentService.findById(valueId);
            if (like.getCancel()){
                comment.setLikeCount(comment.getLikeCount() > 0 ? comment.getLikeCount() - 1 : 0);
            }else {
                comment.setLikeCount(comment.getLikeCount() + 1);
            }
            if (commentService.updateVersionSelective(comment) == 0){
                throw new RuntimeException("系统繁忙请稍后重试");
            }
        }
        return ResponseUtil.ok();
    }
}