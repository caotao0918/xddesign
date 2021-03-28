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
var soluToTemp_td_asButton_text = "转为模板";

//---------------------------------------------------------url查询客户方案列表
var url_queryHouseSolu = "/xddesign/design/customer/solutions";
//----------------------------------------------------------------------------查看方案
var url_showCusSolu = "addhousesolu.html";
//----------------------------------------------------------------------------查看设计图
var url_showRnd = "editrnd.html";
//----------------------------------------------------------------------------查看报表
var url_showQuote = "quote_edit.html";
//----------------------------------------------------------------------------新增方案
// var url_addHouseSlou = "addhousesolu.html";
//只是回退页面
var url_back = "";

$(function(){
    function getData(pageNum,pageSize){
        var list;
        $.ajax({
            url:url_queryHouseSolu,
            data:{'current':pageNum,'size':pageSize},
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
        var add = "";
        for (let i = 0; i < list.length; i++) {
            add += "<tr>" +
                "<td>"+(i + 1)+"</td>" +
                "<td>"+list[i].customerName+"</td>" +
                "<td style = 'display: none;'>"+list[i].soluId+"</td>" +
                "<td>"+list[i].soluName+"</td>" +
                "<td style='display: none;'>"+list[i].houseId+"</td>" +
                "<td>"+list[i].state+"</td>" +
                "<td>"+list[i].addTime+"</td>" +
                "<td class = 'td_asbutton'>"+showSolu_td_asButton_text+"</td>" +
                "<td class = 'td_asbutton'>"+showRnd_td_asButton_text+"</td>" +
                "<td class = 'td_asbutton'>"+showQuote_td_asButton_text+"</td>" +
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
            var houseId = $(this).children('td').eq(4).html();
            var td_showSolu = $(this).children().eq(-4);
            var td_showRnd = $(this).children().eq(-3);
            var td_showQuote = $(this).children().eq(-2);
            var td_soluToTemp = $(this).children().eq(-1);
            td_showSolu.click(function(){
                location.href = url_showCusSolu
                    +"?soluId=" +soluId + "&houseId=" + houseId ;
            })
            td_showRnd.click(function(){
                location.href = url_showRnd
                    +"?soluId=" +soluId + "&soluName=" + soluName ;
            })
            td_showQuote.click(function(){
                location.href = url_showQuote
                    +"?soluId=" +soluId ;
            })
            td_soluToTemp.click(function () {
                layui.use(['layer'], function () {
                    let layer = layui.layer;
                    layer.open({
                        type: 2
                        ,title: '我的方案转模板方案'
                        ,content: 'soluToTemp.html'
                        ,area: ['500px', '480px']
                        ,btn: ['确定', '取消']
                        ,yes: function(index, layero){
                            var iframeWindow = window['layui-layer-iframe'+ index]
                                ,submit = layero.find('iframe').contents().find("#solutotemp-front-submit");

                            //监听提交
                            iframeWindow.layui.form.on('submit(solutotemp-front-submit)', function(data){
                                let field = data.field; //获取提交的字段
                                $.ajax({
                                    url: '/xddesign/design/customer/template/save'
                                    ,type: 'POST'
                                    ,data: field
                                    ,dataType: 'json'
                                    ,success: function (res) {
                                        if (res.status === 0) {
                                            layer.msg(res.msg,{icon:5});
                                            return false;
                                        }
                                        layer.close(index); //关闭弹层
                                        window.location.reload();
                                    }
                                });

                            });
                            submit.trigger('click');
                        }
                        ,success: function (layero, index) {
                            let iframe = window['layui-layer-iframe'+ index]
                            let $ = iframe.layui.$;
                            $('input[name="solutions.soluId"]').val(soluId);
                        }
                    });
                });
            });
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

    // $('#div_plus').mouseover(function(){
    //     $(this).find('img:first-child').attr('src','../../img/plus_hover.svg');
    //     $(this).find('img:last-child').attr('src','../../img/plus1hover.svg');
    // }).mouseleave(function(){
    //     $(this).find('img:first-child').attr('src','../../img/plus_.svg');
    //     $(this).find('img:last-child').attr('src','../../img/plus1.svg');
    // })
    //
    // $('#div_plus').click(function(){
    //     location.href = url_addHouseSlou;
    // })
    $('#img_back').click(function(){
        self.location=document.referrer;
    })

})
/**
 *---------------------------------------------------------------------住宅方案列表页
 */