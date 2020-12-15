package org.lemon.plugin.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.lemon.plugin.model.FundStockModelConfig;
import org.lemon.plugin.model.SettingConfig;
import org.lemon.plugin.model.FundStockModel;
import org.lemon.plugin.utils.HttpClientPool;
import org.lemon.plugin.utils.LogUtil;
import org.lemon.plugin.utils.PinYinUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class TianTianFundHandler extends AbstractHandler {

    private JTable table;
    private JLabel label;
    private Color tableColor;

    public TianTianFundHandler(JTable table, JLabel label) {
        super(0);

        this.table = table;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Fix tree row height
        FontMetrics metrics = table.getFontMetrics(table.getFont());
        table.setRowHeight(Math.max(table.getRowHeight(), metrics.getHeight()));
        this.label = label;
    }

    @Override
    protected void refreshData() throws Exception {
        // 远程读取数据
        List<FundStockModelConfig> fundStockModelConfigList = this.getParseData(settingConfig.getFundSettingConfigs());
        if (settingConfig.getLogCheckBox()) {
            LogUtil.info("抓取股票数据：" + JSON.toJSONString(fundStockModelConfigList));
        }

        SwingUtilities.invokeLater(() -> {
            try {
                List<ColumnInfo> columnInfos = calculateColumnNames(settingConfig);
                String[] columnNames = new String[columnInfos.size()];
                for (int i = 0; i < columnInfos.size(); i++) {
                    if (settingConfig.getPinyinCheckBox()) {
                        columnNames[i] = PinYinUtils.toPinYin(columnInfos.get(i).getName());
                    } else {
                        columnNames[i] = columnInfos.get(i).getName();
                    }
                }

                // 设置数据
                Object[][] tempData = new Object[fundStockModelConfigList.size()][columnNames.length];
                for (int i = 0; i < fundStockModelConfigList.size(); i++) {
                    FundStockModelConfig fundStockModelConfig = fundStockModelConfigList.get(i);
                    // 设置每一行数据信息
                    tempData[i] = super.calculateColumnData(settingConfig, fundStockModelConfig);
                }
                table.setModel(new DefaultTableModel(tempData, columnNames));

                // 更新颜色
                for (ColumnInfo columnInfo : columnInfos) {
                    if (columnInfo.getColor() != null && columnInfo.getColor()) {
                        updateColors(table, columnInfo);
                    }
                    // 隐身模式
                    if (settingConfig.getInvisibleCheckBox()) {
                        updateColors(table, columnInfo);
                    }
                }
                // 隐身模式
                if (settingConfig.getInvisibleCheckBox()) {
                    if (tableColor == null) {
                        tableColor = table.getTableHeader().getForeground();
                    }
                    table.getTableHeader().setForeground(Color.decode(settingConfig.getInvisibleTextField()));
                } else {
                    table.getTableHeader().setForeground(tableColor);
                }

                // 重新设置表宽度
                if (!wrapperWidth) {
                    for (int i = 0; i < columnNames.length; i++) {
                        Integer width = columnInfos.get(i).getWidth();
                        if (width != null && width > 0) {
                            table.getColumnModel().getColumn(i).setWidth(width);
                            table.getColumnModel().getColumn(i).setPreferredWidth(width);
                        }
                    }
                    this.wrapperWidth = true;
                }
            } catch (Exception e) {
                LogUtil.info("基金处理异常：" + ExceptionUtils.getFullStackTrace(e));
            }
        });

        SwingUtilities.invokeLater(() -> label.setText("最后刷新时间:" +
                new SimpleDateFormat("yyyy-MM-dd HH:mm ss").format(new Date())));
    }

    /**
     * 远程获取数据并解析结果
     *
     * @return
     * @throws Exception
     */
    private List<FundStockModelConfig> getParseData(List<SettingConfig.SettingConfigUnit> settingConfigList) throws Exception {
        List<FundStockModelConfig> resultSettingConfigList = new ArrayList<>();
        for (SettingConfig.SettingConfigUnit settingConfigUnit : settingConfigList) {
            String result = HttpClientPool.getHttpClient().get("http://fundgz.1234567.com.cn/js/" +
                    settingConfigUnit.getCode() + ".js?rt=" + System.currentTimeMillis());
            if (settingConfig.getLogCheckBox()) {
                LogUtil.info("第三方股票数据：" + result);
            }

            FundStockModel fundStockModel = new FundStockModel();
            fundStockModel.setCode(settingConfigUnit.getCode());

            JSONObject jsonObject = JSON.parseObject(result.substring(8, result.length() - 2));
            if (jsonObject != null) {
                String name = jsonObject.getString("name");
                BigDecimal yesterdayPriceBigDecimal = jsonObject.getBigDecimal("dwjz");
                BigDecimal nowPriceBigDecimal = jsonObject.getBigDecimal("gsz");
                BigDecimal changePercentBigDecimal = jsonObject.getBigDecimal("gszzl");
                String lastTime = jsonObject.getString("gztime");

                fundStockModel.setName(name);
                fundStockModel.setYesterdayPrice(yesterdayPriceBigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                fundStockModel.setNowPrice(nowPriceBigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                fundStockModel.setChangePrice(nowPriceBigDecimal.subtract(yesterdayPriceBigDecimal).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                String changePercent = changePercentBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                fundStockModel.setChangePercent((changePercent.startsWith("-") ? changePercent : ("+" + changePercent)) + "%");
                fundStockModel.setLastTime(lastTime);
            }
            resultSettingConfigList.add(new FundStockModelConfig(fundStockModel, settingConfigUnit));
        }

        return resultSettingConfigList;
    }

}
