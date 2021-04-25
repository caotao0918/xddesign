//判断是不是手机号
function isphone(phone) {
    var pattern = /^1[3456789]\d{9}$/;
    return pattern.test(phone);
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