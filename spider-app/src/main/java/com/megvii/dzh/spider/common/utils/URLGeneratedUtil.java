package com.megvii.dzh.spider.common.utils;

import com.megvii.dzh.spider.common.constant.Constant;

public class URLGeneratedUtil {

    public final static String PREFIX = "://tieba.baidu.com";


    public static String generatePostURL(String url) {
        return Constant.getSpiderHttpType()+PREFIX + url;
    }
    
    public static String generateHttpURL(String url) {
        return Constant.getSpiderHttpType()+":" + url;
    }
}
