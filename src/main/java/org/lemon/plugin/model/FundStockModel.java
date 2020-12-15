package org.lemon.plugin.model;

import java.util.Objects;

/**
 * 股票模型
 *
 * @author lry
 */
public class FundStockModel {
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 昨天收盘价/累计净值
     */
    private String yesterdayPrice = "0.00";
    /**
     * 现在价格
     */
    private String nowPrice = "0.00";
    /**
     * 涨跌金额
     */
    private String changePrice = "0.00";
    /**
     * 涨跌幅度
     */
    private String changePercent = "0.00";
    /**
     * 仓库数量
     */
    private String num = "0";
    /**
     * 成本单价
     */
    private String costPrice = "0.00";
    /**
     * 更新时间
     */
    private String lastTime;

    public static final String DEFAULT = "--";

    public FundStockModel() {
        this.name = DEFAULT;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(String yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(String changePrice) {
        this.changePrice = changePrice;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FundStockModel bean = (FundStockModel) o;
        return Objects.equals(code, bean.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
