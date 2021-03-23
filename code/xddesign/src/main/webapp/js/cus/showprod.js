/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *----------------------------------------------------------------------------产品详情
 */
var prodeDesc_text = "产品描述";
var prodName_text = "产品名称";
var prod;
$(function(){
    $('#div_proddesc span').html(prodeDesc_text);
    $('#prodname').html(prodName_text);
    function queryProdProp(){
        $.ajax({
            //---------------------------------------------------------------url查询产品
            url:"/xddesign/public/product",
            data: {"productId" : prodId},
            dataType:"json",
            type:"GET",
            async:false,
            success:function(data) {
                prod = data;
            }
        })
    }

    function bindImg(){
        var add = "";
        var imgList = prod.pictureList;
        for(var i = 0;i<imgList.length;i++){
            add += "<img src =" + imgList[i].pictureLink + "\>";
        }
        $('#div_prodimglist div').append(add);
    }



    function bindProdInfo(){
        $('#div_proddesc pre').html(prod.productDesc);
        $("#prodnamevalue").html(prod.productName);
        $("#product_detail").html(prod.productDetail);
    }

    function bindProdProp(){
        var add = "";
        var list = prod.productPropertyList;
        for(var i = 0;i<list.length;i++){
            var objCurr = list[i].value.property;
            var name = objCurr.propertyName;
            var value = list[i].value.valueName;
            add += "<div>" +
                "<div>" + name + "</div>" +
                "<div>" + value + "</div>" +
                "</div>";
        }
        $('#div_prodprop').append(add);
    }


    queryProdProp();
    bindImg();
    bindProdInfo();
    bindProdProp();
    crsl_prodImg('div_prodimglist');

    $('#img_back').click(function(){
        history.go(-1);
    })
})
/**
 *----------------------------------------------------------------------------产品详情
 */