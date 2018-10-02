package com.megvii.dzh.spider.domain.vo;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Record {

    private String taizhan_num; // 台站名
    private String tem; // 温度
    private String hum; // 湿度
    private String pa; // 压强
    private String rain; // 雨量
    private String win_dir; // 风向
    private String win_sp; // 风速
    private String dateStr; // 观测日期（用于Echarts显示格式）
    private Timestamp date; // 观测日期（原始格式）
}
