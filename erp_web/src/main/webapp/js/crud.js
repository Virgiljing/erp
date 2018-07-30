//提交的方法名称
var method = "";
var height = 200;
var listParam = "";
var saveParam = "";
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:name + '!listByPage.action' + listParam,
		columns:columns,
		singleSelect: true,
		pagination: true,
		toolbar: [{
			text: '新增',
			iconCls: 'icon-add',
			handler: function(){
				//设置保存按钮提交的方法为add
				method = "add";
				//关闭编辑窗口
				$('#editDlg').dialog('open');
				
				// 清空表单
				$('#editForm').form('clear');
			}
		},{
			text: '导出',
			iconCls: 'icon-excel',
			handler: function(){
				// 得到查询条件
				var submitData = $('#searchForm').serializeJSON();
				// 导出的类型 + listParam
				$.download(name+'!export.action' + listParam,submitData);
			}
		},{
			text: '导入',
			iconCls: 'icon-save',
			handler: function(){
				// 弹出导入窗口
				$('#doImportDlg').dialog('open');
			}
		}]
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('reload',formData);
	});

	//初始化编辑窗口
	$('#editDlg').dialog({
		title: '编辑',//窗口标题
		width: 300,//窗口宽度
		height: height,//窗口高度
		closed: true,//窗口是是否为关闭状态, true：表示关闭
		modal: true,//模式窗口
		buttons:[{
			text:'保存',
			iconCls: 'icon-save',
			handler:function(){
				// 做表单字段验证，当所有字段都有效的时候返回true
				if(!$('#editForm').form('validate')){
					return;
				}
				//用记输入的部门信息
				var submitData= $('#editForm').serializeJSON();
				$.ajax({
					url: name + '!' + method + '.action' + saveParam,
					data: submitData,
					dataType: 'json',
					type: 'post',
					success:function(rtn){
						//{success:true, message: 操作失败}
						$.messager.alert('提示',rtn.message, 'info',function(){
							if(rtn.success){
								//关闭弹出的窗口
								$('#editDlg').dialog('close');
								//刷新表格
								$('#grid').datagrid('reload');
							}
						});
					}
				});
			}
		},{
			text:'关闭',
			iconCls:'icon-cancel',
			handler:function(){
				//关闭弹出的窗口
				$('#editDlg').dialog('close');
			}
		}]
	});
	
	var doImportDlg = document.getElementById('doImportDlg');
	if(doImportDlg){
		// 有导入窗口
		$('#doImportDlg').dialog({
			title:'导入数据',
			width:340,
			height:116,
			closed:true,
			modal:true,
			buttons:[
				{
					text:'导入',
					iconCls:'icon-save',
					handler:function(){
						// 表单提交的数据类型
						var formData = new FormData($('#doImportForm')[0]);
						$.ajax({
							url : name+'!doImport.action',
							data : formData,
							dataType : 'json',
							type : 'post',
							contentType:false,//传递false以告诉jQuery不要设置任何内容类型文件头, 服务器按字节流形式读取内容  服务端生效
							processData:false,//data选项的数据将被处理，jquery把我们提交的data转换成一个查询字符串提交给服务器  客户端生效
							success : function(rtn) {
								$.messager.alert('提示', rtn.message, 'info',function() {
									if (rtn.success) {
										//关闭导入窗口
										$('#doImportDlg').dialog('close');
										$('#grid').datagrid('reload');
									}
								});
							}
						});
					}
				}
			]
		});
	}

});


/**
 * 删除
 */
function del(uuid){
	$.messager.confirm("确认","确认要删除吗？",function(yes){
		if(yes){
			$.ajax({
				url: name + '!delete?id=' + uuid,
				dataType: 'json',
				type: 'post',
				success:function(rtn){
					$.messager.alert("提示",rtn.message,'info',function(){
						//刷新表格数据
						$('#grid').datagrid('reload');
					});
				}
			});
		}
	});
}

/**
 * 修改
 */
function edit(uuid){
	//弹出窗口
	$('#editDlg').dialog('open');

	//清空表单内容
	$('#editForm').form('clear');

	//设置保存按钮提交的方法为update
	method = "update";

	//加载数据
	//var data = {"t.tele":"12345678","t.uuid":1,"t.address":"建材城西路中腾商务大厦","t.email":"admin@itcast.cn","t.birthday":"1949-10-01","t.gender":1,"t.name":"超级管理员","t.dep.name":"管理员组","t.dep.tele":"000000","t.dep.uuid":1,"t.username":"admin"}
	$('#editForm').form('load',name + '!get?id=' + uuid);
	//$('#editForm').form('load',data);
}