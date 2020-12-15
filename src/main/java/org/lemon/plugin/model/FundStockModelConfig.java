package org.lemon.plugin.model;

import java.io.Serializable;

public class FundStockModelConfig implements Serializable {

    private FundStockModel fundStockModel;
    private SettingConfig.SettingConfigUnit settingConfigUnit;

    public FundStockModelConfig() {
    }

    public FundStockModelConfig(FundStockModel fundStockModel, SettingConfig.SettingConfigUnit settingConfigUnit) {
        this.fundStockModel = fundStockModel;
        this.settingConfigUnit = settingConfigUnit;
    }

    public FundStockModel getFundStockModel() {
        return fundStockModel;
    }

    public void setFundStockModel(FundStockModel fundStockModel) {
        this.fundStockModel = fundStockModel;
    }

    public SettingConfig.SettingConfigUnit getSettingConfigUnit() {
        return settingConfigUnit;
    }

    public void setSettingConfigUnit(SettingConfig.SettingConfigUnit settingConfigUnit) {
        this.settingConfigUnit = settingConfigUnit;
    }
    
}
