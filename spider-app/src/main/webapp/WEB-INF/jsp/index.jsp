<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>百度贴吧爬虫-数据导航页-陈超允</title>
    <style type="text/css">
        .text_slice {
            font-size: 18px;
            font-family: 楷体;
            font-weight: bolder;
            color: red;
            list-style: decimal;
            margin-top: 10px
        }
    </style>
</head>
<body>
<h1 style="font-size: 25px;font-family: 楷体">Java 百度贴吧爬虫-数据分析导航列表</h1>
<h2 style="font-size: 20px;font-family: 楷体">贴吧名:${tbName}，帖子总数:${countPost}，回复总数:${countComment}，用户总数:${countUser}</h2>
<ul>
    <li class="text_slice">
        <a href="postTitleWordCloud">帖子标题的热点词汇</a>
    </li>
    <li class="text_slice">
        <a href="postAndNo">发帖与不发帖用户占比</a>
    </li>
    <li class="text_slice">
        <a href="postHasRepAndNo">发帖有回复与没回复占比</a>
    </li>
    <li class="text_slice">
        <a href="postYears">年发帖量</a>
    </li>
    <li class="text_slice">
        <a href="postMonth">年里的月发帖量</a>
    </li>
    <li class="text_slice">
        <a href="postHours">时发帖量</a>
    </li>
    <li class="text_slice">
        <a href="userLevel">用户等级分布</a>
    </li>
    <li class="text_slice">
        <a href="postTitlesyear2015">2015年度的十大热帖</a>
    </li>
    <li class="text_slice">
        <a href="postTitlesyear2016">2016年度的十大热帖</a>
    </li>
    <li class="text_slice">
        <a href="postTitlesyear2017">2017年度的十大热帖</a>
    </li>
    <li class="text_slice">
        <a href="postTitlesyear2018">2018年度的十大热帖</a>
    </li>
    <li class="text_slice">
        <a href="activeUser2016">2016十大活跃用户</a>
    </li>
    <li class="text_slice">
        <a href="activeUser2017">2017十大活跃用户</a>
    </li>
    <li class="text_slice">
        <a href="activeUser2018">2018十大活跃用户</a>
    </li>
    <li class="text_slice">
        <a href="activeUser">2007~2018十大活跃用户</a>
    </li>
    <li class="text_slice">
        <a href="userFansBar">粉丝最多的10大用户</a>
    </li>
    <li class="text_slice">
        <a href="tbNameWordCloud">大家最喜欢关注的贴吧-词云</a>
    </li>
    <li class="text_slice">
        <a href="userGender">男女比例分布</a>
    </li>
    <li class="text_slice">
        <a href="usertbAge">吧龄分布</a>
    </li>
    <li class="text_slice">
        <a href="postUserTopBar">十大发帖量最多的用户</a>
    </li>
    <li class="text_slice">
        <a href="commentWordCloud">帖子回复的词云</a>
    </li>
    <li class="text_slice">
        <a href="userNameWordCloud">贴吧名词云</a>
    </li>
    <li class="text_slice">
        <a href="userDevicePie">用户设备分布比例</a>
    </li>
</ul>
</body>
</html>