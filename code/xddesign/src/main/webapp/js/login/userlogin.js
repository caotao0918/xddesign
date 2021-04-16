/**
 * Created by Wzz20210118 on 2021/2/18.
 */
/**
 * Created by Wzz20210118 on 2021/2/4.
 */
/**
 *----------------------------------------------------------------------------用户登录
 */
var usernameReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
var btnlogin_text = "进入";
var inputEmptyTip_text = "不能为空或空格！";
var usernameIllegalTip_text = "请输入正确的账号！账号为注册手机号";
$(function(){
    $('#div_userlogin>div:last-of-type span').html(btnlogin_text);
    $('#div_userlogin>div input').blur(function(){
        var pattern = /[`~!@#$^&*()=|\{}':;',\[\].<>/?~！%￥……&*（）——|\{}【】‘；：”“'。，、\'\"？?\\]/g;
        $(this).val($(this).val().replace(pattern,''));
    });
    String.prototype.trim = function () {
        return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
    }

    function checkUserLoginInput(){
        var isOk = true;
        var listNeedInput = $('#div_userlogin>div input');
        for(var i = 0;i<listNeedInput.length;i++){
            var $this = listNeedInput.eq(i);
            if($this.val().trim() == ''){
                lightbox_tip($this.prev().html() + inputEmptyTip_text);
                $this.focus();
                isOk = false;
                break;
            }
        }
        if(isOk){
            var isOk = usernameReg.test($('#div_userlogin>div input:first-of-type').val());
            if(!isOk){
                lightbox_tip(usernameIllegalTip_text);
            }
        }
        return isOk;
    }

    $('#div_userlogin>div:last-of-type').click(
        function login(){
            var isInputOk = checkUserLoginInput();
            if(!isInputOk){
                return;
            }

            $.ajax({
                type: "POST",
                //url: "----------------------------------------------------------------登录",
                url: "/xddesign/user/login",
                data:{'mobile': $('#div_userlogin>div input:first-of-type').val(),
                    'password': $('#div_userlogin>div input:password').val()},
                async: false,
                cache: false,
                dataType: "json",
                success: function (data) {
                    if (data.status===0) {
                        lightbox_tip(data.msg);
                        return false;
                    }else {
                        window.localStorage.clear();
                        window.localStorage.setItem("username",data.data.username);
                        window.localStorage.setItem("userId",data.data.id);
                        let roleName = data.data.role.name;
                        window.localStorage.setItem("rolename", roleName);
                        window.localStorage.setItem("mobile", data.data.mobile);
                        window.localStorage.setItem("roleId", data.data.role.id);
                        window.localStorage.setItem("lastTime", data.data.lastTime);
                        if (roleName === "设计人员") {
                            window.location.href = "../dsg/main.html";
                        }else if (roleName === "管理员") {
                            window.location.href = "../admin/views/index.html";
                        }else if (roleName === "施工人员") {
                            window.location.href = "../worker/tasklist.html";
                        }else {
                            lightbox_tip("敬请期待");
                        }
                    }
                }
            });
        }
    )
    /**
     *-------------------------------------------------------------------------用户登录
     */
})