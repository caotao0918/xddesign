/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *------------------------------------------------------------------------客户住宅列表
 */
var houseName_text = "家庭名称";
var houseType_text = "户型";
var houseAdd_text = "住宅地址";
var cusShowSoluList_text = "查看方案";
var span_lightbox_showTime = 1300;
var myHouseList;
$(function() {
    $.ajax({
        //"-----------------------------------------------------------查询客户住宅列表"
        url: "/public/customer",
        //      data:data,
        dataType:"json",
        type:"GET",
        async:false,
        success:function(data) {
            myHouseList = data.houseList;
            //        alert("----------" + data[0].houseName + "----------");
        }
    })

    for (var i = 0; i < myHouseList.length; i++) {
        var addNodeMain = $("<div class = 'div_myhouse'></div>");
        addNodeMain.attr('id', myHouseList[i].houseId);
        addNodeMain.append("<span class = 'span_text'>"+ houseName_text +"</span>");
        addNodeMain.append("<span class = 'span_housename'>"+ myHouseList[i].houseName +"</span>");
        addNodeMain.append("<span class = 'span_text'>"+ houseType_text +"</span>");
        addNodeMain.append("<span class = 'span_housename'>"+ myHouseList[i].houseType.typeName +"</span>");
        addNodeMain.append("<span class = 'span_text'>"+ houseAdd_text +"</span>");
        addNodeMain.append("<span class = 'span_houseadd'>"+
                //              "我家住在黄土高坡，大风从坡上刮过，不管是西北风还是东南风，" +
                //              "都是我的歌，我的歌。 我家住在黄土高坡，日头" +
            myHouseList[i].houseAddress
            +"</span>");
        $('#div_myhouselist').append(addNodeMain);
    }

    $('#div_myhouselist').children().each(function(){
        var aaa = $(this);
        var t;
        $(this).mouseenter(function(){
            $(this).children().hide();
            $(this).append("<span class = 'span_lightbox'>" +cusShowSoluList_text+"</span>");
            $(this).children(".span_lightbox").click(function(){
                var houseId = $(this).parent().attr('id');
                window.location.href = "listsolu.html?houseId=" + houseId;


            });
            $(this).addClass("div_myhouse_hover");
            t= window.setTimeout( function(){
                //                          aaa.css('pointer-events','none');
                aaa.mouseleave();
            } ,span_lightbox_showTime);
        })

        $(this).mouseleave(function(){
            //              $(this).children(":last-child").remove();
            $(this).children(".span_lightbox").remove();
            $(this).removeClass("div_myhouse_hover");
            $(this).children().show();
            clearTimeout(t);
        })
        $('#img_back').click(function(){
            location.href = "main.html";
        })
    })
})
/**
 *------------------------------------------------------------------------客户住宅列表
 */