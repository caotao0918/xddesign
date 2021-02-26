/**
 * Created by Wzz20210118 on 2021/2/7.
 */
/**
 * ----------------------------------------------------------------------售后支持中心
 */
var afterSaleCntWcm_text = "欢迎来到迅达智能家居售后支持中心";
var qst_text = "常见问题";
var gui_text = "产品手册";
var video_text  ="安装视频";
var leftNavList;
//左侧和上部按钮联动
var fstLevNameChk = "";
var scdLevNameChk = "";

var srhRegArr_qst = [];
srhRegArr_qst.push(new RegExp("为什么"));
srhRegArr_qst.push(new RegExp("不"));
srhRegArr_qst.push(new RegExp("无法"));
srhRegArr_qst.push(new RegExp("怎么"));
srhRegArr_qst.push(new RegExp("问题"));
var srhRegArr_gui = [];
srhRegArr_gui.push(new RegExp("说明"));
srhRegArr_gui.push(new RegExp("手册"));
srhRegArr_gui.push(new RegExp("属性"));
var srhRegArr_video = [];
srhRegArr_video.push(new RegExp("视频"));

$(function() {
    $('#div_aftersalecntbgi>div div:first-child').html(afterSaleCntWcm_text);
    $('#div_opt>div div:first-child').html(qst_text);
    $('#div_opt>div div:nth-child(2)').html(gui_text);
    $('#div_opt>div div:last-child').html(video_text);

    $('#div_aftersalecntbgi img:first-of-type').attr("src" , "/img/schccl.svg");
    $('#div_aftersalecntbgi img:last-of-type').attr("src" , "/img/schline.svg");
    $('#div_aftersalecntbgi img').mouseleave(function(){
        $('#div_aftersalecntbgi img:first-of-type').attr("src" , "/img/schccl.svg");
        $('#div_aftersalecntbgi img:last-of-type').attr("src" , "/img/schline.svg");
    })
    $('#div_aftersalecntbgi img').mouseover(function(){
        $('#div_aftersalecntbgi img:first-of-type').attr("src" , "/img/schcclhover.svg");
        $('#div_aftersalecntbgi img:last-of-type').attr("src" , "/img/schlinehover.svg");
    })

    $('#div_opt>div div').click(function(){
        $(this).addClass('div_optchk');
        $(this).siblings().removeClass('div_optchk');
    })
    $('#div_opt>div div').get(0).click();



    getLeftNav();
    function getLeftNav(){
        $.ajax({
            //----------------------------------------------------------url查询产品分类例如WIFI、照明
            url:"/public/secondlevels/products",
            dataType:"json",
            type:"GET",
            async:false,
            success:function(data) {
                leftNavList =  data;
            }
        })
    }

    bindLeftNav();
    function bindLeftNav(){
        var add = "<ul class='layui-nav layui-nav-tree layui-bg-cyan layui-inline' lay-filter='ul_leftnav' lay-shrink='all'>";
        // for(var i = 0;i<leftNavList.length;i++){
        //     var name_1 = leftNavList[i][0].firstLevel.firstName;
        //     //layui-nav-itemed
        //     add += '<li class="layui-nav-item "> <a href="javascript:;">' +name_1 +'</a>'+
        //         '<dl class="layui-nav-child">';
        //     for(var j = 0;j<leftNavList[i].length;j++){
        //         var name_2 = leftNavList[i][j].secondName;
        //         add += '<dd><a href="javascript:;">'+name_2+'</a></dd>';
        //     }
        //     add += "</dl> </li>";
        // }

        for (var prop in leftNavList) {
            var name_1 = prop;
            add += '<li class="layui-nav-item "> <a href="javascript:;">' +name_1 +'</a>'+
                '<dl class="layui-nav-child">';
            for (var j = 0; j < leftNavList[prop].length; j++) {
                var name_2 = leftNavList[prop][j].productName;
                add += '<dd><a href="javascript:;">'+name_2+'</a></dd>';
            }
            add += "</dl> </li>";
        }

        $('#div_leftnav').append(add);

        //监听导航
        layui.use('element', function(){
            var element = layui.element;
            //监听导航点击
            element.on('nav(ul_leftnav)', function(elem){
                var navTier = elem.siblings().length;
                var fstLevName = $('#div_leftnav li.layui-nav-itemed>a:first-of-type').text();
                if(navTier == 0) {
                    var topOptChk_Text = $('#div_opt>div div.div_optchk').html();
                    var scdLevName = elem.text();
                    //查询问题
                    if (qst_text == topOptChk_Text) {
                        var list = queryQst(fstLevName,scdLevName);
                        clearForQst();
                        bindQst(list);
                        //查询产品手册
                    }else if(gui_text == topOptChk_Text){
                        var list = queryGui(fstLevName,scdLevName);
                        clearForGui();
                        bindGui(list);
                        //查询视频
                    }else if(video_text == topOptChk_Text){
                        var list = queryVideo(fstLevName,scdLevName);
                        console.log(666);
                        clearForVideo();
                        bindVideo(list);
                    }
                    scdLevNameChk = scdLevName;
                }
                fstLevNameChk = fstLevName;
            });
            $('#div_leftnav>ul li:first-of-type>a:first-of-type').click();
            $('#div_leftnav>ul li:first-of-type dd:first-of-type a').click();
        });
    }

    $('#div_opt>div div').each(function(){
        $(this).click(function () {
            var topOptChk_Text = $(this).html();
            //查询问题
            if (qst_text == topOptChk_Text) {
                var list = queryQst(fstLevNameChk, scdLevNameChk);
                clearForQst();
                bindQst(list);
                //查询产品手册
            } else if (gui_text == topOptChk_Text) {
                // var list = queryGui(fstLevNameChk, scdLevNameChk);
                //这里我改成了查询全部的产品手册
                var list = queryGui();
                clearForGui();
                bindGui(list);
                //查询视频
            } else if (video_text == topOptChk_Text) {
                // var list = queryVideo(fstLevNameChk, scdLevNameChk);
                //这里我改成了查询全部的产品视频
                var list = queryVideo();
                console.log(222);
                clearForVideo();
                bindVideo(list);
            }
        })
    })

    function clearForQst(){
        $('#div_leftnav ~ div').remove();
        $("body>div").append('<div id = "div_aftersalecnttab_qst" class = "div_aftersalecnttab_qst"></div>');
    }
    function queryQst(fstLevName,scdLevName){
        var qstList;
        $.ajax({
            //----------------------------------------------------------url根据分类查询问题
            url:"/public/questions",
            data:{
                // fstLevName:fstLevName,
                productName : scdLevName},
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                qstList =  json;
            }
        })
        return qstList;
    }

    function bindQst(qstList){
        var add = '<div class="layui-collapse" lay-accordion="" lay-filter="qqq">';
        for(var i = 0;i<qstList.length;i++){
            var ask = qstList[i].question;
            var asr = qstList[i].answer;
            add += '<div class="layui-colla-item">' +
                             '<h2 class="layui-colla-title">'+ask+'</h2>' +
                             '<div class="layui-colla-content">' +
                                 '<pre>' + asr + '</pre>' +
                             '</div> ' +
                        '</div>';
        }
        add += "</div>";
        $('#div_aftersalecnttab_qst').append(add);
        layui.element.init();
    }

    ////监听问题
    //layui.use(['element', 'layer'], function(){
    //    var element = layui.element;
    //    var layer = layui.layer;
    //    //监听折叠
    //    element.on('collapse(qqq)', function(data){
    //        layer.msg('展开状态：'+ data.show);
    //    });
    //});


    function clearForGui(){
        $('#div_leftnav ~ div').remove();
        $("body>div").append('<div id = "div_aftersalecnttab_gui" class = "div_aftersalecnttab_gui div_prodlist">');
    }
     function queryGui(fstLevName,scdLevName){
         var guiList;
         $.ajax({
             //----------------------------------------------------------url根据分类查询手册
             url:"/public/guide",
             data:{
                 // fstLevName:fstLevName,
                 productName : scdLevName},
             dataType:"json",
             type:"get",
             async:false,
             success:function(json) {
                 guiList =  json;
             }
         })
         return guiList;
     }

    function bindGui(guiList){
        var add = '';
        for(var i = 0;i<guiList.length;i++){
            add += "<div class='div_prod'>" +
                '<img src ="'+guiList[i].pictureLink+'"/>' +
                '<span>'+guiList[i].guideName+'</span>' +
                "</div>";
        }
        $('#div_aftersalecnttab_gui').append(add);

        $('#div_aftersalecnttab_gui>div').each(function(){
            $(this).click(function(){
                var k = $(this).index();
                var guiLink = guiList[k].guideLink;
                //-----------------------------------------------------跳转到产品手册页面
                window.open(guiLink);
            })
        })
    }


    function clearForVideo(){
        $('#div_leftnav ~ div').remove();
        $("body>div").append('<div id = "div_aftersalecnttab_video" class = "div_aftersalecnttab_video">');
    }
    function queryVideo(fstLevName,scdLevName){
        var videoList;
        $.ajax({
            //----------------------------------------------------------url根据分类查询视频
            url:"/public/video",
            data:{
                // fstLevName:fstLevName,
                productName : scdLevName},
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                videoList =  json;
            }
        })
        return videoList;
    }
    function bindVideo(videoList){
        var add = '';
        for(var i = 0;i<videoList.length;i++){
            add += '<div><div>' +
                '<video style = "object-fit : fill;" ' +
                '<source src = "'+videoList[i].videoLink+'" type = "video/mp4">'+
                ' </video>' +
                '<div></div></div><div>' +
                    '<span>' +videoList[i].videoName+'</span>'+
                     '<span>' +videoList[i].videoAddTime+'</span>'+
                '</div></div>';
        }
        $('#div_aftersalecnttab_video').append(add);

        $('#div_aftersalecnttab_video>div>div video').mouseover(function(){
            $(this).attr('controls','controls');
        })
        $('#div_aftersalecnttab_video>div>div video').mouseleave(function(){
            $(this).removeAttr('controls');
            if($(this).get(0).paused) {
                $(this).next().show();
            }
        })
        $('#div_aftersalecnttab_video>div>div video+div').mouseover(function(){
            $(this).hide();
        })
        $('#div_aftersalecnttab_video>div>div video').each(function() {
            $(this).get(0).addEventListener('pause', function () {
                $(this).next().hide();
            })
        })
    }

    $('#div_aftersalecntbgi img').click(function(){
        var srhInput = $(this).siblings('input').val().trim();
        var endText = srhInput.substring(0,5) + srhInput.substring(srhInput.length-5);
        for(var i  =0;i<srhRegArr_qst.length;i++){
            if(srhRegArr_qst[i].test(endText)){
                $('#div_opt>div div').get(0).click();
                testLevName(srhInput);
            }
        }
        for(var i  =0;i<srhRegArr_gui.length;i++){
            if(srhRegArr_gui[i].test(endText)){
                $('#div_opt>div div').get(1).click();
                testLevName(srhInput);
            }
        }
        for(var i  =0;i<srhRegArr_video.length;i++){
            if(srhRegArr_video[i].test(endText)){
                $('#div_opt>div div').get(2).click();
                testLevName(srhInput);
            }
        }
    })

    function testLevName(srhInput){
        $('#div_leftnav li>a:first-of-type').each(function(){
            if(new RegExp($(this).text()).test(srhInput)){
                if($(this).text() != fstLevNameChk) {
                    $(this).click();
                }
                $(this).find('+dl a').each(function () {
                    if(new RegExp($(this).text()).test(srhInput)){
                        $(this).click();
                        return;
                    }
                })
            }
        })
    }


})
