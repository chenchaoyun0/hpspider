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

 <!-- 显示Echarts图表 -->
 <div style="height: 410px; min-height: 100px; margin: 0 auto;"
  id="main"></div>
 <script type="text/javascript">
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'));

		// 指定图表的配置项和数据
		var option = {
			title : { //图表标题
				text : '近几年发帖量数据图表'
			},
			tooltip : {
				trigger : 'axis', //坐标轴触发提示框，多用于柱状、折线图中
			},
			dataZoom : [ {
				type : 'slider', //支持鼠标滚轮缩放
				start : 0, //默认数据初始缩放范围为10%到90%
				end : 100
			}, {
				type : 'inside', //支持单独的滑动条缩放
				start : 0, //默认数据初始缩放范围为10%到90%
				end : 100
			} ],
			legend : { //图表上方的类别显示
				show : true,
				data : [ '年份' ]
			},
			color : [ '#FF3333' //温度曲线颜色
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
				name : '年份',
		        axisLabel : {
		          formatter : '{value} ' //控制输出格式
		        },
				data : []
			//先设置数据值为空，后面用Ajax获取动态数据填入
			},
			yAxis : [ //Y轴（这里我设置了两个Y轴，左右各一个）
			{
				//第一个（左边）Y轴，yAxisIndex为0
				type : 'value',
				name : '发帖量',
				axisLabel : {
					formatter : '{value} 条' //控制输出格式
				}
			}
			],
			series : [ //系列（内容）列表                      
			{
				name : '发帖量',
				type : 'line', //折线图表示（生成温度曲线）
				symbol : 'emptycircle', //设置折线图中表示每个坐标点的符号；emptycircle：空心圆；emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形                        
				data : []
			//数据值通过Ajax动态获取
			}]
		};

		myChart.showLoading(); //数据加载完之前先显示一段简单的loading动画

		var tems = []; //发帖量数组（存放服务器返回的所有温度值）
		var dates = []; //时间数组
		
		$.ajax({ //使用JQuery内置的Ajax方法
			type : "post", //post请求方式
			async : true, //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
			url : "${pageContext.request.contextPath}/getPostGroupBy", //请求发送到ShowInfoIndexServlet处
			data : {
				groupBy : "year"
			}, //请求内包含一个key为name，value为A0001的参数
			dataType : "json", //返回数据形式为json
			success : function(result) {
				//请求成功时执行该函数内容，result即为服务器返回的json对象
				if (result != null && result.length > 0) {
					for (var i = 0; i < result.length; i++) {
						tems.push(result[i].count); //挨个取出温度、湿度、压强等值并填入前面声明的温度、湿度、压强等数组
						dates.push(result[i].data+' 年');
					}
					myChart.hideLoading(); //隐藏加载动画

					myChart.setOption({ //载入数据
						xAxis : {
							data : dates
						//填入X轴数据
						},
						series : [ //填入系列（内容）数据
						{
							// 根据名字对应到相应的系列
							name : '发帖量',
							smooth : 0.3,
							data : tems
						}]
					});

				} else {
					//返回的数据为空时显示提示信息
					alert("图表请求数据为空，可能服务器暂未录入近五天的观测数据，您可以稍后再试！");
					myChart.hideLoading();
				}

			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败，可能是服务器开小差了");
				myChart.hideLoading();
			}
		})
		myChart.setOption(option); //载入图表
	</script>

</body>
</html>