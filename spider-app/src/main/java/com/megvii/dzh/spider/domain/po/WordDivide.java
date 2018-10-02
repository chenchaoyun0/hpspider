package com.megvii.dzh.spider.domain.po;

import javax.persistence.Id;
import lombok.Data;

@Data
public class WordDivide {
    @Id
    private Long id;

    private String word;

    private Integer type;
}