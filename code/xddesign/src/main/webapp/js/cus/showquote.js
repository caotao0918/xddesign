/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *----------------------------------------------------------------------------客户报表
 */
var quoteTitle_text = "当前方案报价单";
var downQuote_text = "下载报表";
var thead_1_text = "序号";
var thead_2_text = "房间名";
var thead_3_text = "产品";
var thead_4_text = "数量";
var thead_5_text = "单价";
var thead_6_text = "总价";
var count_text = "总计";
var quoteList;
$(function(){
    $('#div_quotetitle').html(quoteTitle_text);
    $('#div_downquote').html(downQuote_text);

    queryQuoteList();
    function queryQuoteList(){
        $.ajax({
            //---------------------------------------------------------------url查询报价单
            url:"/public/customer/quotes",
            data: {"soluId" : soluId},
            dataType:"json",
            type:"GET",
            async:false,
            success:function(json) {
                quoteList = json;
            }
        })
    }

    bindQuote();
    function bindQuote(){
        var addTHead = "<tr>" +
            "<td>" +thead_1_text+ "</td>" +
            "<td>" +thead_2_text+ "</td>" +
            "<td>" +thead_3_text+ "</td>" +
            "<td>" +thead_4_text+ "</td>" +
            "<td>" +thead_5_text+ "</td>" +
            "<td>" +thead_6_text+ "</td>" +
            "</tr>";
        $('#t_quote thead').append(addTHead);
        var add = "";
        var countNum = 0;
        var countPrice = 0;
        for(var i = 0;i<quoteList.length;i++){
            add += "<tr>" +
                "<td>"+(i+1)+"</td>" +
                "<td>"+ quoteList[i].roomName + "</td>" +
                "<td>"+ quoteList[i].productName + "</td>" +
                "<td>"+ quoteList[i].productNum + "</td>" +
                "<td>"+ quoteList[i].price + "</td>" +
                "<td>"+ quoteList[i].totalPrice + "</td>" +
                "</tr>";
            countNum += Number(quoteList[i].productNum);
            countPrice += Number(quoteList[i].totalPrice);
        }
        $('#t_quote tbody').append(add);
        var addTFoot = "<tr>" +
            "<td>"+ count_text +"</td>" +
            "<td></td><td></td><td></td><td></td>" +
            "</tr>";
        $('#t_quote tfoot').append(addTFoot);
        //这个属性是td标签的属性，不是css样式，无法挪动到css里面
        $('#t_quote tfoot td:nth-child(2)').attr("colspan","2");
        $('#t_quote tfoot td').eq(-3).html(countNum);
        $('#t_quote tfoot td:last-child').html(countPrice);
    }

    $('#div_downquote').click(function() {
        window.location.href = "/public/customer/quote/toexcel?soluId=" + soluId;
    })
    $('#img_back').click(function() {
        history.go(-1);
    })
})
/**
 *----------------------------------------------------------------------------客户报表
 */