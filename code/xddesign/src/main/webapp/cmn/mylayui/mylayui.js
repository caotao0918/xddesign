/**
 * Created by Wzz20210118 on 2021/1/27.
 */
var shade_tip = 0.4;
var area_tip = ['300px','170px'];
var msgFontsize_tip =  '24px';
var btnFontsize_tip = "18px";
/**
 *提示灯箱
 */
function lightbox_tip(msg){
    layer.msg(msg, {
        //10s后自动关闭
        time: 10000,
        //time: 100000000,
        btn: [ '知道了'],
        shade:shade_tip,
        area:area_tip,
        success: function(layero){
            layero.find('.layui-layer-content').css('display', 'flex');
            layero.find('.layui-layer-content').css('align-items', 'center');
            layero.find('.layui-layer-content').css('justify-content', 'center');
            layero.find('.layui-layer-content').css('text-align', 'left');
            layero.find('.layui-layer-content').css('font-size', msgFontsize_tip);
            layero.find('.layui-layer-btn').css('text-align', 'center');
            layero.find('.layui-layer-btn').css('font-size', btnFontsize_tip);
        }
    })
}

var area_tag = ['500px','250px'];
/**
 * 说明标签
 */
function tag_desc(msg,posX,posY){
    layer.msg(msg, {
        //10s后自动关闭
        time: 10000,
        //time: 100000000,
        btn: [ '知道了'],
        area:area_tag,
        success: function(layero){
            layero.css('margin-left',posX + 'px');
            layero.css('margin-top',posY + 'px');
            layero.find('.layui-layer-content').css('display', 'flex');
            layero.find('.layui-layer-content').css('align-items', 'center');
            layero.find('.layui-layer-content').css('justify-content', 'center');
            layero.find('.layui-layer-content').css('text-align', 'left');
            layero.find('.layui-layer-content').css('font-size', msgFontsize_tip);
            layero.find('.layui-layer-content').css('overflow-y', 'hidden');
            layero.find('.layui-layer-btn').css('text-align', 'center');
            layero.find('.layui-layer-btn').css('font-size', btnFontsize_tip);
        }
    })
}

/**
 * ----------------------------------layui demo 谁不会就看这些，或者去官网看演示
 */
layui.use('layer', function(){ //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

    //触发事件
    var active = {
        setTop: function(){
            var that = this;
            //多窗口模式，层叠置顶
            layer.open({
                type: 2 //此处以iframe举例
                ,title: '当你选择该窗体时，即会在最顶端'
                ,area: ['390px', '260px']
                ,shade: 0
                ,maxmin: true
                ,offset: [ //为了演示，随机坐标
                    Math.random()*($(window).height()-300)
                    ,Math.random()*($(window).width()-390)
                ]
                ,content: '//layer.layui.com/test/settop.html'
                ,btn: ['继续弹出', '全部关闭'] //只是为了演示
                ,yes: function(){
                    $(that).click();
                }
                ,btn2: function(){
                    layer.closeAll();
                }

                ,zIndex: layer.zIndex //重点1
                ,success: function(layero){
                    layer.setTop(layero); //重点2
                }
            });
        }
        ,confirmTrans: function(){
            //配置一个透明的询问框
            layer.msg('大部分参数都是可以公用的<br>合理搭配，展示不一样的风格', {
                time: 20000, //20s后自动关闭
                btn: ['明白了', '知道了', '哦']
            });
        }
        ,notice: function(){
            //示范一个公告层
            layer.open({
                type: 1
                ,title: false //不显示标题栏
                ,closeBtn: false
                ,area: '300px;'
                ,shade: 0.8
                ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                ,btn: ['火速围观', '残忍拒绝']
                ,btnAlign: 'c'
                ,moveType: 1 //拖拽模式，0或者1
                ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">你知道吗？亲！<br>layer ≠ layui<br><br>layer只是作为Layui的一个弹层模块，由于其用户基数较大，所以常常会有人以为layui是layerui<br><br>layer虽然已被 Layui 收编为内置的弹层模块，但仍然会作为一个独立组件全力维护、升级。<br><br>我们此后的征途是星辰大海 ^_^</div>'
                ,success: function(layero){
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').attr({
                        href: 'http://www.layui.com/'
                        ,target: '_blank'
                    });
                }
            });
        }
        ,offset: function(othis){
            var type = othis.data('type')
                ,text = othis.text();

            layer.open({
                type: 1
                ,offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
                ,id: 'layerDemo'+type //防止重复弹出
                ,content: '<div style="padding: 20px 100px;">'+ text +'</div>'
                ,btn: '关闭全部'
                ,btnAlign: 'c' //按钮居中
                ,shade: 0 //不显示遮罩
                ,yes: function(){
                    layer.closeAll();
                }
            });
        }
    };

    $('#layerDemo .layui-btn').on('click', function(){
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });

})
/**
 * ----------------------------------layui demo 谁不会就看这些，或者去官网看演示
 */
/**
 * -------------------------------------------------------------------------------分页
 */
/**
 * LayUI自带表格+分页 他的表格太丑没用
 */
//function tableRender(divId,url,tableTitle_text,pageRenderId){
//    layui.use(['table','element'], function(){
//        var table = layui.table;
//        var element = layui.element;
//        var laypage = layui.laypage;
//
//
//        //执行一个 table 实例
//        table.render({
//            elem: '#'+divId
//            ,height: 420
//            ,url: url //数据接口
//            ,request : {
//                pageName : 'pageNum', // 页码的参数名称，默认：page
//                limitName : 'pageSize' // 每页数据量的参数名，默认：limit
//            }
//            ,limit:10
//            ,limits:[10,20,30]
//            ,method: 'post'
//            ,title: tableTitle_text
//            ,page: {
//                //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
//                elem: pageRenderId
//                , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
//            }
//            ,toolbar: 'default' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
//            ,totalRow: false //开启合计行
//            ,cols: [[ //表头
//                {type: 'checkbox', fixed: 'left'}
//                ,{field: 'soluId', title: 'ID', width:80, sort: true, fixed: 'left'}
//                ,{field: 'soluDesc', title: '方案描述', width:80}
//                //,{fixed: 'right', width: 165, align:'center', toolbar: '#barDemo'}
//            ]],done:function(res){
//                console.log(res);
//            }
//        });
//    })
//}
///**
// * 分页
// */
//function pageRender(divId,count){
//    layui.use(['laypage', 'layer'], function() {
//        var laypage = layui.laypage;
//        laypage.render({
//            elem: divId
//            ,count : count
//            , limit:10
//            , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
//            , jump: function (obj) {
//                console.log(obj);
//                getData(obj.curr,obj.limit);
//            }
//        });
//    })
//}
/**
 * -------------------------------------------------------------------------------分页
 */
/**
 * -----------------------------------------------------------------------crsl图片轮播
 */
/**
 * 产品图片轮播
 * @type {string}
 */
var prodImgWidth_crsl = '341px';
var prodImgHgt_crsl = '211px';
var prodImgInvl_crsl = 2000;
function crsl_prodImg(divId) {
    layui.use(['carousel'], function () {
        var crsl = layui.carousel;
        crsl.render({
            elem: '#' + divId
            , width: prodImgWidth_crsl
            , height: prodImgHgt_crsl
            , autoplay: true
            , interval: prodImgInvl_crsl
            , anim: 'fade'
            , arrow: 'hover'
            , indicator: 'inside'
        });
        //事件
//      carousel.on('change(test10)', function(res){
//        alert(123);
//      });
    })
}
/**
 * -----------------------------------------------------------------------crsl图片轮播
 */
/**
 * ---------------------------------------------------------------------------菜单导航
 */
function leftNavChk(ulId){
    var $chkNode;
    layui.use('element', function(){
        //导航的hover效果、二级菜单等功能，需要依赖element模块
        var element = layui.element;
        //监听导航点击
        element.on('nav(' +ulId+')', function(elem){
            $chkNode = elem;
            return $chkNode;
    //      layer.msg(elem.text());
        });
    });
    }
/**
 * ---------------------------------------------------------------------------菜单导航
 */
/**
 * -----------------------------------------------------------------------客户类型灯箱
 */
var lightbox_area_cusType = ['300px', '170px'];
var lightbox_offset_cusType = ['200px','83px'];
function lightbox_cusType(add){
    var index = layer.open({
        type: 1,
        skin: 'layui-layer-lan',
        area: lightbox_area_cusType,
        offset:lightbox_offset_cusType,
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true,
        success: function(layero){
            $('.layui-layer-title').remove();
            $('.layui-layer-content').remove();
            layero.append(add);
            $('.layui-layer>div').click(function(){
                cusType = $(this).text();
                $('#div_custype').prev().text(cusType);
                $('#div_custype').prev().addClass('div_custypechk');
                layer.close(index);
            })
        }
    });
}
/**
 * -----------------------------------------------------------------------客户类型灯箱
 */
/**
 * -----------------------------------------------------------------------删除确认灯箱
 */
var lightbox_area_delCfm = ['300px', '170px'];
var del_tip = '确认删除？';
var del_cfm_text = "删除";
var del_cnl_text = "取消";
function lightbox_delCfm(){
    var myIndex = layer.open({
        type: 1,
        skin: 'layui-layer-lan',
        area: lightbox_area_delCfm,
        closeBtn: 0, //不显示关闭按钮
        anim: 1,
        btn:[del_cfm_text,del_cnl_text],
        btn1: function(index, layero){
            layer.close(myIndex);
        },
        btn2: function(index, layero){
            layer.close(myIndex);
        },
        btnAlign: 'c',
        content:del_tip,
        shadeClose: true,
        success: function(layero){
            layero.find('.layui-layer-content').css('display', 'flex');
            layero.find('.layui-layer-content').css('align-items', 'center');
            layero.find('.layui-layer-content').css('justify-content', 'center');
            layero.find('.layui-layer-content').css('text-align', 'left');
            layero.find('.layui-layer-content').css('font-size', msgFontsize_tip);
            layero.find('.layui-layer-btn').css('text-align', 'center');
            layero.find('.layui-layer-btn').css('font-size', btnFontsize_tip);
            layero.find('.layui-layer-btn').css('display', 'flex');
            layero.find('.layui-layer-btn').css('align-items', 'center');
            layero.find('.layui-layer-btn').css('justify-content', 'space-around');
        }
    });
}
/**
 * -----------------------------------------------------------------------删除确认灯箱
 */
/**
 * ----------------------------------------------------删除提示灯箱，提示后刷新页面
 */
function lightbox_tip_delAndRld(msg,isReload){
    layer.msg(msg, {
        //10s后自动关闭
        time: 10000,
        //time: 100000000,
        btn: [ '知道了'],
        shade:shade_tip,
        area:area_tip,
        success: function(layero){
            layero.find('.layui-layer-content').css('display', 'flex');
            layero.find('.layui-layer-content').css('align-items', 'center');
            layero.find('.layui-layer-content').css('justify-content', 'center');
            layero.find('.layui-layer-content').css('text-align', 'left');
            layero.find('.layui-layer-content').css('font-size', msgFontsize_tip);
            layero.find('.layui-layer-btn').css('text-align', 'center');
            layero.find('.layui-layer-btn').css('font-size', btnFontsize_tip);
        },
        end:function(){
            if(isReload) {
                location.reload(true);
            }
        }
    })
}
/**
 * ----------------------------------------------------删除提示灯箱，提示后刷新页面
 */
/**
 * ----------------------------------------------------------------产品添加单确认灯箱
 */
var lightbox_area_chkAddProdList = '700px';
var lightbox_offset_chkAddProdList = '100px';
function lightbox_chkAddProdList(add){
    var index = layer.open({
        type: 1,
        skin: 'layui-layer-hei',
        area: lightbox_area_chkAddProdList,
        offset:lightbox_offset_chkAddProdList,
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true,
        fixed:false,
        success: function(layero){
            $('.layui-layer-title').remove();
            $('.layui-layer-content').remove();
            layero.append(add);
            $('.layui-layer-hei>div:last-of-type>div:first-of-type').click(function(){
                layer.close(index);
            })
        }
    });
}
/**
 * ---------------------------------------------------------------产品添加单确认灯箱
 */