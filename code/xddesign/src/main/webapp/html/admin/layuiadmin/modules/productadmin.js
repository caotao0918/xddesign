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
        elem: "#LAY-firstlevel-manage",
        url: "/admin/firstlevel",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "firstId", width: 80, title: "ID", sort: !0}, {
            field: "firstName",
            title: "分类名", templet: '<div><a href="secondlevel.html?firstId={{d.firstId}}">{{ d.firstName }}</a></div>'
        }, {field: "firstDesc", title: "描述"}, {
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-firstlevel-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            layui.$.ajax({
                url: '/admin/firstlevel/delete'
                ,type: 'POST'
                ,data: {"firstId":e.data.firstId}
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
                content: "firstlevelform.html",
                area: ["550px", "480px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-firstlevel-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/admin/firstlevel/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-firstlevel-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                        // layui.table.reload('LAY-secondlevel-manage'); //数据刷新
                        // layer.close(index); //关闭弹层
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='firstId']").val(e.data.firstId);
                    $("input[name='firstName']").val(e.data.firstName);
                    $("textarea[name='firstDesc']").val(e.data.firstDesc);
                }
            })
        }
    }), i.render({
        elem: "#LAY-secondlevel-manage",
        url: "/admin/secondlevels",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "secondId", width: 80, title: "ID", sort: !0}, {
            field: "secondName",
            title: "分类名", templet: '<div><a href="property.html?secondId={{d.secondId}}">{{d.secondName}}</a></div>'
        }, {field: "secondDesc", title: "描述"},{field: "firstLevel", title: "一级分类", templet: '<div>{{d.firstLevel.firstName}}</div>'}, {
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
        where: {
          "firstLevel.firstId":document.getElementsByName("firstlevel").value
        },
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-secondlevel-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            layui.$.ajax({
                url: '/admin/secondlevel/delete'
                ,type: 'POST'
                ,data: {"secondId":e.data.secondId}
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
                title: "编辑产品二级分类",
                content: "secondlevelform.html?firstId="+e.data.firstLevel.firstId,
                area: ["500px", "630px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-secondlevel-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/admin/secondlevel/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-secondlevel-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                        // layui.table.reload('LAY-secondlevel-manage'); //数据刷新
                        // layer.close(index); //关闭弹层
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='secondId']").val(e.data.secondId);
                    $("input[name='secondName']").val(e.data.secondName);
                    $("textarea[name='secondDesc']").val(e.data.secondDesc);
                    $("select[name='firstLevel.firstId']").val(e.data.firstLevel.firstId);
                }
            })
        }
    }), i.render({
        elem: "#LAY-property-manage",
        url: "/admin/secondlevel/property",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "propertyId", width: 80, title: "ID", sort: !0}, {field: "propertyName", title: "属性名"}
        ,{field: "commonValue", title: "常用值"}, {field: "propertyDesc", title: "描述"},{field: "secondLevel", title: "二级分类", templet: '<div>{{d.secondLevel.secondName}}</div>'}
        ,{title: "操作", width: 150, align: "center", fixed: "right",toolbar: "#table-useradmin-admin"}]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        where: {
            "firstId": 0,
            "secondLevel.secondId":document.getElementsByName("secondLevel.secondId").value
        },
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-property-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除？", function (t) {
            layui.$.ajax({
                url: '/admin/property/delete'
                ,type: 'POST'
                ,data: {"propertyId":e.data.propertyId}
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
                title: "编辑产品二级分类",
                content: "propertyform.html",
                area: ["550px", "500px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-property-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/admin/property/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-property-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                        // layui.table.reload('LAY-property-manage'); //数据刷新
                        // layer.close(index); //关闭弹层
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("#levelselect").attr("hidden",true);
                    $("input[name='propertyId']").val(e.data.propertyId);
                    $("input[name='propertyName']").val(e.data.propertyName);
                    $("textarea[name='propertyDesc']").val(e.data.propertyDesc);
                    $("input[name='commonValue']").val(e.data.commonValue);
                }
            })
        }
    }), e("productadmin", {})
});