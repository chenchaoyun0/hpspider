<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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

<title>- 观测数据 -</title>
</head>

<body>

 <div style="height: 410px; min-height: 100px; margin: 0 auto;"
  id="main"></div>
 <script type="text/javascript">
		var myChart = echarts.init(document.getElementById('main'));

		// 指定图表的配置项和数据
		var option = {
			title : { //图表标题
				text : '粉丝最多的10大用户'
			},
			tooltip : {
				trigger : 'axis', //坐标轴触发提示框，多用于柱状、折线图中
			},
			dataZoom : [ {
				type : 'slider', //支持鼠标滚轮缩放
				start : 0, //默认数据初始缩放范围为10%到90%
				end : 100
			}, {
				type : 'inside', //支持单独的滑动人缩放
				start : 0, //默认数据初始缩放范围为10%到90%
				end : 100
			} ],
			legend : { //图表上方的类别显示
				show : true,
				data : [ '访问量' ]
			},
			color : [ '#FFDC35'//2014曲线颜色
			],
			toolbox : { //工具栏显示
				show : true,
				feature : {
					saveAsImage : {}
				//显示“另存为图片”工具
				}
			},
			xAxis : { //X轴
				type : 'category',
				name : '用户名',
				axisLabel : {
					formatter : '{value}' //控制输出格式
				},
				data : []
			//先设置数据值为空，后面用Ajax获取动态数据填入
			},
			yAxis : [ //Y轴（这里我设置了两个Y轴，左右各一个）
			{
				//第一个（左边）Y轴，yAxisIndex为0
				type : 'value',
				name : '访问量',
				axisLabel : {
					formatter : '{value} 次' //控制输出格式
				}
			} ],
			series : [ {
				name : '2014年',
				type : 'bar',
				barWidth : 30,//柱图宽度
				data : []
			} ]
		};

		myChart.showLoading();

		var provinces = []; //类别数组（实际用来盛放X轴坐标值）
		var males = [];

		$.ajax({
			type : "post",
			async : true,
			url : "${pageContext.request.contextPath}/getUserSheqSw",
			data : {
				limit : 30,
                orderBy:"DESC"
			},
			dataType : "json",
			success : function(result) {
				if (result != null && result.length > 0) {
					for (var i = 0; i < result.length; i++) {
						provinces.push(result[i].name); //挨个取出类别并填入类别数组
					}
					for (var i = 0; i < result.length; i++) {
						males.push(result[i].value); //挨个取出销量并填入销量数组
					}
					myChart.hideLoading(); //隐藏加载动画

					myChart.setOption({ //载入数据
						xAxis : {
							data : provinces
						},
						series : [ //填入系列（内容）数据
						{
							// 根据名字对应到相应的系列
							name : '访问量',
							data : males
						} ]
					});

				} else {
					alert("图表请求数据为空，可能服务器暂未录入近五天的观测数据，您可以稍后再试！");
					myChart.hideLoading();
				}

			},
			error : function(errorMsg) {
				alert("图表请求数据失败，可能是服务器开小差了");
				myChart.hideLoading();
			}
		})
		myChart.setOption(option); //载入图表
	</script>

</body>
</html>