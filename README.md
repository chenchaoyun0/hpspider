## 目录
* [README](#README)

****

README
===========================
java 爬虫，采用webmagic框架。目的爬取虎扑步行街的数据，并做一些统计与数据分析，纯属个人娱乐

****
|Author|陈超允|
|---|---
|E-mail|873692191@qq.com

# 技术栈 

- springboot 1.5.7
- springMVC+Rest...
- EChart 统计图
- tx.mybatis 3.4.6 + hikari 连接池
- webmagic 0.7.3（修改版，修复https问题与log优化 下载地址：<https://download.csdn.net/download/sinat_22767969/10703880>）
- mysql 5.7.17 （支持utf8mb4字符编码）

### 表设计
表sql参见 db/hpspider.sql
- [ ] 用户表 user

- [ ] 帖子表 post

- [ ] 回帖表 comment

- [ ] 分词表 word_divide

### 部分数据截图
![](https://img-blog.csdn.net/20181016203129904?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
![](https://img-blog.csdn.net/20181016203407485?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## **计划统计**

### 1. 步行街帖子标题的热点词汇（分析Jrs使用最频繁的词汇）
![](https://img-blog.csdn.net/20181016203813362?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 23. JRS回复的词云（看所有帖子下大家都在说些什么）
![](https://img-blog.csdn.net/20181016211400843?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 24. JRS昵称名词云（大家最喜欢用什么词起名）
![](https://img-blog.csdn.net/20181016211501148?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 19. 步行街男女比例分布!!（男女Jrs比例，到底是？）
![](https://img-blog.csdn.net/20181016210643709?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 13. Jrs所在地分布
![](https://img-blog.csdn.net/20181016210255330?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 13.1. 女Jrs所在地分布
![](https://img-blog.csdn.net/20181016210507796?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 20. Jrs设备分布
![](https://img-blog.csdn.net/20181016211212526?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 7. NBA主队分布 **
![](https://img-blog.csdn.net/2018101620564819?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 4. 年发帖量（分析近5年来发帖量最多的哪年）
![](https://img-blog.csdn.net/20181016204631779?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 5. 年里的月发帖量（分析每年中，大家都喜欢在几月份发帖）
![](https://img-blog.csdn.net/20181016205011832?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 6. 时发帖量（分析大家每天最爱在什么时候发帖）
![](https://img-blog.csdn.net/20181016205059308?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 7. 时回复量（分析大家每天最爱在什么时候回帖）
![](https://img-blog.csdn.net/20181016205214529?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 8. 论坛等级分布（分析Jrs各等级比例）
![](https://img-blog.csdn.net/20181016210203598?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 9. 年度的十大热帖（按年统计每年讨论最热的帖子）
![](https://img-blog.csdn.net/20181016210757345?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
![](https://img-blog.csdn.net/20181016210812463?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 10. 历史来十大热帖
![](https://img-blog.csdn.net/20181016210902135?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 11. 年度点亮前十的回复
![](https://img-blog.csdn.net/20181016210957829?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 12. 十大活跃Jrs，按年分组（所谓的达人/大神）
![](https://img-blog.csdn.net/20181016211339321?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 14. 粉丝最多的10大Jrs（人气最高的明星）
![](https://img-blog.csdn.net/20181016211021126?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 15. 粉丝最多的女jr
![](https://img-blog.csdn.net/20181016214024995?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 16. 访问量前十的Jrs
![](https://img-blog.csdn.net/20181016211108482?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 17. 访问量最多的女jr
![](https://img-blog.csdn.net/20181016214201130?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 18. 社区声望排行
![](https://img-blog.csdn.net/2018101621115620?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 21. jrs加入时间分布曲线图（哪年小JRs新增最多）
![](https://img-blog.csdn.net/20181016211252195?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 22. 十大发帖量最多的Jrs（看看哪些人最爱在发帖了）
![](https://img-blog.csdn.net/20181016211339321?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 2. 发帖与不发帖Jrs占比（潜水/只回帖Jrs与常发帖Jrs占比）
![](https://img-blog.csdn.net/20181016204328432?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 3. 发帖有回复与没回复占比（石沉大海的帖子占比）
![](https://img-blog.csdn.net/20181016204449680?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzIyNzY3OTY5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
### 25. Jrs的头像大集合（一页展示所有Jrs的头像，并做图像分析出“平均头像”长啥样）
![]()


## 数据分析/效果展示

- ### 见博客：https://blog.csdn.net/sinat_22767969/article/details/83096619
