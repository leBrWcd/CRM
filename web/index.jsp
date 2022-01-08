<%--
  Created by IntelliJ IDEA.
  User: Lebr7Wcd
  Date: 2022/1/5
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String basePath = request.getScheme() + "://" + request.getServerName() + ":"
          + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>ajax</title>
    <script src="js/jquery-3.6.0.js"></script>
    <script>
      $(function (){
        $("#btn").click(function (){
          $.ajax({
            url : "mytest02.do",
            type : "get",
            dataType : "json",
            success : function (respData){
              $.each(respData,function (i,n){
                alert(n.id);
                alert(n.name);
                alert(n.age);
              })
            }
          })
        });
      })
    </script>
  </head>
  <body>
    <button id="btn">点击我</button>
  </body>
</html>
