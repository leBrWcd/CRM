<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>

	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-3.6.0.js"></script>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function (){
			//设置为顶级窗口
			if(window.top!=window){
				window.top.location=window.location;
			}
			//清空用户输入框
			$("#loginAct").val("");
			//用户名输入框聚焦
			$("#loginAct").focus();
			$("#loginAct").click(function (){
				//清空消息提示
				$("#msg").html("");
			})

			//点击登录按钮时登录或者按下回车键登录
			$("#loginBtn").click(function (){
				login();
			});
			$(window).keydown(function(event){
				if(event.keyCode == 13){
					login();
				}
			})
		})

		function login(){
			//账号密码不能为空
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			var AutoLogin = $("#AutoLogin").val();
			if(loginAct == "" || loginPwd == ""){
				$("#msg").html("账号和密码不能为空");
				//如果出错，终止js代码的执行
				return false;
			}
			//程序运行到此处表示账号密码不为空，那么接着发送请求去后台验证账号和密码
			$.ajax({
				url : "user/login.do",
				data : {
					"loginAct" : loginAct,
					"loginPwd" : loginPwd,
					"AutoLogin" : AutoLogin
				},
				type : "post",
				dataType : "json",
				success : function (respData){
					//需要后台返回{"success" : true/false, "msg" : "登录结果"}
					if(respData.success){
						//登录成功，跳转到首页
						window.location.href = "workbench/index.jsp";
					}else{
						$("#msg").html(respData.msg);
					}
				}
			})
		}
	</script>
	</head>
	<body>
		<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
			<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
		</div>
		<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
			<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022 LebrWcd</span></div>
		</div>

		<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
			<div style="position: absolute; top: 0px; right: 60px;">
				<div class="page-header">
					<h1>登录</h1>
				</div>
				<form action="workbench/index.jsp" class="form-horizontal" role="form">
					<div class="form-group form-group-lg">
						<div style="width: 350px;">
							<input id="loginAct" class="form-control" type="text" placeholder="用户名">
						</div>
						<div style="width: 350px; position: relative;top: 20px;">
							<input id="loginPwd" class="form-control" type="password" placeholder="密码">
						</div>
						<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

								<span id="msg" style="color: red;font-size: 15px"></span>
						</div>
						<button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
						<br>
					</div>
					<br>
					<span><input id="AutoLogin" type="checkbox" value="ok"> 十天内免登录</span>
				</form>
			</div>
		</div>
	</body>
</html>