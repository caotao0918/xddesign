<%--
  Created by IntelliJ IDEA.
  User: Wzz20210118
  Date: 2021/1/26
  Time: 8:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../cmn/tag/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>客户登录</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" href="${ctx}/cmn/layui/css/layui.css"  media="all">
  <link rel="stylesheet" type="text/css" href="${ctx}/css/login/cuslogin.css" />
  <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.js"></script>--%>
  <script charset="utf-8" src="${ctx}/cmn/layui/layui.js" ></script>
  <script type="text/javascript" src="${ctx}/cmn/mylayui/mylayui.js"></script>
  <script type="text/javascript" src="${ctx}/js/login/cuslogin.js"></script>
</head>

<body>
<div id = "div_bgi" class = "div_bgi">
  <div class = "div_logo"></div>

  <div id = "div_cuslogin" class = "div_cuslogin">
    <div>
      <span>账号</span>
      <input/>
    </div>
    <div>
      <span>密码</span>
      <input type = "password" />
    </div>
    <div>
      <span></span>
    </div>
  </div>
</div>

</body>

<script type="text/javascript">

</script>

</html>