package org.lemon.plugin;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.lemon.plugin.model.SettingConfig;
import org.lemon.plugin.utils.LogUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DingSetting implements Configurable {

    private JPanel panel;

    private JTextArea fundTextArea;
    private JTextArea stockTextArea;

    private JTextField fundRefreshSecond;
    private JTextField stockRefreshSecond;

    // === 模式

    private JCheckBox pinyinCheckBox;
    private JCheckBox blackWhiteCheckBox;
    private JCheckBox logCheckBox;

    // === 股票

    private JCheckBox nameCheckBox;
    private JCheckBox codeCheckBox;
    private JCheckBox nowPriceCheckBox;
    private JCheckBox changePriceCheckBox;
    private JCheckBox changePercentCheckBox;
    private JCheckBox costNumCheckBox;
    private JCheckBox costPriceCheckBox;
    private JCheckBox todayAmountCheckBox;
    private JCheckBox allRateOfReturnCheckBox;
    private JCheckBox allAmountCheckBox;
    private JCheckBox allMarketValueCheckBox;
    private JCheckBox lastTimeCheckBox;
    /**
     * 隐身颜色代码
     */
    private JTextField invisibleTextField;
    /**
     * 隐身开关
     */
    private JCheckBox invisibleCheckBox;
    /**
     * 上涨颜色代码
     */
    private JTextField riseTextField;
    /**
     * 下跌颜色代码
     */
    private JTextField fallTextField;
    /**
     * 其它颜色代码
     */
    private JTextField otherTextField;

    /**
     * 开始时间
     */
    private JTextField startTimeTextField;
    /**
     * 结束时间
     */
    private JTextField endTimeTextField;
    /**
     * 是否开启按时间段抓取模式
     */
    private JCheckBox timeSpiderCheckBox;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Ding";
    }

    @Override
    public @Nullable JComponent createComponent() {
        String json = PropertiesComponent.getInstance().getValue("ding_plugin");
        SettingConfig settingConfig;
        if (json == null || json.length() == 0) {
            settingConfig = new SettingConfig();
        } else {
            settingConfig = JSON.parseObject(json, SettingConfig.class);
        }

        if (settingConfig.getFundSettingConfigStr() == null || settingConfig.getFundSettingConfigStr().length() == 0) {
            SettingConfig.SettingConfigUnit fund1 = new SettingConfig.SettingConfigUnit("医疗ETF", "512170", 0.00, 0.00);
            SettingConfig.SettingConfigUnit fund2 = new SettingConfig.SettingConfigUnit("消费ETF", "510150", 0.00, 0.00);
            List<SettingConfig.SettingConfigUnit> fundSettingConfigList = new ArrayList<>();
            fundSettingConfigList.add(fund1);
            fundSettingConfigList.add(fund2);
            settingConfig.setFundSettingConfigStr(JSON.toJSONString(fund1) + "\\n" + JSON.toJSONString(fund2));
            settingConfig.setFundSettingConfigs(fundSettingConfigList);
        }
        if (settingConfig.getStockSettingConfigStr() == null || settingConfig.getStockSettingConfigStr().length() == 0) {
            SettingConfig.SettingConfigUnit stock1 = new SettingConfig.SettingConfigUnit("上证指数", "sh000001", 0.00, 0.00);
            SettingConfig.SettingConfigUnit stock2 = new SettingConfig.SettingConfigUnit("深证成指", "sz399001", 0.00, 0.00);
            SettingConfig.SettingConfigUnit stock3 = new SettingConfig.SettingConfigUnit("创业板指", "sz399006", 0.00, 0.00);
            List<SettingConfig.SettingConfigUnit> settingConfigUnitList = new ArrayList<>();
            settingConfigUnitList.add(stock1);
            settingConfigUnitList.add(stock2);
            settingConfigUnitList.add(stock3);
            settingConfig.setStockSettingConfigStr(JSON.toJSONString(stock1) + "\\n" + JSON.toJSONString(stock2) + "\\n" + JSON.toJSONString(stock3));
            settingConfig.setSettingConfigUnits(settingConfigUnitList);
        }

        // 读取配置并渲染
        fundTextArea.setText(settingConfig.getFundSettingConfigStr());
        stockTextArea.setText(settingConfig.getStockSettingConfigStr());

        fundRefreshSecond.setText(String.valueOf(settingConfig.getFundRefreshSecond()));
        stockRefreshSecond.setText(String.valueOf(settingConfig.getStockRefreshSecond()));

        pinyinCheckBox.setSelected(settingConfig.getPinyinCheckBox());
        blackWhiteCheckBox.setSelected(settingConfig.getBlackWhiteCheckBox());
        logCheckBox.setSelected(settingConfig.getLogCheckBox());

        nameCheckBox.setSelected(settingConfig.getNameCheckBox());
        codeCheckBox.setSelected(settingConfig.getCodeCheckBox());
        nowPriceCheckBox.setSelected(settingConfig.getNowPriceCheckBox());
        changePriceCheckBox.setSelected(settingConfig.getChangePriceCheckBox());
        changePercentCheckBox.setSelected(settingConfig.getChangePercentCheckBox());
        costNumCheckBox.setSelected(settingConfig.getCostNumCheckBox());
        costPriceCheckBox.setSelected(settingConfig.getCostPriceCheckBox());
        todayAmountCheckBox.setSelected(settingConfig.getTodayAmountCheckBox());
        allRateOfReturnCheckBox.setSelected(settingConfig.getAllRateOfReturnCheckBox());
        allAmountCheckBox.setSelected(settingConfig.getAllAmountCheckBox());
        lastTimeCheckBox.setSelected(settingConfig.getLastTimeCheckBox());

        // 隐身颜色代码
        invisibleTextField.setText(settingConfig.getInvisibleTextField());
        // 隐身开关
        invisibleCheckBox.setSelected(settingConfig.getInvisibleCheckBox());
        // 上涨颜色代码
        riseTextField.setText(settingConfig.getRiseTextField());
        // 下跌颜色代码
        fallTextField.setText(settingConfig.getFallTextField());
        // 其它颜色代码
        otherTextField.setText(settingConfig.getOtherTextField());
        // 开始时间
        startTimeTextField.setText(settingConfig.getStartTimeTextField());
        // 结束时间
        endTimeTextField.setText(settingConfig.getEndTimeTextField());
        // 是否按时间段抓取
        timeSpiderCheckBox.setSelected(settingConfig.getTimeSpiderCheckBox());

        return panel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] fundLineArray = fundTextArea.getText().split("\\n");
        List<SettingConfig.SettingConfigUnit> fundSettingConfigs = new ArrayList<>();
        for (String fundLine : fundLineArray) {
            fundSettingConfigs.add(JSON.parseObject(fundLine, SettingConfig.SettingConfigUnit.class));
        }

        String[] stockLineArray = stockTextArea.getText().split("\\n");
        List<SettingConfig.SettingConfigUnit> settingConfigUnits = new ArrayList<>();
        for (String stockLine : stockLineArray) {
            settingConfigUnits.add(JSON.parseObject(stockLine, SettingConfig.SettingConfigUnit.class));
        }

        // 保存配置
        SettingConfig settingConfig = new SettingConfig();
        settingConfig.setFundSettingConfigs(fundSettingConfigs);
        settingConfig.setSettingConfigUnits(settingConfigUnits);

        settingConfig.setFundSettingConfigStr(fundTextArea.getText());
        settingConfig.setStockSettingConfigStr(stockTextArea.getText());

        settingConfig.setFundRefreshSecond(Integer.valueOf(fundRefreshSecond.getText()));
        settingConfig.setStockRefreshSecond(Integer.valueOf(stockRefreshSecond.getText()));

        settingConfig.setPinyinCheckBox(pinyinCheckBox.isSelected());
        settingConfig.setBlackWhiteCheckBox(blackWhiteCheckBox.isSelected());
        settingConfig.setLogCheckBox(logCheckBox.isSelected());

        settingConfig.setNameCheckBox(nameCheckBox.isSelected());
        settingConfig.setCodeCheckBox(codeCheckBox.isSelected());
        settingConfig.setNowPriceCheckBox(nowPriceCheckBox.isSelected());
        settingConfig.setChangePriceCheckBox(changePriceCheckBox.isSelected());
        settingConfig.setChangePercentCheckBox(changePercentCheckBox.isSelected());
        settingConfig.setCostNumCheckBox(costNumCheckBox.isSelected());
        settingConfig.setCostPriceCheckBox(costPriceCheckBox.isSelected());
        settingConfig.setTodayAmountCheckBox(todayAmountCheckBox.isSelected());
        settingConfig.setAllRateOfReturnCheckBox(allRateOfReturnCheckBox.isSelected());
        settingConfig.setAllAmountCheckBox(allAmountCheckBox.isSelected());
        settingConfig.setAllMarketValue(allMarketValueCheckBox.isSelected());
        settingConfig.setLastTimeCheckBox(lastTimeCheckBox.isSelected());

        // 隐身颜色代码
        settingConfig.setInvisibleTextField(invisibleTextField.getText());
        // 隐身开关
        settingConfig.setInvisibleCheckBox(invisibleCheckBox.isSelected());
        // 上涨颜色代码
        settingConfig.setRiseTextField(riseTextField.getText());
        // 下跌颜色代码
        settingConfig.setFallTextField(fallTextField.getText());
        // 其它颜色代码
        settingConfig.setOtherTextField(otherTextField.getText());
        // 开始时间
        settingConfig.setStartTimeTextField(startTimeTextField.getText());
        // 结束时间
        settingConfig.setEndTimeTextField(endTimeTextField.getText());
        // 是否按时间段抓取
        settingConfig.setTimeSpiderCheckBox(timeSpiderCheckBox.isSelected());

        String json = JSON.toJSONString(settingConfig);
        PropertiesComponent.getInstance().setValue("ding_plugin", json);
        LogUtil.info("Ding更新了配置：" + json);
        if (FundWindow.FUND_WINDOW != null) {
            FundWindow.FUND_WINDOW.onInit();
            FundWindow.FUND_WINDOW.getStockWindow().onInit();
        }
    }

}
