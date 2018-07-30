
$(function(){
	
	$('#grid').datagrid({
		singleSelect: true,
		pagination: true,
		url: 'inventory!listByPage.action',
		columns:[[
					{field:'uuid',title:'编号',width:100},
					{field:'goodsName',title:'商品',width:100},
					{field:'storeName',title:'仓库',width:100},
					{field:'num',title:'数量',width:100},
					{field:'type',title:'类型',width:100, formatter: function(value){
						if(value * 1 == 1) {
							return '盘盈';
						}
						if(value * 1 == 2) {
							return '盘亏';
						}
						return '火星来的';
					}},
					{field:'createtime',title:'登记日期',width:100, formatter: formatDate},
					{field:'checktime',title:'审核日期',width:100, formatter: formatDate},
					{field:'createrName',title:'登记人',width:100},
					{field:'checkerName',title:'审核人',width:100},
					{field:'state',title:'状态',width:100, formatter: formatState},
					{field:'remark',title:'备注',width:100}
		        ]]
	
	})
	
	//绑定点击查询事件
	$('#btnSearch').bind('click',function(){
		var searchData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('reload', searchData);
	})
	
})