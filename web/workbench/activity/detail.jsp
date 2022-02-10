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
		<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
		<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			//默认情况下取消和保存按钮是隐藏的
			var cancelAndSaveBtnDefault = true;

			$(function(){

				$("#remark").focus(function(){
					if(cancelAndSaveBtnDefault){
						//设置remarkDiv的高度为130px
						$("#remarkDiv").css("height","130px");
						//显示
						$("#cancelAndSaveBtn").show("2000");
						cancelAndSaveBtnDefault = false;
					}
				});

				$("#cancelBtn").click(function(){
					//显示
					$("#cancelAndSaveBtn").hide();
					//设置remarkDiv的高度为130px
					$("#remarkDiv").css("height","90px");
					cancelAndSaveBtnDefault = true;
				});

				$(".remarkDiv").mouseover(function(){
					$(this).children("div").children("div").show();
				});

				$(".remarkDiv").mouseout(function(){
					$(this).children("div").children("div").hide();
				});

				$(".myHref").mouseover(function(){
					$(this).children("span").css("color","red");
				});

				$(".myHref").mouseout(function(){
					$(this).children("span").css("color","#E6E6E6");
				});

				$("#remarkBody").on("mouseover",".remarkDiv",function(){
					$(this).children("div").children("div").show();
				})
				$("#remarkBody").on("mouseout",".remarkDiv",function(){
					$(this).children("div").children("div").hide();
				})



				//页面加载完毕后局部刷新备注信息列表
				showRemarkInfo();

				//添加备注信息，为保存备注信息按钮绑定事件
				$("#saveRemarkBtn").click(function (){

					//通过id发送ajax
					$.ajax({
						url : "activity/saveRemark.do",
						data : {
							"noteContent" : $.trim($("#remark").val()),
							"activityId" : "${a.id}"
						},
						type : "POST",
						dataType : "json",
						success : function (respData){
							//期待后端返回来的数据: {"success" : " " , "activityRm" : {...这里是将div的信息扑上去的数据} }

							if(respData.success){

								var html = "";
								//添加成功，将新增加的备注信息的div 放到文本域上方
								html += '<div id = '+respData.activityRm.id+' class="remarkDiv" style="height: 60px;">';
								html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
								html += '<div style="position: relative; top: -40px; left: 40px;" >';
								html += '<h5>'+respData.activityRm.noteContent+'</h5>';
								html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;"> '+(respData.activityRm.createTime)+ ' 备注人: ' + (respData.activityRm.createBy)+'</small>';
								html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
								html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"  onclick="editRemark(\''+respData.activityRm.id+'\') " style="font-size: 20px; color: #FF0000;"></span></a>';
								html += '&nbsp;&nbsp;&nbsp;&nbsp;';
								html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" onclick="deleteById(\''+respData.activityRm.id+'\') " style="font-size: 20px; color:  #FF0000;"></span></a>';
								html += '</div>';
								html += '</div>';
								html += '</div>';

								$("#showRemark").before(html);
								//清空文本域中的内容
								$("#remark").val("");


							}else{
								alert("添加失败！");
							}
						}
					})
				})

				//为修改备注信息的模态窗口中的更新按钮绑定事件
				$("#updateRemarkBtn").click(function (){
					var id = $("#remarkId").val();
					//发送ajax请求
					$.ajax({
						url : "activity/updateRemark.do",
						data : {
							"id" : id,
							"noteContent" : $.trim($("#noteContent").val()),

						},
						type : "POST",
						dataType : "json",
						success : function (respData){
							//期待后端返回来的数据: {"success" : " " , "ar" : {...这里是将div的信息扑上去的数据} }
							if(respData.success){
								//修改备注成功
								//更新div中相应的备注信息，需要修改的内容有noteContent,editTime,editBy
								$("e"+id).html(respData.ar.noteContent);
								$("s"+id).html(respData.ar.editTime +" 备注人："+ respData.ar.editBy);

								//更新内容之后，关闭模态窗口
								$("#editRemarkModal").modal("hide");
							}else{
								alert("修改失败！");
							}
						}
					})
				})

			});//end of $(function)

			function showRemarkInfo(){

				$.get("activity/getRemarkListByAid.do", {"activityId" : "${a.id}"}, function (respData){
					var html = "";
					$.each(respData,function (i,n){
						html += '<div id = '+n.id+' class="remarkDiv" style="height: 60px;">';
						html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
						html += '<div style="position: relative; top: -40px; left: 40px;" >';
						html += '<h5 id="e'+n.id+'">'+n.noteContent+'</h5>';
						html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray; id="s'+n.id+'""> '+(n.editFlag==0?n.createTime:n.editTime)+ ' 备注人: ' + (n.editFlag==0?n.createBy:n.editBy)+'</small>';
						html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
						html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"  onclick="editRemark(\''+n.id+'\')" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '&nbsp;&nbsp;&nbsp;&nbsp;';
						html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" onclick="deleteById(\''+n.id+'\') " style="font-size: 20px; color:  #FF0000;"></span></a>';
						html += '</div>';
						html += '</div>';
						html += '</div>';

					})
					$("#showRemark").before(html);
				},"json")
			}//end of showRemarkInfo()

			function deleteById(id){
				//通过id发送ajax
				$.ajax({
					url : "activity/removeRemark.do",
					data : {"id" : id},
					type : "POST",
					dataType : "json",
					success : function (respData){
						if(respData.success){
							//删除成功，处理备注信息列表
							//showRemarkInfo();

							//找到需要删除记录的div，通过id将div移除
							$("#"+id).remove();

						}else{
							alert(respData.msg);
						}
					}
				})
			}//end of deleteById

			function editRemark(id){
				//将id赋值给隐藏域中的id
				$("#remarkId").val(id);
				//通过eid获得h5标签中的noteContent
				var noteContent = $("#e"+id).html();
				//将noteContent展示在模态窗口中的文本域中
				$("#noteContent").val(noteContent);
				//打开修改备注的模态窗口
				$("#editRemarkModal").modal("show");

			}

		</script>
	</head>
	<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
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
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-marketActivityOwner">
                                    <option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>
                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                            </div>
                            <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-cost" value="5,000">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${a.name} <small>${a.startDate} ~ ${a.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${a.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${a.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${a.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${a.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${a.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${a.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${a.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<%--
		<!-- 备注 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		--%>

		<div id="showRemark" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>