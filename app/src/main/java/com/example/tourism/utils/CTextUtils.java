package com.example.tourism.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本字符串获取相关工具类
 */
public class CTextUtils {

    /**
     * 截取用户个人主页URL 例如https://travel.qunar.com/space/151869121@qunar#js_notes#1
     * 结果为https://travel.qunar.com/space/151869121
     *
     * @param pUrl
     * @return
     */
    public static String getPersonHomePage(String pUrl) {
        if (pUrl.contains("@")) {
            String[] array = pUrl.split("@");
            return array[0];
        }
        return pUrl;
    }

    /**
     * 提取文章编号 例如 https://travel.qunar.com/space/151869121@qunar#js_notes#1
     * 转成 151869121
     *
     * @param pUrl 传入的URL地址
     * @return
     */
    public static String getTitleId(String pUrl) {
        if (pUrl.contains("@")) {
            String[] array = pUrl.split("@");
            //lastIndexOf(String str)：返回此字符串中最后一次出现的索引(从0开始)，没有则返回-1
            int index = array[0].lastIndexOf("/");
            //substring(int beginIndex)：字符串截取(截取的第一个数的索引就是1)
            return array[0].substring(index + 1);
        }
        return pUrl;
    }

    /**
     * 截取日期时间 例如2019-09-07 出发 |共3天
     * 截取后2019-09-07
     * @param pStr
     * @return
     */
    public static String getTime(String pStr) {
        if (pStr.contains(" ")) {
            String[] array = pStr.split(" ");
            return array[0];
        }
        return pStr;
    }

    /**
     * 截取从|后面的所有字符
     * @param pStr
     * @return
     */
    public static String getDays(String pStr) {
        return pStr.substring(pStr.lastIndexOf("|")+1);
    }

    /**
     * 多空格字符串截取使用List集合
     *
     * @param pStr
     * @return
     */
    public static List<String> getStrList(String pStr) {
        List<String> list = new ArrayList<>();
        if (pStr.contains(" ")) {
            String[] array = pStr.split(" ");
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);
            }
            return list;
        }
        return list;
    }

    /**
     * 截取途径字符串 例如 三亚 第一市场 > 蜈支洲岛 > 西岛：表示途径和行程
     * 三亚 第一市场：表示两个途径地点
     * 先把途径和行程进行拆开
     * 再把途径字段进行拆开
     *
     * @param pStr
     * @return
     */
    public static List<String> getChannelString(String pStr) {
        List<String> list = new ArrayList<>();
        if (pStr.contains(" > ")) {
            String[] array = pStr.split(" > ");
            if (array[0].contains(" ")) {
                String[] temp = array[0].split(" ");
                String channel = "";
                if (temp.length > 1) {
                    for (String str : temp) {
                        channel += temp;
                    }
                }
                for (int i = 0; i < temp.length; i++) {
                    list.add(temp[i]);
                }
                return list;
            }
            return list;
        }
        return list;
    }

    /**
     * 截取途径字符串
     * @param pStr
     * @return
     */
    public static String getCannelStr(String pStr) {
        if (pStr.contains("行程")) {
            String[] array = pStr.split("行程");
            return array[0];
        }
        return pStr;
    }

    public static String getTripStr(String pStr) {
        if (pStr.contains("行程")) {
            String[] array = pStr.split("行程");
            return array[array.length-1];
        }
        return pStr;
    }

    /**
     * 对长字符串行程进行拆解，拆解后添加到List集合中
     * 例如 第一市场 蜈支洲岛 西岛 亚龙湾 海棠湾 三亚湾 拖伞  多个行程
     * 拆解后需要进行一一对应
     * @param pStr
     * @return
     */
    public static List<String> getTripsString(String pStr) {
        List<String> list = new ArrayList<>();
        if (pStr.contains(" ")) {
            String[] array = pStr.split(" ");
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);
            }
            return list;
        }
        return list;
    }

    public static String getBrowses(String pStr) {
        if (pStr.contains("\uE09D")) {
            String[] array = pStr.split("\uE09D");
            return array[array.length-1];
        }
        return pStr;
    }

    public static String getFoubles(String pStr) {
        if (pStr.contains("\uF04F")) {
            String[] array = pStr.split("\uF04F");
            return array[array.length-1];
        }
        return pStr;
    }

    public static String getComment(String pStr) {
        if (pStr.contains("\uF060")) {
            String[] array = pStr.split("\uF060");
            return array[array.length-1];
        }
        return pStr;
    }

    public static String getBlankSpaces(String pStr, int type) {
        if (pStr.contains(" ")) {
            String[] array = pStr.split(" ");
            if (type == 0) return array[0];
            else return array[array.length - 1];
        }
        return pStr;
    }

    /**
     * 字符串截取
     *
     * @param pStr 返回的数据
     * @param type 返回的索引类型
     * @return
     */
    public static String getIncomingStringSplits(String pStr, int type) {
        if (pStr.contains("|")) {
            String[] array = pStr.split("\\|");
            return array[type];
        }
        return pStr;
    }

    /**
     * 传入中间字符串截取一串字符，
     * 并通过索引返回截取后指定的字符串
     *
     * @param pStr 传入需要截取的字符串
     * @param value 哪个字符截取
     * @param index 截取索引
     * @return
     */
    public static String getAutomaticInterceptString(String pStr, String value, int index) {
        if (pStr.contains(value)) {
            String[] array = pStr.split(value);
            return array[index];
        }
        return pStr;
    }

}
