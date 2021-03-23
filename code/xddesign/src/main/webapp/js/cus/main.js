/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *--------------------------------------------------------------------------客户主页面
 */
var cusOpt_text_1 = "家庭管理";
var cusOpt_text_2 = "售后支持";
var cusOpt_welcome = "欢迎尊贵的顾客：";
var cusName = window.localStorage.getItem("cusName");

$(function() {
    $('#div_housemgt').html(cusOpt_text_1);
    $('#div_aftersalesp').html(cusOpt_text_2);
    $('#div_housemgt').click(function () {
        location.href = "myhouselist.html";
    })
    $('#div_aftersalesp').click(function () {
        location.href = "aftersalecnt.html";
    })

    $('.div_cusname').html(cusOpt_welcome+cusName);
})
/**
 *--------------------------------------------------------------------------客户主页面
 */