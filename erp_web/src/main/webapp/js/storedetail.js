$(function() {
	$('#grid').datagrid({
		title:'商品库存列表',
		rownumbers:true,
		url : 'storedetail!listByPage.action',
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'storeName',title:'仓库',width:100},
			{field:'goodsName',title:'商品',width:100},
			{field:'num',title:'数量',width:100}
		] ]
	});
	
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('reload',formData);
	});
});