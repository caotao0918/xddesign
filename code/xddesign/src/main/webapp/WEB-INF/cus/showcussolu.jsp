<%--
  Created by IntelliJ IDEA.
  User: Wzz20210118
  Date: 2021/1/31
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../cmn/tag/tag.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>方案详情</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <!-- we code these -->
  <link rel="stylesheet" type="text/css" href="${ctx}/css/cus/showcussolu.css" />
  <script type="text/javascript" src="${ctx}/js/cus/showcussolu.js"></script>
</head>
<body>
<div>
  <div>
    <img id = "img_back" class = "img_back" src="${ctx}/img/back.svg"/>
  </div>
  <div id = "div_soluname" class = "div_soluname">
    <span></span>
  </div>
  <div id = "div_roomlist" class = "div_roomlist">
  </div>
  <div id = "div_tab" class = "div_tab">
  </div>

</div>
</body>
<script type="text/javascript">
  $('#div_soluname span').html('${solu.soluName}');
  function bindRoom() {
    var add = "<div>" + allProd_text + "</div>";
    <c:forEach items="${solu.roomList}" var="roomCurr" varStatus="curr">
    add += "<div>" + '${roomCurr.roomName}' + "</div>";
    </c:forEach>
    $('#div_roomlist').append(add);
    $('#div_roomlist').children().addClass("div_room");
  }
  function bindProdList(){
    var add = "";
    <c:forEach items="${solu.roomList}" var="roomCurr" varStatus="curr">
    add += "<div>";
    <c:forEach items="${roomCurr.productNumList}" var="prodNumCurr" varStatus="curr">
    for(var i  = 0;i< ${prodNumCurr.pnNum};i++){
      add += "<div>" +
              "<img src = '${prodNumCurr.product.pictureList[0].pictureLink}' />" +
              "<span>" + '${prodNumCurr.product.productName}' + "</span>" +
              "<span style = 'display: none;'>" + '${prodNumCurr.product.productId}' + "</span>" +
              "</div>";
    }
    </c:forEach>
    add += "</div>";
    </c:forEach>

    $('#div_tab').append(add);
    var addFirst = "<div>";
    $('#div_tab>div').each(function(){
      addFirst += $(this).html();
    })
    addFirst += "</div>";
    $('#div_tab').prepend(addFirst);

    $('#div_tab>div').addClass("div_prodlist");
    $('#div_tab>div div').addClass("div_prod");
  }
</script>
</html>
