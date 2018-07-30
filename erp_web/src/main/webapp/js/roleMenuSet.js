$(function() {
	/*$('#tree').tree({
		url:'role!readRoleMenus.action',
		checkbox:true,//定义是否在每一个借点之前都显示复选框。
		animate:true
		data: [{
			text: 'Item1AAAAA',
			children: [{
				text: 'Item11BBBB',
				checked:true
			},{
				text: 'Item12'
			}]
		},{
			text: 'Item2',
			children: [{
				text: 'Item21',
				children: [{
					text: 'Item211'
				},{
					text: 'Item212'
				}]
			},{
				text: 'Item22'
			}]
		}]
	});*/

	// 角色列表
	$('#grid').datagrid({
		url : 'role!list.action',
		singleSelect : true,
		columns : [ [ 
			{field:'uuid',title:'编号',width:100},
			{field:'name',title:'名称',width:100}
		] ],
		onClickRow:function(rowIndex, rowData){
			// 显示角色权限
			$('#tree').tree({
				url:'role!readRoleMenus.action?id=' + rowData.uuid,
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
			$.messager.alert('提示', '请选择角色', 'info');
			return;
		}
		// 保存所有选中的菜单编号
		var ids = []; // new Array();
		if(nodes){
			for(var i = 0; i < nodes.length; i++){
				// 菜单的编号
				ids.push(nodes[i].id);
			}
		}

		var submitData = {};
		submitData.id=row.uuid;
		//结果字符串由逗号分隔，菜单编号多个以逗号分割
		submitData.ids = ids.toString();
		
		$.ajax({
			url : 'role!updateRoleMenus.action',
			data : submitData,
			dataType : 'json',
			type : 'post',
			success : function(rtn) {
				$.messager.alert('提示', rtn.message, 'info');
			}
		});
	});
	
	
	
	
	
});