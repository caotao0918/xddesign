/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    var i = (layui.$, layui.layer, layui.laytpl, layui.setter, layui.view, layui.admin);
    i.events.logout = function () {
        let $ = layui.$;
        $.ajax({
            url: "/xddesign/user/logout", type: "POST", dataType: "JSON", success: function (res) {
                if (res.status === 1) {
                    window.localStorage.clear();
                    window.location.href = "../../index.html";
                }else {
                    layui.layer("退出失败");
                }
            }
        })
    }, e("common", {})
});