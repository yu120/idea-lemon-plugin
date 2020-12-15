package org.lemon.plugin.model;

import org.lemon.plugin.FundFieldInfo;
import org.lemon.plugin.StockFieldInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SettingConfig implements Serializable {

    private List<SettingConfigUnit> fundSettingConfigs = new ArrayList<>();
    private List<SettingConfigUnit> settingConfigUnits = new ArrayList<>();

    private String fundSettingConfigStr = "";
    private String stockSettingConfigStr = "";

    private Integer fundRefreshSecond = 10;
    private Integer stockRefreshSecond = 10;

    // === 模式

    private Boolean pinyinCheckBox = false;
    private Boolean blackWhiteCheckBox = false;
    private Boolean logCheckBox = false;

    // === 股票

    @FundFieldInfo(value = "名称", width = 120)
    @StockFieldInfo("名称")
    private Boolean nameCheckBox = true;
    @FundFieldInfo("代码")
    @StockFieldInfo("代码")
    private Boolean codeCheckBox = true;
    @FundFieldInfo(value = "估算净值")
    @StockFieldInfo(value = "当前价格")
    private Boolean nowPriceCheckBox = true;
    @FundFieldInfo(value = "估算涨额", color = true)
    @StockFieldInfo(value = "涨跌金额", color = true)
    private Boolean changePriceCheckBox = true;
    @FundFieldInfo(value = "估算幅度", color = true)
    @StockFieldInfo(value = "涨跌幅度", color = true)
    private Boolean changePercentCheckBox = true;
    @FundFieldInfo("持有份额")
    @StockFieldInfo("持有股数")
    private Boolean costNumCheckBox = true;
    @FundFieldInfo("成本价格")
    @StockFieldInfo("成本价格")
    private Boolean costPriceCheckBox = true;
    @FundFieldInfo(value = "今日盈亏", color = true)
    @StockFieldInfo(value = "今日盈亏", color = true)
    private Boolean todayAmountCheckBox = true;
    @FundFieldInfo(value = "累计盈亏", color = true)
    @StockFieldInfo(value = "累计盈亏", color = true)
    private Boolean allAmountCheckBox = true;
    @FundFieldInfo(value = "累计收益率", color = true)
    @StockFieldInfo(value = "累计收益率", color = true)
    private Boolean allRateOfReturnCheckBox = true;
    @FundFieldInfo(value = "更新时间", width = 120)
    @StockFieldInfo(value = "更新时间", width = 130)
    private Boolean lastTimeCheckBox = true;

    /**
     * 隐身颜色代码
     */
    private String invisibleTextField = "#D8D8D8";
    /**
     * 隐身开关
     */
    private Boolean invisibleCheckBox = false;
    /**
     * 上涨颜色代码
     */
    private String riseTextField = "#FF6347";
    /**
     * 下跌颜色代码
     */
    private String fallTextField = "#3CB371";
    /**
     * 其它颜色代码
     */
    private String otherTextField = "#A9A9A9";
    /**
     * 开始时间
     */
    private String startTimeTextField = "08:30:00";
    /**
     * 结束时间
     */
    private String endTimeTextField = "16:00:00";
    /**
     * 是否开启按时间段抓取模式
     */
    private Boolean timeSpiderCheckBox = true;

    public List<SettingConfigUnit> getFundSettingConfigs() {
        return fundSettingConfigs;
    }

    public void setFundSettingConfigs(List<SettingConfigUnit> fundSettingConfigs) {
        this.fundSettingConfigs = fundSettingConfigs;
    }

    public List<SettingConfigUnit> getSettingConfigUnits() {
        return settingConfigUnits;
    }

    public void setSettingConfigUnits(List<SettingConfigUnit> settingConfigUnits) {
        this.settingConfigUnits = settingConfigUnits;
    }

    public String getFundSettingConfigStr() {
        return fundSettingConfigStr;
    }

    public void setFundSettingConfigStr(String fundSettingConfigStr) {
        this.fundSettingConfigStr = fundSettingConfigStr;
    }

    public String getStockSettingConfigStr() {
        return stockSettingConfigStr;
    }

    public void setStockSettingConfigStr(String stockSettingConfigStr) {
        this.stockSettingConfigStr = stockSettingConfigStr;
    }

    public Integer getFundRefreshSecond() {
        return fundRefreshSecond;
    }

    public void setFundRefreshSecond(Integer fundRefreshSecond) {
        this.fundRefreshSecond = fundRefreshSecond;
    }

    public Integer getStockRefreshSecond() {
        return stockRefreshSecond;
    }

    public void setStockRefreshSecond(Integer stockRefreshSecond) {
        this.stockRefreshSecond = stockRefreshSecond;
    }

    public Boolean getPinyinCheckBox() {
        return pinyinCheckBox;
    }

    public void setPinyinCheckBox(Boolean pinyinCheckBox) {
        this.pinyinCheckBox = pinyinCheckBox;
    }

    public Boolean getBlackWhiteCheckBox() {
        return blackWhiteCheckBox;
    }

    public void setBlackWhiteCheckBox(Boolean blackWhiteCheckBox) {
        this.blackWhiteCheckBox = blackWhiteCheckBox;
    }

    public Boolean getLogCheckBox() {
        return logCheckBox;
    }

    public void setLogCheckBox(Boolean logCheckBox) {
        this.logCheckBox = logCheckBox;
    }

    public Boolean getNameCheckBox() {
        return nameCheckBox;
    }

    public void setNameCheckBox(Boolean nameCheckBox) {
        this.nameCheckBox = nameCheckBox;
    }

    public Boolean getCodeCheckBox() {
        return codeCheckBox;
    }

    public void setCodeCheckBox(Boolean codeCheckBox) {
        this.codeCheckBox = codeCheckBox;
    }

    public Boolean getNowPriceCheckBox() {
        return nowPriceCheckBox;
    }

    public void setNowPriceCheckBox(Boolean nowPriceCheckBox) {
        this.nowPriceCheckBox = nowPriceCheckBox;
    }

    public Boolean getChangePriceCheckBox() {
        return changePriceCheckBox;
    }

    public void setChangePriceCheckBox(Boolean changePriceCheckBox) {
        this.changePriceCheckBox = changePriceCheckBox;
    }

    public Boolean getChangePercentCheckBox() {
        return changePercentCheckBox;
    }

    public void setChangePercentCheckBox(Boolean changePercentCheckBox) {
        this.changePercentCheckBox = changePercentCheckBox;
    }

    public Boolean getCostNumCheckBox() {
        return costNumCheckBox;
    }

    public void setCostNumCheckBox(Boolean costNumCheckBox) {
        this.costNumCheckBox = costNumCheckBox;
    }

    public Boolean getCostPriceCheckBox() {
        return costPriceCheckBox;
    }

    public void setCostPriceCheckBox(Boolean costPriceCheckBox) {
        this.costPriceCheckBox = costPriceCheckBox;
    }

    public Boolean getTodayAmountCheckBox() {
        return todayAmountCheckBox;
    }

    public void setTodayAmountCheckBox(Boolean todayAmountCheckBox) {
        this.todayAmountCheckBox = todayAmountCheckBox;
    }

    public Boolean getAllRateOfReturnCheckBox() {
        return allRateOfReturnCheckBox;
    }

    public void setAllRateOfReturnCheckBox(Boolean allRateOfReturnCheckBox) {
        this.allRateOfReturnCheckBox = allRateOfReturnCheckBox;
    }

    public Boolean getAllAmountCheckBox() {
        return allAmountCheckBox;
    }

    public void setAllAmountCheckBox(Boolean allAmountCheckBox) {
        this.allAmountCheckBox = allAmountCheckBox;
    }

    public Boolean getLastTimeCheckBox() {
        return lastTimeCheckBox;
    }

    public void setLastTimeCheckBox(Boolean lastTimeCheckBox) {
        this.lastTimeCheckBox = lastTimeCheckBox;
    }

    public String getInvisibleTextField() {
        return invisibleTextField;
    }

    public void setInvisibleTextField(String invisibleTextField) {
        this.invisibleTextField = invisibleTextField;
    }

    public Boolean getInvisibleCheckBox() {
        return invisibleCheckBox;
    }

    public void setInvisibleCheckBox(Boolean invisibleCheckBox) {
        this.invisibleCheckBox = invisibleCheckBox;
    }

    public String getRiseTextField() {
        return riseTextField;
    }

    public void setRiseTextField(String riseTextField) {
        this.riseTextField = riseTextField;
    }

    public String getFallTextField() {
        return fallTextField;
    }

    public void setFallTextField(String fallTextField) {
        this.fallTextField = fallTextField;
    }

    public String getOtherTextField() {
        return otherTextField;
    }

    public void setOtherTextField(String otherTextField) {
        this.otherTextField = otherTextField;
    }

    public String getStartTimeTextField() {
        return startTimeTextField;
    }

    public void setStartTimeTextField(String startTimeTextField) {
        this.startTimeTextField = startTimeTextField;
    }

    public String getEndTimeTextField() {
        return endTimeTextField;
    }

    public void setEndTimeTextField(String endTimeTextField) {
        this.endTimeTextField = endTimeTextField;
    }

    public Boolean getTimeSpiderCheckBox() {
        return timeSpiderCheckBox;
    }

    public void setTimeSpiderCheckBox(Boolean timeSpiderCheckBox) {
        this.timeSpiderCheckBox = timeSpiderCheckBox;
    }

    public static class SettingConfigUnit implements Serializable {
        private String name;
        private String code;
        private Double num;
        private Double price;

        public SettingConfigUnit() {

        }

        public SettingConfigUnit(String name, String code, Double num, Double price) {
            this.name = name;
            this.code = code;
            this.num = num;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Double getNum() {
            return num;
        }

        public void setNum(Double num) {
            this.num = num;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

}
