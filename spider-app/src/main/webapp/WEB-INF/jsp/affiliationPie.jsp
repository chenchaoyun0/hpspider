<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/js/echarts.common.min.js"></script>
    <style>
        .mainStyle {
            width: 100%;
            height: 100%;
            margin: 0;
        }
    </style>
</head>
<body>
<div id="pie" class="mainStyle"></div>
<script>
  //初始化echarts
  var pieCharts = echarts.init(document.getElementById("pie"));
  //设置属性
  pieCharts.setOption({
    title: {
      text: 'NBA主队',
      subtext: 'JRS NBA主队分布分布',
      x: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
      orient: 'vertical',
      x: 'left',
      data: []
    },
    toolbox: {
      show: true,
      feature: {
        mark: {
          show: true
        },
        dataView: {
          show: true,
          readOnly: false
        },
        magicType: {
          show: true,
          type: ['pie', 'funnel'],
          option: {
            funnel: {
              x: '25%',
              width: '50%',
              funnelAlign: 'left',
              max: 1548
            }
          }
        },
        restore: {
          show: true
        },
        saveAsImage: {
          show: true
        }
      }
    },
    calculable: true,
    series: [{
      name: 'NBA主队',
      type: 'pie',
      radius: '70%',
      center: ['50%', '60%'],
      //标签
      itemStyle: {
        normal: {
          label: {
            show: true,
            position: 'outer',
            formatter: '{b}  {d}%',
            distance: 0.4,
            textStyle: {
              align: 'center',
              baseline: 'middle',
              fontFamily: '微软雅黑',
              fontSize: 15,
              fontWeight: 'bolder'
            }
          },
          labelLine: {
            show: true
          }
        }
      },
      data: []
    }]
  });
  //显示一段动画
  pieCharts.showLoading();
  var typeArrays = []; //数组
  //异步请求数据
  $.ajax({
    type: "post",
    async: true,
    url: '${pageContext.request.contextPath}/getAffiliationPie',
    data: [],
    dataType: "json",
    success: function (result) {
      for (var i = 0; i < result.length; i++) {
        typeArrays.push(result[i].name);
      }
      pieCharts.hideLoading();//隐藏加载动画
      pieCharts.setOption({
        title: {
          text: 'NBA主队',
          subtext: 'NBA主队分布',
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
          orient: 'vertical',
          x: 'left',
          data: typeArrays
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: true,
              readOnly: false
            },
            magicType: {
              show: true,
              type: ['pie', 'funnel'],
              option: {
                funnel: {
                  x: '25%',
                  width: '50%',
                  funnelAlign: 'left',
                  max: 1548
                }
              }
            },
            restore: {
              show: true
            },
            saveAsImage: {
              show: true
            }
          }
        },
        calculable: true,
        series: [{
          name: 'NBA主队',
          type: 'pie',
          radius: '70%',
          center: ['50%', '60%'],
          data: result
        }]
      });
    }
  })
</script>
</body>
</html>