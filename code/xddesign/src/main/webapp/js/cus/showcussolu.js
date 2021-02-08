/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *----------------------------------------------------------------------------方案详情
 */
var allProd_text = "所有产品";
$(function(){
    bindRoom();
    bindProdList();

    $('#div_roomlist div').click(function(){
        $(this).addClass("div_room_chk");
        $(this).siblings().removeClass("div_room_chk");
        var indexCurr = $(this).index();
        $('#div_tab>div').eq(indexCurr).show();
        $('#div_tab>div').eq(indexCurr).siblings().hide();
    })
    $('#div_roomlist div').eq(0).click();

    $('#div_tab>div div').click(function(){
        var prodId = $(this).children('span:last-child').html();
        location.href(ctx + "-------------------------------------------------产品详情");
    })


    $('#img_back').click(function(){
        location.href(ctx + "---------------------------------------------客户方案列表");
    })
})
/**
 *----------------------------------------------------------------------------方案详情
 */