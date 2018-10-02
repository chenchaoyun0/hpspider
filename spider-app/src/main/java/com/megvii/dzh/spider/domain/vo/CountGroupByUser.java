package com.megvii.dzh.spider.domain.vo;

import lombok.Data;

@Data
public class CountGroupByUser {
    private String userName;
    private long postCount;
}
