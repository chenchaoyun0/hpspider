<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="utf-8">
<script type="text/javascript" src="/js/jquery-3.2.1.min.js"></script>
<script src='https://cdn.bootcss.com/echarts/3.7.0/echarts.simple.js'></script>
<script src='/js/echarts-wordcloud.js'></script>
</head>
<body>
 <style>
html, body, #main {
 width: 100%;
 height: 100%;
 margin: 0;
}
</style>
 <div id='main'></div>
 <script>
		var chart = echarts.init(document.getElementById('main'));

		var option = {
			title : { //图表标题
				text : '年度20大热帖'
			},
			tooltip : {},
			backgroundColor : '#F7F7F7',
			series : [ {
				type : 'wordCloud',
				gridSize : 2,
				sizeRange : [ 12, 48 ],
				rotationRange : [ 0, 0 ],
				shape : 'circle',
				width : 1000,
				height : 1000,
				drawOutOfBound : true,
				autoSize : {
					enable : true,
					minSize : 6
				},
				textStyle : {
					normal : {
						color : function() {
							return 'rgb('
									+ [ Math.round(Math.random() * 160),
											Math.round(Math.random() * 160),
											Math.round(Math.random() * 160) ]
											.join(',') + ')';
						}
					},
					emphasis : {
						shadowBlur : 10,
						shadowColor : '#333'
					}
				},
				data : [ {
					name : 'Crow S Club',
					value : 10000,
					textStyle : {
						normal : {
							color : 'black'
						},
						emphasis : {
							color : 'red'
						}
					}
				} ]
			} ]
		};

		chart.setOption(option);

		chart.showLoading();

		$.ajax({
			type : "post",
			async : true,
			url : "getPostTitlesyear",
			data : {
				year : "2015"
			},
			dataType : "json",
			success : function(result) {
				chart.hideLoading(); //隐藏加载动画
				chart.setOption({ //加载数据图表
					series : [ {// 根据名字对应到相应的系列
						data : result
					} ]
				});
			},
			error : function(errorMsg) {
				//请求失败时执行该函数
				alert("图表请求数据失败!");
				myChart.hideLoading();
			}
		})

		window.onresize = chart.resize;
	</script>
</body>
</html>

