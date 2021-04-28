/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        elem: "#LAY-customer-manage",
        url: "/xddesign/design/customers",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 100, title: "ID", sort: !0},
            {field: "username", title: "客户名称", minWidth: 100, templet: '<div><a href="house.html?id={{d.id}}">{{d.username}}</a></div>'}, {field: "mobile", title: "电话"},
            {field: "desc", title: "客户类别"}, {field: "demand", title: "客户需求", sort: !0},{field: "address", title: "联系地址"},
            {field: "design", title: "所属设计人员",templet:'<div>{{!d.design.username?"":d.design.username}}</div>'},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-customer-admin"}]],
        request:{
          pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        where: {
            "design.id":0,
            "code":0
        },
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-customer-manage)", function (e) {
        e.data;
        if ("del" === e.event)
            // layer.prompt({formType: 1, title: "敏感操作，请验证口令"}
            // , function (t, i) {
            // layer.close(i),
            layer.confirm("真的删除行么", {icon:3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/design/customer/del'
                    ,type: 'POST'
                    ,data: {"id":e.data.id}
                    ,dataType: 'json'
                    ,success: function (res) {
                        if (res.status === 0) {
                            layer.msg(res.msg, {icon:5});
                            return false;
                        }
                        e.del();
                        layui.table.reload('LAY-customer-manage');
                    }
                });
                layer.close(t);
            });
        // });
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑客户",
                content: "customerform.html?code="+e.data.code+"&designId="+e.data.design.id,
                maxmin: !0,
                area: ["520px", "650px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-customer-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/design/customer/saveOrUpdate'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-customer-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
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
                    $("input[name='address']").val(e.data.address);
                    $("textarea[name='demand']").val(e.data.demand);
                    $("input[name='pwd']").attr("placeholder","不修改密码时不必输入");
                }
            })
        }
    }), i.render({
            elem: "#LAY-house-manage",
            url: "/xddesign/design/customer/house",
            cols: [[{type: "checkbox", fixed: "left"}, {field: "houseId", width: 100, title: "ID", sort: !0},
                {field: "houseName", title: "房子名称", minWidth: 100, templet: '<div><a href="solutions.html?houseId={{d.houseId}}">{{d.houseName}}</a></div>'}
                ,{field: "houseAddress", title: "地址"}, {field: "customer", title: "所属客户",templet: '<div>{{ d.customer.username }}</div>'},
                {field: "houseType", title: "房子户型",templet:'<div>{{ d.houseType.typeName }}</div>'},
                {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-house-admin"}]],
            request:{
                pageName: 'current'
                ,limitName: 'size'
            },
            page: !0,
            limit: 10,
            skin:'row',
            even:true,
            where: {
                "customerId": document.getElementsByName("customerId").value,
                "houseType.typeId":0
            },
            parseData: function(res){ //res 即为原始返回的数据
                if(res.total === 0) {
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
        }), i.on("tool(LAY-house-manage)", function (e) {
        e.data;
        if ("del" === e.event)
            // layer.prompt({formType: 1, title: "敏感操作，请验证口令"}
            // , function (t, i) {
            // layer.close(i),
            layer.confirm("真的删除行么", {icon:3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/design/customer/house/del'
                    ,type: 'POST'
                    ,data: {"houseId":e.data.houseId}
                    ,dataType: 'json'
                    ,success: function (res) {
                        if (res.status == 0) {
                            layer.msg(res.msg, {icon:5});
                            return false;
                        }
                        e.del();
                        layui.table.reload('LAY-house-manage');
                    }
                });
                layer.close(t);
            });
        // });
        else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑房子",
                content: "houseform.html?typeId="+e.data.houseType.typeId,
                maxmin: !0,
                area: ["550px", "700px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-house-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        let houseType = {};
                        houseType['typeName'] = field.typeName;
                        houseType['typeId'] = field.typeId;
                        field['houseType'] = houseType;
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/design/customer/house/saveorupdate'
                            ,type: 'POST'
                            ,data: JSON.stringify(field)
                            ,dataType: 'json'
                            ,contentType:'application/json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-house-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    // console.log(e.data);
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='houseId']").val(e.data.houseId);
                    $("input[name='houseName']").val(e.data.houseName);
                    $("input[name='houseAddress']").val(e.data.houseAddress);
                    $("input[name='typeName']").val(e.data.houseType.typeName);
                }
            })
        }
    }), i.render({
        elem: "#LAY-solutions-manage",
        url: "/xddesign/design/customer/solutions",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "soluId", width: 100, title: "ID", sort: !0},
            {field: "soluName", title: "方案名称", minWidth: 100, templet: '<div><a href="room.html?soluId={{d.soluId}}">{{d.soluName}}</a></div>'}
            ,{field: "soluDesc", title: "方案描述"},{field: "state", title: "方案状态"}, {field: "addTime", title: "方案上次修改时间"},
            {field: "shareSign", title: "是否共享为了模板",templet: '<div>{{# if(d.shareSign==true) { }}{{ "是" }} {{# }else{ }}{{ "否" }} {{# } }}</div>'},
            {field: "design", title: "所属设计人员",templet: '<div>{{ d.design.username }}</div>'},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-solutions-admin"}]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        where: {
            "houseId": document.getElementsByName("houseId").value,
            "design.id":0
        },
        parseData: function(res){ //res 即为原始返回的数据
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
    }), i.on("tool(LAY-solutions-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此方案？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/design/customer/solutions/delete'
                ,type: 'POST'
                ,data: {"soluId":e.data.soluId}
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status == 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                    layui.table.reload('LAY-solutions-manage');
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑方案",
                content: "solutionsform.html",
                area: ["500px", "480px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-solutions-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/customer/solutions/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-solutions-manage'); //数据刷新
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
                    $("input[name='soluId']").val(e.data.soluId);
                    $("input[name='soluName']").val(e.data.soluName);
                    $("textarea[name='soluDesc']").val(e.data.soluDesc);
                }
            })
        }
    }), i.render({
            elem: "#LAY-room-manage",
            url: "/xddesign/admin/customer/room",
            cols: [[{type: "checkbox", fixed: "left"}
            ,{field: "roomId", width: 80, title: "ID", sort: !0}
            ,{field: "roomName", title: "房间名称", templet: '<div><a href="productnum.html?roomId={{d.roomId}}">{{d.roomName}}</a></div>'}
            ,{field: "roomDesc", title: "房间描述"},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-room-admin"}]],
            request:{
                pageName: 'current'
                ,limitName: 'size'
            },
            page: !0,
            limit: 10,
            skin:'row',
            even:true,
            where: {
                "soluId": document.getElementsByName("soluId").value,
            },
            parseData: function(res){ //res 即为原始返回的数据
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
        }), i.on("tool(LAY-room-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此房间？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/customer/room/delete'
                ,type: 'POST'
                ,data: {
                    "soluId":e.data.soluId,
                    "roomId": e.data.roomId
                }
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status == 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                    layui.table.reload('LAY-room-manage');
                }
                ,error: function () {
                    layer.msg("服务器错误，请重试", {icon:5});
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑房间",
                content: "roomform.html",
                area: ["500px", "480px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-room-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/customer/room/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-room-manage'); //数据刷新
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
                    $("input[name='roomId']").val(e.data.roomId);
                    $("input[name='roomName']").val(e.data.roomName);
                    $("textarea[name='roomDesc']").val(e.data.roomDesc);
                }
            })
        }
    }) , i.render({
        elem: "#LAY-pn-manage",
        url: "/xddesign/admin/customer/pn",
        cols: [[{type: "checkbox", fixed: "left"}
            ,{field: "pnId", width: 80, title: "ID", sort: !0}
            ,{field: "product", title: "产品名称", templet: '<div>{{d.product.productName}}</div>'}
            ,{field: "pnNum", title: "产品数量"},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-pn-admin"}]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        page: !0,
        limit: 10,
        skin:'row',
        even:true,
        where: {
            "roomId": document.getElementsByName("roomId").value,
        },
        parseData: function(res){ //res 即为原始返回的数据
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
    }), i.on("tool(LAY-pn-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此行？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/customer/pn/delete'
                ,type: 'POST'
                ,data: {
                    "pnId": e.data.pnId
                }
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status == 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                    layui.table.reload('LAY-pn-manage');
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑产品数量",
                content: "productnumform.html",
                area: ["500px", "480px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-pn-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/customer/pn/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-pn-manage'); //数据刷新
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
                    $("input[name='pnId']").val(e.data.pnId);
                    $("input[name='product.productName']").val(e.data.product.productName);
                    $("input[name='pnNum']").val(e.data.pnNum);
                }
            })
        }
    }) , i.render({
        elem: "#LAY-renderings-manage",
        url: "/xddesign/admin/solutions/renderings",
        cols: [[{field: "rendId", width: 50, title: "ID", sort: !0}, {field: "rendName", title: "名称"}, {field: "rendPath", title: "图片", templet: "#imgTpl"}
        , {field: "rendDesc", title: "描述"}, {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-renderings-admin"}]],
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-renderings-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此房间？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/design/customer/renderings/delete'
                ,type: 'POST'
                ,data: {
                    "rendId": e.data.rendId
                }
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status == 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                    layui.table.reload('LAY-renderings-manage');
                }
                ,error: function () {
                    layer.msg("服务器错误，请重试", {icon:5});
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            layer.photos({
                photos: {
                    "title": "方案效果图", //相册标题
                    "id": 1, //相册id
                    "start": 0, //初始显示的图片序号，默认0
                    "data": [   //相册包含的图片，数组格式
                        {
                            "alt": e.data.rendName,
                            "pid": e.data.rendId, //图片id
                            "src": e.data.rendPath, //原图地址
                            "thumb": "" //缩略图地址
                        }
                    ]
                }
            });
        }
    }) ,i.render({
        elem: "#LAY-quote-manage",
        url: "/xddesign/admin/solutions/quotes",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "quoteId", width: 80, title: "ID", sort: !0}, {field: "roomName", title: "房间名"}
        , {field: "productName", title: "产品名称"}, {field: "productNum", title: "产品数量"},{field: "price", title: "产品单价/元"}
        , {title: "操作", width: 180, align: "center", fixed: "right", toolbar: "#table-quote-admin"}]],
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
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-quote-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此条报价？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/design/customer/quote/delete'
                ,type: 'POST'
                ,data: {
                    "quoteId": e.data.quoteId
                }
                ,dataType: 'json'
                ,success: function (res) {
                    if (res.status === 0) {
                        layer.msg(res.msg, {icon:5});
                        return false;
                    }
                    e.del();
                    layui.table.reload('LAY-quote-manage');
                }
                ,error: function () {
                    layer.msg("服务器错误，请重试", {icon:5});
                }
            });
            layer.close(t);
        }); else if ("edit" === e.event) {
            layer.open({
                type: 2,
                title: "编辑价格",
                content: "quoteform.html",
                area: ["450px", "450px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-quote-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/design/customer/quote/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-quote-manage'); //数据刷新
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
                    $("input[name='quoteId']").val(e.data.quoteId);
                    $("input[name='price']").val(e.data.price);
                }
            })
        }
    }), i.render({
        elem: "#LAY-housetype-manage",
        url: "/xddesign/admin/customer/houseType",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "typeId", width: 80, title: "ID", sort: !0},
            {field: "typeName", title: "户型名称"},{field: "typeDesc", title: "户型描述"},
            {field: "typeNum", title: "户型数量"},
            {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-housetype-admin"}]],
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
    }), i.on("tool(LAY-housetype-manage)", function (e) {
        if ("del" === e.event){
            layer.confirm("真的删除行么", {icon:3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/admin/housetype/del'
                    ,type: 'POST'
                    ,data: {"typeId":e.data.typeId}
                    ,dataType: 'json'
                    ,success: function (res) {
                        if (res.status === 0) {
                            layer.msg(res.msg, {icon:5});
                            return false;
                        }
                        e.del();
                        layui.table.reload('LAY-housetype-manage');
                    }
                });
                layer.close(t);
            });
        }else if ("edit" === e.event) {
            layer.open({
                type: 2,
                title: "编辑户型",
                content: "housetypeform.html",
                maxmin: !0,
                area: ["550px", "700px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-housetype-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = {};
                        let name_shi = iframeWindow.layui.$('#name_shi').val() + '室';
                        let name_ting = iframeWindow.layui.$('#name_ting').val() + '厅';
                        let name_chu = iframeWindow.layui.$('#name_chu').val() + '厨';
                        let name_wei = iframeWindow.layui.$('#name_wei').val() + '卫';
                        let name_tai = iframeWindow.layui.$('#name_tai').val() + '阳台';
                        field.typeName = name_shi + name_ting + name_chu + name_wei + name_tai;
                        field.typeDesc = iframeWindow.layui.$('#typeDesc').val();
                        field.typeId = iframeWindow.layui.$("input[name='typeId']").val();
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/housetype/save'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-housetype-manage'); //数据刷新
                                layer.close(index); //关闭弹层
                            }
                        });
                    });

                    submit.trigger('click');
                }
                , success: function (layero,index) {
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("input[name='typeId']").val(e.data.typeId);
                    let type_name = e.data.typeName;
                    let name_shi = type_name.split('室')[0];
                    let name_ting = type_name.split('厅')[0];
                    name_ting = name_ting.charAt(name_ting.length -1);
                    let name_chu = type_name.split('厨')[0];
                    name_chu = name_chu.charAt(name_chu.length -1);
                    let name_wei = type_name.split('卫')[0];
                    name_wei = name_wei.charAt(name_wei.length -1);
                    let name_tai = type_name.split('阳')[0];
                    name_tai = name_tai.charAt(name_tai.length -1);
                    $('#name_shi').val(name_shi);
                    $('#name_ting').val(name_ting);
                    $('#name_chu').val(name_chu);
                    $('#name_wei').val(name_wei);
                    $('#name_tai').val(name_tai);
                    $("#typeDesc").val(e.data.typeDesc);
                }
            })
        }
    }), e("customeradmin", {})
});