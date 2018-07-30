$(function() {
	//加入一个全局设置，使得图片导出功能的额提示换成中文
	 Highcharts.setOptions({
         lang: {
               printChart:"打印图表",
               downloadJPEG: "下载JPEG 图片" , 
               downloadPDF: "下载PDF文档"  ,
               downloadPNG: "下载PNG 图片"  ,
               downloadSVG: "下载SVG 矢量图" , 
               exportButtonTitle: "导出图片" 
         }
     });
	var date = new Date();
	var year = date.getFullYear();// 获取年份值
	// 设置年份值
	$('#year').combobox('select',year);
	
	$('#grid').datagrid({
		title:'销售退货趋势列表',
		url : 'returnReport!returnTrendReport.action',
		singleSelect : true,
		queryParams:{year:year},
		columns : [ [ 
			{field : 'name',title : '商品类型',width : 100}, 
			{field : 'y',title : '退货金额',width : 100}
		] ],
		onLoadSuccess:function(data){
			//在数据加载成功的时候触发。
			//alert(JSON.stringify(data));
			showChart(data.rows);
			
		}
	});
	
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('reload',formData);
	});
	
	
});

function showChart(_data){
	//月
	var months = [];
	for(var i = 1; i <=12; i++){
		months.push(i + "月");
	}
	
    $('#chart').highcharts({
        chart: {
            zoomType: 'xy'
        },
        title: {
            text: $('#year').combobox('getValue')+'年度销售退货趋势图'
        },
        subtitle: {
            text: 'Itheima.com'
        },
        credits: {
            text: 'itheima.com',
            href: 'www.itheima.com'
        },
        xAxis: [{
            categories: months,
            crosshair: true
        }],
        yAxis: [{ // Primary yAxis
            labels: {
                format: '{value}',
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            },
            title: {
                text: '',
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            }
        }, { // Secondary yAxis
            title: {
                text: '销售退款金额',
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            labels: {
                format: '{value} ￥',
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            opposite: true
        }],
        tooltip: {
            shared: true
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            x: 120,
            verticalAlign: 'top',
            y: 100,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
        },
        series: [{
            name: '金额',
            type: 'column',
            yAxis: 1,
            data: _data,
            tooltip: {
                valueSuffix: ' ￥ '
            }
        }]
    });
}





