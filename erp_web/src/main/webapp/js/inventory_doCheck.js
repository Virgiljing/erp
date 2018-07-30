$(function(){
	$('#grid').datagrid({
		title:"盘盈盘亏审核",
		url : 'inventory!listByPage.action?t1.state=0',//只能是列出未审核的盘盈盘亏记录
		singleSelect : true,
		pagination:true,
		columns : [[
			{field:'uuid',title:'编号',width:100},
			{field:'goodsName',title:'商品',width:100},
			{field:'storeName',title:'仓库',width:100},
			{field:'num',title:'数量',width:100},
			{field:'type',title:'类型',width:100,formatter:function(value){
				if(value*1 == 1){
					return '盘盈';
				}
				if(value*1 == 2){
					return '盘亏';
				}
			}},
			{field:'createtime',title:'登记日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			{field:'createrName',title:'登记人',width:100},
			{field:'checkerName',title:'审核人',width:100},
			{field:'state',title:'状态',width:100,formatter:function(value){
				if(value*1 == 0){
					return '未审核';
				}
				if(value*1 == 1){
					return '已审核';
				}
			}}			
		]],
		onDblClickRow:function(rowIndex, rowData){
			//弹出审核窗口
			$('#inventoryDlg').dialog('open')
			
			$('#uuid').html(rowData.uuid);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#goodsName').html(rowData.goodsName);
			$('#storeName').html(rowData.storeName);
			$('#num').html(rowData.num);
			
			var tyname = ""; 
			 
			if(rowData.type * 1 == 1){
				tyname = "盘盈";
			}
			if(rowData.type * 1 == 2){
				tyname = "盘亏";
			}
			
			$('#type').html(tyname);
			$('#remark').html(rowData.remark);
			
		}
	});
	
	//审核窗口初始化
	$('#inventoryDlg').dialog({
		title:'盘盈盘亏审核',
		width:260,
		height:300,
		closed:true,
		modal:true,
		buttons:[
			{
				text:'审核',
				iconCls:'icon-search',
				handler:doCheck
			}
			]
	});
	
})
/**
 * 盘盈盘亏审核
 * @returns
 */
function doCheck(){
	$.messager.confirm('确认', '确认要审核吗?', function(yes) {
		if (yes) {
			$.ajax({
				url : 'inventory!doCheck.action',
				data : {id:$('#uuid').html()},// 订单编号
				dataType : 'json',
				type : 'post',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭详情窗口
							$('#inventoryDlg').dialog('close');
							// 刷新订单列表
							$('#grid').datagrid('reload');
						}
					});
				}
			});
		}
	});
}

