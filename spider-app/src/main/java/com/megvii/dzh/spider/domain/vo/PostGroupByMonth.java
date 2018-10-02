package com.megvii.dzh.spider.domain.vo;

import lombok.Data;

@Data
public class PostGroupByMonth {
    private String year;
    private String month;
    private int count;
}
