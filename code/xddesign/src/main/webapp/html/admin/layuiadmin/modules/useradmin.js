/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        elem: "#LAY-user-manage",
        url: "/admin/user",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 100, title: "ID", sort: !0},
            {field: "username", title: "用户名", minWidth: 100}, {field: "mobile", title: "手机号"}, {field: "role", title: "角色",templet:'<div>{{d.role.name}}</div>'},
            {field: "lastTime", title: "上次登陆时间"}, {field: "addTime", title: "加入时间", sort: !0},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-webuser"}]],
        request:{
          pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        where: {"role.id":0},
        parseData: function(res){ //res 即为原始返回的数据
            if(res.total == 0) {
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-manage)", function (e) {
        e.data;
        if ("del" === e.event)
            // layer.prompt({formType: 1, title: "敏感操作，请验证口令"}
            // , function (t, i) {
            // layer.close(i),
            layer.confirm("真的删除行么", function (t) {
                layui.$.ajax({
                    url: '/admin/user/del'
                    ,type: 'POST'
                    ,data: JSON.stringify(e.data)
                    ,dataType: 'json'
                    ,contentType: 'application/json;charset=utf-8'
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
        // });
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑用户",
                content: "userform.html?roleId=" + e.data.role.id,
                maxmin: !0,
                area: ["500px", "450px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-user-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/admin/user/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'text'
                            ,success: function (res) {
                                layer.msg(res);
                            }
                        });
                        layui.table.reload('LAY-user-manage'); //数据刷新
                        layer.close(index); //关闭弹层
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // console.log(e.data);
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='id']").val(e.data.id);
                    $("input[name='username']").val(e.data.username);
                    $("input[name='mobile']").val(e.data.mobile);
                    $("input[name='pwd']").attr("placeholder","不修改密码时不必输入");
                }
            })
        }
    }), i.render({
        elem: "#LAY-user-back-role",
        url: "/admin/roles",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 80, title: "ID", sort: !0}, {
            field: "name",
            title: "角色名"
        }, {field: "desc", title: "具体描述"}, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-admin"
        }]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        parseData: function(res) { //res 即为原始返回的数据
            if(res.total == 0) {
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
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此角色？", function (t) {
            layui.$.ajax({
                url: '/admin/role/del'
                ,type: 'POST'
                ,data: {"id":e.data.id}
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status == 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "roleform.html",
                area: ["500px", "480px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-user-role-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/admin/role/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                }
                            }
                        });
                        layui.table.reload('LAY-user-back-role'); //数据刷新
                        layer.close(index); //关闭弹层
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='id']").val(e.data.id);
                    $("input[name='name']").val(e.data.name);
                    $("textarea[name='desc']").val(e.data.desc);
                }
            })
        }
    }), e("useradmin", {})
});