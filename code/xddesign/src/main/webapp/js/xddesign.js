//判断是不是手机号
function isphone(phone) {
    var pattern = /^1[34578]\d{9}$/;
    return pattern.test(phone);
}

//返回上一页
function backpage() {
    history.go(-1);
}

//展示商品信息
function showproduct(id) {
    $.get("/xddesign/public/product", {"productId": id}, function (product) {
        $("#pname").html("<b>名称：</b>" + product.productName);
        var pimg = product.pictureList[0].pictureLink.replace("\\", "/");
        $("#pimg").append("<img src='" + pimg + "'>");
        $("#pdesc").append("<b>描述：</b><br>" + product.productDesc);

        var content = "";
        propertylist = product.productPropertyList;
        for (var i = 0; i < propertylist.length; i++) {
            property = propertylist[i];
            var name = property.value.property.propertyName;
            var val = property.value.valueName;
            content += "<div class='col-xs-6 col-md-3'><b>" + name + "</b>：" + val + "</div>";
        }
        $("#ptool").append(content);
        $("#pdetail").append(product.productDetail);
        // alert(product.productDetail);
    }, "json");
    $("#myModal").modal("show");
}

//加载菜单
function loadmenu() {
    $("#menu").load("/xddesign/html/menu.html");
}