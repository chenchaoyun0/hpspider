<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 在IE运行最新的渲染模式 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 初始化移动浏览显示  -->
    <meta name="Author" content="Dreamer-1.">

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/echarts.common.min.js"></script>
    <script src="/js/theme/infographic.js"></script>
    <style>
        .mainStyle {
            width: 100%;
            height: 100%;
            margin: 0;
        }
    </style>

    <title>- 观测数据 -</title>
</head>

<body>

<div class="mainStyle" id="main"></div>
<script type="text/javascript">
  var myChart = echarts.init(document.getElementById('main'));

  // 指定图表的配置项和数据
  var option = {
    title: { //图表标题
      text: '用户所在地分布'
    },
    tooltip: {
      trigger: 'axis', //坐标轴触发提示框，多用于柱状、折线图中
    },
    dataZoom: [{
      type: 'slider', //支持鼠标滚轮缩放
      start: 0, //默认数据初始缩放范围为10%到90%
      end: 100
    }, {
      type: 'inside', //支持单独的滑动条缩放
      start: 0, //默认数据初始缩放范围为10%到90%
      end: 100
    }],
    legend: { //图表上方的类别显示
      show: true,
      data: ['男性', '女性', '性别未知']
    },
    color: [
      '#00BFFF', //2015曲线颜色,
      '#FF1493', //2014曲线颜色
      '#778899' //2018图颜色,
    ],
    toolbox: { //工具栏显示
      show: true,
      feature: {
        saveAsImage: {}
        //显示“另存为图片”工具
      }
    },
    xAxis: { //X轴
      data: []
      //先设置数据值为空，后面用Ajax获取动态数据填入
    },
    yAxis: {},
    series: [{
      name: '男性',
      type: 'bar',
      stack: 'one',
      data: []
    },
      {
        name: '女性',
        type: 'bar',
        stack: 'one',
        data: []
      },
      {
        name: '性别未知',
        type: 'bar',
        stack: 'one',
        data: []
      }
    ]
  };

  myChart.showLoading();

  var provinces = [];    //类别数组（实际用来盛放X轴坐标值）
  var males = [];
  var females = [];
  var unknown = [];

  $.ajax({
    type: "post",
    async: true,
    url: "${pageContext.request.contextPath}/getUserProvinceBar",
    data: {
      limit: 30
    },
    dataType: "json",
    success: function (result) {
      //请求成功时执行该函数内容，result即为服务器返回的json对象
      if (result) {
        for (var i = 0; i < result.length; i++) {
          provinces.push(result[i].province);    //挨个取出类别并填入类别数组
        }
        for (var i = 0; i < result.length; i++) {
          males.push(result[i].maleNum);    //挨个取出销量并填入销量数组
        }
        for (var i = 0; i < result.length; i++) {
          females.push(result[i].femaleNum);
        }
        for (var i = 0; i < result.length; i++) {
          unknown.push(result[i].unknownNum);
        }
        myChart.hideLoading(); //隐藏加载动画

        myChart.setOption({ //载入数据
          xAxis: {
            data: provinces
          },
          series: [
            {// 根据名字对应到相应的系列
              name: '男性',
              data: males
            },
            {
              name: '女性',
              data: females
            },
            {
              name: '性别未知',
              data: unknown
            }]
        });

      } else {
        alert("图表请求数据为空，可能服务器暂未录入近五天的观测数据，您可以稍后再试！");
        myChart.hideLoading();
      }

    },
    error: function (errorMsg) {
      alert("图表请求数据失败，可能是服务器开小差了");
      myChart.hideLoading();
    }
  })
  myChart.setOption(option); //载入图表
</script>

</body>
</html>