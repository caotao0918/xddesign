/**
 * Created by Wzz20210118 on 2021/2/19.
 */
/**
 * ----------------------------------------------------------------------添加新客户
 */
var username_text = "姓名：";
var password_text = "密码：";
var passwordChk_text = "确认密码：";
var mbl_text = "手机号：";
var add_text = "联系地址：";
var demand_text = "客户需求：";
var cusType_text = "客户类别：";
var saveAndExit_text = "保存并退出";
var updAndExit_text = "修改并退出";
var nextStep_text = "下一步";

var mblReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
var inputEmptyTip_text = "不能为空或空格！";
var passwordDft = 123456;
var passwordError_text = "两次输入的密码不一致！";
var mblIllegalTip_text = "请输入正确的手机号！";
var addIllegalTip_text = "地址不能为空！";
var cusTypeIllegalTip_text = "请选择客户类型！";

var cusTypeList = {'个人':5,'小区':1,'装修队':2,'开发商':3,'其他':4};
var cusType = "";
var cusId = "";

var cusShowSoluList_text = "查看方案";
var span_lightbox_showTime = 1000;
var houseName_text = "家庭名称";
var houseType_text = "户型";
var houseAdd_text = "地址";
var houseDivDel_text = "删除";
var houseDivSave_text = "保存";
var houseDivUpd_text = "修改";

$(function() {
    bindText();
    function bindText(){
        $('#input_usename').prev().text(username_text);
        $('#input_password').prev().text(password_text);
        $('#input_password').val(passwordDft);
        $('#input_passwordchk').prev().text(passwordChk_text);
        $('#input_passwordchk').val(passwordDft);
        $('#input_mbl').prev().text(mbl_text);
        $('#textarea_add').prev().text(add_text);
        $('#textarea_demand').prev().text(demand_text);
        $('#div_custype').siblings('span').text(cusType_text);
        $('#div_saveandexit_1').text(saveAndExit_text);
        $('#div_nextstep_1').text(nextStep_text);
    }

    $('input').blur(function(){
        var pattern = /[`~!@#$^&*()=|\{}':;',\[\].<>/?~！%￥……&*（）——|\{}【】‘；：”“'。，、\'\"？?\\]/g;
        $(this).val($(this).val().replace(pattern,''));
    });
    String.prototype.trim = function () {
        return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
    }

    function chkPsnInfoInput(){
        var isOk = true;
        var listNeedInput = $('#div_psninfo>div input');
        for(var i = 0;i<listNeedInput.length;i++){
            var $this = listNeedInput.eq(i);
            console.log(i+"-->"+$this);
            if($this.val().trim() == ''){
                lightbox_tip(($this.prev().text()).substr(0,($this.prev().text()).length - 1) + inputEmptyTip_text);
                $this.focus();
                isOk = false;
                return isOk;
            }
        }
        if( $('#input_password').val().trim() != $('#input_passwordchk').val().trim() ){
            lightbox_tip(passwordError_text);
            isOk = false;
            return isOk;
        }
        if(isOk){
            var isOk = mblReg.test($('#div_psninfo>div:nth-child(4) input').val());
            if(!isOk){
                lightbox_tip(mblIllegalTip_text);
                return isOk;
            }
        }

        if($('#div_psninfo textarea').val().trim() == ''){
            lightbox_tip(addIllegalTip_text);
            isOk =  false;
            return isOk;
        }
        if(cusType == ''){
            lightbox_tip(cusTypeIllegalTip_text);
            isOk = false;
            return isOk;
        }
        return isOk;
    }

    function evalCusPsnInfo(){
        var cusPsnInfo = {};
        cusPsnInfo['username'] = $('#input_usename').val().trim();
        cusPsnInfo['pwd'] = $('#input_password').val().trim();
        cusPsnInfo['mobile'] = $('#input_mbl').val().trim();
        cusPsnInfo['address'] = $('#textarea_add').val();
        cusPsnInfo['demand'] = $('#textarea_demand').val();
        cusPsnInfo['desc'] = cusType;
        for(var i in cusTypeList) {
            if(cusType == i) {
                cusPsnInfo['code'] = cusTypeList[i];
                break;
            }
        }
        if(cusId != ""){
            cusPsnInfo['id'] = cusId;
        }
        return cusPsnInfo;
    }

    $('#div_custype').prev().click(function () {
        var add = "";
        for(var i in cusTypeList){
            add += '<div class = "div_custype div_lightbox_custype div_lightbox_custype_1">'+i+'</div>';
        }
        lightbox_cusType(add);
    })
    $('#div_custype').next().click(function(){
        $('#div_custype').prev().removeClass('div_custypechk');
        $('#div_custype').prev().text('+');
        cusType = '';
    })

    $('#div_saveandexit_1').click(function () {
        if(chkPsnInfoInput()) {
            var rst = addCusPsnInfo(evalCusPsnInfo());
            if(0 == rst.status) {
                lightbox_tip(rst.msg);
                return;
            }else{
                $('#div_saveandexit_1').text(updAndExit_text);
                if(cusId == ""){
                    cusId = rst.id;
                }
                //lightbox_tip("登录密码默认为123456");
                //location.href = "---------------------------------------跳转到客户列表页";
                location.href = "listmycus.html";
            }
        }
    })
    $('#div_nextstep_1').click(function(){
        if(chkPsnInfoInput()) {
            var rst = addCusPsnInfo(evalCusPsnInfo());
            if(0 == rst.status) {
                lightbox_tip(rst.msg);
                return;
            }else{
                $('#div_saveandexit_1').text(updAndExit_text);
                if(cusId == ""){
                    cusId = rst.id;
                }
                $('#div_psninfo').hide();
                $("#div_psninfo input").attr("disabled" , "disabled");
                $('#div_houselist').show();
                $("#div_houselist input").removeAttr("disabled").stop();
                lightbox_tip("登陆密码默认为123456");
            }
        }
    })

    function getHouseType() {
        let typelist;
        $.ajax({
            url: "/xddesign/design/customer/houseType"
            ,dataType: "json"
            ,type: "GET"
            ,async: false
            ,success: function (res) {
                typelist = res;
            }
        });
        return typelist;
    }

    function addCusPsnInfo(cus){
        var rst;
        $.ajax({
            //----------------------------------------------------------添加客户个人信息
            url:"/xddesign/design/customer/saveOrUpdate",
            data:cus,
            dataType:"json",
            type:"post",
            async:false,
            success:function(json) {
                rst = json;
            }
        })
        return rst;
    }

    $('#div_addhouse img:first-of-type').attr('src','../../img/addhouseplus_.svg');
    $('#div_addhouse img:last-of-type').attr('src','../../img/addhouseplus1.svg');
    $('#div_addhouse').mouseenter(function () {
        $('#div_addhouse img:first-of-type').attr('src','../../img/addhouseplus_hover.svg');
        $('#div_addhouse img:last-of-type').attr('src','../../img/addhouseplus1hover.svg');
    })
    $('#div_addhouse').mouseleave(function () {
        $('#div_addhouse img:first-of-type').attr('src','../../img/addhouseplus_.svg');
        $('#div_addhouse img:last-of-type').attr('src','../../img/addhouseplus1.svg');
    })

    $('#div_addhouse').click(function() {
        var add = '<div>' +
            '<div class = "div_house">' +
            '<span class = "span_text">' + houseName_text + '</span> <input class = "span_housename">' +
            '<span class = "span_text">' + houseType_text + '</span> <input class = "span_housename" placeholder="请输入或选择下方的选项">' +
            '<span class = "span_text">选择户型</span> <select class = "span_housename"></select>' +
            '<span class = "span_text">' + houseAdd_text + '</span> ' +
            '<textarea class = "span_houseadd" maxlength="50"></textarea>' +
            '</div>' +
            '<div class = "div_housedivopt div_housedivdel">' + houseDivDel_text + '</div>' +
            '<div class = "div_housedivopt div_housedivsave">' + houseDivSave_text + '</div>' +
            '</div>';
        $(this).before(add);
        let typelist = getHouseType();
        let typeOPt = '<option>--请选择--</option>';
        for (let i = 0; i < typelist.length; i++) {
            typeOPt += '<option>'+typelist[i].typeName+'</option>';
        }
        let typeselect = $('select.span_housename');
        typeselect.empty();
        typeselect.html(typeOPt);
        typeselect.bind('change', function () {
            $(this).prev().prev().val($(this).children("option:selected").text());
        });

        $('input').blur(function () {
            var pattern = /[`~!@#$^&*()=|\{}':;',\[\].<>/?~！%￥……&*（）——|\{}【】‘；：”“'。，、\'\"？?\\]/g;
            $(this).val($(this).val().replace(pattern, ''));
        });
        String.prototype.trim = function () {
            return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
        }

        $('.div_housedivdel').each(function () {
            if($(this).siblings('.div_house').attr('id') == undefined) {
                $(this).click(function () {
                    $(this).parent().remove();
                })
            }
        })

        $('.div_housedivsave').each(function () {
            $(this).off('click');
            $(this).click(function () {
                if (chkHouseInput($(this))) {
                    var rst = addHouse(evalHouse($(this)));
                    lightbox_tip(rst.msg);
                    if(0 == rst.status) {
                        return;
                    }else{
                        $(this).text(houseDivUpd_text);
                        var aaa = $(this).siblings('.div_house');
                        var houseId = rst.id;
                        aaa.attr('id',houseId);

                        $(this).siblings('.div_housedivdel').off('click');
                        $(this).siblings('.div_housedivdel').click(function () {
                            lightbox_delCfm();
                            $('.layui-layer-btn0').click(function(){
                                var delRst = delHouse(houseId);
                                lightbox_tip(delRst.msg);
                                if(delRst.status == 1) {
                                    aaa.parent().remove();
                                }
                            })
                        })


                        var houseType = aaa.children('input').eq(1).val().trim();
                        var t;
                        aaa.mouseenter(function(){
                            aaa.children().hide();
                            aaa.append("<span class = 'span_lightbox'>" +cusShowSoluList_text+"</span>");
                            aaa.children(".span_lightbox").click(function(){
                                window.localStorage.setItem('houseId',houseId);
                                window.localStorage.setItem('houseType',houseType);
                                    location.href = "listhousesolu.html?houseId="+houseId;
                                });
                            aaa.addClass("div_house_hover");
                                t= window.setTimeout( function(){
                                    //                          aaa.css('pointer-events','none');
                                    aaa.mouseleave();
                                } ,span_lightbox_showTime);
                            })

                        aaa.mouseleave(function(){
                            aaa.children(".span_lightbox").remove();
                            aaa.removeClass("div_house_hover");
                            aaa.children().show();
                                clearTimeout(t);
                         })


                        var fstNullNode = getFstNullNode();
                        if(fstNullNode != ''){
                            var moveNode = $(this).parent();
                            var fstNullNodeRfn = fstNullNode.next();
                            var moveNodeRfn = moveNode.next();
                            if(fstNullNodeRfn.index() != moveNode.index() && fstNullNode.index() < moveNode.index()){
                                addCartoon(fstNullNode,moveNode);
                                fstNullNode.insertBefore(moveNodeRfn);
                                moveNode.insertBefore(fstNullNodeRfn);
                            }else if(fstNullNodeRfn.index() == moveNode.index()){
                                addCartoon(fstNullNode,moveNode);
                                moveNode.remove();
                                moveNode.insertBefore(fstNullNode);
                            }
                        }
                    }
                }
            })
        })
    })

    function getFstNullNode(){
        var fstNullNode = '';
        for(var i = 0;i<$('#div_houselist>div').length;i++) {
            var nodeCurr = $('#div_houselist>div').eq(i);
            for (var j = 0; j < nodeCurr.find('input,textarea').length; j++) {
                if (nodeCurr.find('input,textarea').eq(j).val().trim() == '') {
                    fstNullNode = nodeCurr;
                    return fstNullNode;
                }
            }
        }
        return fstNullNode;
    }

    function addCartoon(a,b){
        var clearTransform=function(Div,time){
            setTimeout(function(){
                Div.css({'transform':'inherit','-webkit-transform':'inherit'});
            },time)
        }
        //记录两容器原始高宽
        var oldOpt={ax:a.width(),ay:a.height(),bx:b.width(),by:b.height()};

        //获取两容器屏幕位置
        var a_pos=a.offset();
        var b_pos=b.offset();
        //获取两容器偏移值
        var offset_x=a_pos.left-b_pos.left;
        var offset_y=a_pos.top-b_pos.top;

        //第一个花括号里面是动画内容，可以为空，但不能省去中括号
        a.animate({},function(){
            //偏移值取反
            var offset_x_=-offset_x;
            var offset_y_=-offset_y;
            var transformStr='rotate(360deg) translate('+offset_x_+'px,'+offset_y_+'px)';
            a.css({'width':oldOpt.bx+'px','height':oldOpt.by+'px','transform':transformStr,'-webkit-transform':transformStr});
            clearTransform(a,0);
        })

        b.animate({},function(){
            var transformStr='rotate(360deg) translate('+offset_x+'px,'+offset_y+'px)';
            b.css({'width':oldOpt.ax+'px','height':oldOpt.ay+'px','transform':transformStr,'-webkit-transform':transformStr});
            clearTransform(b,0);
        })
    }



    function chkHouseInput($saveHouseBtn){
        var isOk = true;
        var listNeedInput = $saveHouseBtn.siblings('.div_house').children('input');
        for(var i = 0;i<listNeedInput.length;i++){
            var $this = listNeedInput.eq(i);
            if($this.val().trim() == ''){
                lightbox_tip($this.prev().text() + inputEmptyTip_text);
                $this.focus();
                isOk = false;
                return isOk;
            }
        }

        if($saveHouseBtn.siblings('.div_house').children('textarea').val() == ''){
            lightbox_tip($('.div_house textarea').prev().text() + inputEmptyTip_text);
            isOk = false;
            return isOk;
        }

        return isOk;
    }

    function evalHouse($saveHouseBtn){
        var house = {};
        house['customerId'] = cusId;
        house['houseName'] = $saveHouseBtn.siblings('.div_house').children('input').eq(0).val().trim();
        var houseType = {};
        houseType['typeName'] = $saveHouseBtn.siblings('.div_house').children('input').eq(1).val().trim();
        house['houseType'] = houseType;
        house['houseAddress'] = $saveHouseBtn.siblings('.div_house').children('textarea').val();
        if(undefined != $saveHouseBtn.siblings('.div_house').attr('id')) {
            house['houseId'] = $saveHouseBtn.siblings('.div_house').attr('id');
        }
        return JSON.stringify(house);
    }

    function addHouse(house){
        var rst;
        $.ajax({
            //--------------------------------------------------------------------添加住宅
            url:"/xddesign/design/customer/house/saveorupdate",
            data:house,
            dataType:"json",
            contentType:'application/json',
            traditional : true,
            type:"post",
            async:false,
            success:function(json) {
                rst = json;
            }
        })
        return rst;
    }

    function delHouse(houseId){
        var rst;
        $.ajax({
            //--------------------------------------------------------------------删除住宅
            url:"/xddesign/design/customer/house/delete",
            data:{'houseId':houseId},
            dataType:"json",
            type:"post",
            async:false,
            success:function(json) {
                rst = json;
            }
        })
        return rst;
    }

    $('#div_psninfo').show();
    $("#div_psninfo input").removeAttr("disabled").stop();
    $('#div_houselist').hide();
    $("#div_houselist input").attr("disabled" , "disabled");


    $('#img_back').click(function(){
        if($('#div_psninfo').is(':hidden')){
            $('#div_psninfo').show();
            $("#div_psninfo input").removeAttr("disabled").stop();
            $('#div_houselist').hide();
            $("#div_houselist input").attr("disabled" , "disabled");
        }else{
            //location.href = "---------------------------------------跳转到客户列表页";
            location.href = "listmycus.html";
        }
    })
})