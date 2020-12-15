package org.lemon.plugin;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.lemon.plugin.model.SettingConfig;
import org.lemon.plugin.utils.LogUtil;
import org.lemon.plugin.handler.TianTianFundHandler;

import javax.swing.*;

public class FundWindow implements ToolWindowFactory {

    public static FundWindow FUND_WINDOW;

    private JPanel mPanel;
    private JTable table1;
    private JButton refreshButton;
    private JLabel label;

    private TianTianFundHandler handler;
    private StockWindow stockWindow = new StockWindow();

    public StockWindow getStockWindow() {
        return stockWindow;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        FUND_WINDOW = this;

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mPanel, "Fund", false);
        Content contentStock = contentFactory.createContent(stockWindow.getPanel1(), "Stock", false);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().addContent(contentStock);

        LogUtil.setProject(project);

        refreshButton.addActionListener(e -> {
            onInit();
        });
    }

    @Override
    public void init(ToolWindow window) {
        handler = new TianTianFundHandler(table1, label);
        onInit();
        stockWindow.onInit();
    }

    public void onInit() {
        String json = PropertiesComponent.getInstance().getValue("ding_plugin");
        if (json == null || json.length() == 0) {
            return;
        }

        SettingConfig settingConfig = JSON.parseObject(json, SettingConfig.class);
        handler.refreshHandle(settingConfig);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

}
