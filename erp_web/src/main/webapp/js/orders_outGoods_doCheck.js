$(function(){
	// 订单列表
	$('#grid').datagrid({
		title:'采购退货列表',
		url : 'returnorders!listByPage.action?t1.type=1&t1.state=0', // 只能列出未审核采购退货订单  
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'录入日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			/*{field:'starttime',title:'确认日期',width:100,formatter:formatDate},*/
			{field:'endtime',title:'出库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'checkerName',title:'审核员',width:100},
		/*	{field:'starterName',title:'确认员',width:100},*/
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'供应商',width:100},
			{field:'totalmoney',title:'总金额',width:100},
			{field:'state',title:'订单状态',width:100,formatter:ReturnformatState},
			{field:'waybillsn',title:'运单号',width:100}
		] ],
		onDblClickRow:function(rowIndex, rowData){
			// 弹出详情窗口
			$('#ordersDlg').dialog('open');
			$('#uuid').html(rowData.uuid);
			$('#supplierName').html(rowData.supplierName);
			$('#state').html(formatState(rowData.state));
			$('#createrName').html(rowData.createrName);
			$('#checkerName').html(rowData.checkerName);
			/*$('#starterName').html(rowData.starterName);*/
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#checktime').html(formatDate(rowData.checktime));
			/*$('#starttime').html(formatDate(rowData.starttime));*/
			$('#endtime').html(formatDate(rowData.endtime));
			
			// 加载明细的数据
			$('#itemgrid').datagrid('loadData',rowData.returnorderdetails);
		}
	});
	
	// 订单详情窗口初始化
	$('#ordersDlg').dialog({
		title:'订单详情',
		width:700,
		height:340,
		closed:true,
		modal:true,
		toolbar:[
			{
				text : '审核',
				iconCls : 'icon-search',
				handler : doCheck
			}
		]
	});
	
	//明细表格初始化
	$('#itemgrid').datagrid({
		title:'商品列表',
		singleSelect : true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:60},
			{field:'goodsuuid',title:'商品编号',width:80},
			{field:'goodsname',title:'商品名称',width:100},
			{field:'price',title:'价格',width:100},
			{field:'returnnum',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:ReturnformatDetailState}
		] ]
	});
});

/**
 * 订单审核
 * @returns
 */
function doCheck(){
	$.messager.confirm('确认', '确认要审核吗?', function(yes) {
		if (yes) {
			$.ajax({
				url : 'returnorders!type1outGoodsDoCheck.action',
				data : {id:$('#uuid').html()},// 订单编号
				dataType : 'json',
				type : 'post',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭详情窗口
							$('#ordersDlg').dialog('close');
							// 刷新订单列表
							$('#grid').datagrid('reload');
						}
					});
				}
			});
		}
	});
}
