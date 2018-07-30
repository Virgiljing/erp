// 标识当前做的是哪种类型的订单，默认处理的是采购
type=2;
$(function(){
	// 订单列表
	$('#grid').datagrid({
		title:'销售订单列表',
		url : 'orders!listByPage.action?t1.type=2', // 列出销售订单
		singleSelect : true,
		pagination:true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'生成日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'endtime',title:'入库日期',width:100,formatter:formatDate},
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
			// 运单号
			$('#waybillsn').html(rowData.waybillsn);
			
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
		modal:true,
		toolbar:[
			{
				text : '导出',
				iconCls : 'icon-excel',
				handler : function() {
					$.download("orders!exportById.action",{id:$('#uuid').html()});
				}
			},
			{
				text : '物流详情',
				iconCls : 'icon-search',
				handler : function() {
					if(!$('#waybillsn').html()){
						// 没有运单号，不需要弹出窗口
						$.messager.alert('提示', '没有运单号，没有物流信息', 'info');
						return;
					}
					// 弹出窗口
					$('#waybillDlg').dialog('open');
					// 加载路径信息
					$('#waybillgrid').datagrid({
						title:'订单路径详情',
						url : 'orders!waybilldetailList.action',
						queryParams:{waybillsn:$('#waybillsn').html()},
						singleSelect : true,
						columns : [ [ 
							{field:'exedate',title:'执行日期',width:100},
				  		    {field:'exetime',title:'执行时间',width:100},
				  		    {field:'info',title:'执行信息',width:200}
				  		] ]
					});
				}
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
			{field:'num',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:formatDetailState}
		] ]
	});
	
	// 物流详情窗口初始化
	$('#waybillDlg').dialog({
		title:'物流路径信息',
		width:500,
		height:300,
		closed:true,
		modal:true
	});
	
});