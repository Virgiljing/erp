$(function(){
	$('#grid').datagrid({
		url : 'emp!listByPage.action',
		singleSelect : true,
		columns : [[
            {field:'uuid',title:'编号',width:100},
            {field:'username',title:'登陆名',width:100},
            {field:'name',title:'真实姓名',width:100},
            {field:'gender',title:'性别',width:100,formatter:function(value){
                if(value * 1 == 1){ // 类型强转
                    return '男';
                }
                if(value * 1 == 0){
                    return '女';
                }
            }},
            {field:'email',title:'邮件地址',width:100},
            {field:'tele',title:'联系电话',width:100},
            {field:'address',title:'联系地址',width:100},
            {field:'birthday',title:'出生年月日',width:100,formatter:formatDate},
            {field:'dep',title:'部门',width:100,formatter:function(value){
                if(value){ // value代表着dep对象
                    return value.name
                }
            }},

            {field:'-',title:'操作',width:100,formatter: function(value,row,index){
                var oper = "<a href=\"javascript:void(0)\" onclick=\"updatePwd_reset(" + row.uuid + ')">重置密码</a>';
                return oper;
            }}
        ]]
	});
	
	// 重置密码窗口初始化
	$('#editDlg').dialog({
		title:'重置密码',
		width:260,
		height:120,
		closed:true,
		modal:true,
		buttons:[
			{
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					var submitData = $('#editForm').serializeJSON();
					$.ajax({
						url : 'emp!updatePwd_reset.action',
						data : submitData,
						dataType : 'json',
						type : 'post',
						success : function(rtn) {
							$.messager.alert('提示', rtn.message, 'info',function(){
								if(rtn.success){
									// 关闭重置密码的窗口
									$('#editDlg').dialog('close');
								}
							});
						}
					});
				}
			}
			]
	});
});

function updatePwd_reset(uuid){
	//$('#editForm').form('load',{id:uuid});
	$('#id').val(uuid);
	// 打开窗口
	$('#editDlg').dialog('open');
}