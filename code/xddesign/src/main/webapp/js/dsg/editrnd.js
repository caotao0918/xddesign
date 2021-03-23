/**
 *--------------------------------------------------------------------------编辑方案设计图
 */
var soluId = UrlParam.param("soluId");
$(function(){

    $('#img_back').click(function() {
        history.go(-1);
    });
})
;layui.define(["table", "form"], function (e) {
    var t = layui.$;
    var i = layui.table;
    var form = layui.form;
    i.render({
        elem: "#LAY-dsg-renderings",
        url: "/xddesign/public/customer/renderings",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "rendId", width: 80, title: "ID", sort: !0},
            {field: "rendName", title: "名称"}, {field: "rendPath", title: "图片", templet: "#imgTpl"}, {field: "rendDesc", title: "描述"},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-webuser"}]],
        skin: 'row',
        even: true,
        where: {"soluId": soluId},
        parseData: function (res) { //res 即为原始返回的数据
            if (res.total == 0) {
                return {
                    'code': 201, //接口状态
                    'msg': '无数据', //提示文本
                    'count': 0, //数据长度
                    'data': [] //数据列表，是直接填充进表格中的数组
                }
            } else if (res.records.length == 0) {
                return {
                    'code': 201, //接口状态
                    'msg': '无数据', //提示文本
                    'count': 0, //数据长度
                    'data': [] //数据列表，是直接填充进表格中的数组
                }
            } else {
                return {
                    "code": 0,
                    "count": res.total, //解析数据长度
                    "data": res.records //解析数据列表
                }
            }
        },
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-dsg-renderings)", function (e) {
        if ("del" === e.event) {
            layer.confirm("真的删除么", {icon:3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: ''
                    , type: 'POST'
                    , data: JSON.stringify(e.data)
                    , dataType: 'json'
                    , contentType: 'application/json;charset=utf-8'
                    , success: function (res) {
                        if (res.status === 0) {
                            layer.msg(res.msg, {icon: 5});
                            return false;
                        }
                        e.del();
                    }
                });
                layer.close(t);
            });
        }
    })
});