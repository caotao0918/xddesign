/**
 * Created by Wzz20210118 on 2021/2/24.
 */
/**
 *----------------------------------------------------------------------------方案详情
 */
var allProd_text = "所有产品";
var roomNameDft_text = "默认房间";
var dsgId = window.localStorage.getItem("userId");
var houseId = window.localStorage.getItem("houseId");
var houseType = window.localStorage.getItem("houseType");
var soluName_text = "_新建方案_";
var saveSolu_text = "保存";
//----------------------------------------------------------------------------产品详情
var url_prodProp = "../customer/showprod.html";
//----------------------------------------------------------------------------方案列表
var url_listHouseSolu = "listhousesolu.html";
var url_addSolu = "/xddesign/design/customer/solutions/saveorupdate";

var addRoomProdListNode = "";
var soluNameNull_text = "方案名称不能为空或空格！";
var noProd_text = "请新建房间并配置至少一个产品！";
var soluId = UrlParam.param("soluId");


$(function(){

    if (UrlParam.hasParam("houseId")) {
        houseId = UrlParam.param("houseId");
    }

    $('#div_1').show();
    $('#div_2').hide();

    $('#div_plus').mouseover(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_hover.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1hover.svg');
    }).mouseleave(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1.svg');
    })

    $('#div_soluname input').val(houseType + soluName_text +new Date().toLocaleDateString());
    $('#div_savesolu').text(saveSolu_text);

    if (soluId == null) {
        $('#div_solustate').hide();
    }else{
        //得到方案状态列表
        $.ajax({
            url:"/xddesign/design/solutions/state",
            dataType:"json",
            type:"GET",
            async:false,
            success:function(data) {
                for (let i = 0; i < data.length; i++) {
                    $(".div_solustate select").append('<option value="'+data[i].code+'">'+data[i].msg+'</option>');
                }
            }
        });
    }
    //为方案状态select赋初值
    function defaultstatevalue(state){
        $(".div_solustate select").find('option').each(function () {
            if ($(this).text() == state) {
                $(this).prop('selected', true);
            }
        });
    }
    //修改方案状态
    $('#solutionsstateupdate').click(function () {
        if (soluId == null) {
            lightbox_tip('还未创建方案');
        }else{
            $.ajax({
                url:"/xddesign/design/solutions/state/update",
                data: {
                    "soluId": soluId,
                    "code": $(".div_solustate select").val()
                },
                dataType:"json",
                type:"POST",
                success:function(res) {
                    self.location=document.referrer;
                },
                error: function () {
                    lightbox_tip('出错啦');
                }
            });
        }
    });

    function bindRoom_2() {
        var add = "<div>" + allProd_text + "</div>";
        for (var i = 0; i < roomList.length; i++) {
            add += "<div>" + roomList[i].roomName + "</div>";
        }
        add += '<div id = "div_plus" class = "div_plus"><img src = "../../img/plus_.svg"/><img src = "../../img/plus1.svg"/>';
        $('#div_roomlist').html('');
        $('#div_roomlist').append(add);
        $('#div_roomlist').children().addClass("div_room");
    }

    function bindProdList_2(){
        var add = "";
        for (var j = 0; j < roomList.length; j++) {
            productNumList = roomList[j].productNumList;
            add += "<div>";
            for (var l = 0; l < productNumList.length; l++) {
                productNum = productNumList[l];
                for (var num = 0; num < productNum.pnNum; num++) {
                    add += "<div>" +
                        "<img src =" +productNum.product.pictureList[0].pictureLink + "\>" +
                        "<span>" + productNum.product.productName + "</span>" +
                        "<span style = 'display: none;'>" + productNum.product.productId + "</span>" +
                        "<span style = 'display: none;'>" + productNum.product.price + "</span>" +
                        "</div>";
                }
            }
            add += "</div>";
        }

        $('#div_tab_1').append(add);
        var addFirst = "<div>";
        $('#div_tab_1>div').each(function(){
            addFirst += $(this).html();
        })
        addFirst += "</div>";
        $('#div_tab_1').prepend(addFirst);//所有产品

        $('#div_tab_1>div').addClass("div_prodlist_1");
        $('#div_tab_1>div div').addClass("div_prod_1");
        $(".div_prodlist_1:not(:first)").each(function () {
            var addProdList = "<div><img/><img/></div>";
            $(this).append(addProdList);
            $(this).find("div:last-child").addClass("div_addprod");
            addProdDivBindEvent();
            $('.div_prodlist_1:not(:first-of-type) .div_prod_1').unbind('dragend');
            $('.div_prodlist_1:not(:first-of-type) .div_prod_1').attr('draggable',true);
            $('.div_prodlist_1:not(:first-of-type) .div_prod_1').bind('dragend',function(e){
                var $this = $(this);
                lightbox_delCfm();
                $('.layui-layer-lan .layui-layer-btn0').click(function(){
                    var delId = $this.find('span:hidden').eq(0).text();
                    $this.remove();
                    for(var i = 0;i< $('.div_prodlist_1:first-of-type>div').length;i++){
                        var idCurr = $('.div_prodlist_1:first-of-type>div').eq(i).find('span').eq(1).text();
                        if(idCurr == delId){
                            $('.div_prodlist_1:first-of-type>div').eq(i).remove();
                            return;
                        }
                    }
                })
            })
        });
    }

    //初始化方案
    if (soluId != null) {
        $.ajax({
            url:"/xddesign/public/customer/solution",
            data:{'soluId':soluId},
            dataType:"json",
            type:"GET",
            async:false,
            success:function(data) {
                solu = data;
                soluName = data.soluName;
                roomList = data.roomList;
            }
        });
        $('#div_soluname input').val(soluName);
        //为方案状态select赋初值
        defaultstatevalue(solu.state);
        bindRoom_2();//初始化房间列表
        bindProdList_2();//初始化每个房间的产品列表
        $('#div_roomlist div').click(function(){
            $(this).addClass("div_room_chk");
            $(this).siblings().removeClass("div_room_chk");
            var indexCurr = $(this).index();
            $('#div_tab_1>div').eq(indexCurr).show();
            $('#div_tab_1>div').eq(indexCurr).siblings().hide();
        });
        $('#div_roomlist div').eq(0).click();
    }else{
        bindRoom();
        bindProdList();
    }

    function bindRoom() {
        var add = "<div>" + allProd_text + "</div>";

        $('#div_roomlist>div:last-of-type').before(add);
        $('#div_roomlist>div:not(:last-of-type)').addClass("div_room");
    }

    function bindProdList(){
        var add = "<div></div>";
        $('#div_tab_1').prepend(add);
        prodListBindEvent();
    }

    $('.div_prodlist_1 .div_prod_1').click(function(){
        var prodId = $(this).children('span:hidden').eq(0).html();
        location.href = url_prodProp + "?prodId=" + prodId;
    })


    //点击房间栏的加号
    $('#div_plus').click(function(){
        addRoomAndProdList();
    })

    function addRoomAndProdList(){
        var roomName = roomNameDft_text +"_"+$('#div_roomlist>div:last-of-type').index();
        var addRoom = "<div>" + roomName + "</div>";
        $('#div_roomlist>div:last-of-type').before(addRoom);
        $('#div_roomlist>div:not(:last-of-type)').addClass("div_room");

        var addProdList = "<div><div><img/><img/></div></div>";
        $('#div_tab_1').append(addProdList);
        $('#div_tab_1>div>div:last').addClass("div_addprod");

        prodListBindEvent();
        roomBindEvent();
        addProdDivBindEvent();
        $('#div_roomlist>div:last-of-type').prev().click();
    }


    roomBindEvent();
    function roomBindEvent() {
        $('#div_roomlist>div:not(:last-of-type)').click(function () {
            $(this).addClass("div_room_chk");
            $(this).siblings().removeClass("div_room_chk");
            var indexCurr = $(this).index();
            $('#div_tab_1>div').eq(indexCurr).css('z-index',1997).css('display', 'block');
            $('#div_tab_1>div').eq(indexCurr).siblings().css('z-index',100).css('display', 'none');
        })

        roomRename();
        roomDel();
    }
    $("#div_roomlist div").eq(0).click();

    function prodListBindEvent(){
        $('#div_tab_1>div').addClass("div_prodlist_1");
        $('#div_tab_1>div>div:not(:last-of-type)').addClass("div_prod_1");
    }

    //房间重命名
    function roomRename(){
        $('#div_roomlist>div:not(:first,:last)').unbind('dblclick');
        $('#div_roomlist>div:not(:first,:last)').dblclick(function () {
            var roomNameOld = $(this).text();
            $(this).text('');
            $(this).addClass('div_roomRename');
            $(this).append('<input type = "text" value = \"' + roomNameOld +'\" >');
            $(this).children('input').focus();
            $(this).children('input').blur(function(){
                $(this).parent().attr('draggable',true);
                $(this).parent().removeClass('div_roomRename');
                $(this).parent().text($(this).val());
            })
            $(this).attr('draggable',false);
        })
    }

    //删除房间之后更新所有产品
    function updateAllProduct() {
      $('.div_prodlist_1').eq(0).html('');
      $(".div_prodlist_1:not(:first)").each(function () {
          $(this).find(".div_prod_1").each(function () {
            $('.div_prodlist_1').eq(0).append('<div class="div_prod_1" draggable="true">'+$(this).html()+'</div>');
          });
      });
    }
    //删除房间
    function roomDel(){
        $('#div_roomlist>div:not(:first,:last)').attr('draggable',true);
        $('#div_roomlist>div:not(:first,:last)').each(function () {
            $(this).unbind('dragend');
            $(this).bind('dragend',function(e){
                var roomCurr = $(this);
                lightbox_delCfm();
                $('.layui-layer-btn0').click(function(){
                    $('#div_tab_1>div').eq(roomCurr.index()).children().each(function(){
                        var delId = $(this).find('span:hidden').eq(0).text();
                        $('#div_tab_1>div:first-of-type>div').each(function(){
                            var idCurr = $(this).find('span:hidden').eq(0).text();
                            if(delId == idCurr){
                                $(this).remove();
                                return true;
                            }
                        })
                    })
                    $('#div_tab_1>div').eq(roomCurr.index()).remove();
                    roomCurr.remove();
                    updateAllProduct();
                    $('#div_roomlist div').eq(0).click();
                })
            })
        })

    }

    function addProdDivBindEvent(){
        addProdDivCss();
        $('.div_addprod').off('click');
        $('.div_addprod').click(function(){
            $('#div_1').hide();
            $('#div_2').show();
            addRoomProdListNode = $(this).parent();
        })
    }

    function addProdDivCss(){
        $('.div_addprod img:first-of-type').attr('src','../../img/addhouseplus_.svg');
        $('.div_addprod img:last-of-type').attr('src','../../img/addhouseplus1.svg');
        $('.div_addprod').mouseenter(function () {
            $('.div_addprod img:first-of-type').attr('src','../../img/addhouseplus_hover.svg');
            $('.div_addprod img:last-of-type').attr('src','../../img/addhouseplus1hover.svg');
        })
        $('.div_addprod').mouseleave(function () {
            $('.div_addprod img:first-of-type').attr('src','../../img/addhouseplus_.svg');
            $('.div_addprod img:last-of-type').attr('src','../../img/addhouseplus1.svg');
        })
    }

    //点击保存，添加方案
    $('#div_savesolu').click(function(){
        if(!chkSoluInput()){
            return;
        }
        var solu = {};
        if (soluId != null) {
            solu['soluId'] = soluId;
        }
        solu['soluName'] = $('#div_soluname input').val().trim();
        solu['design'] = {'id':dsgId};
        solu['houseId'] = houseId;
        solu['roomList'] = [];
        $('#div_roomlist>div:not(:first,:last)').each(function(){
            var roomName = $(this).text().trim();
            var productNumList = [];
            // var arrProdId = [];
            let price;
            let productId;
            let productName;
            if(0 == $('#div_tab_1>div').eq($(this).index()).children('.div_prod_1').length){
                return true;
            }
            $('#div_tab_1>div').eq($(this).index()).children('.div_prod_1').each(function(){
                // arrProdId.push($(this).find('span:hidden').eq(0).text());
                productName = $(this).find('span').eq(0).text();//产品名称
                productId = $(this).find('span').eq(1).text();//产品id
                price = $(this).find('span').eq(2).text();//产品价格
                productNumList.push({'pnNum':1,'product':{'productId':productId, 'price':price, 'productName':productName}});
            })
            // var mapProd = getRepeatNum(arrProdId);
            // for(var key in mapProd){
            //     productNumList.push({'pnNum':mapProd[key],'product':{'productId':key}});
            // }
            solu['roomList'].push({'roomName':roomName,'productNumList':productNumList});
        })
        $.ajax({
            url:url_addSolu,
            data:JSON.stringify(solu),
            contentType:'application/json',
            dataType:"json",
            type:"post",
            async:false,
            success:function(json) {
                if(1 == json.status){
                    // window.history.back();
                    self.location=document.referrer;
                }else {
                    lightbox_tip(json.msg);
                    return false;
                }
            }
        })
    })

    //检查是否输入了方案名称
    function chkSoluInput(){
        var isOk = false;
        if('' == $('#div_soluname input').val().trim()){
            lightbox_tip(soluNameNull_text);
            return isOk;
        }
        if(0 == $('#div_tab_1>div:first-of-type .div_prod_1').length){
            lightbox_tip(noProd_text);
            return isOk;
        }
        isOk = true;
        return isOk;
    }

    //统计产品数量
    function getRepeatNum(arr){
        var obj = {};
        for(var i= 0, l = arr.length; i< l; i++){
            var item = arr[i];
            obj[item] = (obj[item] +1 ) || 1;
        }
        return obj;
    }


    $('.img_back_1').click(function(){
        // window.history.back();
        self.location=document.referrer;
    })
})
/**
 *----------------------------------------------------------------------------方案详情
 *
 */
/**
 * Created by Wzz20210118 on 2021/2/25.
 */
/**
 *----------------------------------------------------------------------------产品列表
 */
var cnlAllChk_text = "取消全部";
var showChk_text = "查看选中";
var saveChk_text = "直接添加";
var leftNavList;

//----------------------------------------------------------url查询产品分类例如WIFI、照明
var url_queryLeftNav = "/xddesign/design/customer/solutions/levels";
//---------------------------------------------------------url查询产品分类对应产品
var url_queryProdList = "/xddesign/design/customer/solutions/level/products";

var chkAddProdListTitle_text = "添加产品清单";
var chkCnl_text = "取消";
var chkOk_text = "确定";
var chkNull_text = "未选中任何产品！";
$(function(){
    bindDftEvent();
    function bindDftEvent(){
        $('.div_top_2>div:first').text(saveChk_text);
        $('.div_top_2>div').eq(1).text(showChk_text);
        $('.div_top_2>div:last').text(cnlAllChk_text);

        getLeftNav();
        bindLeftNav();

        var list = getAllProdList();
        bindProdList(list);
        prodBindEvent();
    }

    //得到一级分类和二级分类
    function getLeftNav(){
        $.ajax({
            url:url_queryLeftNav,
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                leftNavList =  json;
            }
        })
    }

    //将一级分类和二级分类固定到左侧导航栏
    function bindLeftNav(){
        var add = "<ul class='layui-nav layui-nav-tree layui-bg-cyan layui-inline' lay-filter='ul_leftnav' lay-shrink='all'>";
        for(var i = 0;i<leftNavList.length;i++){
            var name_1 = leftNavList[i][0].firstLevel.firstName;
            add += '<li class="layui-nav-item "> <a href="javascript:;">' +name_1 +'</a>'+
                '<dl class="layui-nav-child">';
            for(var j = 0;j<leftNavList[i].length;j++){
                var name_2 = leftNavList[i][j].secondName;
                add += '<dd><a href="javascript:;" name = "'+leftNavList[i][j].secondId+'">'+name_2+'</a></dd>';
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
                //var fstLevName = $('#div_leftnav li.layui-nav-itemed>a:first-of-type').text();
                if(navTier == 0) {
                    //var scdLevName = elem.text();
                    var nameSign = elem.attr('name');
                    $('#div_tab_2>div').each(function(){
                        if(nameSign == $(this).attr('name')){
                            $(this).css('z-index',1997);
                            $(this).siblings().css('z-index',100);
                        }
                    })
                }
            })
            $('#div_leftnav>ul li:first-of-type>a:first-of-type').click();
            $('#div_leftnav>ul li:first-of-type dd:first-of-type a').click();
        });
    }

    //得到所有产品
    function getAllProdList(){
        var list;
        $.ajax({
            url:url_queryProdList,
            contentType:'application/json;charset=UTF-8',
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                list = json;
            }
        })
        return list;
    }
    //将产品放到对应的二级分类下
    function bindProdList(list){
        var add = "";
        for(var j = 0;j<list.length;j++) {
            var map = list[j];
            for (var key in map) {
                var prodList = map[key];
                var nameSign = eval("(" + key +")").secondId;
                add += "<div name = '" + nameSign + "'>";
                for (var i = 0; i < prodList.length; i++) {
                    add += "<div>" +
                        "<img src =" + prodList[i].pictureList[0].pictureLink + "\>" +
                        "<span>" + prodList[i].productName + "</span>" +
                        "<span style = 'display: none;'>" + prodList[i].productId + "</span>" +
                        "<span style = 'display: none;'>" + prodList[i].price + "</span>" +
                        "<img src = '../../img/prodchk.png' />" +
                        "</div>";

                }
                add += "</div>";
            }
        }
        $('#div_tab_2').append(add);
    }
    function prodBindEvent() {
        $('#div_tab_2>div').addClass("div_prodlist");
        $('#div_tab_2>div>div').addClass("div_prod_2");
        $('.div_2 .div_prod_2>img:last-of-type').hide();
        $('.div_2 .div_prod_2').click(function () {
            if ($(this).find('img:last-of-type').is(':hidden')) {
                $(this).find('img:last-of-type').fadeIn(100);
                $(this).addClass('div_prodchk');
            } else {
                $(this).find('img:last-of-type').hide();
                $(this).removeClass('div_prodchk');
            }
        })
    }

    //取消全部
    $('.div_top_2>div:last').click(function(){
        if($('.div_2 .div_prod_2>img:last-child:visible').length != 0) {
            $('.div_2 .div_prod_2>img:last-child:visible').parent().removeClass('div_prodchk');
            $('.div_2 .div_prod_2>img:last-child:visible').hide();
        }
    })

    //点击查看选中的产品
    $('.div_top_2>div').eq(1).click(function(){
        if($('.div_2 .div_prod_2>img:last-child:visible').length == 0) {
            lightbox_tip(chkNull_text);
        }else{
            var add = '<div>' + chkAddProdListTitle_text + '</div>';
            add += '<div class = "div_prodlistnoload">';
            $('.div_2 .div_prod_2>img:last-child:visible').each(function () {
                var prodChkCurr = $(this).parent();
                add += "<div>";
                add += getCountNode();
                add += prodChkCurr.prop("outerHTML");
                add += '</div>';
            })
            add += '</div>';
            add += "<div>" +
                "<div class = 'div_chkbtn'>" + chkCnl_text + "</div>" +
                "<div class = 'div_chkbtn'>" + chkOk_text + "</div>" +
                "</div>";
            lightbox_chkAddProdList(add);
            bindCountEvent();

            $('.layui-layer-hei .div_prod_2').click(function () {
                if ($(this).find('img:last-of-type').is(':hidden')) {
                    $(this).find('img:last-of-type').fadeIn(100);
                    $(this).addClass('div_prodchk');
                    $(this).prev().find('input').val(parseInt($(this).prev().find('input').val()) + 1);
                } else {
                    $(this).find('img:last-of-type').hide();
                    $(this).removeClass('div_prodchk');
                    $(this).prev().find('input').val(0);
                }
            })

            $('.layui-layer-hei>div:last-of-type>div:last-of-type').click(function(){
                var countVal = 0;
                $(this).parent().prev().children().each(function(){
                    countVal += parseInt($(this).find('input').val());
                })
                if(0 == countVal){
                    lightbox_tip(chkNull_text);
                    return;
                }

                var addProdList = {};
                $(this).parent().prev().children().each(function(){
                    var value = parseInt($(this).find('input').val());
                    if(0 == value){
                        return true;
                    }
                    var nodeProdCurr = $(this).children('div:last');
                    var nodeNeed = nodeProdCurr.clone();
                    nodeNeed.children().last().remove();
                    var value = parseInt($(this).find('input').val());
                    addProdList[nodeNeed.prop("outerHTML")] = value;
                })

                $('.layui-layer-hei>div:last-of-type>div:first-of-type').click();
                for(var key in addProdList){
                    var numCurr = addProdList[key];
                    for(var i = 0;i<numCurr;i++){
                        addRoomProdListNode.find('.div_addprod').before(key);
                    }
                }
                addRoomProdListNode.find('.div_addprod').prevAll().removeClass();
                addRoomProdListNode.find('.div_addprod').prevAll().addClass('div_prod_1');

                addRoomProdListNode.parent().children().eq(0).children().remove();
                $(".div_prodlist_1:not(:first)").each(function () {
                    $(this).find(".div_prod_1").each(function () {
                        addRoomProdListNode.parent().children().eq(0).append('<div class="div_prod_1" draggable="true">'+$(this).html()+'</div>');
                    });
                });
                prodBindDelEvent();
                prodBindShowEvent();
                $('#div_1').show();
                $('.div_top_2>div:last').click();
                $('#div_2').hide();
            })
        }
    })


    function prodBindDelEvent(){
        $('.div_prodlist_1:not(:first-of-type) .div_prod_1').unbind('dragend');
        $('.div_prodlist_1:not(:first-of-type) .div_prod_1').attr('draggable',true);
        $('.div_prodlist_1:not(:first-of-type) .div_prod_1').bind('dragend',function(e){
            var $this = $(this);
            lightbox_delCfm();
            $('.layui-layer-lan .layui-layer-btn0').click(function(){
                var delId = $this.find('span:hidden').eq(0).text();
                $this.remove();
                for(var i = 0;i< $('.div_prodlist_1:first-of-type>div').length;i++){
                    var idCurr = $('.div_prodlist_1:first-of-type>div').eq(i).find('span').eq(1).text();
                    if(idCurr == delId){
                        $('.div_prodlist_1:first-of-type>div').eq(i).remove();
                        return;
                    }
                }
            })
        })
    }

    function prodBindShowEvent(){
        $('.div_prodlist_1 .div_prod_1').unbind('click');
        $('.div_prodlist_1 .div_prod_1').bind('click',function(e){
            var prodId = $(this).find('span:hidden').eq(0).text();
            window.open("../customer/showprod.html?prodId="+prodId);
        })
    }

    //直接添加
    $('.div_top_2>div:first').click(function(){
        if($('.div_2 .div_prod_2>img:last-child:visible').length == 0) {
            lightbox_tip(chkNull_text);
        }else{
            var addProdList = {};
            $('.div_2 .div_prod_2>img:last-child:visible').each(function(){
                var value = 1;
                var nodeNeed = $(this).parent().clone();
                nodeNeed.children().last().remove();
                addProdList[nodeNeed.prop("outerHTML")] = value;
            })

            for(var key in addProdList){
                addRoomProdListNode.find('.div_addprod').before(key);
            }
            addRoomProdListNode.find('.div_addprod').prevAll().removeClass();
            addRoomProdListNode.find('.div_addprod').prevAll().addClass('div_prod_1');
            addRoomProdListNode.parent().children().eq(0).children().remove();
            $(".div_prodlist_1:not(:first)").each(function () {
                $(this).find(".div_prod_1").each(function () {
                    addRoomProdListNode.parent().children().eq(0).append('<div class="div_prod_1" draggable="true">'+$(this).html()+'</div>');
                });
            });
            prodBindDelEvent();
            prodBindShowEvent();
            $('#div_1').show();
            $('.div_top_2>div:last').click();
            $('#div_2').hide();
        }
    })

    function getCountNode(){
        var node = '<div id = "" class = "div_count">' +
            '<div class="div_plus__2">' +
            '<img src = "../../img/plus_.svg"/>' +
            '</div>' +
            '<input value = "1" oninput="value=value.replace(/[^\\d]/g,\'\')" maxlength="3"/>' +
            '<div class = "div_plus_2">' +
            '<img src = "../../img/plus_.svg"/>' +
            '<img src = "../../img/plus1.svg"/>' +
            '</div>' +
            '</div>';
        return node;
    }

    function bindCountEvent(){
        $('.div_plus_2').mouseover(function(){
            $(this).find('img:first-child').attr('src','../../img/plus_hover.svg');
            $(this).find('img:last-child').attr('src','../../img/plus1hover.svg');
        }).mouseleave(function(){
            $(this).find('img:first-child').attr('src','../../img/plus_.svg');
            $(this).find('img:last-child').attr('src','../../img/plus1.svg');
        })

        $('.div_plus__2').mouseover(function(){
            $(this).find('img').attr('src','../../img/plus_hover.svg');
        }).mouseleave(function(){
            $(this).find('img').attr('src','../../img/plus_.svg');
        })

        $('.div_count input').blur(function(){
            if('' == $(this).val()){
                $(this).val(0);
            }
        })

        $('.div_count').each(function(){
            $(this).find('div:first-of-type').click(function(){
                var num = parseInt($(this).next().val());
                if(0 == num){
                }else {
                    if(1 == num){
                        $(this).parent().next().find('img:last-of-type').hide();
                        $(this).parent().next().removeClass('div_prodchk');
                    }
                    $(this).next().val(num - 1);
                }
            })
            $(this).find('div:last-of-type').click(function(){
                var num = parseInt($(this).prev().val());
                if(999 == num){
                }else {
                    if(0 == num){
                        $(this).parent().next().find('img:last-of-type').fadeIn(100);
                        $(this).parent().next().addClass('div_prodchk');
                    }
                    $(this).prev().val(num + 1);
                }
            })

            $(this).find('input').bind("input propertychange", function() {
                if (0 == parseInt($(this).val())) {
                    $(this).parent().next().find('img:last-of-type').hide();
                    $(this).parent().next().removeClass('div_prodchk');
                } else {
                    $(this).parent().next().find('img:last-of-type').fadeIn(100);
                    $(this).parent().next().addClass('div_prodchk');
                }
            })

            $(this).find('div:first-of-type').mousedown(function(){
                $(this).removeClass('div_plus__2');
                $(this).addClass('div_clickcss');
            })
            $(this).find('div:first-of-type').mouseup(function(){
                $(this).addClass('div_plus__2');
                $(this).removeClass('div_clickcss');
            })
            $(this).find('div:last-of-type').mousedown(function(){
                $(this).removeClass('div_plus_2');
                $(this).addClass('div_clickcss');
            })
            $(this).find('div:last-of-type').mouseup(function(){
                $(this).addClass('div_plus_2');
                $(this).removeClass('div_clickcss');
            })
            $(this).find('div').mousedown(function(){
                $(this).siblings('input').css('box-shadow','inset 2px 2px 2px 0 rgba(255,255,255,.5), 7px 7px 20px 0 rgba(0,0,0,.1), 4px 4px 5px 0 rgba(0,0,0,.1)');
                $(this).siblings('input').css('font-weight','bold');
            })
            $(this).find('div').mouseup(function(){
                $(this).siblings('input').css('box-shadow','none');
                $(this).siblings('input').css('font-weight','normal');
            })

            $(this).find('div:first-of-type').mouseleave(function(){
                $(this).addClass('div_plus__2');
            })
            $(this).find('div:last-of-type').mouseleave(function() {
                $(this).addClass('div_plus_2');
            })
            $(this).find('div').mouseleave(function(){
                $(this).removeClass('div_clickcss');
                $(this).siblings('input').css('box-shadow','none');
                $(this).siblings('input').css('font-weight','normal');
            })
        })
    }

    $('.img_back_2').click(function(){
        $('#div_1').show();
        $('#div_2').hide();
    })

})
/**
 *----------------------------------------------------------------------------产品列表
 */