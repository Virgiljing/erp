$(function() {
	$('#grid').datagrid({
		title:'销售退货统计列表',
		url : 'sell!sellReturnSum.action',
		singleSelect : true,
		columns : [ [ 
			{field : 'name',title : '商品类型',width : 100}, 
			{field : 'y',title : '退款额',width : 100}
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
		if(formData.endDate != ''){
			// 结束日期有值，补23:59:59
			formData.endDate+=" 23:59:59.999";
		}
		$('#grid').datagrid('reload',formData);
	});
	
	
});

function showChart(_data){

	$.each(_data,function(i,d){
		//d {};
		d.drilldown=true;
	});
	$('#chart').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie',
            events: {
				drillup: function(e) {
					// 上钻回调事件
					console.log(e.seriesOptions);
				},
				drilldown: function (e) {
					if (!e.seriesOptions) {
						var chart = this;
						//alert(JSON.stringify(e.point));
						var goodstypename = e.point.name;
						var goodstypeuuid=e.point.goodstypeuuid;
						
						var formData = $('#searchForm').serializeJSON();
						if(formData.endDate != ''){
							// 结束日期有值，补23:59:59
							formData.endDate+=" 23:59:59.999";
						}
						formData.goodstypeuuid=goodstypeuuid;
						chart.showLoading('正在努力加载中 ...');
						$.ajax({
							url : 'sell!orderReport2.action',
							data : formData,
							dataType : 'json',
							type : 'post',
							success : function(rtn) {
								var obj = {};
								obj.data=rtn;
								obj.name=goodstypename;
								chart.hideLoading();
								chart.addSeriesAsDrilldown(e.point, obj);
							}
						});
						// Show the loading label
						//chart.showLoading('Simulating Ajax ...');
						//setTimeout(function () {
						//	chart.hideLoading();
						//	chart.addSeriesAsDrilldown(e.point, series);
						//}, 1000);
					}
				}
			}
        },
        title: {
            text: '销售统计'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                },
                showInLegend: true
            }
        },
        credits: {
        	enabled: true,
        	href: "http://www.itheima.com",
        	text: "itheima.com"
        },
        series: [{
            name: "百分比",
            colorByPoint: true,
            data: _data
        }]
    });
}