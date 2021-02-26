/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *--------------------------------------------------------------------------客户主页面
 */
var cusOpt_text_1 = "家庭管理";
var cusOpt_text_2 = "售后支持";

$(function() {
    $('#div_housemgt').html(cusOpt_text_1);
    $('#div_aftersalesp').html(cusOpt_text_2);
    $('#div_housemgt').click(function () {
        location.href = "/customer/myhouselist.html";
    })
    $('#div_aftersalesp').click(function () {
        location.href = "/customer/aftersalecnt.html";
    })
})
/**
 *--------------------------------------------------------------------------客户主页面
 */