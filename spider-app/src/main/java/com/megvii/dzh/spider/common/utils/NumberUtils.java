package com.megvii.dzh.spider.common.utils;

public class NumberUtils {
    /**
     * 生成随机数 小于 length
     * @param length
     * @return
     */
    public static int randomInt(int length) {
        return (int) (Math.random() * length);
    }
}
