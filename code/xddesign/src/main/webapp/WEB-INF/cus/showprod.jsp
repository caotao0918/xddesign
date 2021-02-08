<%--
  Created by IntelliJ IDEA.
  User: Wzz20210118
  Date: 2021/2/2
  Time: 10:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../cmn/tag/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>产品详情</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" type="text/css" href="${ctx}/css/cus/showprod.css" />
  <link rel="stylesheet" href="${ctx}/cmn/layui/css/layui.css"  media="all">
  <script charset="utf-8" src="${ctx}/cmn/layui/layui.js" ></script>
  <script type="text/javascript" src="${ctx}/cmn/mylayui/mylayui.js"></script>
  <script type="text/javascript" src="${ctx}/js/cus/showprod.js"></script>
</head>
<body>
<div>
  <div>
    <img id = "img_back" class = "img_back" src="${ctx}/img/back.svg"/>
  </div>
  <div id="div_prodimglist" class="div_prodimglist layui-carousel" lay-filter="div_prodimglist">
    <div carousel-item="">

    </div>
  </div>

  <div id = "div_proddesc" class = "div_proddesc">
    <span></span>
    <pre></pre>
  </div>

   <div id = "div_prodprop" class = "div_prodprop">

   </div>


</div>
</body>
<script type="text/javascript">
  var prodId = '${prodId}';
</script>
</html>
