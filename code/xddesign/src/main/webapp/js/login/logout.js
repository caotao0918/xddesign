function userlogout() {
    $.ajax({
        url: '/user/logout',
        dataType:"json",
        type:"post",
        success:function(res) {
            if(1 == res.status){
                window.localStorage.clear();
                location.href = '../login/userlogin.html';
            }else {
                alert("退出失败");
            }
        }
    });
}

function cuslogout() {
    $.ajax({
        url: '/customer/logout',
        dataType:"json",
        type:"POST",
        success:function(res) {
            if(1 == res.status){
                window.localStorage.clear();
                location.href = '../login/cuslogin.html';
            }else {
                alert("退出失败,请重新登录");
            }
        }
    });
}

// 解决Ajax异步请求 springMvc 不跳转页面的问题
$.ajaxSetup( {
    //设置ajax请求结束后的执行动作
    complete :
        function(xhr, status) {
            //拦截器拦截没有权限跳转
            // 通过xhr取得响应头
            var REDIRECT = xhr.getResponseHeader("REDIRECT");
            //如果响应头中包含 REDIRECT 则说明是拦截器返回的
            if (REDIRECT === "REDIRECT")
            {
                window.location.href = xhr.getResponseHeader("CONTENTPATH");
            }
        }
});