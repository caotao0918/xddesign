/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *----------------------------------------------------------------------------方案详情
 */
var allProd_text = "所有产品";
var solu;
var soluId = UrlParam.param("soluId");
var soluName;
var roomList;
var productNumList;
var productNum;
$(function(){

    $.ajax({
        //----------------------------------------------------------url查询每个house对应的soulu列表
        url:"/public/customer/solution",
        data:{'soluId':soluId},
        dataType:"json",
        type:"GET",
        async:false,
        success:function(data) {
            solu = data;
            soluName = data.soluName;
            roomList = data.roomList;
        }
    })
    $('#div_soluname span').html(soluName);

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
        location.href = "showprod.html?prodId=" + prodId;
    })


    $('#img_back').click(function(){
        history.go(-1);
    })
})
/**
 *----------------------------------------------------------------------------方案详情
 */