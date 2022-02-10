<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>统计图表</title>
    <meta charset="UTF-8">
    <script type="text/javascript" src="Echarts/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-3.6.0.js"></script>
    <script type="text/javascript">

        $(function (){

            //页面加载完毕时展现统计图表
            showCharts();

        })



        function showCharts(){

            $.ajax({
                url : "transaction/getCharts.do",
                type: "get",
                dataType : "json",

                success : function (respData){

                    /*
                    *   respData : {"total" : 100,
                    *               "dataList" :
                    *                           [
                    *                               {"value": 50,"name" : 01资质审查}
                    *                                {"value": 60,"name" : 07成交}
                    *                                               ....
                    *                           ]
                    *              }
                    * */


                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    // 指定图表的配置项和数据
                    option = {
                        title: {
                            text: '漏斗图',
                            subtext: '交易阶段统计图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            feature: {
                                dataView: { readOnly: false },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                        },
                        series: [
                            {
                                name:'漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: respData.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'inside'
                                    },
                                    emphasis: {
                                        textStyle: {
                                            fontSize: 20
                                        }
                                    }
                                },
                                labelLine: {
                                    normal: {
                                        length: 10,
                                        lineStyle: {
                                            width: 1,
                                            type: 'solid'
                                        }
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        borderColor: '#fff',
                                        borderWidth: 1
                                    }
                                },
                                data: respData.dataList/*[
                            {value: 60, name: '访问'},
                            {value: 40, name: '咨询'},
                            {value: 20, name: '订单'},
                            {value: 80, name: '点击'},
                            {value: 100, name: '展现'}
                        ]*/
                            }
                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);




                }
            })
        }
    </script>
</head>
<body>

    <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="main" style="width: 800px;height:600px;"></div>

</body>
</html>