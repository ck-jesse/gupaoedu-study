package com.coy.gupaoedu.study.spring.easyexcel.weeget;

public enum CodeEnums {

    //--------------系统参数----------------

    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败，请重试"),
    ERROR(-1, "操作失败"),
    INTERNAL_SERVER_ERROR(500, "内部异常"),
    API_NOT_FOUND(404, "未找到该API"),
    SYS_CONTAINS_ERROR(201, "操作完成，但包含错误"),
    SYS_INPUT_ERROR           	(202, "请求参数错误"),
    SYS_NOT_LOGIN			  	(203, "请先登录"),
    SYS_OFFLINE_LOGIN         	(204, "下线通知：已在其他浏览器登录"),
    SYS_NETWORK_ERROR         	(205, "网络不稳定，请重试"),
    SYS_ERRORS_MINUS_SIX      	(206, "用户没有操作该功能的权限"),
    SYS_GET_INTO_FUSE         	(207, "内部接口异常，进入熔断"),
    SYS_ZIP_FILE_ERROR        	(208, "传入的zip文件不正确"),
    SYS_REPETITIVE_OPERATION  	(209, "请不要重复操作"),
    SYS_RESULT_LOGIN_ERROR    	(230, "登录失败，用户名或密码错误"),
    SYS_RESULT_LOGIN_OVERTIME 	(231, "登录已过期，请重新登录"),
    SYS_URL_ERROR			  	(232, "请求路径错误，请修改后重试"),
    SYS_VERIFYCODE_ERROR      	(10997, "验证码错误"),
    SYS_VERIFYCODE_EXPIRED    	(10998, "验证码已过期"),
    SYS_VERIFYCODE_HAVE_USED  	(10999, "手机验证码已被使用，请重新获取"),
    SYS_PHONE_BIND            	(10000, "该手机已绑定过账号"),
    SYS_ORG_UNBIND_ACCOUNT    	(10001, "该组织未绑定支付账号"),
    SYS_IMGCODE_ERROR         	(10002, "图形验证码不正确或已使用"),
    SYS_SEND_SMS_TIRED        	(10003, "操作过于频繁,请稍后再试"),
    SYS_SEND_MESSAGE_ERROR    	(10004, "SEND_MESSAGE_ERROR"),
    SYS_SEND_NO_FUNCTION_ERROR	(10005, "后台消息推送定时功能还没做"),
    SYS_NOT_FIND_BOOKING_ERROR	(10201, "找不到该拼团记录"),
    SYS_ERROR_DESIGN_IMAGES   	(10528, "图片处理失败"),
    SYS_SYSTEM_ERR(100000, "系统错误"),
    SYS_PARA_ERR(100001, "请求参数错误"),
    SYS_STATUS_FORBIDDEN(100002, "禁止访问"),
    SYS_SERVICE_DOWN(100003, "微服务不可用"),
    SYS_SERVICE_BUSY(1000031, "服务器繁忙，请稍后再试"),
    SYS_SIGN_ERR(100004, "url sign 签名不一致"),
    SYS_TIMESTAMP_ERR(100005, "timestamp与服务器时间相差较大"),
    SYS_NONCE_REPEAT(100006, "重复提交"),
    SYS_INVALID_TOKEN(100007, "无效的token"),
    SYS_EXPIRED_TOKEN(100008, "token已经过期"),
    SYS_JSONMAPPINGEXCEPTION(100009, "JSON映射异常"),
    SYS_SYS_PARAMETER_NULL(100010, "请联系管理员配置正确相关参数"),
    SYS_SYS_BIZ_ERR(100011, "业务逻辑异常"),
    SYS_PARAM_VALIDATE_REFUSE(100012, "校验不通过"),
    SYS_REPEAT(100013, "网络开小差，请稍后重试"),
    SYS_APINAME_ERR(100014, "接口名参数不一致"),
    SYS_DATA_ISNULL(100015, "配置数据为空"),
    SYS_PARAM_TYPE_ERROR(100016, "请求参数缺失/长度/格式错误"),
    SYS_CONDITION_NOT_ENOUGH(100017, "明天来签到，即可提现1元现金，明天记得早点来签到哦"),
    SYS_HSRJ_TOKEN_NOT_VALID(401, "TOKEN无效"),
    SYS_REDO(10025, "重复操作"),
    SYS_EXIST_NAME(10002, "名称已经存在"),
    SYS_MULTIPLE_RECORD(10053, "存在多条记录"),
    SYS_RECORD_NOT_EXIST(10013, "暂无数据"),
    SYS_RECORD_EXIST(10014, "记录已经存在"),
    SYS_USER_ROLE_LIMIT(10008, "用户没有操作的权限"),
    SYS_EMAIL_USED(10009, "邮箱已经被使用"),
    SYS_USER_VERSION_NULL(1330004, "请联系管理员配置不存在的版本信息"),
    //---------end--系统参数----------------


    //=========================↓↓↓↓↓↓↓↓↓↓↓↓用户模块↓↓↓↓↓↓↓↓↓↓↓↓=========================



    USER_NOT_EXIST                     (20001, "不存在该用户或已被冻结"),
    USER_ALREDAY_HAS_SIMILE_USER            (20002, "已存在相同账号的用户，请重新设置"),
    USER_NOT_ADMIN                     (20003, "你不是后台系统管理员，无法操作"),
    USER_BANK_USER_NOT_EXIST                (20005, "银行账号不存在"),
    USER_BANK_USER_DELETE_FAIL              (20006, "删除银行账号失败"),
    USER_DO_NOT_REPEAT_OPERATION            (20007, "请不要重复操作"),
    USER_ORGANIZATION_BINDING_FAIL          (20008, "绑定组织信息失败"),
    USER_WEIXIN_ADDRESS_NOT_EXIST           (20361, "不存在该地址"),
    USER_UPDATE_USER_FAIL                   (20009, "更新用户失败"),
    USER_ORGANIZATION_NOT_EXIST             (20031, "店铺不存在"),
    USER_ERRORS_INSERT_ORGANIZATION         (20032, "ERRORS_INSERT_ORGANIZATION"),
    USER_PHONE_NUMBER_USED                  (20033, "该手机号已被注册，请使用其他手机号码"),
    USER_PHONE_NUMBER_EXIST                 (20034, "已存在相同手机号的店铺"),
    USER_ORGANIZATION_UPDATE_FAIL           (20030, "修改店铺失败"),
    USER_ORGANIZATION_IS_DISABLE            (20035, "店铺已被禁用"),
    USER_ORGANIZATION_IS_BINDING            (20036, "ORGANIZATION_IS_BINDING"),
    USER_ORGANIZATION_LEVEL_NOT_EXIST       (20037, "不存在该店铺等级设置"),
    USER_ORGANIZATION_LEVEL_AUDIT_NOT_EXIST (20038, "不存在店铺等级审核记录"),
    USER_ORGANIZATION_LEVEL_ERROR           (20039, "当前店铺等级高于或等于要升的等级"),
    USER_ORGANIZATION_IS_AUDITING           (20040, "当前店铺存在待审核记录，请优先处理待审核"),
    USER_INVITECODE_FALSE                   (20041, "邀请码不正确"),
    USER_INVITECODE_IS_USED                 (20042, "邀请码已使用"),
    USER_INVITECODE_IS_EXPIRED              (20043, "邀请码已过期"),
    USER_INVITECODE_GENERATE_FAIL           (20044, "生成邀请码失败"),
    USER_ADD_ORGANIZATION_USER_FAIL         (20045, "添加店铺用户失败"),
    USER_INVITECODE_UPDATE_FAIL             (20046, "邀请码更新失败"),
    USER_PHONE_NUM_EXISTED                  (20102, "手机号已存在"),
    USER_ORGANIZATION_MUST_HAS_ONE_ADMIN    (20047, "每个组织至少要有一个管理员"),
    USER_INFO_NO_VIEWING               (20048, "您所在小组无法查看该用户信息"),
    USER_NOT_OPERATE_HIGHER_LEVEL           (20049, "不能越级操作上级部门"),
    USER_OPENGID_IS_NULL                    (20544, "openGId为空"),
    USER_BATCH_ADD_PRICE_LIMIT              (20511, "全局批量加价不能超过30"),
    USER_GROUP_NOT_EXIST                    (20061, "集团不存在"),
    USER_ALREADY_HAS_SIMILE_NAME_TAG        (20062, "已经存在相同名字的标签"),
    USER_ADD_TAG_FAIL                       (20063, "添加标签失败"),
    USER_TAG_NOT_EXIST                      (20064, "标签不存在"),
    USER_TAG_IS_USING                       (20065, "该标签还有用户使用，无法删除"),
    USER_TAG_DELETE_FAIL                    (20066, "删除标签失败"),
    USER_TAG_EDIT_FAIL                      (20067, "编辑标签信息失败"),
    USER_ALREADY_HAS_SIMILE_NAME_DEPARTMENT (20091, "已经存在相同名字的部门"),
    USER_DEPARTMENT_NOT_EXIST               (20092, "部门不存在"),
    USER_ADD_DEPARTMENT_FAIL                (20093, "添加部门失败"),
    USER_DEPARTMENT_HAS_DEPARTMENT_CHILDREN (20094, "该部门下面还有子部门，不能删除"),
    USER_DEPARTMENT_HAS_CLERK_CHILDREN      (20095, "该部门下面还有员工，无法删除"),
    USER_DELETE_DEPARTMENT_FAIL             (20096, "删除部门失败"),
    USER_EDIT_DEPARTMENT_FAIL               (20097, "编辑部门信息失败"),
    USER_MORE_THAN_ORGANIZATION_COUNT       (20098, "超过组织允许添加最大员工数"),
    USER_SIMPLE_PHONE_CLERK_EXIST           (20099, "已经存在相同手机号码的员工"),
    USER_ADD_CLERK_FAIL                     (20123, "添加员工失败"),
    USER_CLERK_NOT_EXIST                    (20124, "员工不存在"),
    USER_ADDRESS_NOT_EXIST                  (20125, "不存在该地址"),
    USER_GLOBAL_ADD_PIRCE_CAN_NOT_MORETHAN_T(20126, "全局批量加价不能超过200"),
    USER_DO_NOT_CHECK_AGAIN                 (20127, "不要重复签到"),
    USER_ACT_MOTHAN_SHARE                   (20129, "ACT_MOTHAN_SHARE"),
    USER_UPDATE_CLERK_FAIL                  (20130, "更新员工信息失败"),
    USER_ORGANIZATION_ADD_FAIL              (20131, "ORGANIZATION_ADD_FAIL"),


    //=========================↑↑↑↑↑↑↑↑↑↑↑↑用户模块↑↑↑↑↑↑↑↑↑↑↑↑=========================


    //=========================↓↓↓↓↓↓↓↓↓↓↓↓商品模块↓↓↓↓↓↓↓↓↓↓↓↓=========================

    GOODS_SPEC_NOT_EXIST               (40124, "组织商品规格不存在"),
    GOODS_GROUP_NOT_EXIST              (40362, "商品分组不存在"),
    GOODS_NOT_EXIST                    (40113, "商品不存在"),
    GOODS_BRAND_NOT_EXIST                    (40017, "品牌不存在"),
    GOODS_LIMITED_ONE                  (40451, "该商品限购一组"),
    GOODS_STOCK_INSUFFICIENT           (40278, "当前商品库存不足"),
    GOODS_WAREHOUSE_SPEC_NOT_EXIST           (40120, "仓库商品规格不存在"),
    GOODS_UPDATE_WAREHOUSE_SPEC_FAIL         (45121, "更新仓库商品库存失败"),
    GOODS_CALLOUT_QTY_MUST_LESSTHAN_GOODS_QTY(40226, "调出商品的数量不可大于现有商品数量"),
    GOODS_OFF_SHELF                    (40268, "商品已下架"),
    GOODS_ADD_PRICE_NOT_GT_GOODS_GROUP_MAX   (40512, "不能超过当前分组可加价最大值"),
    GOODS_ADD_PRICE_NOT_GT_GOODS_MAX         (40513, "不能超过当前商品可加价最大值"),
    GOODS_BRAND_ADD_FAIL                     (40016, "添加品牌失败"),
    GOODS_BRAND_GOODS_EXIST_NOT_CANNOT_DELETE(40034, "该品牌下面有商品，无法删除"),
    GOODS_BRAND_DELETE_FAIL                  (40018, "删除品牌失败"),
    GOODS_BRAND_UPDATE_FAIL                  (40043, "编辑品牌信息失败"),
    GOODS_BRAND_PARENT_NOT_EXIST             (40514, "不存在此品牌父类"),
    GOODS_BRAND_PARENT_EXIST                 (40515, "已存在此品牌父类"),
    GOODS_BRAND_PARENT_EXIST_BRAND_CATEGORY  (40516, "此品牌父类存在分类，不能删除"),
    GOODS_BRAND_CATEGORY_EXIST               (40517, "已存在此品牌分类"),
    GOODS_BRAND_CATEGORY_NOT_EXIST           (40518, "不已存在此品牌分类"),
    GOODS_NUMBER_EXIST                 (40300, "已存在同样的货号，请输入另一货号"),
    GOODS_BAR_CODE_EXIST                     (40301, "已存在同样的条形码，请输入另一条形码"),
    GOODS_HAVE_ORDER_NOT_DELETE        (40200, "商品还有订单，无法删除"),
    GOODS_HAVE_STOCK_UPDATE_NOT_DELETE (40254, "商品有出入库记录，无法删除"),
    GOODS_TYPE_NOT_EXIST               (40123, "商品类别不存在"),
    GOODS_SPEC_NAME_EXIST              (40303, "该规格名称已经存在"),
    GOODS_WAREHOUSE_EXIST                    (40060, "已经存在相同名字的仓库"),
    GOODS_WAREHOUSE_ADD_FAIL                 (40061, "添加仓库失败"),
    GOODS_WAREHOUSE_NOT_EXIST                (40062, "仓库不存在"),
    GOODS_WAREHOUSE_UPDATE_FAIL              (40065, "编辑仓库信息失败"),
    GOODS_WAREHOUSE_USE_NOT_DELETE           (40064, "该仓库还有组织使用，无法删除"),
    GOODS_WAREHOUSE_DELETE_FAIL              (40063, "删除仓库失败"),
    GOODS_SCATEGORY_NOT_EXIST                (40012, "自定义分类不存在"),
    GOODS_INVENTORY_CHANGE_NOT_EXIST         (40122, "库存操作记录不存在"),
    GOODS_INVENTORY_CHANGE_NOT_CHANGED       (40247, "调库不可选择同样的仓库"),
    GOODS_ORDER_EVALUATE_EXIST               (40510, "订单评价记录已存在"),


    //=========================↑↑↑↑↑↑↑↑↑↑↑↑商品模块↑↑↑↑↑↑↑↑↑↑↑↑=========================


    //=========================↓↓↓↓↓↓↓↓↓↓↓↓订单模块↓↓↓↓↓↓↓↓↓↓↓↓=========================


    ORDER_AREA_SIGNATURE_CODE_REPEAT         (30237, "OT是其他地区的特征码，无法重复添加"),
    ORDER_AREA_OTHER_CANNOT_MODIFY_DELETION  (30238, "其他地区不能编辑，也不能删除"),
    ORDER_AREA_NAME_EXIST                    (30037, "已经存在相同名字的地区"),
    ORDER_AREA_NOT_EXIST                     (30039, "地区不存在"),
    ORDER_AREA_ADD_FAIL                      (30038, "添加地区失败"),
    ORDER_AREA_CHILD_EXIST                   (30041, "该地区下面还有子地区，不能删除"),
    ORDER_AREA_DELETE_FAIL                   (30040, "删除地区失败"),
    ORDER_AREA_MODIFY_FAIL                   (30042, "编辑地区信息失败"),
    ORDER_NOT_EXIST                    (30135, "订单不存在"),
    ORDER_SHOPPINGCART_NOT_EXIST             (30378, "该购物车不存在"),
    ORDER_NO_NEED_TO_AUDIT             (30169, "订单已归档，无需再次审核"),
    ORDER_ADD_AUDIT_RECORD_FAIL              (30153, "添加审核记录失败"),
    ORDER_UPDATE_ORDER_FAIL                  (30146, "更新订单信息失败"),
    ORDER_CUSTOMER_ORDER_PERMITTED_FREEZING  (30382, "只有c端下单才允许冻结"),
    ORDER_DELIVERED                    (30147, "该订单已经发完货"),
    ORDER_GOODS_QUANTITY_IS_NEGATIVE   (30339, "负数商品订单无法修改"),
    ORDER_ALL_REFUND                   (30398, "该订单商品已全部退款，禁止退款"),
    ORDER_MARKED_OUT_OF_STOCK          (30399, "订单商品已标记缺货"),
    ORDER_ADD_REFUND_FAIL                    (30400, "添加退款记录失败"),
    ORDER_UPDATE_REFUND_FAIL                 (30401, "退款成功,更新记录失败"),
    ORDER_REFUND_FAIL                  (30402, "订单退款失败"),
    ORDER_DELIVER_RECORD_NOT_EXIST     (30150, "订单发货记录不存在"),
    ORDER_EXPRESS_NOT_EXIST                  (30165, "物流公司不存在"),
    ORDER_DELIVER_TOTAL_ZERO                 (30246, "发货总数量不能为0"),
    ORDER_IS_ACTUAL_DELIVERY                 (30247, "判断能否可以实际发货"),
    ORDER_ADD_INVENTORY_CHANGE_FAIL          (30118, "添加库存操作记录失败"),
    ORDER_NOT_SURE_DELIVER                   (30242, "未确认发货，无法修改物流信息"),
    ORDER_REFUND_GOODS_NOT_EXIST             (30396, "退款商品不存在"),
    ORDER_REFUND_QTY_MUST_LESSTHAN_ORDER_QTY (30397, "退款商品数量不能大于订单数量"),
    ORDER_REFUND_NOT_EXIST                   (30232, "退款记录不存在"),
    ORDER_ADD_ORDER_FAIL                     (30379, "新增订单失败"),
    ORDER_AMOUNT_IS_NEGATIVE           (30360, "订单金额不能为负数"),
    ORDER_ID_ERROR                           (30450, "身份证错误"),
    ORDER_GOODS_NOT_SOLD_TIME                (30377, "商品不在可售时间范围内"),
    ORDER_CONFIRM_RECEIPT              (30387, "该订单已确认收货"),
    ORDER_DELIVER_STATE_NOT_FINISHED         (30491, "发货状态不是发完货"),
    ORDER_ADDRESS_NOT_SUPPORT                (30492, "当前地址不发货"),


    //=========================↑↑↑↑↑↑↑↑↑↑↑↑订单模块↑↑↑↑↑↑↑↑↑↑↑↑=========================


    //=========================↓↓↓↓↓↓↓↓↓↓↓↓促销活动模块↓↓↓↓↓↓↓↓↓↓↓↓=========================


    ACT_POPUP_TODAY_ACTIVITED              (50420, "今日已弹窗"),
    ACT_POPUP_NOT_OPEN                     (50421, "弹窗未开启"),
    ACT_ADD_POPUP_FAIL                     (50422, "添加弹窗失败"),
    ACT_POPUP_NOT_EXIST                    (50423, "弹窗数据不存在"),
    ACT_NOT_EXIST                      (50426, "活动不存在"),
    ACT_ADD_EXCHANGE_GOODS_FAIL            (50427, "添加兑换中心列表失败"),
    ACT_ADD_EXCHANGE_RECORD_FAIL           (50428, "添加兑换记录失败"),
    ACT_EXCHANGE_GOODS_NOT_EXIST           (50429, "兑换中心列表不存在"),
    ACT_ADD_COUPON_FAIL                    (50430, "添加优惠券失败"),
    ACT_COUPON_NOT_EXIST                   (50431, "优惠券不存在"),
    ACT_UPDATE_COUPON_FAIL                 (50432, "修改优惠券状态失败"),
    ACT_DELETE_COUPON_FAIL                 (50433, "删除优惠券失败"),
    ACT_COUPON_ROBBED                      (50434, "优惠券被抢光了"),
    ACT_COUPON_RECEIVED                    (50435, "已经领取过优惠券"),
    ACT_EXCHANGE_GOODS_CONVERTED           (50436, "兑换中心列表已被用户兑换"),
    ACT_COUPON_RECEIVE_FAIL                (50437, "领取优惠券失败"),
    ACT_GOLD_QUANTITY_INSUFFICIENT         (50438, "云米数量不足"),
    ACT_EXCHANGE_GOODS_STOCK_INSUFFICIENT  (50439, "兑换商品库存不足"),
    ACT_EXCHANGE_GOODS_QUANTITY_LIMIT      (50440, "兑换商品数量超出限制"),
    ACT_SIGN_IN_REPEAT                     (50441, "不要重复签到"),
    ACT_SHARE_QUANTITY_LIMIT               (50442, "已达当日分享次数上限"),
    ACT_COUPON_RECEIVE_LIMIT               (50445, "该券已达您今日领取上限"),
    ACT_STEP_TEAM_NOT_EXIST            (50446, "微信步数活动队伍不存在"),
    ACT_UPDATE_FAIL                    (50447, "活动修改失败"),
    ACT_STEP_TEAM_FULL                 (50448, "微信步数活动队伍已满员"),
    ACT_STEP_TEAM_JOIN_LIMIT           (50449, "参与的微信步数活动次数超过当天最大值"),
    ACT_CHOP_NOT_EXIST                 (50453, "砍单活动不存在"),
    ACT_CHOP_NO_EDITOR                 (50454, "砍单活动以开始,不能编辑"),
    ACT_CHOP_NO_DELETE                 (50455, "砍单活动以开始,不能删除"),
    ACT_CHOP_NO_EDITOR_AND_DELETE      (50456, "砍单进行中,商品不能进行编辑或删除"),
    ACT_CHOP_GOODS_NOT_EXIST           (50457, "砍单活动商品不存在"),
    ACT_CHOP_FAIL                          (50475, "帮砍失败,服务器繁忙,请稍后再试"),
    ACT_CHOP_GOODS_RECORD_NOT_EXIST        (50458, "砍单记录不存在"),
    ACT_GOODS_CHOP_DONE                    (50459, "当前商品已砍单完成"),
    ACT_CHOP_ORDER_TIME_LIMIT              (50460, "超出砍单可下单时间"),
    ACT_CHOP_GOODS_SOLD_NO_LOWER_SHELF     (50461, "砍单商品售卖中,不能下架"),
    ACT_CHOP_GOODS_ADDED_THIS_GOODS        (50463, "本次砍单活动已添加过该商品,不能重复添加"),
    ACT_CHOP_HELPED_FRIEND                 (50464, "您已经帮好友砍过了"),
    ACT_CHOP_ORDER_HAS_BEEN_PLACED         (50465, "该砍单已下单不能重复下单"),
    ACT_CHOP_JOIN_LIMIT                    (50466, "您已超出该活动参与次数,请选择其它场次或邀请好友砍单"),
    ACT_UNKNOW_COUPON_LIMIT_TYPE           (50470, "优惠券门槛类型未知"),
    ACT_ERRORS_SELF_COUPON                 (50471, "不能领取自己送出的优惠券"),
    ACT_OUTOF_COUPON_COUNT                 (50472, "赠送数量超过拥有的数量"),
    ACT_UNKNOW_COUPON                      (50473, "UNKNOW_COUPON"),
    ACT_ERRORS_SHARE_COUPON                (50474, "优惠券赠送失败"),
    ACT_ERRORS_OUTOF_TIME                  (50476, "该优惠券已过期"),
    ACT_ERRORS_MONEY_LACK                  (50477, "订单金额不满足此优惠券使用条件"),
    ACT_ERRORS_GOODS_LACK                  (50478, "商品数量不满足此优惠券使用条件"),
    ACT_ERRORS_NOT_FIRST_ORDER             (50479, "该优惠券仅限首单使用"),
    ACT_ERRORS_COUPON_NOT_USE              (50480, "该优惠券只能用于赠送"),
    ACT_ERRORS_EXPRESS_CARD_NOT_EXIST      (50482, "免邮卡不存在"),
    ACT_ERRORS_COUPON_ROLL_COUNT           (50483, "取消分享数量异常"),
    ACT_ERRORS_OUT_CONVERT_COUNT           (50484, "超出当前奖励可兑换数量"),
    ACT_ERRORS_COUPON_NOT_USER             (50485, "操作的优惠券不属于当前用户"),
    ACT_GOLD_EXCHANGE_QUANTITY_LIMIT       (50496, "超出云米兑换邀请码限制数量"),
    ACT_SHARE_OVERDUE                      (50498, "该分享已过期"),
    ACT_ERRORS_DISTRIBUTE_USER             (50504, "优惠券发放对象不满足条件"),
    ACT_ERRORS_COUPON_COUNT                (50505, "优惠券库存少于发放数量"),
    ACT_ERRORS_COUPON_DISTRIBUTE_COUNT     (50506, "优惠券发放数量少于库存数量"),
    ACT_ACTIVITY_BANNER_NOT_EXIST      (50509, "小程序活动海报不存在"),
    ACT_MENTORING_PROGRAM_NOT_EXIST    (50524, "互助计划活动不存在"),
    ACT_ERRORS_ACT_DISCOUNT_RULE           (50533, "满减规则错误"),
    ACT_ERRORS_COUPON_RANGE                (50444, "请选择优惠券使用范围"),
    ACT_ERRORS_ACT_RULE                    (50425, "优惠金额不能大于满减金额"),
    ACT_COUPON_UPDATE_STATE_FAIL           (50424, "修改状态失败"),
    ACT_NOT_START_YET                  (50547, "活动未开始!"),
    ACT_HAS_END                        (50548, "活动已结束!"),
    ACT_ORG_ERR                        (50549, "店铺未参加活动!"),
    ACT_SUBSCRIBED                         (50550, "已订阅该直播活动!"),
    ACT_USER_YXCOUPON_ADD_FAIL             (50536, "添加用户优惠券失败!"),
    ACT_MATERIAL_NOT_EXIST                 (50551, "素材不存在"),
    ACT_GROUP_BOOKING_FINISHED             (50551, "素材不存在"),
    ACT_GROUP_BOOKING_RE_JOIN              (50552, "您已加入该团，无法继续操作"),
    ACT_GOLD_RECORD_ADD_FAIL               (50554, "新增云米失败"),


    //=========================↑↑↑↑↑↑↑↑↑↑↑↑促销活动模块↑↑↑↑↑↑↑↑↑↑↑↑=========================



    //=========================↓↓↓↓↓↓↓↓↓↓↓↓支付模块↓↓↓↓↓↓↓↓↓↓↓↓=========================


    ACT_TOP_UP_ACCOUNT					(60539, "充值账户错误!"),
    ACT_NOT_REPLY_PAY					(60564, "请勿重复支付~"),


    //=========================↑↑↑↑↑↑↑↑↑↑↑↑支付模块↑↑↑↑↑↑↑↑↑↑↑↑=========================



    //--------------第三方参数----------------
    RPC_SUCCESS(2, "操作成功"),
    RPC_SERVICE_DOWN(503, "微服务不可用"),
    RPC_SERVICE_TIME_OUT(504, "微服务调用超时"),
    //---------end--第三方参数----------------




    ;
    private Integer code;
    private String msg;

    private CodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}