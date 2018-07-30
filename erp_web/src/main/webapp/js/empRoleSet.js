$(function() {

	// 用户列表
	$('#grid').datagrid({
		url : 'emp!list.action',
		singleSelect : true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'name',title:'名称',width:100}
		] ],
		onClickRow:function(rowIndex, rowData){
			// 显示用户角色
			$('#tree').tree({
				url:'emp!readEmpRoles.action?id=' + rowData.uuid,
				checkbox:true,
				animate:true
			});
		}
	});
	
	// 更新
	$('#btnSave').bind('click',function(){
		// 获取所有选中的节点
		var nodes = $('#tree').tree('getChecked');
		var row = $('#grid').datagrid('getSelected');
		if(null == row){
			$.messager.alert('提示', '请选择用户', 'info');
			return;
		}
		// 保存所有选中的角色编号
		var ids = []; // new Array();
		if(nodes){
			for(var i = 0; i < nodes.length; i++){
				// 角色的编号
				ids.push(nodes[i].id);
			}
		}

		var submitData = {};
		submitData.id=row.uuid;
		//结果字符串由逗号分隔，角色编号多个以逗号分割
		submitData.ids = ids.toString();
		
		$.ajax({
			url : 'emp!updateEmpRoles.action',
			data : submitData,
			dataType : 'json',
			type : 'post',
			success : function(rtn) {
				$.messager.alert('提示', rtn.message, 'info');
			}
		});
	});
	
	
	
	
	
});