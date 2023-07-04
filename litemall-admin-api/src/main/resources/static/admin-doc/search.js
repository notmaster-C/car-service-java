let api = [];
const apiDocListSize = 1
api.push({
    name: 'default',
    order: '1',
    list: []
})
api[0].list.push({
    alias: 'AdminAdController',
    order: '1',
    link: '广告管理',
    desc: '广告管理',
    list: []
})
api[0].list[0].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/ad/list',
    desc: '查询',
});
api[0].list[0].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/ad/create',
    desc: '添加',
});
api[0].list[0].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/ad/read',
    desc: '详情',
});
api[0].list[0].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/ad/update',
    desc: '编辑',
});
api[0].list[0].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/ad/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminAddressController',
    order: '2',
    link: '收货地址',
    desc: '收货地址',
    list: []
})
api[0].list[1].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/address/list',
    desc: '查询',
});
api[0].list.push({
    alias: 'AdminAdminController',
    order: '3',
    link: '管理员管理',
    desc: '管理员管理',
    list: []
})
api[0].list[2].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/list',
    desc: '查询',
});
api[0].list[2].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/read',
    desc: '详情',
});
api[0].list[2].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/logout',
    desc: '管理员强制退出登录',
});
api[0].list[2].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/create',
    desc: '添加',
});
api[0].list[2].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/update',
    desc: '编辑',
});
api[0].list[2].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/admin/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminAftersaleController',
    order: '4',
    link: '订单售后',
    desc: '订单售后',
    list: []
})
api[0].list[3].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/list',
    desc: '查询',
});
api[0].list[3].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/recept',
    desc: '审核通过',
});
api[0].list[3].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/batch-recept',
    desc: '批量通过',
});
api[0].list[3].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/reject',
    desc: '审核拒绝',
});
api[0].list[3].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/batch-reject',
    desc: '批量拒绝',
});
api[0].list[3].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/aftersale/refund',
    desc: '退款',
});
api[0].list.push({
    alias: 'AdminAuthController',
    order: '5',
    link: '授权登陆',
    desc: '授权登陆',
    list: []
})
api[0].list[4].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/captcha',
    desc: '登陆验证码',
});
api[0].list[4].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/login',
    desc: '管理员登陆',
});
api[0].list[4].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/login_by_qr',
    desc: '登陆二维码',
});
api[0].list[4].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/logout',
    desc: '退出登陆',
});
api[0].list[4].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/info',
    desc: '管理员权限信息',
});
api[0].list[4].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/401',
    desc: '401',
});
api[0].list[4].list.push({
    order: '7',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/index',
    desc: 'index',
});
api[0].list[4].list.push({
    order: '8',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/auth/403',
    desc: '403',
});
api[0].list.push({
    alias: 'AdminBrandController',
    order: '6',
    link: '品牌管理',
    desc: '品牌管理',
    list: []
})
api[0].list[5].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/brand/list',
    desc: '查询',
});
api[0].list[5].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/brand/read',
    desc: '详情',
});
api[0].list[5].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/brand/create',
    desc: '添加',
});
api[0].list[5].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/brand/update',
    desc: '编辑',
});
api[0].list[5].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/brand/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminCategoryController',
    order: '7',
    link: '类目管理',
    desc: '类目管理',
    list: []
})
api[0].list[6].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/list',
    desc: '查询',
});
api[0].list[6].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/l1',
    desc: '一级分类查询',
});
api[0].list[6].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/create',
    desc: '添加分类',
});
api[0].list[6].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/read',
    desc: '分类详情',
});
api[0].list[6].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/update',
    desc: '编辑分类',
});
api[0].list[6].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/category/delete',
    desc: '分类删除',
});
api[0].list.push({
    alias: 'AdminCollectController',
    order: '8',
    link: '用户收藏',
    desc: '用户收藏',
    list: []
})
api[0].list[7].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/collect/list',
    desc: '查询',
});
api[0].list.push({
    alias: 'AdminCommentController',
    order: '9',
    link: '商品评论',
    desc: '商品评论',
    list: []
})
api[0].list[8].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/comment/list',
    desc: '查询',
});
api[0].list[8].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/comment/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminConfigController',
    order: '10',
    link: '系统配置',
    desc: '系统配置',
    list: []
})
api[0].list[9].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/mall',
    desc: '商场配置详情',
});
api[0].list[9].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/mall',
    desc: '商场配置编辑',
});
api[0].list[9].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/express',
    desc: '运费配置详情',
});
api[0].list[9].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/express',
    desc: '运费配置编辑',
});
api[0].list[9].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/order',
    desc: '订单配置详情',
});
api[0].list[9].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/order',
    desc: '订单配置编辑',
});
api[0].list[9].list.push({
    order: '7',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/wx',
    desc: '小程序配置详情',
});
api[0].list[9].list.push({
    order: '8',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/config/wx',
    desc: '小程序配置编辑',
});
api[0].list.push({
    alias: 'AdminCouponController',
    order: '11',
    link: '优惠券管理',
    desc: '优惠券管理',
    list: []
})
api[0].list[10].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/list',
    desc: '查询',
});
api[0].list[10].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/join',
    desc: '查询用户',
});
api[0].list[10].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/delete',
    desc: '删除',
});
api[0].list[10].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/read',
    desc: '详情',
});
api[0].list[10].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/create',
    desc: '添加',
});
api[0].list[10].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/coupon/update',
    desc: '编辑',
});
api[0].list.push({
    alias: 'AdminDashbordController',
    order: '12',
    link: '首页仪表盘',
    desc: '首页仪表盘',
    list: []
})
api[0].list[11].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/dashboard',
    desc: '首页仪表盘',
});
api[0].list.push({
    alias: 'AdminDynamicController',
    order: '13',
    link: '动态信息发布',
    desc: '动态信息发布',
    list: []
})
api[0].list[12].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/dynamic/list',
    desc: '动态列表',
});
api[0].list[12].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/dynamic/update',
    desc: '修改日常',
});
api[0].list[12].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/dynamic/create',
    desc: '发布日常',
});
api[0].list[12].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/dynamic/delete',
    desc: '删除日常',
});
api[0].list.push({
    alias: 'AdminFeedbackController',
    order: '14',
    link: '意见反馈',
    desc: '意见反馈',
    list: []
})
api[0].list[13].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/feedback/list',
    desc: '查询',
});
api[0].list.push({
    alias: 'AdminFootprintController',
    order: '15',
    link: '用户足迹',
    desc: '用户足迹',
    list: []
})
api[0].list[14].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/footprint/list',
    desc: '查询',
});
api[0].list.push({
    alias: 'AdminGoodsController',
    order: '16',
    link: '商品管理',
    desc: '商品管理',
    list: []
})
api[0].list[15].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/list',
    desc: '查询商品',
});
api[0].list[15].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/catAndBrand',
    desc: '获取店铺与分类选择列表',
});
api[0].list[15].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/update',
    desc: '编辑商品',
});
api[0].list[15].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/delete',
    desc: '删除商品',
});
api[0].list[15].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/on-sale',
    desc: '商品上下架',
});
api[0].list[15].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/create',
    desc: '添加商品',
});
api[0].list[15].list.push({
    order: '7',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/goods/detail',
    desc: '商品详情',
});
api[0].list.push({
    alias: 'AdminGrouponController',
    order: '17',
    link: '团购管理',
    desc: '团购管理',
    list: []
})
api[0].list[16].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/join',
    desc: '查询参与用户',
});
api[0].list[16].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/read',
    desc: '团购规则详情',
});
api[0].list[16].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/list',
    desc: '团购规则列表',
});
api[0].list[16].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/update',
    desc: '团购规则编辑',
});
api[0].list[16].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/create',
    desc: '团购规则添加',
});
api[0].list[16].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/groupon/delete',
    desc: '团购规则删除',
});
api[0].list.push({
    alias: 'AdminHistoryController',
    order: '18',
    link: '搜索历史',
    desc: '搜索历史',
    list: []
})
api[0].list[17].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/history/list',
    desc: '搜索历史',
});
api[0].list.push({
    alias: 'AdminIndexController',
    order: '19',
    link: '',
    desc: '',
    list: []
})
api[0].list[18].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/index',
    desc: '',
});
api[0].list[18].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/guest',
    desc: '',
});
api[0].list[18].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/authn',
    desc: '',
});
api[0].list[18].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/user',
    desc: '',
});
api[0].list[18].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/admin',
    desc: '',
});
api[0].list[18].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/index/admin2',
    desc: '',
});
api[0].list.push({
    alias: 'AdminIssueController',
    order: '20',
    link: '通用问题',
    desc: '通用问题',
    list: []
})
api[0].list[19].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/issue/list',
    desc: '列表',
});
api[0].list[19].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/issue/read',
    desc: '详情',
});
api[0].list[19].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/issue/create',
    desc: '添加',
});
api[0].list[19].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/issue/update',
    desc: '编辑',
});
api[0].list[19].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/issue/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminKeywordController',
    order: '21',
    link: '关键词',
    desc: '关键词',
    list: []
})
api[0].list[20].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/keyword/list',
    desc: '查询',
});
api[0].list[20].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/keyword/read',
    desc: '详情',
});
api[0].list[20].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/keyword/create',
    desc: '添加',
});
api[0].list[20].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/keyword/update',
    desc: '编辑',
});
api[0].list[20].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/keyword/delete',
    desc: '删除',
});
api[0].list.push({
    alias: 'AdminLogController',
    order: '22',
    link: '操作日志',
    desc: '操作日志',
    list: []
})
api[0].list[21].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/log/list',
    desc: '查询',
});
api[0].list[21].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/log/system-list',
    desc: '系统日志列表查询',
});
api[0].list[21].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/log/system-read',
    desc: '系统日志列表读取  异步推送数据',
});
api[0].list.push({
    alias: 'AdminNoticeAdminController',
    order: '23',
    link: '管理员通知信息',
    desc: '管理员通知信息',
    list: []
})
api[0].list[22].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/list',
    desc: '通知列表',
});
api[0].list[22].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/count',
    desc: '通知信息条数',
});
api[0].list[22].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/cat',
    desc: '查看通知',
});
api[0].list[22].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/batch-cat',
    desc: '批量浏览通知',
});
api[0].list[22].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/delete',
    desc: '删除通知',
});
api[0].list[22].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/notice/batch-delete',
    desc: '批量删除通知',
});
api[0].list.push({
    alias: 'AdminNoticeController',
    order: '24',
    link: '通知管理',
    desc: '通知管理',
    list: []
})
api[0].list[23].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/list',
    desc: '查询',
});
api[0].list[23].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/create',
    desc: '添加',
});
api[0].list[23].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/read',
    desc: '详情',
});
api[0].list[23].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/update',
    desc: '编辑',
});
api[0].list[23].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/delete',
    desc: '删除',
});
api[0].list[23].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/notice/batch-delete',
    desc: '批量删除',
});
api[0].list.push({
    alias: 'AdminOrderController',
    order: '25',
    link: '订单管理',
    desc: '订单管理',
    list: []
})
api[0].list[24].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/list',
    desc: '查询订单',
});
api[0].list[24].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/channel',
    desc: '查询物流公司',
});
api[0].list[24].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/info',
    desc: '查询订单数量',
});
api[0].list[24].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/detail',
    desc: '订单详情',
});
api[0].list[24].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/cancel',
    desc: '订单取消',
});
api[0].list[24].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/refund',
    desc: '订单退款',
});
api[0].list[24].list.push({
    order: '7',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/ship',
    desc: '发货',
});
api[0].list[24].list.push({
    order: '8',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/pay',
    desc: '线下收款',
});
api[0].list[24].list.push({
    order: '9',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/delete',
    desc: '删除订单',
});
api[0].list[24].list.push({
    order: '10',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/order/reply',
    desc: '回复订单商品',
});
api[0].list.push({
    alias: 'AdminProfileController',
    order: '26',
    link: '管理员个人账号管理',
    desc: '管理员个人账号管理',
    list: []
})
api[0].list[25].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/password',
    desc: '修改密码',
});
api[0].list[25].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/detail',
    desc: '账号详情',
});
api[0].list[25].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/profile/update',
    desc: '更新账号信息',
});
api[0].list.push({
    alias: 'AdminRegionController',
    order: '27',
    link: '地区管理',
    desc: '地区管理',
    list: []
})
api[0].list[26].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/region/list/sub',
    desc: '获取地区子列表',
});
api[0].list[26].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/region/list',
    desc: '获取地区列表',
});
api[0].list.push({
    alias: 'AdminRewardController',
    order: '28',
    link: '赏金管理',
    desc: '赏金管理',
    list: []
})
api[0].list[27].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/join',
    desc: '查询参与用户',
});
api[0].list[27].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/list',
    desc: '赏金规则查询',
});
api[0].list[27].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/read',
    desc: '赏金规则详情',
});
api[0].list[27].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/update',
    desc: '编辑赏金规则',
});
api[0].list[27].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/create',
    desc: '添加赏金规则',
});
api[0].list[27].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/reward/delete',
    desc: '删除赏金规则',
});
api[0].list.push({
    alias: 'AdminRoleController',
    order: '29',
    link: '角色管理',
    desc: '角色管理',
    list: []
})
api[0].list[28].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/list',
    desc: '角色列表查询',
});
api[0].list[28].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/options',
    desc: '查询角色map  [{角色id,角色名称}]',
});
api[0].list[28].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/read',
    desc: '查询角色详情',
});
api[0].list[28].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/create',
    desc: '角色添加',
});
api[0].list[28].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/update',
    desc: '角色编辑',
});
api[0].list[28].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/delete',
    desc: '角色删除',
});
api[0].list[28].list.push({
    order: '7',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/permissions',
    desc: '管理员的权限情况',
});
api[0].list[28].list.push({
    order: '8',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/role/permissions',
    desc: '更新管理员权限',
});
api[0].list.push({
    alias: 'AdminShareController',
    order: '30',
    link: '分享管理',
    desc: '分享管理',
    list: []
})
api[0].list[29].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/share/list',
    desc: '分享记录查询',
});
api[0].list.push({
    alias: 'AdminStatController',
    order: '31',
    link: '统计管理',
    desc: '统计管理',
    list: []
})
api[0].list[30].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/stat/user',
    desc: '用户统计',
});
api[0].list[30].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/stat/order',
    desc: '订单统计',
});
api[0].list[30].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/stat/goods',
    desc: '商品统计',
});
api[0].list.push({
    alias: 'AdminStorageController',
    order: '32',
    link: '对象存储管理',
    desc: '对象存储管理',
    list: []
})
api[0].list[31].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/list',
    desc: '对象列表查询',
});
api[0].list[31].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/create',
    desc: '对象上传',
});
api[0].list[31].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/read',
    desc: '对象详情',
});
api[0].list[31].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/update',
    desc: '对象编辑',
});
api[0].list[31].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/delete',
    desc: '删除对象',
});
api[0].list[31].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/storage/uploadFile',
    desc: '百度富文本编辑器文件上传',
});
api[0].list.push({
    alias: 'AdminTenantController',
    order: '33',
    link: '多租户管理',
    desc: '多租户管理',
    list: []
})
api[0].list[32].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/tenant/list',
    desc: '租户查询',
});
api[0].list[32].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/tenant/create',
    desc: '添加租户',
});
api[0].list[32].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/tenant/update',
    desc: '修改租户',
});
api[0].list[32].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/tenant/delete',
    desc: '删除租户',
});
api[0].list.push({
    alias: 'AdminTopicController',
    order: '34',
    link: '专题管理',
    desc: '专题管理',
    list: []
})
api[0].list[33].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/list',
    desc: '专题列表',
});
api[0].list[33].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/create',
    desc: '专题添加',
});
api[0].list[33].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/read',
    desc: '专题详情',
});
api[0].list[33].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/update',
    desc: '专题编辑',
});
api[0].list[33].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/delete',
    desc: '专题删除',
});
api[0].list[33].list.push({
    order: '6',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/topic/batch-delete',
    desc: '专题批量删除',
});
api[0].list.push({
    alias: 'AdminUserController',
    order: '35',
    link: '用户管理',
    desc: '用户管理',
    list: []
})
api[0].list[34].list.push({
    order: '1',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/user/list',
    desc: '用户查询',
});
api[0].list[34].list.push({
    order: '2',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/user/detail',
    desc: '用户详情',
});
api[0].list[34].list.push({
    order: '3',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/user/update',
    desc: '用户编辑',
});
api[0].list[34].list.push({
    order: '4',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/user/upload',
    desc: '用户批量上传',
});
api[0].list[34].list.push({
    order: '5',
    deprecated: 'false',
    url: 'https://www.ysling.com.cn/admin/user/deal-list',
    desc: '用户交易记录',
});
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        const search = document.getElementById('search');
        const searchValue = search.value.toLocaleLowerCase();

        let searchGroup = [];
        for (let i = 0; i < api.length; i++) {

            let apiGroup = api[i];

            let searchArr = [];
            for (let i = 0; i < apiGroup.list.length; i++) {
                let apiData = apiGroup.list[i];
                const desc = apiData.desc;
                if (desc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                    searchArr.push({
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        list: apiData.list
                    });
                } else {
                    let methodList = apiData.list || [];
                    let methodListTemp = [];
                    for (let j = 0; j < methodList.length; j++) {
                        const methodData = methodList[j];
                        const methodDesc = methodData.desc;
                        if (methodDesc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                            methodListTemp.push(methodData);
                            break;
                        }
                    }
                    if (methodListTemp.length > 0) {
                        const data = {
                            order: apiData.order,
                            desc: apiData.desc,
                            link: apiData.link,
                            list: methodListTemp
                        };
                        searchArr.push(data);
                    }
                }
            }
            if (apiGroup.name.toLocaleLowerCase().indexOf(searchValue) > -1) {
                searchGroup.push({
                    name: apiGroup.name,
                    order: apiGroup.order,
                    list: searchArr
                });
                continue;
            }
            if (searchArr.length === 0) {
                continue;
            }
            searchGroup.push({
                name: apiGroup.name,
                order: apiGroup.order,
                list: searchArr
            });
        }
        let html;
        if (searchValue === '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            let $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiGroups, liClass, display) {
    let html = "";
    if (apiGroups.length > 0) {
        if (apiDocListSize === 1) {
            let apiData = apiGroups[0].list;
            let order = apiGroups[0].order;
            for (let j = 0; j < apiData.length; j++) {
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_'+order+'_'+apiData[j].order+'_' + apiData[j].link + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="'+display+'">';
                let doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated === 'true') {
                        spanString='<span class="line-through">';
                    } else {
                        spanString='<span>';
                    }
                    html += '<li><a href="#_'+order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_'+apiGroup.order+'_' + apiGroup.name + '">' + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="'+liClass+'">';
                    html += '<a class="dd" href="#_'+apiGroup.order+'_'+ apiData[j].order + '_'+ apiData[j].link + '">' +apiGroup.order+'.'+ apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="'+display+'">';
                    let doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                       let spanString;
                       if (doc[m].deprecated === 'true') {
                           spanString='<span class="line-through">';
                       } else {
                           spanString='<span>';
                       }
                       html += '<li><a href="#_'+apiGroup.order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">'+apiGroup.order+'.' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                   }
                    html += '</ul>';
                    html += '</li>';
                }

                html += '</ul>';
                html += '</li>';
            }
        }
    }
    return html;
}