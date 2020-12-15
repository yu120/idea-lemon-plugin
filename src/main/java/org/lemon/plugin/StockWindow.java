package org.lemon.plugin;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import org.lemon.plugin.handler.TencentStockHandler;
import org.lemon.plugin.model.SettingConfig;

import javax.swing.*;

/**
 * 股票窗口
 *
 * @author lry
 */
public class StockWindow {

    private JPanel panel1;
    private JTable table1;
    private JLabel label;
    private JButton refreshButton;
    private TencentStockHandler handler;

    public JPanel getPanel1() {
        return panel1;
    }

    public StockWindow() {
        handler = new TencentStockHandler(table1, label);
        refreshButton.addActionListener(e -> {
            onInit();
        });
    }

    public void onInit() {
        String json = PropertiesComponent.getInstance().getValue("ding_plugin");
        if (json == null || json.length() == 0) {
            return;
        }

        SettingConfig settingConfig = JSON.parseObject(json, SettingConfig.class);
        handler.refreshHandle(settingConfig);
    }

}
