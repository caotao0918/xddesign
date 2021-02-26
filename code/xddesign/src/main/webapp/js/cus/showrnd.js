/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *--------------------------------------------------------------------------方案设计图
 */
var soluImg_title = "方案设计图";
var soluImgList;
$(function(){
    $('#div_soluimgtitle').html(soluImg_title);
    querySoluImg();
    function querySoluImg(){
        $.ajax({
            //---------------------------------------------------------------url查询设计图
            url:"/public/customer/renderings",
            data: {"soluId" : soluId},
            dataType:"json",
            type:"GET",
            async:false,
            success:function(json) {
                soluImgList = json;
            }
        })
    }
    bindSoluImg();
    function bindSoluImg(){
        var add = "";
        for(var i = 0;i<soluImgList.length;i++){
            add += "<div>" +
                "<img src ='" + soluImgList[i].rendPath +"'\>" +
                "</div>";
        }
        $('#div_soluimglist').append(add);

        $('#div_soluimglist div').each(function(){
            $(this).mouseenter(function(){
                var i = $(this).index();
                var posX = event.pageX - 700;
                var posY = event.pageY - 700;
                var msg = "<pre>" +
                    soluImgList[i].rendName +
                    "<br/>" +
                    soluImgList[i].rendDesc +
                    "</pre>";
                tag_desc(msg,posX,posY);
            })
        })
    }


    $('#img_back').click(function() {
        history.go(-1);
    })
})
/**
 *--------------------------------------------------------------------------方案设计图
 */