/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *----------------------------------------------------------------------------产品详情
 */
var prodeDesc_text = "产品描述";
var prod;
$(function(){
    $('#div_proddesc span').html(prodeDesc_text);
    function queryProdProp(){
        $.ajax({
            //---------------------------------------------------------------url查询产品
            url:ctx + "/queryProdProp",
            data: {"prodId" : prodId},
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                prod = json;
            }
        })
    }

    function bindImg(){
        var add = "";
        var imgList = prod.pictureList;
        for(var i = 0;i<imgList.length;i++){
            add += "<img src ='" + imgList[i].pictureLink +"'/>";
        }
        $('#div_prodimglist div').append(add);
    }

    function bindProdDesc(){
        $('#div_proddesc pre').html(prod.productDesc);
    }

    function bindProdProp(){
        var add = "";
        var list = prod.productPropertyList;
        for(var i = 0;i<list.length;i++){
            var objCurr = list[i].value.property;
            var name = objCurr.propertyName;
            var value = objCurr.commonValue;
            add += "<div>" +
                "<div>" + name + "</div>" +
                "<div>" + value + "</div>" +
                "</div>";
        }
        $('#div_prodprop').append(add);
    }


    queryProdProp();
    bindImg();
    bindProdDesc();
    bindProdProp();
    crsl_prodImg('div_prodimglist');

    $('#img_back').click(function(){
         location.href(ctx + "--------------------------------回到刚才的客户方案页面");
    })
})
/**
 *----------------------------------------------------------------------------产品详情
 */