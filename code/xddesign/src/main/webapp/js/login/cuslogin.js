/**
 * Created by Wzz20210118 on 2021/2/4.
 */
/**
 *----------------------------------------------------------------------------客户登录
 */
var usernameReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
var btnlogin_text = "进入";
var inputEmptyTip_text = "不能为空或空格！";
var usernameIllegalTip_text = "请输入正确的账号！账号为注册手机号";
$(function(){
    $('#div_cuslogin>div:last-of-type span').html(btnlogin_text);
    $('#div_cuslogin>div input').blur(function(){
        var pattern = /[`~!@#$^&*()=|\{}':;',\[\].<>/?~！%￥……&*（）——|\{}【】‘；：”“'。，、\'\"？?\\]/g;
        $(this).val($(this).val().replace(pattern,''));
    });
    String.prototype.trim = function () {
        return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
    }

    function checkCusLoginInput(){
        var isOk = true;
        var listNeedInput = $('#div_cuslogin>div input');
        for(var i = 0;i<listNeedInput.length;i++){
            var $this = listNeedInput.eq(i);
            if($this.val().trim() === ''){
                lightbox_tip($this.prev().html() + inputEmptyTip_text);
                $this.focus();
                isOk = false;
                break;
            }
        }
        if(isOk){
            var isOk = usernameReg.test($('#div_cuslogin>div input:first-of-type').val());
            if(!isOk){
                lightbox_tip(usernameIllegalTip_text);
            }
        }
        return isOk;
    }

    // $('#div_cuslogin>div input:first-of-type').blur(function(){
    //     if($(this).val().trim() != ''){
    //         $.ajax({
    //             type: "GET",
    //             url: "--------------------------------------------检校账号是否存在",
    //             data: {'username':$(this).val()},
    //             async: false,
    //             cache: false,
    //             dataType: "json",
    //             beforeSend: function () {
    //
    //             },
    //             complete: function () {
    //
    //             },
    //             success: function (data) {
    //
    //             }
    //         });
    //     }
    // })

    $('#div_cuslogin>div:last-of-type').click(
        function login(){
            var isInputOk = checkCusLoginInput();
            if(!isInputOk){
                return;
            }
            $.ajax({
                type: "POST",
                url: "/xddesign/customer/login",
                data:{'mobile': $('#div_cuslogin>div input:first-of-type').val(),
                    'pwd': $('#div_cuslogin>div input:password').val()},
                dataType: "json",
                beforeSend: function () {
                },
                complete: function () {
                },
                success: function (data) {
                    if (data.status===0) {
                        lightbox_tip(data.msg);
                    }else {
                        window.localStorage.setItem("cusName",data.data.username);
                        window.localStorage.setItem("cusId",data.data.id);
                        window.location.href = "../customer/myhouselist.html";
                    }
                }
            });
        }
    )
    /**
     *-------------------------------------------------------------------------客户登录
     */
})