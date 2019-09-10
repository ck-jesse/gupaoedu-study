package com.coy.gupaoedu.study.mybatisplus.common.domain;

/**
 * 针对分页的请求入参对象
 * <p>
 * 注：若接口有业务参数，可继承该类，若无，则可直接使用
 *
 * @author chenck
 * @date 2019/9/5 10:36
 */
public class BasePageRequest extends BaseRequest {

    private static final long serialVersionUID = 922359575400359797L;
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
    /**
     * 页下标
     */
    private Integer pageNum = 1;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
