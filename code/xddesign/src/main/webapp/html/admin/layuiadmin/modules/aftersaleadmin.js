/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    i.render({
        elem: "#LAY-question-manage"
        , url: "/xddesign/admin/questions"
        , cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 80, title: "ID", sort: !0}
        , {field: "keyword", title: "产品"}
        , {field: "question", title: "问题", minWidth: 100}, {field: "answer", title: "回答"}
        , {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]]
        , request:{
            pageName: 'current'
            ,limitName: 'size'
        }
        , page: !0
        , limit: 10
        , skin:'row'
        , even:true
        , parseData: function(res){ //res 即为原始返回的数据
            if(res.total == 0) {
                return {
                    'code': 201, //接口状态
                    'msg': '无数据', //提示文本
                    'count': 0, //数据长度
                    'data': [] //数据列表，是直接填充进表格中的数组
                }
            }else if (res.records.length == 0) {
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
    }), i.on("tool(LAY-question-manage)", function (e) {
        if ("del" === e.event) {
            layer.confirm("真的删除么", {icon: 3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/admin/question/delete'
                    , type: 'POST'
                    , data: {"id": e.data.id}
                    , dataType: 'json'
                    , success: function (res) {
                        if (res.status === 0) {
                            layer.msg(res.msg, {icon: 5});
                            return false;
                        }
                        e.del();
                    }
                    ,error: function () {
                        layer.msg("出错啦", {icon:2});
                    }
                });
                layer.close(t);
            });
        }else if ("edit" === e.event) {
            layer.open({
                type: 2,
                title: "编辑常见问题",
                content: "questionform.html",
                maxmin: !0,
                area: ["500px", "450px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-question-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/question/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-question-manage'); //数据刷新
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
                    $("#level").prop("hidden", true);
                    $("input[name='id']").val(e.data.id);
                    $("input[name='question']").val(e.data.question);
                    $("textarea[name='answer']").val(e.data.answer);
                }
            })
        }
    }), i.render({
        elem: "#LAY-guide-manage",
        url: "/xddesign/admin/guides",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "guideId", width: 80, title: "ID", sort: !0}, {
            field: "guideName",
            title: "手册名称", templet: '<div><a href="{{d.guideLink}}" target="_blank">{{ d.guideName }}</a></div>'
        }, {field: "guideDesc", title: "手册描述"},{field: "guideAddTime", title: "添加时间"}, {
            title: "操作",
            width: 280,
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
            }else if (res.records.length == 0) {
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
    }), i.on("tool(LAY-guide-manage)", function (e) {
        if ("del" === e.event) layer.confirm("确定删除？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/firstlevel/delete'
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
                            url: '/xddesign/admin/firstlevel/saveorupdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-guide-manage'); //数据刷新
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
        } else if ("pic" === e.event) {
            layer.photos({
                photos: {
                    "title": "封面图片", //相册标题
                    "id": 1, //相册id
                    "start": 0, //初始显示的图片序号，默认0
                    "data": [   //相册包含的图片，数组格式
                        {
                            "alt": e.data.guideName,
                            "pid": e.data.guideId, //图片id
                            "src": e.data.pictureLink, //原图地址
                            "thumb": "" //缩略图地址
                        }
                    ]
                }
            });
        }
    }), i.render({
        elem: "#LAY-secondlevel-manage",
        url: "/xddesign/admin/secondlevels",
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
            }else if (res.records.length == 0) {
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
        if ("del" === e.event) layer.confirm("确定删除？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/secondlevel/delete'
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
                            url: '/xddesign/admin/secondlevel/saveorupdate'
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
    }), e("aftersaleadmin", {})
});