$(function(){
	// 订单列表
	$('#grid').datagrid({
		title:'退货订单列表',
		url : 'returnorders!listByPage.action?t1.type=2&t1.state=1', //只能列出已审核的销售退货未入库订单
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'录入日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			{field:'endtime',title:'入库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'checkerName',title:'审核员',width:100},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'客户',width:100},
			{field:'totalmoney',title:'合计金额',width:100},
			{field:'state',title:'状态',width:100,formatter:ReturnformatState},
			{field:'waybillsn',title:'运单号',width:100}
		] ],
		onDblClickRow:function(rowIndex, rowData){
			// 弹出详情窗口
			$('#ordersDlg').dialog('open');
			//赋值
			$('#uuid').html(rowData.uuid);
			$('#supplierName').html(rowData.supplierName);
			$('#state').html(ReturnformatState(rowData.state));
			$('#createrName').html(rowData.createrName);
			$('#checkerName').html(rowData.checkerName);
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#checktime').html(formatDate(rowData.checktime));
			$('#endtime').html(formatDate(rowData.endtime));
			
			// 加载明细的数据
			$('#itemgrid').datagrid('loadData',rowData.returnorderdetails);
		}
	});
	
	// 订单详情窗口初始化
	$('#ordersDlg').dialog({
		title:'销售订单退货详情',
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
			{field:'returnnum',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:function(value){
				if(value * 1  == 0){
					return "未入库";
				}
				if(value * 1  == 1){
					return "已入库";
				}
			}}
			
		] ],
		onDblClickRow:function(rowIndex, rowData){
			// 弹出入库窗口
			$('#itemDlg').dialog('open');
			$('#goodsuuid').html(rowData.goodsuuid);
			$('#goodsname').html(rowData.goodsname);
			$('#returnnum').html(rowData.returnnum);
			// 注意：input要使用val
			$('#id').val(rowData.uuid);
		}
	});
	
	// 销售退货入库窗口初始化
	$('#itemDlg').dialog({
		title:'销售退货入库',
		width:300,
		height:200,
		closed:true,
		modal:true,
		buttons:[
			{
				text:'销售退货入库',
				iconCls:'icon-save',
				handler:doInStore
			}
		]
	});
});

/**
 * 销售退货入库
 * @returns
 */
function doInStore(){
	$.messager.confirm('确认', '确认要退货入库吗', function(yes) {
		if (yes) {
			var submitData = $('#itemForm').serializeJSON();
			if(submitData.storeuuid == ''){
				$.messager.alert('提示', '请选择仓库', 'info');
				return;
			}
			$.ajax({
				url : 'returnorderdetail!doInStore.action',
				data : submitData,
				dataType : 'json',
				type : 'post',
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭入库窗口
							$('#itemDlg').dialog('close');
							// 修改明细的状态
							//   获取选中的明细
							var row = $('#itemgrid').datagrid('getSelected');//返回第一个被选中的行或如果没有选中的行则返回null。
							row.state='1'; // 设置状态为退货未入库0,退货已入库1,但此时界面没有刷新,仍为0
							//  让明细表格的数据刷新，重新刷新明细的状态
							var data = $('#itemgrid').datagrid('getData');
							// 重新加载数据, 删除旧行, 导致状态刷新
							$('#itemgrid').datagrid('loadData',data);
							// 进行判断是否所有的明细都入库了, 所有行的state=3
							/*for(var i = 0; i < data.rows.length; i++){
								break;
							}*/
							var flag = true; // 假设所有都入库了
							$.each(data.rows,function(i,r){
								if(r.state * 1 == 0){
									// 还有没入库的
									flag = false;// 标识为不能关闭
									return false;// 退出循环, java break;
								}
							});
							
							// 如果所有都入库了，
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