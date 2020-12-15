package org.lemon.plugin.handler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.lemon.plugin.model.FundStockModelConfig;
import org.lemon.plugin.model.SettingConfig;
import org.lemon.plugin.model.FundStockModel;
import org.lemon.plugin.utils.HttpClientPool;
import org.lemon.plugin.utils.LogUtil;
import org.lemon.plugin.utils.PinYinUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class TencentStockHandler extends AbstractHandler {

    private JTable table;
    private JLabel label;
    private Color tableColor;

    public TencentStockHandler(JTable table, JLabel label) {
        super(1);

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
        List<FundStockModelConfig> fundStockModelConfigList = this.getParseData(settingConfig.getSettingConfigUnits());
        if (settingConfig.getLogCheckBox()) {
            LogUtil.info("抓取股票数据：" + JSON.toJSONString(fundStockModelConfigList));
        }

        // Swing执行
        SwingUtilities.invokeLater(() -> {
            try {
                // 获取列名和宽度
                List<ColumnInfo> columnInfos = calculateColumnNames(settingConfig);
                String[] columnNames = new String[columnInfos.size()];
                for (int i = 0; i < columnInfos.size(); i++) {
                    ColumnInfo columnInfo = columnInfos.get(i);
                    columnNames[i] = settingConfig.getPinyinCheckBox() ? PinYinUtils.toPinYin(columnInfo.getName()) : columnInfo.getName();
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
                        TableColumn tableColumn = table.getColumnModel().getColumn(i);
                        if (width != null && width > 0) {
                            tableColumn.setWidth(width);
                            tableColumn.setPreferredWidth(width);
                        }
                    }
                    this.wrapperWidth = true;
                }
            } catch (Exception e) {
                LogUtil.info("股票处理异常：" + ExceptionUtils.getFullStackTrace(e));
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
        StringBuilder codesStr = new StringBuilder();
        for (int i = 0; i < settingConfigList.size(); i++) {
            SettingConfig.SettingConfigUnit settingConfigUnit = settingConfigList.get(i);
            codesStr.append(settingConfigUnit.getCode());
            if (i < settingConfigList.size() - 1) {
                codesStr.append(',');
            }
        }

        String result = HttpClientPool.getHttpClient().get("http://qt.gtimg.cn/q=" + codesStr.toString().toLowerCase());
        if (settingConfig.getLogCheckBox()) {
            LogUtil.info("第三方股票数据：" + result);
        }

        String[] lines = result.split("\n");
        Map<String, FundStockModel> modelMap = new HashMap<>();
        for (String line : lines) {
            String[] values = line.substring(line.indexOf("=") + 2, line.length() - 2).split("~");
            FundStockModel fundStockModel = new FundStockModel();
            fundStockModel.setCode(line.substring(line.indexOf("_") + 1, line.indexOf("=")));
            fundStockModel.setName(values[1]);
            fundStockModel.setNowPrice(values[3]);
            fundStockModel.setYesterdayPrice(values[4]);
            fundStockModel.setChangePrice(values[31].startsWith("-") ? values[31] : "+" + values[31]);
            fundStockModel.setChangePercent((values[32].startsWith("-") ? values[32] : ("+" + values[32])) + "%");
            fundStockModel.setLastTime(LocalDateTime.parse(values[30], DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            modelMap.put(fundStockModel.getCode(), fundStockModel);
        }

        List<FundStockModelConfig> resultSettingConfigList = new ArrayList<>();
        for (SettingConfig.SettingConfigUnit settingConfigUnit : settingConfigList) {
            FundStockModel fundStockModel = new FundStockModel();
            fundStockModel.setCode(settingConfigUnit.getCode());
            FundStockModel temp = modelMap.get(settingConfigUnit.getCode());
            if (temp != null) {
                fundStockModel.setName(temp.getName());
                fundStockModel.setNowPrice(temp.getNowPrice());
                fundStockModel.setChangePrice(temp.getChangePrice());
                fundStockModel.setChangePercent(temp.getChangePercent());
                fundStockModel.setLastTime(temp.getLastTime());
                resultSettingConfigList.add(new FundStockModelConfig(fundStockModel, settingConfigUnit));
            }
        }

        return resultSettingConfigList;
    }

}
