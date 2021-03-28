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
var showSolu_td_asButton_text = "查看";
var soluToTemp_td_asButton_text = "删除模板";
var showQuote_td_asButton_text = "查看报表";
$(function(){
    function getData(pageNum,pageSize){
        var list;

        $.ajax({
            //----------------------------------------------------------url查询每个house对应的soulu列表
            url:"/xddesign/design/customer/templates",
            data:{'pageNum':pageNum,'pageSize':pageSize},
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
                "<td>"+list[i].tempName+"</td>" +
                "<td style = 'display: none;'>"+list[i].solutions.soluId+"</td>" +
                "<td>"+list[i].tempDesc+"</td>" +
                "<td>"+list[i].houseType.typeName+"</td>" +
                "<td style = 'display: none;'>"+list[i].design.id+"</td>" +
                "<td style = 'display: none;'>"+list[i].tempId+"</td>" +
                "<td>"+list[i].design.username+"</td>" +
                "<td class = 'td_asbutton'>"+showSolu_td_asButton_text+"</td>" +
                "<td class = 'td_asbutton'>"+soluToTemp_td_asButton_text+"</td>" +
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
            let soluId = $(this).children('td:hidden').eq(0).html();
            let userId = $(this).children('td:hidden').eq(1).html();
            let tempId = $(this).children('td:hidden').eq(2).html();
            var td_showSolu = $(this).children().eq(-2);
            var td_delTemp = $(this).children().eq(-1);
            td_showSolu.click(function(){
                window.location.href = "showtemplate.html?soluId=" +soluId ;
            })
            td_delTemp.click(function () {
                layui.use(['layer'], function () {
                    let layer = layui.layer;
                    let $ = layui.$;
                    layer.confirm("确定删除吗？", {icon:3, title: '提示'}, function (t) {
                        $.ajax({
                            url: '/xddesign/design/customer/template/delete'
                            ,type: 'POST'
                            ,data: {
                                "tempId": tempId
                                ,"userId": userId
                            }
                            ,dataType: 'json'
                            ,success: function (res) {
                                if (res.status === 0) {
                                    layer.msg(res.msg,{icon:5});
                                    return false;
                                }
                                window.location.reload();
                            }
                            ,error: function () {
                                layer.msg("服务器出错啦", {icon:2});
                            }
                        });
                        layer.close(t);
                    });
                });
            });
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