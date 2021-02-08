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
  <title>方案报价表</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" type="text/css" href="${ctx}/css/cus/showquote.css" />
    <script type="text/javascript" src="${ctx}/js/cus/showquote.js"></script>
</head>
<body>
<div class = "div_quotepage">
    <div>
      <img id = "img_back" class = "img_back" src="${ctx}/img/back.svg"/>
    </div>
    <div id = "div_quotetitle" class = "div_quotetitle"></div>
    <div>
      <table id = "t_quote" class = "t_quote">
        <thead></thead>
        <tbody></tbody>
        <tfoot></tfoot>
      </table>
    </div>

    <div id = "div_downquote" class = "div_downquote">
    </div>
</div>
</body>
<script type="text/javascript">
    var soluId = '${soluId}';
</script>
</html>
