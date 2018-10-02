package com.megvii.dzh.spider.domain.po;

import java.util.Date;
import javax.persistence.Id;
import lombok.Data;

@Data
public class Comment {
    @Id
    private Long id;

    private String userName;

    private String postUrl;

    private Date time;

    private String userDevice;
    
    private String content;
}