package com.megvii.dzh.spider.domain.po;

import javax.persistence.Id;
import lombok.Data;

@Data
public class UserTbs {
    @Id
    private Long id;

    private String userName;

    private String tbName;

    private Integer tbLevel;
}