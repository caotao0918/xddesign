//判断是不是手机号
function isphone(phone) {
    var pattern = /^1[34578]\d{9}$/;
    return pattern.test(phone);
}

//判断是不是数字
function isnum(val) {
    if (val != '') {
        var reg = /^[0-9]+.?[0-9]*$/;
        var pattern = new RegExp(reg);
        return pattern.test(val);
    }
}

//返回上一页
function backpage() {
    history.go(-1);
}

//加载菜单
function loadmenu() {
    let userId = window.localStorage.getItem("userId");
    let cusId = window.localStorage.getItem("cusId");
    $("#menu").load("/xddesign/html/menu.html");
    if (cusId != null) {
        // $("#menu").load("/xddesign/html/menu.html");
    }
}

//提示信息
function setinfo(info, success) {
    $(".info,#info").removeClass("alert-success");
    $(".info,#info").removeClass("alert-danger");
    if (success) {
        $(".info,#info").addClass("alert-success");
    } else {
        $(".info,#info").addClass("alert-danger");
    }
    $(".info,#info").show();
    $(".info,#info").html(info);
}

//输入框内容不为空按钮才可点击
function valsetel(inputid, btnid) {
    var inputobj = $("#" + inputid);
    var btnobj = $("#" + btnid);
    inputobj.bind("input propertychange", function () {
        if (inputobj.val() == "") {
            btnobj.attr("disabled", true);
        } else {
            btnobj.attr("disabled", false);
        }
    });
}

//多个输入框都不为空按钮才可点击
function valsbtns(inputids, btnids) {
    var content = "";
    for (var i = 0; i < inputids.length; i++) {
        content += "#" + inputids[i] + ",";
    }
    content = content.substring(0, content.length - 1);
    $(content).bind("input propertychange", function () {
        var isnull = false;
        for (var i = 0; i < inputids.length; i++) {
            if ($("#" + inputids[i]).val() == "") {
                isnull = true;
            }
        }
        for (var i = 0; i < btnids.length; i++) {
            if (isnull) {
                $("#" + btnids[i]).attr("disabled", true);
            } else {
                $("#" + btnids[i]).attr("disabled", false);
            }
        }
    });
}

//获取地址参数
function geturlparabyname(url, name) {
    var res = undefined;
    if (url.indexOf("?") == -1) {
        return res;
    }
    var para = url.split("?")[1];
    if (para.indexOf("&") == -1) {
        return para.split("=")[0] == name ? para.split("=")[1] : res;
    } else {
        var paras = para.split("&");
        $.each(paras, function (i, val) {
            if (val.split("=")[0] == name) {
                res = val.split("=")[1];
            }
        });
        return res;
    }
}

//同级按钮只选中当前
function choosecurrbtn(obj) {
    $(obj).removeClass("btn-default");
    $(obj).addClass("btn-primary");
    $(obj).siblings().removeClass("btn-default");
    $(obj).siblings().removeClass("btn-primary");
    $(obj).siblings().addClass("btn-default");
}

//查看设计
function showdesign(designid, tempname) {
    window.location.href = "/xddesign/html/showdesign.html?designid=" + designid + "&tempname=" + tempname;
}

//查看报表
function showreport(designid) {
    window.location.href = "/xddesign/html/showreport.html?designid=" + designid;
}

//查看效果图
function showrendering(designid) {
    window.location.href = "/xddesign/html/showrendering.html?designid=" + designid;
}

//首页
function toxddesign() {
    location.href = "/xddesign/html/index.html";
}