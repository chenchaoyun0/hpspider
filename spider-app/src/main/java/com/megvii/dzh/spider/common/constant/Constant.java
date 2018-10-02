package com.megvii.dzh.spider.common.constant;

/**
 * 常量类
 */
public class Constant {

    public static final String UTF8 = "UTF-8";
    public static final String ORDER_NUM = "ZF201710169692T66jkr";// 讯代理订单号
    public static final String SECRET = "3b23ace31a2447baa44d624e9c5fd0f5";// 讯代理密码
    /**
     * 爬虫贴吧名称
     */
    public static String tbName = "太原工业学院";

    public static String spiderHttpType="http";

    public static String getTbName() {
        return tbName;
    }

    public static void setTbName(String tbName) {
        Constant.tbName = tbName;
    }

    public static String getSpiderHttpType() {
        return spiderHttpType;
    }

    public static void setSpiderHttpType(String spiderHttpType) {
        Constant.spiderHttpType = spiderHttpType;
    }
}
