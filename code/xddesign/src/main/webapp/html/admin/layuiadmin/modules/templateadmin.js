/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    i.render({
        elem: "#LAY-template-manage",
        url: "/xddesign/design/customer/templates",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "tempId", width: 80, title: "ID", sort: !0},
            {field: "tempName", title: "名称", minWidth: 100}, {field: "tempDesc", title: "描述"}, {field: "houseType", title: "户型",templet:'<div>{{d.houseType.typeName}}</div>'},
            {field: "design", title: "设计人员", templet: '<div>{{d.design.username}}</div>'}, {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        parseData: function(res){ //res 即为原始返回的数据
            if(res.total === 0) {
                return {
                    'code': 201, //接口状态
                    'msg': '无数据', //提示文本
                    'count': 0, //数据长度
                    'data': [] //数据列表，是直接填充进表格中的数组
                }
            }else if (res.records.length === 0) {
                return {
                    'code': 201, //接口状态
                    'msg': '无数据', //提示文本
                    'count': 0, //数据长度
                    'data': [] //数据列表，是直接填充进表格中的数组
                }
            }else {
                return {
                    "code": 0,
                    "count": res.total, //解析数据长度
                    "data": res.records //解析数据列表
                }
            }
        },
        // height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-template-manage)", function (e) {
        if ("del" === e.event) {
            layer.confirm("真的删除么", {icon:3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/design/customer/template/delete'
                    ,type: 'POST'
                    ,data: {"tempId": e.data.tempId, "userId": e.data.design.id}
                    ,dataType: 'json'
                    ,success: function (res) {
                        if (res.status === 0) {
                            layer.msg(res.msg, {icon:5});
                            return false;
                        }
                        e.del();
                    }
                });
                layer.close(t);
            });
        }
        else if ("edit" === e.event) {
            layer.open({
                type: 2,
                title: "编辑模板信息",
                content: "templateform.html",
                maxmin: !0,
                area: ["500px", "450px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-template-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/design/customer/template/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg, {icon:5});
                                }
                                layui.table.reload('LAY-template-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='tempId']").val(e.data.tempId);
                    $("input[name='tempName']").val(e.data.tempName);
                    $("textarea[name='tempDesc']").val(e.data.tempDesc);
                }
            })
        }
    }), e("templateadmin", {})
});