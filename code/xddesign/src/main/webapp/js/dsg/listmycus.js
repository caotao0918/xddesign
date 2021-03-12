/**
 * Created by Wzz20210118 on 2021/2/18.
 */
/**
 * ----------------------------------------------------------------------我的客户列表
 */
//总条数默认值
var countDflt = 100;
//总条数真实值
var countReal;
//pageSize选择
var array_pageSize_myCus = [10,20,30];
//默认pageSize
var pageSizeDflt = 10;
var dsgName = window.localStorage.getItem("username");

$(function() {
    $('#div_plus').mouseover(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_hover.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1hover.svg');
    }).mouseleave(function(){
        $(this).find('img:first-child').attr('src','../../img/plus_.svg');
        $(this).find('img:last-child').attr('src','../../img/plus1.svg');
    })

    $('#div_plus').click(function(){
        //----------------------------------------------------------跳转到添加客户页面
        location.href = '/dsg/addcus.html ';
    })

    function getData(pageNum,pageSize){
        var list;
        $.ajax({
            //----------------------------------------------------------url查询设计师的客户
            url:"/xddesign/design/customers",
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

    function bindData(list) {
        $('#t_cus').children().remove();
        var add = "";
        for (var i = 0; i < list.length; i++) {
            add += "<tr>" +
                "<td>" + (i + 1) + "</td>" +
                "<td>" + list[i].username + "</td>" +
                "<td style = 'display: none;'>" + list[i].id + "</td>" +
                "<td>" + list[i].desc + "</td>" +
                "<td>" + list[i].mobile + "</td>" +
                "<td>" + list[i].address + "</td>" +
                "<td>" + dsgName + "</td>" +
                "<td></td>" +
                "</tr>";
        }
        $('#t_cus').append(add);
        $('#t_cus tr td:last-of-type').click(function(){
            lightbox_delCfm();
            var cusId = $(this).siblings(':hidden').html();
            $('.layui-layer-btn0').click(function() {
                var rst = delCus(cusId);
                var isReload = false;
                if(1 == rst.status){
                    isReload = true;
                }
                lightbox_tip_delAndRld(rst.msg,isReload);
            })
        })

        $('#t_cus tr td:not(:last-of-type)').click(function(){
            var cusId = $(this).siblings(':hidden').html();
            var cusName = $(this).siblings(':hidden').prev().html();
            window.localStorage.setItem("cusId",cusId);
            window.localStorage.setItem("cusName",cusName);
            //------------------------------------------------------------修改客户信息页
            location.href = "/dsg/updcus.html";
        })



    }

    function delCus(cusId){
        var rst;
        $.ajax({
            //----------------------------------------------------------删除客户
            url:"/xddesign/design/customer/del",
            data:{'id':cusId},
            dataType:"json",
            type:"POST",
            async:false,
            success:function(json) {
                rst =  json;
            }
        })
        return rst;
    }

    function pageRnd(divId,count){
        layui.use(['laypage', 'layer'], function() {
            var laypage = layui.laypage;
            laypage.render({
                elem: divId
                ,count : count
                , limit:pageSizeDflt
                ,limits:array_pageSize_myCus
                , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
                , jump: function (obj) {
                    var list = getData(obj.curr,obj.limit);
                    //第一次进页面和增删的时候，刷新总条数
                    if(countReal != count) {
                        pageRnd('div_pagernd', countReal);
                    }
                    bindData(list);
                    $('#div_pagernd').css('width',$('#t_cus tr:first').css('width'));
                }
            });
        })
    }
    pageRnd('div_pagernd',countDflt);

    $('#img_back').click(function(){
        location.href = "main.html";
    })
})