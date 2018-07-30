$(function () {
    //加载表格数据
    $('#grid').datagrid({
        url:'inventory!listByPage.action',
        columns:[[
            {field:'uuid',title:'编号',width:100},
            {field:'goodsName',title:'商品',width:100},
            {field:'storeName',title:'仓库',width:100},
            {field:'num',title:'数量',width:100},
            {field:'type',title:'类型',width:100,formatter:function(value){
                    if(value * 1 == 1) {
                        return '盘盈';
                    }
                    if(value * 1 == 2) {
                        return '盘亏';
                    }
                }},
            {field:'createtime',title:'登记日期',width:100, formatter: formatDate},
            {field:'checktime',title:'审核日期',width:100, formatter: formatDate},
            {field:'createrName',title:'登记人',width:100},
            {field:'checkerName',title:'审核人',width:100},
            {field:'state',title:'状态',width:100, formatter: formatState},
            {field:'remark',title:'备注',width:100},

        ]],
        singleSelect: true,
        pagination: true,
        toolbar: [{
            text: '盘盈盘亏登记',
            iconCls: 'icon-add',
            handler: function(){
                $('#editDlg').dialog('open');

                // 清空表单
                $('#editForm').form('clear');
            }
        }]
    });

    //editDlg初始化
    $("#editDlg").dialog({
        title:'盘盈盘亏登记',
        width:300,
        height:200,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-save',
            handler:function () {
                // 验证表单字段
                var inventoryNum=$("#inventoryNum").val();
                if(inventoryNum==''){
                    $.messager.alert('提示','请输入盘点数量','info');
                    return;
                }


                //获取表单数据
                var submitData= $('#editForm').serializeJSON();
                //发送异步请求，提交数据到服务端
                $.ajax({
                    url:'inventory!add.action',
                    data:submitData,
                    dataType:'json',
                    type:'post',
                    success:function (result) {
                        $.messager.alert('提示',result.message,'info',function () {
                            if(result.success){
                                // 关闭登记窗口并刷新列表
                                $('#editDlg').dialog('close');
                                $('#grid').datagrid('reload');
                            }
                        })
                    }
                })

            }
        }]
    })
})