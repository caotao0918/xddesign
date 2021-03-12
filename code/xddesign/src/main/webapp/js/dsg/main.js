/**
 * Created by Wzz20210118 on 2021/2/18.
 */
/**
 *-----------------------------------------------------------------------设计师主页面
 */
var wcm_text = "欢迎尊贵的设计师：";
var last_time_text = "您上次登陆时间为：";
var username = window.localStorage.getItem("username");
var lastTime = window.localStorage.getItem("lastTime");

//-----------------------------------------------------------------------------跳转URL
var dataList = {'客户管理':'/dsg/listmycus.html',
    '方案管理':{
        '客户方案':'https://www.baidu.com/',
        '我的模板方案':'https://www.baidu.com/',
        '公共模板方案':'https://www.baidu.com/',
        '快速建方案':'https://www.baidu.com/',
        '自定义方案':'https://www.baidu.com/'
    },
    '查看产品':'https://www.baidu.com/'
};
$(function(){
    $('#div_wcm').html(wcm_text + username + "," + last_time_text + lastTime);

    bindLeftNav();
    function bindLeftNav() {
        var add = "<ul class='layui-nav layui-nav-tree layui-bg-cyan layui-inline' lay-filter='ul_leftnav' lay-shrink='all'>";
        for (var i in dataList) {
            if (typeof dataList[i] != 'object') {
                add += '<li class="layui-nav-item "> <a href="' + dataList[i] + '">' + i + '</a>';
                add += '</li>';
            } else {
                add += '<li class="layui-nav-item "> <a href=javascript:;>' + i + '</a>';
                add += '<dl class="layui-nav-child">';
                for (var j in dataList[i]) {
                    add += '<dd><a href="' + dataList[i][j] + '">' + j + '</a></dd>';
                }
                add += "</dl> </li>";
            }
        }
        add += "</ul>";

        $('#div_leftnav').append(add);

        //监听导航
        layui.use('element', function () {
           var element = layui.element;
           //监听导航点击
           element.on('nav(ul_leftnav)', function (elem) {
               var navTier = elem.siblings().length;
               var fstLevName = $('#div_leftnav li.layui-nav-itemed>a:first-of-type').text();

           });
        })
    }

    })