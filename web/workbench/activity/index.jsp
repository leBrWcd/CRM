<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
	<html>
	<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){

		//页面一进来就显示市场活动信息列表
		pageList(1,3);

		//为创建按钮绑定点击事件
		$("#addBtn").click(function (){
			//发生ajax连接后台获取所有user
			$.ajax({
				url : "activity/user/getUserList.do",
				type : "GET",
				dataType : "json",
				success : function (respData){
					//响应回来的是一个List<User> uList ,处理为json是一个json数组，需要采用$.each()遍历
					var html = "<option></option>";
					$.each(respData,function (i,n){
						html += "<option value = " + n.id +"> " +  n.name + "</option>"
					});
					//填充option标签
					$("#create-marketActivityOwner").html(html);
					//显示的所有者默认是登录的用户
					var CurrentUserId = "${user.id}";
					$("#create-marketActivityOwner").val(CurrentUserId);
					//先获得模态窗口的id,然后使用modal(),参数:"show" 打开模态窗口，"hide":关闭模态窗口
					$("#createActivityModal").modal("show");

				}
			})
		})//end of addBtn 增加市场活动(数据未保存到数据库中)

		//为保存按钮绑定点击事件，发送ajax请求创建信息
		$("#saveBtn").click(function (){

			$.ajax({
				url : "activity/saveActivity.do",
				type : "POST",
				data : {
					"owner" : $.trim($("#create-marketActivityOwner").val()),
					"name" : $.trim($("#create-marketActivityName").val()),
					"startDate" : $.trim($("#create-startTime").val()),
					"endDate" : $.trim($("#create-endTime").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" :$.trim($("#create-describe").val())
				},
				dataType : "json",
				success : function (respData){
					if(respData.success){
						//添加成功
						alert(respData.msg);
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
						//刷新市场活动信息列表
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//清空添加操作模态窗口中表单的数据
						$("#activityAddForm").get(0).reset();

					}else{
						alert(respData.msg);
					}
				}
			})
		})//end of saveBtn  保存添加操作(将数据保存到数据库中)

		//点击查询按钮后局部刷新市场活动信息列表
		$("#searchBtn").click(function (){

			//点击查询按钮的时候，将查询内容保存到隐藏域当中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,3);
		})//end of searchBtn  查

		//为删除按钮绑定事件
		$("#deleteBtn").click(function (){
			//获取复选框选中的Jquery对象，是一个数组
			var $checkEle = $("input[name='check']:checked");
			if($checkEle.length == 0){
				alert("请选中需要删除的数据");
			}else{

				//给用户一个友好的提示，是否要删除数据
				if(confirm("你确定要删除数据吗?")){
					//已选中，可能是一个或者多个
					//最后发送的url:activity/delete.do?id=213&id=123&id=12
					var param = "";
					for(var i =0;i<$checkEle.length;i++){
						param += "id="+$($checkEle[i]).val();
						//判断id是否不是最后一个，如果不是，加上&
						if(i < $checkEle.length -1 ){
							param +="&";
						}
					}
					//发送ajax请求
					$.ajax({
						url : "activity/delete.do",
						data : param,
						dataType : "json",
						Type : "POST",
						success : function (respData){
							if(respData.success){
								alert(respData.msg);
								//刷新市场活动列表
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert(respData.msg);
							}
						}
					})
				}
			}
		})//end of deleteBtn   删

		//按修改按钮绑定事件
		$("#editBtn").click(function (){
			//修改一般只能一次修改一个,判断复选框的选择情况
			if($("input[name = 'check']:checked").length == 0){
				alert("请选择需要删除的市场活动");
			}else if($("input[name = 'check']:checked").length > 1){
				alert("一次只能修改一个");
			}else{
				//选择一个,$check的val()就是单条市场活动记录的id值
				var $check = $("input[name='check']:checked");

				//通过该市场活动的id值往后台发送请求，获取到该市场活动的其他信息，平铺到模态窗口上，再展现模态窗口
				//但是id值不能出现在模态窗口上，所以使用隐藏域
				//发送ajax到后台获取单条市场活动记录
				$.ajax({
					url:"activity/getActivityAndUserList.do",
					data : {"id" : $check.val()},
					dataType : "json",
					Type : "GET",
					success : function (respData){
						//前端要什么，后台就返回什么，很明显，前端需要一个用户列表来平铺到选择框中的所有者，以便后续修改操作
						//修改所有者，同时还需要一个传过去的id对应的市场活动，用来平铺点击修改哪个市场活动对应的信息
						//{"uList" : {'wcd'....},"activity" : {"id" : .. ,"name" : ..,"owner":..,....}}
						var html = "";
						$.each(respData.uList,function (i,n){
							html += '<option value="'+n.id+'">'+n.name+'</option>';
						})
						$("#edit-marketActivityOwner").html(html);
						//平铺单条市场活动记录
						$("#edit-id").val(respData.activity.id);		//隐藏域中的id是为了当我点击更新时，传给后台让后台知道我要修改哪一条数据
						$("#edit-marketActivityName").val(respData.activity.name);
						$("#edit-marketActivityOwner").val(respData.activity.owner);
						$("#edit-startTime").val(respData.activity.startDate);
						$("#edit-endTime").val(respData.activity.endDate);
						$("#edit-cost").val(respData.activity.cost);
						$("#edit-describe").val(respData.activity.description);

						//展现修改市场活动的模态窗口
						$("#editActivityModal").modal("show");

					}
				})

			}
		});//end of editBtn   改(数据库中的数据未修改)
		//为更新按钮绑定事件
		$("#updateBtn").click(function (){
			//根据隐藏域中的id修改数据
			$.ajax({
				url : "activity/update.do",
				type : "POST",
				data : {
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-marketActivityOwner").val()),
					"name" : $.trim($("#edit-marketActivityName").val()),
					"startDate" : $.trim($("#edit-startTime").val()),
					"endDate" : $.trim($("#edit-endTime").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" :$.trim($("#edit-describe").val())
				},
				dataType : "json",
				success : function (respData){
					if(respData.success){
						//更新成功
						alert(respData.msg);
						//关闭添加操作的模态窗口
						$("#editActivityModal").modal("hide");
						//刷新市场活动信息列表
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else{
						//更新失败
						alert(respData.msg);
					}
				}
			})
		});//end of updateBtn  (修改数据库中的数据)

		//复选框的全选和取消全选
		$("#allChecked").click(function (){
			$("input[name ='check']").prop("checked",this.checked);
		})
		$("#activityBody").on("click",$("input[name ='check']"),function (){
			$("#allChecked").prop("checked",$("input[name ='check']").length == $("input[name='check']:checked").length)
		})

		//日历控件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});


	});//end of $(funtion())
	<!--分页操作-->
	function pageList(pageNo,pageSize){

		$("#allChecked").prop("checked",false);

		//查询前，将隐藏域中的内容取出来，重新赋予到搜索框中
		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-startDate").val($("#hidden-startDate").val());
		$("#search-endDate").val($("#hidden-endDate").val());
		$.ajax({
			url: "activity/pageList.do",
			data: {
				"pageNo": pageNo,
				"pageSize": pageSize,
				//前面两个是做分页的必要条件
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner").val()),
				"startDate": $.trim($("#search-startDate").val()),
				"endDate": $.trim($("#search-endDate").val())
				//这四个是做查询操作需要的条件
			},
			type: "GET",
			dataType: "json",
			success: function (respData) {
				/*
                    respData,我们期待后台给我们返回什么数据
                    1.展示市场活动列表需要的数据 dataList
                    "dataList" :[{id : "1513"},{owner : "wcd"},...]
                    2.分页控件需要的总记录调试
                    "total" : 100
                    综合起来，那么我们需要后台返回来的数据结构是这样一个json
                    {"total" : 100,"dataList" :[{id : "1513"},{owner : "wcd"},...] }
                */
				//往tbody里面拼接html
				$("#activityBody").html("");
				var html = "";
				$.each(respData.dataList, function (i, n) {
					html += '<tr class="active">';
					html += '	<td><input type="checkbox" name="check" value='+ n.id + '></td>';
					html += '	<td><a id="TodetailPage" style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '	<td>'+n.owner+'</td>'
					html += '	<td>'+n.startDate+'</td>';
					html += '	<td>'+n.endDate+'</td>';
					html += '</tr>';
				});
				$("#activityBody").html(html);
				//计算总页数
				var totalPages = respData.total%pageSize==0?respData.total/pageSize:parseInt(respData.total/pageSize)+1;
				//数据处理完毕后，结合分页查询，对最前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: respData.total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					onChangePage : function(event, data){   //改变页数后，调用pageList(),注意这里的data是分页插件自己的
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}//end of pageList
</script>
</head>
<body>

	<!--隐藏域，分别保存搜索框中的内容-->
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								 	<!--连接数据库填充-->
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<!--隐藏域，存放市场活动记录的id-->
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	

	<!--市场活动列表-->
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="allChecked"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>