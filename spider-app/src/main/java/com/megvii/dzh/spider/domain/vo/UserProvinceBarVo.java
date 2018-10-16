package com.megvii.dzh.spider.domain.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserProvinceBarVo implements Serializable {
  private String province;
  private long totalNum;
  private long maleNum;
  private long femaleNum;
  private long unknownNum;
}
