package org.lemon.plugin.utils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

public class PinYinUtils {

    public static String toPinYin(String input) {
        return Pinyin.toPinyin(input, "_").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(toPinYinFirst("张三撒"));
    }

    public static String toPinYinFirst(String input) {
        String[] strArray = Pinyin.toPinyin(input, "_").toLowerCase().split("_");
        List<String> firstList = new ArrayList<>();
        for (String str : strArray) {
            firstList.add(str.substring(0, 1));
        }

        return String.join("", firstList);
    }

}
