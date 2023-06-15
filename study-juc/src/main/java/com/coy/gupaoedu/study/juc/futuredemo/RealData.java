package com.coy.gupaoedu.study.juc.futuredemo;

/**
 * RealData表示真实的数据，就好比午餐，获得它的成本比较高，需要很多时间
 * <p>
 * 理解：真实的数据，需要一些时间来准备数据，也就是最终获得的结果。
 *
 * @author chenck
 * @date 2023/6/15 16:52
 */
public class RealData implements Data {

    protected final String result;

    public RealData(String para) {
        StringBuffer sb = new StringBuffer(para);
        //假设这里很慢很慢，构造RealData不是一个容易的事
        result = sb.toString();
    }

    public String getResult() {
        return result;
    }

}
