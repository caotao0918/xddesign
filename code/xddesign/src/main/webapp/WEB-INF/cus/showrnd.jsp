<%--
  Created by IntelliJ IDEA.
  User: Wzz20210118
  Date: 2021/1/31
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../cmn/tag/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>设计图</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" href="${ctx}/cmn/layui/css/layui.css"  media="all">
  <link rel="stylesheet" type="text/css" href="${ctx}/css/cus/showrnd.css" />
  <script type="text/javascript" src="${ctx}/cmn/layui/layui.js"></script>
  <script type="text/javascript" src="${ctx}/cmn/mylayui/mylayui.js"></script>
  <script type="text/javascript" src="${ctx}/js/cus/showrnd.js"></script>
</head>
<body>
<div>
  <img id = "img_back" class = "img_back" src="${ctx}/img/back.svg"/>
</div>
<div id = "div_soluimgtitle" class = "div_soluimgtitle"></div>
<div id = "div_soluimglist" class = "div_soluimglist">

</div>
</body>
</body>
<script type="text/javascript">
  var soluId = '${soluId}';
</script>
</html>
