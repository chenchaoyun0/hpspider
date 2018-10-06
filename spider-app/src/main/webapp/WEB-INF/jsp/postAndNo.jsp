<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/js/echarts.common.min.js"></script>
</head>
<style>
    .mainStyle {
        width: 100%;
        height: 100%;
        margin: 0;
    }
</style>
<body>
<div id="pie" class="mainStyle"></div>
<script>
  //初始化echarts
  var pieCharts = echarts.init(document.getElementById("pie"));
  //设置属性
  pieCharts.setOption({
    title: {
      text: '人数',
      subtext: '发过帖与不发帖比例',
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
      name: '人数',
      type: 'pie',
      radius: '70%',
      center: ['50%', '60%'],
      //标签
      itemStyle: {
        normal: {
          label: {
            show: true,
            position: 'outer',
            formatter: '{b} {d}%',
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
  //异步请求数据
  $.ajax({
    type: "post",
    async: true,
    url: '${pageContext.request.contextPath}/getPostAndNo',
    data: [],
    dataType: "json",
    success: function (result) {
      pieCharts.hideLoading();//隐藏加载动画
      pieCharts.setOption({
        title: {
          text: '人数',
          subtext: '发过帖与不发帖比例',
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
          name: '人数',
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