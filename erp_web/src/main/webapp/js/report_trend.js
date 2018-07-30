$(function() {
	var date = new Date();
	var year = date.getFullYear();// 获取年份值
	// 设置年份值
	$('#year').combobox('select',year);
	
	$('#grid').datagrid({
		title:'销售趋势列表',
		url : 'report!trendReport.action',
		singleSelect : true,
		queryParams:{year:year},
		columns : [ [ 
			{field : 'name',title : '商品类型',width : 100}, 
			{field : 'y',title : '销售额',width : 100}
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
	var months = [];
	for(var i = 1; i <=12; i++){
		months.push(i + "月");
	}
	$('#chart').highcharts({
		title: {
            text: $('#year').combobox('getValue')+'年度销售趋势图',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: itheima.com',
            x: -20
        },
        xAxis: {
            categories: months
        },
        yAxis: {
            title: {
                text: 'RMB (￥)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valuePrefix: '￥'
        },
        legend: {
            layout: 'vertical',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
        },
        series: [{
            name: '销售额',
            data: _data
        }]
    });
}