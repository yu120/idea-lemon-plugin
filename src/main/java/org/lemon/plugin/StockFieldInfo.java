package org.lemon.plugin;

import java.lang.annotation.*;

/**
 * 股票字段
 *
 * @author lry
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StockFieldInfo {

    /**
     * 列标题
     */
    String value();

    /**
     * 是否颜色渲染
     */
    boolean color() default false;

    /**
     * 列宽度,0表示默认
     */
    int width() default 0;

    /**
     * 比较值,0表示默认
     */
    double compare() default 0.00;

}