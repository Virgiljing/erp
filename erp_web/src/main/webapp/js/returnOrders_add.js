    

var currentIndex=-1;
var name="returnorders";
    var columns=[[
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'录入日期',width:100,formatter:formatDate},
			{field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			{field:'endtime',title:'入库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'checkerName',title:'审核员',width:100},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'客户',width:100},
			{field:'totalmoney',title:'总金额',width:100},
			{field:'state',title:'订单状态',width:100,formatter:function(value){
				if(value='0'){
					return '未审核';
				}else if(value='1'){
					return '已审核';
				}else{
					return '已结束';
				}
			}},
			{field:'waybillsn',title:'运单号',width:100}
           
        ]];

$(function(){
	
	$('#grid').datagrid({
		url:'returnorders!listByPage.action?t1.type=2&t1.state=0',
		columns:columns,
		showFooter:true,
		singleSelect: true,
        toolbar:[{
        	text:'销售退货登记',
    		iconCls: 'icon-add',
    		handler: function(){
    			//开启对话窗口
    			$('#editDlg').dialog('open');
    		}
        	
        }]
		
	});
	
	
	$('#editDlg').dialog({
		title:'增加订单',
		width:700,
		height:340,
		closed:true,
		modal:true
	});
	
	//定义客户选择框
	$('#supplier').combogrid({    
		mode:'remote',
	    panelWidth:800,    
	    idField:'uuid',    
	    textField:'name',    
	    url:'supplier!list.action?t1.type=2',    //客户的编号是2
	    columns:[[    
			{field:'uuid',title:'编号',width:100},
			{field:'name',title:'名称',width:100},
			{field:'address',title:'联系地址',width:100},
			{field:'contact',title:'联系人',width:100},
			{field:'tele',title:'联系电话',width:100},
			{field:'email',title:'邮件地址',width:100}
	    ]]    
	});  
	
	
	
	$('#grid_add').datagrid({
		url:'',
		columns:[[
					{field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{
						disabled:true}}
					},
					{field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
		  					valueField:'name',
		  					textField:'name',
		  					url:'goods!list.action',
		  					onSelect:function(data){
		  						//在触发函数的时候拿到选项的对象,然后通过坐标拿到编辑器并且赋值
		  						var edituuid = getEditor('goodsuuid');//uuid编辑器
		  						$(edituuid.target).val(data.uuid);
		  						var editprice= getEditor('price');//outprice的编辑器
		  						$(editprice.target).val(data.outprice);
		  						
		  						//选择商品的时候调用计算方法
		  						cal();
		  						sum();
		  						
		  					}
	  					}}
					},
					{field:'price',title:'价格(￥)',width:100,editor:{type:'numberbox',options:{
		  				min:0,precision:2,disabled:true
		  				}}
		  			},
					{field:'returnnum',title:'数量',width:100,editor:{type:'numberbox',options:{
		  				min:0
		  				}}
		  			},
		  			{field:'money',title:'金额(￥)',width:100,editor:{type:'numberbox',options:{
		  				min:0,
		  				precision:2,
		  				disabled:true,
		  				}}
		  			},
		            {field:'-',title:'操作',width:100,formatter: function(value,row,index){
		                  var oper =' <a href="javascript:void(0)" onclick="del(' + index + ')">删除</a>';
		                  //修改使得操作行没有合计字段
		                  if(row.returnnum=='合计(￥)'){
		                 		return;
		                 	}
		                  return oper;
		              	}
		  			}
		        ]],
        showFooter:true,
		singleSelect: true,
		toolbar:[
	                 {
	        			text:'增加',
		        		iconCls: 'icon-add',
		        		handler: function(){
		        			//先将之前编辑的行进行关掉,然后更新当前行的索引
		        			if(currentIndex>-1){
		        				$('#grid_add').datagrid('endEdit',currentIndex);
		        			}
		        			
		        			$('#grid_add').datagrid('appendRow',{price:0,returnnum:0,money:0});
		        			
		        			currentIndex= $('#grid_add').datagrid('getRows').length - 1;
		        			
		        			$('#grid_add').datagrid('beginEdit',currentIndex);
		        			
		        			bindGridEvent();
		        		}
	    			 },{
	    					text:'提交订单',
	    					iconCls: 'icon-save',
	    					handler:function(){
	    					//将表格栏的数据转换成json字符串传送到前端
	    						var submitData = $('#editForm').serializeJSON();
	    						if(submitData['t.supplieruuid']==''){
	    							$.messager.alert('提示',"请您先选择客户",'info');
	    							return;
	    						}
	    						if(currentIndex > -1){
	    							// 存在编辑的行, 则把它关闭，数据就会进入表格中
	    							$('#grid_add').datagrid('endEdit',currentIndex);
	    						}  
	    						var rows = $('#grid_add').datagrid('getRows');
	    						var json = JSON.stringify(rows);
	    						if(rows.length==0){
	    							$.messager.alert('提示',"请您添加退单详情!",'info');
	    							return;
	    						}
	    						
	    						for(var i = 0 ; i<rows.length;i++){
	    							if(rows[i].goodsname==''){
	    								$.messager.alert('提示',"请您选择退单详情的商品名字!",'info');
		    							return;
	    							}
	    							
	    							if(rows[i].returnnum==0){
	    								$.messager.alert('提示',"退单详情的商品数目不允许为空!",'info');
		    							return;
	    							}
	    						}
	    						submitData.json = json;
	    						$.ajax({
	    							url : 'returnorders!addSaleReturnOrder.action',
	    							data : submitData,
	    							dataType : 'json',
	    							type : 'post',
	    							success : function(rtn) {
	    								$.messager.alert('提示', rtn.message, 'info',function(){
	    									if(rtn.success){
	    										// 清空供应商
	    										$('#supplier').combogrid('clear');
	    										// 加载空的数据达到清空的效果
	    										$('#grid_add').datagrid('loadData',{total:0,rows:[],footer:[{returnnum:0,money:0}]});
	    										//关闭会话窗口,重载基本页面
	    										$('#editDlg').dialog('close');
	    										$('#grid').datagrid('reload');
	    									}
	    								});
	    							}
	    						});
	    						
	    						
	    						
	    					}
	    				}
	    			 ],
        
        onClickRow:function(rowIndex, rowData){
			//当点击新的记录行的时候,把原先的记录行关掉,并且开启新的记录行的编辑
			//,然后更新当前行记录
        	$('#grid_add').datagrid('endEdit',currentIndex);
        	currentIndex= rowIndex;
        	$('#grid_add').datagrid('beginEdit',currentIndex);
        	bindGridEvent();
		}
		
		
	});
	
	//当弹出窗口加载完成之后,添加页脚
	$('#grid_add').datagrid('reloadFooter',[{returnnum:'合计(￥)',money:0}]);	
	
	
	//拿到对应字段的编辑器对象
	function  getEditor(field){
		
		return  $('#grid_add').datagrid('getEditor',{index:currentIndex,field:field});
		
	}
	
	
	
	
	function cal(){
		//拿到指定行的数量和价格的编辑器
		//分别通过编辑器拿到价格和数量大小
		var numEditor = getEditor('returnnum');
		var priceEditor = getEditor('price');
		var returnnum = $(numEditor.target).val();
		var price = $(priceEditor.target).val();
		//计算得到总的价格,然后赋值到totalmoney中
		var money=  (1*returnnum*price).toFixed(2);
		var moneyEditor =getEditor('money');
		$(moneyEditor.target).val(money);
		
		
		// 让money进入到表格的数据中
		// 获取表格的所有数据
		var rows = $('#grid_add').datagrid('getRows');// 返回当前页的所有行。 
		// 当前正处于编辑的行
		rows[currentIndex].money = money;
		
		
	}
	
	function  bindGridEvent(){
		//针对每一行,拿到对应的数量编辑器
		//绑定数量编辑器的keyup事件
		var numEditor = getEditor('returnnum');
		$(numEditor.target).bind('keyup',function(){
			cal();
			sum();
		});
		
		
	}
	
	
	
	
	
	
	
	
})

//设定计算总金额的函数
	function sum(){
		// 返回当前页的所有行。 
		var rows = $('#grid_add').datagrid('getRows');
		var totalMoney = 0;
		$.each(rows,function(i,row){
			totalMoney =totalMoney+ row.money * 1;
		});
		totalMoney = totalMoney.toFixed(2);
		
		// 更新页脚行并载入新数据
		$('#grid_add').datagrid('reloadFooter',[
			{returnnum:'合计(￥)', money: totalMoney}
		]);
		
	}


function del(index){
		if(currentIndex>-1){
			$('#grid_add').datagrid('endEdit',currentIndex);
		}
		currentIndex=-1;
		$('#grid_add').datagrid('deleteRow',index);
		var data= $('#grid_add').datagrid('getData');
		$('#grid_add').datagrid('loadData',data);
		sum();
	}