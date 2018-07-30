// 标识当前做的是哪种类型的订单，默认处理的是采购
type=2;
$(function(){
	// 订单列表
	$('#grid').datagrid({
		title:'销售订单列表',
		url : 'orders!listByPage.action?t1.type=2&t1.state=0', // 列出未出库的订单
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'生成日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'endtime',title:'出库日期',width:100,formatter:formatDate},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'客户',width:100},
			{field:'totalmoney',title:'合计金额',width:100},
			{field:'state',title:'状态',width:100,formatter:formatState},
			{field:'waybillsn',title:'运单号',width:100}
		] ],
		onDblClickRow:function(rowIndex, rowData){
			// 弹出详情窗口
			$('#ordersDlg').dialog('open');
			
			
			$('#uuid').html(rowData.uuid);
			$('#supplierName').html(rowData.supplierName);
			$('#state').html(formatState(rowData.state));
			$('#createrName').html(rowData.createrName);
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#endtime').html(formatDate(rowData.endtime));
			
			// 加载明细的数据
			$('#itemgrid').datagrid('loadData',rowData.orderDetails);
		}
	});
	
	// 订单详情窗口初始化
	$('#ordersDlg').dialog({
		title:'订单详情',
		width:700,
		height:340,
		closed:true,
		modal:true
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
			{field:'num',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:formatDetailState}
		] ],
		onDblClickRow:function(rowIndex, rowData){
			// 弹出出库窗口
			$('#itemDlg').dialog('open');
			$('#goodsuuid').html(rowData.goodsuuid);
			$('#goodsname').html(rowData.goodsname);
			$('#num').html(rowData.num);
			// 注意：input要使用val
			$('#id').val(rowData.uuid);
		}
	});
	
	// 出库窗口初始化
	$('#itemDlg').dialog({
		title:'出库',
		width:300,
		height:200,
		closed:true,
		modal:true,
		buttons:[
			{
				text:'出库',
				iconCls:'icon-save',
				handler:doOutStore
			}
		]
	});
});

/**
 * 出库
 * @returns
 */
function doOutStore(){
	$.messager.confirm('确认', '确认要出库吗', function(yes) {
		if (yes) {
			var submitData = $('#itemForm').serializeJSON();
			if(submitData.storeuuid == ''){
				$.messager.alert('提示', '请选择仓库', 'info');
				return;
			}
			$.ajax({
				url : 'orderdetail!doOutStore.action',
				data : submitData,
				dataType : 'json',
				type : 'post',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭出库窗口
							$('#itemDlg').dialog('close');
							// 修改明细的状态
							//   获取选中的明细
							var row = $('#itemgrid').datagrid('getSelected');//返回第一个被选中的行或如果没有选中的行则返回null。
							row.state='1'; // 设置状态为已出库, 但此时界面没有刷新
							//  让明细表格的数据刷新，重新刷新明细的状态
							var data = $('#itemgrid').datagrid('getData');
							// 重新加载数据, 删除旧行, 导致状态刷新
							$('#itemgrid').datagrid('loadData',data);
							// 进行判断是否所有的明细都出库了, 所有行的state=1
							/*for(var i = 0; i < data.rows.length; i++){
								break;
							}*/
							var flag = true; // 假设所有都出库了
							$.each(data.rows,function(i,r){
								if(r.state * 1 == 0){
									// 还有没出库的
									flag = false;// 标识为不能关闭
									return false;// 退出循环, java break;
								}
							});
							
							// 如果所有都出库了，
							if(flag){
								// 关闭详情窗口
								$('#ordersDlg').dialog('close');
								// 刷新订单列表
								$('#grid').datagrid('reload');
							}
						}
					});
				}
			});
		}
	});
}