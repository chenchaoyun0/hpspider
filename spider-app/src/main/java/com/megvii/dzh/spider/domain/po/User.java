package com.megvii.dzh.spider.domain.po;

import javax.persistence.Id;
import lombok.Data;

@Data
public class User {
    @Id
    private Long id;

    private String userName;

    private Integer gender;

    private Double tbAge;

    private Integer postCount;

    private Integer followCount;

    private Integer fansCount;

    private String userHeadUrl;

    private String userHomeUrl;
}