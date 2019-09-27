package com.coy.gupaoedu.study.mybatisplus.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pg_member_integral_record")
public class MemberIntegralRecordEntity {

    private static final long serialVersionUID = 3779117111091781709L;

    private Long id;

    private Integer dr;

    private String createPerson;

    private Date createTime;

    private String updatePerson;

    private Date updateTime;

    private Long tenantId;

    private Long instanceId;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 会员编号
     */
    private String cardCode;

    /**
     * 上级会员id
     */
    private Integer level;

    /**
     * 上级会员编号
     */
    private Integer maxLevel;

    /**
     * 个人积分
     */
    private BigDecimal amount1;

    /**
     * 个人累计积分
     */
    private BigDecimal amount2;

    /**
     * 0.5%积分
     */
    private BigDecimal amount3;

    /**
     * 游学积分
     */
    private BigDecimal amount4;

    /**
     * 持续进步奖分积分
     */
    private BigDecimal amount5;

    /**
     * 德列宝锅具活动编号
     */
    private String amount6Code;

    /**
     * 德列宝锅具个人积分
     */
    private BigDecimal amount6;

    /**
     * 德列宝锅具客户群积分
     */
    private BigDecimal amount7;

    /**
     * 德列宝锅具协助积分
     */
    private BigDecimal amount8;

    /**
     *  玛丽艳活动编号
     */
    private String amount9Code;

    /**
     * 玛丽艳个人积分
     */
    private BigDecimal amount9;

    /**
     * 玛丽艳客户群积分
     */
    private BigDecimal amount10;

    /**
     * 玛丽艳协助积分
     */
    private BigDecimal amount11;

    /**
     * 健康食品活动编号
     */
    private String amount12Code;

    /**
     * 健康食品个人积分
     */
    private BigDecimal amount12;

    /**
     * 健康食品客户群积分
     */
    private BigDecimal amount13;

    /**
     * 健康食品协助积分
     */
    private BigDecimal amount14;

    /**
     * 国际葡萄酒活动编号
     */
    private String amount15Code;

    /**
     * 国际葡萄酒个人积分
     */
    private BigDecimal amount15;

    /**
     * 国际葡萄酒客户群积分
     */
    private BigDecimal amount16;

    /**
     * 国际葡萄酒协助积分
     */
    private BigDecimal amount17;

    /**
     * 南非葡萄酒活动编号
     */
    private String amount18Code;

    /**
     * 南非葡萄酒个人积分
     */
    private BigDecimal amount18;

    /**
     * 南非葡萄酒客户群积分
     */
    private BigDecimal amount19;

    /**
     * 南非葡萄酒协助积分
     */
    private BigDecimal amount20;

    /**
     * 国产红酒活动编号
     */
    private String amount21Code;

    /**
     * 国产红酒个人积分
     */
    private BigDecimal amount21;

    /**
     * 国产红酒客户群积分
     */
    private BigDecimal amount22;

    /**
     * 国产红酒协助积分
     */
    private BigDecimal amount23;

    /**
     * 智利葡萄酒活动编号
     */
    private String amount24Code;

    /**
     * 智利葡萄酒个人积分
     */
    private BigDecimal amount24;

    /**
     * 智利葡萄酒客户群积分
     */
    private BigDecimal amount25;

    /**
     * 智利葡萄酒协助积分
     */
    private BigDecimal amount26;

    /**
     * 维E润手霜活动编号
     */
    private String amount27Code;

    /**
     * 维E润手霜个人积分
     */
    private BigDecimal amount27;

    /**
     * 维E润手霜客户群积分
     */
    private BigDecimal amount28;

    /**
     * 维E润手霜协助积分
     */
    private BigDecimal amount29;

    /**
     * ACC个人活动编号
     */
    private String amount30Code;

    /**
     * ACC个人积分
     */
    private BigDecimal amount30;

    /**
     * ACC客户群积分
     */
    private BigDecimal amount31;

    /**
     * ACC协助积分
     */
    private BigDecimal amount32;

    /**
     * 考核期内德列宝锅具积分（累计积分）
     */
    private BigDecimal amount33;

    /**
     * 德列宝锅活动开始时间
     */
    private Date amount33DateStart;
    /**
     * 德列宝锅活动结束时间
     */
    private Date amount33DateEnd;
    /**
     * 考核期内玛丽艳积分
     */
    private BigDecimal amount36;
    /**
     * 玛丽艳活动开始时间
     */
    private Date  amount36DateStart;
    /**
     * 玛丽艳活动结束时间
     */
    private Date  amount36DateEnd;
    /**
     * 考核期内健康食品积分
     */
    private BigDecimal amount39;
    /**
     * 健康食品活动开始时间
     */
    private Date  amount39DateStart;
    /**
     * 健康食品活动结束时间
     */
    private Date  amount39DateEnd;
    /**
     * 考核期内国际葡萄酒积分
     */
    private BigDecimal amount42;
    /**
     *  国际葡萄酒活动开始时间
     */
    private Date  amount42DateStart;
    /**
     * 国际葡萄酒活动结束时间
     */
    private Date  amount42DateEnd;
    /**
     * 考核期内南非葡萄酒积分
     */
    private BigDecimal amount45;
    /**
     * 南非葡萄酒活动开始时间
     */
    private Date amount45DateStart;
    /**
     * 南非葡萄酒活动结束时间
     */
    private Date  amount45DateEnd;
    /**
     * 考核期内国产红酒积
     */
    private BigDecimal amount48;
    /**
     * 国产红酒活动开始时间
     */
    private Date  amount48DateStart;
    /**
     * 国产红酒活动结束时间
     */
    private Date  amount48DateEnd;
    /**
     * 考核期内智利葡萄酒积分
     */
    private BigDecimal amount51;
    /**
     * 智利葡萄酒活动开始时间
     */
    private Date  amount51DateStart;
    /**
     * 智利葡萄酒活动结束时间
     */
    private Date  amount51DateEnd;
    /**
     * 考核期内维E润手霜积分
     */
    private BigDecimal amount54;
    /**
     * 维E润手霜活动开始时间
     */
    private Date  amount54DateStart;
    /**
     * 维E润手霜活动结束时间
     */
    private Date   amount54DateEnd;
    /**
     * 考核期内ACC积分
     */
    private BigDecimal amount57;
    /**
     * ACC活动开始时间
     */
    private Date amount57DateStart;
    /**
     * ACC活动结束时间
     */
    private Date  amount57DateEnd;


    /**
     *  DO.05%积分
     */
    private BigDecimal amount60;

    /**
     * 商城pv
     */
    private BigDecimal mallPv;

    /**
     *  团队积分
     */
//    private BigDecimal amount61;
}
