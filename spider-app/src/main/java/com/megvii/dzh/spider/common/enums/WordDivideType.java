package com.megvii.dzh.spider.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum WordDivideType {
    CONTENT(3, "回帖内容"), //
    POST_TITLE(2, "帖子标题"), //
    USER_NAME(1, "用户名称");
    /**
     * 代码
     */
    private Integer code;;
    /**
     * 名称
     */
    private String title;

    private WordDivideType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    private static Map<Integer, WordDivideType> codeMap;
    private static Map<String, WordDivideType> titleMap;
    static {
        codeMap = new HashMap<Integer, WordDivideType>();
        titleMap = new HashMap<String, WordDivideType>();
        for (WordDivideType type : WordDivideType.values()) {
            codeMap.put(type.code, type);
            titleMap.put(type.name(), type);
        }
    }

    public static WordDivideType getByCode(Integer idx) {
        return codeMap.get(idx);
    }

    public static WordDivideType getByTitle(String name) {
        return titleMap.get(name);
    }
}

