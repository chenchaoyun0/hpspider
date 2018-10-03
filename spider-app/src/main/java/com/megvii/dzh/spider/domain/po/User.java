package com.megvii.dzh.spider.domain.po;

import java.util.Date;
import javax.persistence.Id;
import lombok.Data;

@Data
public class User {

  @Id
  private Long id;

  private String userName;

  private String province;

  private String city;

  private Integer gender;

  private Integer level;

  private Date joinDate;

  private Integer followCount;

  private Integer fansCount;

  private String userHeadUrl;

  private String userHomeUrl;

  private Long viewTotal;
}