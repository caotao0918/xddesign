/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *---------------------------------------------------------------------住宅方案列表页
 */
//总条数默认值
var countDflt = 100;
//总条数真实值
var countReal;
//pageSize选择
var array_pageSize_houseSolu = [10,20,30];
//默认pageSize
var pageSizeDflt = 10;
var showSolu_td_asButton_text = "修改方案";
var showRnd_td_asButton_text = "修改效果图";
var showQuote_td_asButton_text = "修改报表";

var houseId = window.localStorage.getItem("houseId");

//---------------------------------------------------------url查询每个house对应的soulu列表
var url_queryHouseSolu = "/xddesign/public/customer/house/solutions";
//----------------------------------------------------------------------------查看方案
var url_showCusSolu = "addhousesolu.html";
//----------------------------------------------------------------------------查看设计图
var url_showRnd = "editrnd.html";
//----------------------------------------------------------------------------查看报表
var url_showQuote = "quote_edit.html";
//----------------------------------------------------------------------------新增方案
var url_addHouseSlou = "addhousesolu.html";
//只是回退页面
var url_back = "";

$(function(){
    function getData(pageNum,pageSize){
        var list;
        $.ajax({
            url:url_queryHouseSolu,
            data:{'pageNum':pageNum,'pageSize':pageSize, "houseId": houseId},
            dataType:"json",
            type:"get",
            async:false,
            success:function(json) {
                countReal = json.total;
                list =  json.records;
            }
        })
        return list;
    }

    function bindData(list){
        $('#t_solu').children().remove();
        var cusName = window.localStorage.getItem("cusName");
        var add = "";
        for (var i = 0; i < list.length; i++) {
            add += "<tr>" +
                "<td>"+(i + 1)+"</td>" +
                "<td>"+cusName+"</td>" +
                "<td style = 'display: none;'>"+list[i].soluId+"</td>" +
                "<td>"+list[i].soluName+"</td>" +
                "<td>"+list[i].state+"</td>" +
                "<td>"+list[i].addTime+"</td>" +
                "<td class = 'td_asbutton'>"+showSolu_td_asButton_text+"</td>" +
                "<td class = 'td_asbutton'>"+showRnd_td_asButton_text+"</td>" +
                "<td class = 'td_asbutton'>"+showQuote_td_asButton_text+"</td>" +
                "</tr>";
        }
        $('#t_solu').append(add);

        $('#t_solu tr:odd').each(function() {
            $(this).children('td:lt(6)').css('background-color', '#CBB6A1');
        })
        $('#t_solu tr').each(function(){
            $(this).mouseover(function(){
                $(this).parent().children('tr').each(function() {
                    $(this).children('td:lt(6)').css('background-color', '#FFFFFF');
                })
                $(this).children('td:lt(6)').css('background-color','#F59A23');
                $(this).children('td:gt(5)').css('box-shadow', 'inset 2px 2px 2px 0 rgba(255,255,255,.5), 7px 7px 20px 0 rgba(0,0,0,.1), 4px 4px 5px 0 rgba(0,0,0,.1)'
                );
            })
            $(this).mouseleave(function(){
                $(this).children('td:lt(6)').css('background-color','#FFFFFF');
                $(this).children('td:gt(5)').css('box-shadow', 'none');
            })
        })
        $('#t_solu').mouseleave(function(){
            $('#t_solu tr:odd').each(function(){
                $(this).children('td:lt(6)').css('background-color', '#CBB6A1');
            })
        })

        $('#t_solu tr').each(function(){
            var soluId = $(this).children('td:hidden').html();
            var soluName = $(this).children('td').eq(3).html();
            var td_showSolu = $(this).children().eq(-3);
            var td_showRnd = $(this).children().eq(-2);
            var td_showQuote = $(this).children().eq(-1);
            td_showSolu.click(function(){
                location.href = url_showCusSolu
                    +"?soluId=" +soluId ;
            })
            td_showRnd.click(function(){
                location.href = url_showRnd
                    +"?soluId=" +soluId + "&soluName=" + soluName ;
            })
            td_showQuote.click(function(){
                location.href = url_showQuote
                    +"?soluId=" +soluId ;
            })
        })
    }

    function pagernd(divId,count){
        layui.use(['laypage', 'layer'], function() {
            var laypage = layui.laypage;
            laypage.render({
                elem: divId
                ,count : count
                , limit:pageSizeDflt
                ,limits:array_pageSize_houseSolu
                , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
                , jump: function (obj) {
                    var list = getData(obj.curr,obj.limit);
                    //第一次进页面和增删的时候，刷新总条数
                    if(countReal != count) {
                        pagernd('div_pagernd', countReal);
                    }
                    bindData(list);
                    $('#div_pagernd').css('width',$('#t_solu tr:first').css('width'));
                }
            });
        })
    }
    pagernd('div_pagernd',countDflt);

    $('#div_plus').mouseover(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_hover.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1hover.svg');
    }).mouseleave(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1.svg');
    })

    $('#div_plus').click(function(){
        location.href = url_addHouseSlou;
    })
    $('#img_back').click(function(){
        window.location.href = "updcus.html";
    })

})
/**
 *---------------------------------------------------------------------住宅方案列表页
 */