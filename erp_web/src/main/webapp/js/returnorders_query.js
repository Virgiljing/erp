$(function () {
    // 订单列表
    $('#grid').datagrid({
        title: '采购退货列表',
        url: 'returnorders!myListByPage.action?t1.type=1', // 列出我的采购退货
        singleSelect: true,
        pagination: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'createtime', title: '录入日期', width: 100, formatter: formatDate},
            {field: 'checktime', title: '审核日期', width: 100, formatter: formatDate},
            {field: 'endtime', title: '出库日期', width: 100, formatter: formatDate},
            {field: 'createrName', title: '下单员', width: 100},
            {field: 'checkerName', title: '审核员', width: 100},
            {field: 'enderName', title: '库管员', width: 100},
            {field: 'supplierName', title: '供应商', width: 100},
            {field: 'totalmoney', title: '合计金额', width: 100},
            {field: 'state', title: '订单状态', width: 100, formatter: ReturnformatState},
            {field: 'waybillsn', title: '运单号', width: 100, formatter: function(value){
            	if(value) {
            		return '<a href="javascript:void(0);" onclick="searchWS( ' + value + ' )" >运单详情</a>';
            	}
            	return '没有运单';
            }}
        ]]

    });
    
	// 物流详情窗口初始化
	$('#waybillDlg').dialog({
        title: '运单详情',
		width:500,
		height:300,
		closed:true,
		modal:true
	});
	
	$('#waybillGrid').datagrid({
        url: 'returnorders!waybilldetailList.action', 
        singleSelect: true,
        columns: [[
            {field: 'exedate', title: '执行日期', width: 100},
            {field: 'exetime', title: '执行时间', width: 80},
            {field: 'info', title: '执行信息', width: 300}
        ]]

    });

});

function searchWS(value) {
	$('#waybillDlg').dialog('open');
	$('#waybillGrid').datagrid('loadData',{total:0,rows:[]});
	$('#waybillGrid').datagrid('load',{waybillsn:value});

}