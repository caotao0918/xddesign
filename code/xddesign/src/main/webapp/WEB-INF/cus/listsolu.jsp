<%--
  Created by IntelliJ IDEA.
  User: Wzz20210118
  Date: 2021/1/28
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../cmn/tag/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>方案列表</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" type="text/css" href="${ctx}/css/cus/listsolu.css" />
  <link rel="stylesheet" href="${ctx}/cmn/layui/css/layui.css"  media="all">
  <script charset="utf-8" src="${ctx}/cmn/layui/layui.js" ></script>
  <script type="text/javascript" src="${ctx}/js/cus/listsolu.js"></script>
</head>
<body>
<div>
  <div>
    <img id = "img_back" class = "img_back" src="${ctx}/img/back.svg"/>
  </div>
  <div>
    <table id = "t_solu" class = "t_solu">
    </table>
  </div>
  <div id = "div_pagerender" class = "div_pagerender">

  </div>



</div>

</body>
<script type="text/javascript">
    var cusId = '${cusId}';
    var cusName = '${cusName}';
</script>
</html>
