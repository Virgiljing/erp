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
            {field: 'state', title: '订单状态', width: 100, formatter: formatState},
            {field: 'waybillsn', title: '运单号', width: 100}
        ]],
        toolbar: [
            {
                text: '采购退货登记',
                iconCls: 'icon-add',
                handler: function () {
                    // 弹出采购退货登记的窗口
                    $('#addReturnOrdersDlg').dialog('open');
                }
            }
        ]
    });

    // // 订单详情窗口初始化
    // $('#returnOrdersDlg').dialog({
    //     title: '订单详情',
    //     width: 700,
    //     height: 340,
    //     closed: true,
    //     modal: true
    // });


});