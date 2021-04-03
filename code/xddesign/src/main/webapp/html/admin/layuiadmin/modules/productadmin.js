/** layuiAdmin.std-v1.4.0 LPPL License By https://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        elem: "#LAY-product-manage",
        url: "/xddesign/public/products",
        cols: [[{field: "productId", width: 80, title: "ID", sort: !0},
            {field: "productName", title: "产品名", minWidth: 100}, {field: "productModels", title: "产品型号"}
            , {field: "productLink", title: "产品官网链接",templet:'<div><a href="{{d.productLink}}">{{d.productLink}}</a></div>'},
            {field: "price", title: "产品价格", sort: !0}, {field: "productDesc", title: "产品描述"},
            {title: "操作", width: 440, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
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
    }), i.on("tool(LAY-product-manage)", function (e) {
        if ("del" === e.event) {
            layer.confirm("真的删除么", {icon: 3, title: '提示'}, function (t) {
                layui.$.ajax({
                    url: '/xddesign/admin/product/del'
                    , type: 'POST'
                    , data: {"productId": e.data.productId}
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
                title: "修改产品信息",
                content: "addproduct.html?firstId=" + e.data.secondLevel.firstLevel.firstId + "&secondId=" + e.data.secondLevel.secondId + "&secondName=" + e.data.secondLevel.secondName,
                maxmin: !0,
                area: ["1000px", "700px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-product-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/product/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-product-manage'); //数据刷新
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
                    $("#productproperty").prop("hidden", true);
                    $("#addpic").attr("hidden", true);
                    $("input[name='productId']").val(e.data.productId);
                    $("input[name='productName']").val(e.data.productName);
                    $("input[name='productModels']").val(e.data.productModels);
                    $("input[name='price']").val(e.data.price);
                    $("input[name='productLink']").val(e.data.productLink);
                    $("textarea[name='productDesc']").val(e.data.productDesc);
                }
            })
        } else if ("detail" === e.event) {
            let index = layer.open({
                type: 2,
                title: "编辑产品详情",
                content: "productdetail.html",
                maxmin: !0,
                area: ["1000px", "800px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-productdetail-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let iframe = window['layui-layer-iframe' + index];
                        let $ = iframe.layui.$;
                        let productId = $("#productId").val();
                        let value = $("#layeditDemo").val();
                        layui.$.ajax({
                            url: '/xddesign/admin/product/detail/saveorupdate'
                            ,type: 'POST'
                            ,data: {
                                "productId": productId
                                ,"productDetail": value
                            }
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg, {icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-product-manage'); //数据刷新
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
                    $("#productId").val(e.data.productId);
                    $("#layeditDemo").val(e.data.productDetail);
                }
            });
            layer.full(index);
        }else if ("pic" === e.event) {
            let index = layer.open({
                type: 2,
                title: "产品图片管理",
                content: "picture.html?productId=" + e.data.productId + '&productName=' + e.data.productName,
                maxmin: !0,
                area: ["1000px", "800px"]
                // , btn: ["确定", "取消"]
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("#searchpic").attr("hidden", true);
                }
            });
            layer.full(index);
        }else if ("prop" === e.event) {
            let index = layer.open({
                type: 2,
                title: "产品属性管理",
                content: "productproperty.html?productId=" + e.data.productId + '&secondId=' + e.data.secondLevel.secondId,
                maxmin: !0
                // area: ["1000px", "800px"]
                , success: function (layero,index) {
                    // 获取子页面的iframe
                    let iframe = window['layui-layer-iframe' + index];
                    let $ = iframe.layui.$;
                    $("#searchprop").attr("hidden", true);
                }
            });
            layer.full(index);
        }
    }), i.render({
        elem: "#LAY-firstlevel-manage",
        url: "/xddesign/admin/firstlevel",
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
    }), i.on("tool(LAY-firstlevel-manage)", function (e) {
        e.data;
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
    }), i.render({
        elem: "#LAY-property-manage",
        url: "/xddesign/admin/secondlevel/property",
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
    }), i.on("tool(LAY-property-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/property/delete'
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
                title: "编辑二级分类属性信息",
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
                            url: '/xddesign/admin/property/saveorupdate'
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
    }), i.render({
        elem: "#LAY-productproperty-manage",
        url: "/xddesign/admin/product/property",
        cols: [[{field: "valueId", width: 80, title: "ID", sort: !0}, {field: "productName", title: "产品名称"}
        , {field: "propertyName", title: "属性名"}, {field: "valueName", title: "属性值"}
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
            "productId": productId
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
                console.log("进入这一层了");
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
    }), i.on("tool(LAY-productproperty-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/product/property/delete'
                ,type: 'POST'
                ,data: {"valueId":e.data.valueId}
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
                title: "编辑产品属性值",
                content: "productpropertyform.html",
                area: ["480px", "450px"],
                btn: ["确定", "取消"]
                ,yes: function(index, layero){
                    let iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-productproperty-front-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    //监听提交
                    iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
                        let field = data.field; //获取提交的字段
                        //提交 Ajax 成功后，静态更新表格中的数据
                        layui.$.ajax({
                            url: '/xddesign/admin/product/property/update'
                            ,type: 'POST'
                            ,data: field
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status == 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                layui.table.reload('LAY-productproperty-manage'); //数据刷新
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
                    $("input[name='valueId']").val(e.data.valueId);
                    $("input[name='valueName']").val(e.data.valueName);
                    $("input[name='productName']").val(e.data.productName);
                    $("input[name='propertyName']").val(e.data.propertyName);
                }
            })
        }
    }), i.render({
        elem: "#LAY-picture-manage",
        url: "/xddesign/admin/product/picture",
        cols: [[{field: "pictureId", width: 80, title: "ID", sort: !0}, {field: "pictureName", title: "图片名称", width: 300}
            , {field: "pictureLink", title: "图片", templet: "#imgTpl", width: 330}, {field: "pictureAddTime", title: "图片上传时间", width: 200}
            ,{field: "defaultPicture", title: "默认图片", width: 180, templet: "#switchTpl", unresize: true}
            ,{title: "操作", width: 180, align: "center", fixed: "right",toolbar: "#table-useradmin-admin"}]],
        request:{
            pageName: 'current'
            ,limitName: 'size'
        },
        where: {"productId": productId},
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
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-picture-manage)", function (e) {
        if ("del" === e.event) layer.confirm("确定删除？", {icon:3, title: '提示'}, function (t) {
            layui.$.ajax({
                url: '/xddesign/admin/product/picture/delete'
                ,type: 'POST'
                ,data: {"id":e.data.pictureId}
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
            layer.photos({
                photos: {
                    "title": "产品图片", //相册标题
                    "id": 1, //相册id
                    "start": 0, //初始显示的图片序号，默认0
                    "data": [   //相册包含的图片，数组格式
                        {
                            "alt": e.data.pictureName,
                            "pid": e.data.pictureId, //图片id
                            "src": e.data.pictureLink, //原图地址
                            "thumb": "" //缩略图地址
                        }
                    ]
                }
            });
        }
    }), e("productadmin", {})
});