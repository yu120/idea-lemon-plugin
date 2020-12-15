package org.lemon.plugin.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.intellij.ui.JBColor;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.lemon.plugin.FundFieldInfo;
import org.lemon.plugin.StockFieldInfo;
import org.lemon.plugin.model.FundStockModelConfig;
import org.lemon.plugin.model.SettingConfig;
import org.lemon.plugin.model.FundStockModel;
import org.lemon.plugin.utils.LogUtil;
import org.lemon.plugin.utils.PinYinUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHandler {

    protected SettingConfig settingConfig;

    private int category;
    protected boolean wrapperWidth;
    private Runnable task = null;
    private ScheduledThreadPoolExecutor executor = null;

    public AbstractHandler(int category) {
        this.category = category;
    }

    public void refreshHandle(SettingConfig settingConfig) {
        this.settingConfig = settingConfig;
        this.wrapperWidth = false;
        LogUtil.info("更新配置数据：" + this.getClass().getSimpleName());

        if (task == null) {
            this.executor = new ScheduledThreadPoolExecutor(1,
                    new ThreadFactoryBuilder().setNameFormat("stock").setDaemon(true).build());
        } else {
            executor.remove(task);
        }

        executor.scheduleWithFixedDelay(task = () -> {
            // 九点到下午4点才更新数据
            if (settingConfig.getTimeSpiderCheckBox()) {
                try {
                    LocalTime now = LocalTime.now();
                    LocalTime startTime = LocalTime.parse(settingConfig.getStartTimeTextField());
                    LocalTime endTime = LocalTime.parse(settingConfig.getEndTimeTextField());
                    if (now.isBefore(startTime) || now.isAfter(endTime)) {
                        return;
                    }
                } catch (Exception ignore) {
                    return;
                }
            }

            try {
                refreshData();
            } catch (Exception e) {
                LogUtil.info("抓取数据：" + ExceptionUtils.getFullStackTrace(e));
            }
        }, 0, settingConfig.getFundRefreshSecond(), TimeUnit.SECONDS);
    }

    /**
     * 更新字体颜色
     */
    protected void updateColors(JTable table, ColumnInfo columnInfo) {
        TableColumn tableColumn = table.getColumn(columnInfo.getName());
        if (tableColumn == null) {
            return;
        }

        tableColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                BigDecimal temp = BigDecimal.valueOf(0);
                try {
                    temp = new BigDecimal(value.toString().substring(0, value.toString().length() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (settingConfig.getInvisibleCheckBox()) {
                    // 隐身模式
                    setForeground(JBColor.decode(settingConfig.getInvisibleTextField()));
                } else {
                    int flag = temp.compareTo(BigDecimal.valueOf(columnInfo.getCompare()));
                    if (flag > 0) {
                        setForeground(settingConfig.getBlackWhiteCheckBox() ? JBColor.decode(
                                settingConfig.getOtherTextField()) : JBColor.decode(settingConfig.getRiseTextField()));
                    } else if (flag < 0) {
                        setForeground(settingConfig.getBlackWhiteCheckBox() ? JBColor.decode(
                                settingConfig.getOtherTextField()) : JBColor.decode(settingConfig.getFallTextField()));
                    } else {
                        setForeground(JBColor.decode(settingConfig.getOtherTextField()));
                    }
                }

                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    protected List<ColumnInfo> calculateColumnNames(SettingConfig settingConfig) throws Exception {
        List<ColumnInfo> columnNames = new ArrayList<>();
        Field[] fields = settingConfig.getClass().getDeclaredFields();
        for (Field field : fields) {
            FundFieldInfo fundFieldInfo = field.getAnnotation(FundFieldInfo.class);
            StockFieldInfo stockFieldInfo = field.getAnnotation(StockFieldInfo.class);
            if (fundFieldInfo != null && stockFieldInfo != null) {
                field.setAccessible(true);
                if ((boolean) field.get(settingConfig)) {
                    columnNames.add(category == 0 ? new ColumnInfo(fundFieldInfo) : new ColumnInfo(stockFieldInfo));
                }
            }
        }

        return columnNames;
    }

    /**
     * 返回的数组字段的顺序必须和SettingConfig类中字段的顺序保持一致
     */
    protected Object[] calculateColumnData(SettingConfig settingConfig, FundStockModelConfig fundStockModelConfig) {
        FundStockModel fundStockModel = fundStockModelConfig.getFundStockModel();
        SettingConfig.SettingConfigUnit settingConfigUnit = fundStockModelConfig.getSettingConfigUnit();

        List<Object> columnData = new ArrayList<>();
        // 计算是否有自定义名称
        String name = settingConfigUnit.getName();
        if (StringUtils.isBlank(name) || FundStockModel.DEFAULT.equals(name)) {
            name = fundStockModel.getName();
        }

        if (settingConfig.getNameCheckBox()) {
            columnData.add(settingConfig.getPinyinCheckBox() ? PinYinUtils.toPinYin(name) : name);
        }
        // 代码
        if (settingConfig.getCodeCheckBox()) {
            columnData.add(fundStockModel.getCode());
        }
        // 当前价格
        if (settingConfig.getNowPriceCheckBox()) {
            columnData.add(fundStockModel.getNowPrice());
        }
        // 涨跌金额
        if (settingConfig.getChangePriceCheckBox()) {
            columnData.add(fundStockModel.getChangePrice());
        }
        // 涨跌幅度
        if (settingConfig.getChangePercentCheckBox()) {
            columnData.add(fundStockModel.getChangePercent());
        }
        if (settingConfig.getCostNumCheckBox()) {
            columnData.add(settingConfigUnit.getNum());
        }
        if (settingConfig.getCostPriceCheckBox()) {
            columnData.add(settingConfigUnit.getPrice());
        }
        if (settingConfig.getTodayAmountCheckBox()) {
            BigDecimal stockTodayAmount = new BigDecimal(fundStockModel.getChangePrice())
                    .multiply(BigDecimal.valueOf(settingConfigUnit.getNum()));
            if (stockTodayAmount.doubleValue() >= 0) {
                columnData.add("+" + stockTodayAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                columnData.add(stockTodayAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        if (settingConfig.getAllAmountCheckBox()) {
            BigDecimal stockAllAmount = new BigDecimal(fundStockModel.getNowPrice())
                    .subtract(BigDecimal.valueOf(settingConfigUnit.getPrice()))
                    .multiply(BigDecimal.valueOf(settingConfigUnit.getNum()));
            if (stockAllAmount.doubleValue() >= 0) {
                columnData.add("+" + stockAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                columnData.add(stockAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        if (settingConfig.getAllRateOfReturnCheckBox()) {
            // 总收益=（当前价格-成本价格)×总数量
            BigDecimal stockAllAmount = new BigDecimal(fundStockModel.getNowPrice())
                    .subtract(BigDecimal.valueOf(settingConfigUnit.getPrice()))
                    .multiply(BigDecimal.valueOf(settingConfigUnit.getNum()));
            // 总成本
            BigDecimal costAllAmount = BigDecimal.valueOf(settingConfigUnit.getPrice()).multiply(BigDecimal.valueOf(settingConfigUnit.getNum()));
            BigDecimal allRate;
            if (costAllAmount.doubleValue() > 0) {
                allRate = BigDecimal.valueOf(100.00).multiply(stockAllAmount.divide(costAllAmount, 4, BigDecimal.ROUND_HALF_UP));
            } else {
                allRate = BigDecimal.valueOf(0.00);
            }
            if (allRate.doubleValue() >= 0) {
                columnData.add("+" + allRate.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            } else {
                columnData.add(allRate.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            }
        }
        if (settingConfig.getAllMarketValue()) {
            // 总市值=当前价格×总数量
            columnData.add(new BigDecimal(fundStockModel.getNowPrice())
                    .multiply(BigDecimal.valueOf(settingConfigUnit.getNum()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (settingConfig.getLastTimeCheckBox()) {
            columnData.add(fundStockModel.getLastTime());
        }

        return columnData.toArray();
    }

    protected abstract void refreshData() throws Exception;

    public static class ColumnInfo {
        private String name;
        private Integer width;
        private Boolean color;
        private double compare;

        public ColumnInfo() {
        }

        public ColumnInfo(StockFieldInfo stockFieldInfo) {
            this.name = stockFieldInfo.value();
            this.width = stockFieldInfo.width();
            this.color = stockFieldInfo.color();
            this.compare = stockFieldInfo.compare();
        }

        public ColumnInfo(FundFieldInfo fundFieldInfo) {
            this.name = fundFieldInfo.value();
            this.width = fundFieldInfo.width();
            this.color = fundFieldInfo.color();
            this.compare = fundFieldInfo.compare();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Boolean getColor() {
            return color;
        }

        public void setColor(Boolean color) {
            this.color = color;
        }

        public double getCompare() {
            return compare;
        }

        public void setCompare(double compare) {
            this.compare = compare;
        }
    }

}
