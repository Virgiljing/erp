// 保存当前编辑行的下标
var existEditIndex = -1;
$(function () {
    //明细表格初始化
    $('#addReturnOrdersGrid').datagrid({
        title: '商品列表',
        singleSelect: true,
        columns: [[
            {
                field: 'goodsuuid',
                title: '商品编号',
                width: 80,
                editor: {type: 'numberbox', options: {disabled: true}}
            },
            {field: 'goodsname', title: '商品名称', width: 100},
            {field: 'price', title: '价格', width: 100},
            {
                field: 'num',
                title: '数量',
                width: 100
            },
            {field: 'money', title: '金额', width: 100},
            {field: 'state', title: '状态', width: 60, formatter: formatDetailState},
            {
                field: 'storeNum',
                title: '库存数量',
                width: 100
            },
            {
                field: 'returnnum', title: '退货数量', width: 60, editor: {
                    type: 'numberbox', options: {
                        min: 0,
                    }
                }
            }
        ]],
        onClickRow: function (rowIndex, rowData) {
            // rowindex 点击的行的下标,
            // rowData, 行的数据
            // 存在编辑的行, 则把它关闭，数据就会进入表格中
            $('#addReturnOrdersGrid').datagrid('endEdit', existEditIndex);
            // 开启编辑
            $('#addReturnOrdersGrid').datagrid('beginEdit', rowIndex);
            // 此时编辑的行下标
            existEditIndex = rowIndex;
            // $(this).datagrid('unselectRow', rowIndex);
        },
        toolbar: [
            {
                text: '提交',
                iconCls: 'icon-save',
                handler: function () {
                    var submitData = $('#orderForm').serializeJSON();
                    if (submitData['t.ordersuuid'] == '') {
                        $.messager.alert('提示', '请选择订单', 'info');
                        return;
                    }
                    // 关闭下在编辑的行
                    if (existEditIndex > -1) {
                        // 存在编辑的行, 则把它关闭，数据就会进入表格中
                        $('#addReturnOrdersGrid').datagrid('endEdit', existEditIndex);
                    }
                    // 明细的数据
                    var rows = $('#addReturnOrdersGrid').datagrid('getRows');
                    // 把所有的明细转成json格式的字符串
                    var json = JSON.stringify(rows);
                    submitData.json = json;
                    $.ajax({
                        url: 'returnorders!add.action',
                        data: submitData,
                        dataType: 'json',
                        type: 'post',
                        success: function (rtn) {
                            $.messager.alert('提示', rtn.message, 'info', function () {
                                if (rtn.success) {
                                    // 清空采购订单
                                    $('#ordersuuid').combogrid('clear');
                                    // 加载空的数据达到清空的效果
                                    $('#addReturnOrdersGrid').datagrid('loadData', {
                                        total: 0,
                                        rows: [],
                                        footer: [{num: 0, money: 0}]
                                    });
                                    // 关闭采购退货登记窗口
                                    $('#addOrdersDlg').dialog('close');
                                    // 刷新订单列表
                                    $('#grid').datagrid('reload');
                                }
                            });
                        }
                    });
                }
            }
        ],
    });

// 采购订单下拉表格
    $('#ordersuuid').combogrid({
        panelWidth: 750,// 面板宽度
        idField: 'uuid',// 要提交的数据
        textField: 'uuid',
        url: 'orders!listByPage.action?t1.type=1&t1.state=3',
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'createtime', title: '生成日期', width: 100, formatter: formatDate},
            {field: 'checktime', title: '审核日期', width: 100, formatter: formatDate},
            {field: 'starttime', title: '确认日期', width: 100, formatter: formatDate},
            {field: 'endtime', title: '入库日期', width: 100, formatter: formatDate},
            {field: 'createrName', title: '下单员', width: 100},
            {field: 'checkerName', title: '审核员', width: 100},
            {field: 'starterName', title: '采购员', width: 100},
            {field: 'enderName', title: '库管员', width: 100},
            {field: 'supplierName', title: '供应商', width: 100},
            {field: 'totalmoney', title: '合计金额', width: 100},
            {field: 'state', title: '状态', width: 100, formatter: formatState},
            {field: 'waybillsn', title: '运单号', width: 100}
        ]],
        onClickRow: function (rowIndex, rowData) {
            $('#addReturnOrdersGrid').datagrid('loadData', rowData.orderDetails);
        }
    });
    $('#addReturnOrdersDlg').dialog({
        title: '添加订单',
        width: 700,
        height: 400,
        closed: true,
        modal: true
    });

});










