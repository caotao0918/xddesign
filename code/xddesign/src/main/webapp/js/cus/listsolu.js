/**
 * Created by Wzz20210118 on 2021/2/5.
 */
/**
 *---------------------------------------------------------------------客户方案列表页
 */
//总条数默认值
var countDflt = 100;
//总条数真实值
var countReal;
//    var tableTitle_text = "方案列表";
//pageSize选择
var array_pageSize_houseSolu = [10,20,30];
//默认pageSize
var pageSizeDflt = 10;
var showSolu_td_asButton_text = "查看方案";
var showRnd_td_asButton_text = "查看效果图";
var showQuote_td_asButton_text = "查看报表";
var cusName = window.localStorage.getItem("cusName");
var cusId = window.localStorage.getItem("cusId");
$(function(){
    function getData(pageNum,pageSize){
        var list;
        var houseId = UrlParam.param("houseId");
        console.log(houseId);

        $.ajax({
            //----------------------------------------------------------url查询每个house对应的soulu列表
            url:"/public/customer/house/solutions",
            data:{'houseId':houseId,'pageNum':pageNum,'pageSize':pageSize},
            dataType:"json",
            type:"GET",
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
            })
            $(this).mouseleave(function(){
                $(this).children('td:lt(6)').css('background-color','#FFFFFF');
            })
        })
        $('#t_solu').mouseleave(function(){
            $('#t_solu tr:odd').each(function(){
                $(this).children('td:lt(6)').css('background-color', '#CBB6A1');
            })
        })

        $('#t_solu tr').each(function(){
            var soluId = $(this).children('td:hidden').html();
            var td_showSolu = $(this).children().eq(-3);
            var td_showRnd = $(this).children().eq(-2);
            var td_showQuote = $(this).children().eq(-1);
            td_showSolu.click(function(){
                window.location.href = "/customer/showcussolu.html?soluId=" +soluId ;
                //----------------------------------------------------------------查看方案
            })
            td_showRnd.click(function(){
                window.location.href = "/customer/showrnd.html?soluId=" +soluId ;
                //--------------------------------------------------------------查看效果图
            })
            td_showQuote.click(function(){
                window.location.href = "/customer/showquote.html?soluId=" +soluId ;
                //----------------------------------------------------------------查看报表
            })
        })
    }

    function pageRender(divId,count){
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
                        pageRender('div_pagerender', countReal);
                    }
                    bindData(list);
                    $('#div_pagerender').css('width',$('#t_solu tr:first').css('width'));
                }
            });
        })
    }
    pageRender('div_pagerender',countDflt);

    $('#img_back').click(function(){
        history.go(-1);
    })

})
/**
 *---------------------------------------------------------------------客户方案列表页
 */