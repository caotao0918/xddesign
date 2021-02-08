package cn.zmdxd.xddesign.common.handler;

import cn.zmdxd.xddesign.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wzz20210118 on 2021/1/25.
 */
@Controller
public class MyController {

    @RequestMapping("/gotoCusLogin")
    public String gotoCustomerLogin(){
        return "login/cuslogin";
    }

    @RequestMapping("/gotoCusMain")
    public String gotoCustomerMain(){
        return "cus/main";
    }

    @RequestMapping("/gotoMyHouseList")
    public String gotoMyHouseList(){
        return "cus/myhouselist";
    }

    @RequestMapping("/queryMyHouseList")
    @ResponseBody
    public List<House> queryMyHouseList() {
       return getMyHouseList();
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public List<House>  getMyHouseList(){
        Product product_1 = new Product();
        product_1.setProductId(1);
        product_1.setProductName("筒灯A款");
        Product product_2 = new Product();
        product_2.setProductId(2);
        product_2.setProductName("筒灯A款");
        Product product_3 = new Product();
        product_3.setProductId(3);
        product_3.setProductName("筒灯B款");

        Product product_4 = new Product();
        product_4.setProductId(4);
        product_4.setProductName("加湿器");

        ProductNum productNum_1 = new ProductNum();
        productNum_1.setPnId(1);
        productNum_1.setPnNum(2);
        productNum_1.setProduct(product_1);
        productNum_1.setPnId(2);
        productNum_1.setPnNum(1);
        productNum_1.setProduct(product_2);

        ProductNum productNum_2 = new ProductNum();
        productNum_2.setPnId(2);
        productNum_2.setPnNum(1);
        productNum_2.setProduct(product_3);

        List<ProductNum> productNumList = new ArrayList<ProductNum>(10);
        productNumList.add(productNum_1);
        productNumList.add(productNum_2);

        Room room_1 = new Room();
        room_1.setRoomId(1);
        room_1.setProductNumList(productNumList);

        Room room_2 = new Room();
        room_2.setRoomId(2);
        room_2.setProductNumList(productNumList);

        List<Room> roomList = new ArrayList<Room>(10);
        roomList.add(room_1);
        roomList.add(room_2);

        Solutions solutions_1 = new Solutions();
        solutions_1.setSoluId(1);
        solutions_1.setSoluDesc("一房一厅1号方案");
        solutions_1.setRoomList(roomList);

        Solutions solutions_2 = new Solutions();
        solutions_2.setSoluId(2);
        solutions_2.setSoluDesc("一房一厅2号方案");
        solutions_2.setRoomList(roomList);

        List<Solutions> solutionsList = new ArrayList<Solutions>(10);
        solutionsList.add(solutions_1);
        solutionsList.add(solutions_2);

        House house_1 = new House();
        house_1.setHouseId(1);
        house_1.setHouseName("我的老家");
        house_1.setHouseAddress("朝阳区1环1巷1号");
//        house_1.setHouseType("一房一厅");
        house_1.setSolutionsList(solutionsList);

        House house_2 = new House();
        house_2.setHouseId(2);
        house_2.setHouseName("上班住房");
        house_2.setHouseAddress("清水湾复式单元11号");
//        house_2.setHouseType("两房一厅");
        house_2.setSolutionsList(solutionsList);

        List<House> houseList = new ArrayList<House>(10);
        houseList.add(house_1);
        houseList.add(house_2);

        return houseList;
    }

    @RequestMapping("/gotoListSolu")
    public String gotoListSolu(ModelMap mm){
        mm.addAttribute("cusId", 1);
        mm.addAttribute("cusName", "张一山");
        return "cus/listsolu";
    }

    @RequestMapping("/queryHouseSolu")
    @ResponseBody
    public  Map<String,Object> queryHouseSolu(Integer pageNum,Integer pageSize){
        System.out.println("-----------------" + pageNum + "-----------------");
        System.out.println("-----------------" + pageSize + "-----------------");
        Map<String,Object> map = new HashMap<String, Object>(10);
        map.put("count",19);
        if(pageNum ==1){
            map.put("list",getHouseSolu_page_1());
            return map;
        }else{
            map.put("list",getHouseSolu_page_2());
            return map;
        }
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public   List<Solutions>  getHouseSolu_page_1(){
        List<Solutions> solutionsList = new ArrayList<Solutions>(10);
        for(int i = 0;i<10;i++){
            Solutions solutions_1 = new Solutions();
            solutions_1.setSoluId( i + 1);
            solutions_1.setSoluName("一房一厅1号方案");
            solutions_1.setState("设计完毕");
            solutions_1.setAddTime(new Timestamp(1611906506161L));
            solutionsList.add(solutions_1);
        }
        return solutionsList;
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public  List<Solutions>  getHouseSolu_page_2(){
        List<Solutions> solutionsList = new ArrayList<Solutions>(10);
        for(int i = 0;i<9;i++){
            Solutions solutions_1 = new Solutions();
            solutions_1.setSoluId( i + 10 +1);
            solutions_1.setSoluName("一房一厅111号方案");
            solutions_1.setState("施工中");
            solutions_1.setAddTime(new Timestamp(1611906506161L));
            solutionsList.add(solutions_1);
        }
        return solutionsList;
    }

    @RequestMapping("/gotoShowCusSolu")
    public String gotoShowCusSolu(Integer soluId, ModelMap mm){
        System.out.println("---------" + soluId);
        mm.addAttribute("solu", getSolu());
        return "cus/showcussolu";
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public Solutions getSolu(){
        Picture picture_1 = new Picture();
        picture_1.setPictureId(601);
        picture_1.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/eeb94e7fed66407998860b9e83a6ff91.jpg");
        Picture picture_2 = new Picture();
        picture_2.setPictureId(602);
        picture_2.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/21f35f840c8a46099288fbcc80080a29.jpg");

        List<Picture> pictureList_1 = new ArrayList<Picture>(10);
        pictureList_1.add(picture_1);
        pictureList_1.add(picture_2);

        Picture picture_3 = new Picture();
        picture_3.setPictureId(603);
        picture_3.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/d58e092e89054e1bbd60580f1c604d37.jpg");
        List<Picture> pictureList_3 = new ArrayList<Picture>(10);
        pictureList_3.add(picture_3);

        Picture picture_4 = new Picture();
        picture_4.setPictureId(604);
        picture_4.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/9/11bff8db99ba4d54ba0f6a75ba0ca651.jpg");
        List<Picture> pictureList_4 = new ArrayList<Picture>(10);
        pictureList_4.add(picture_4);


        Product product_1 = new Product();
        product_1.setProductId(1);
        product_1.setProductName("筒灯A款");
        product_1.setPictureList(pictureList_1);
        Product product_2 = new Product();
        product_2.setProductId(2);
        product_2.setProductName("筒灯B款");
        product_2.setPictureList(pictureList_3);

        Product product_3 = new Product();
        product_3.setProductId(3);
        product_3.setProductName("加湿器");
        product_3.setPictureList(pictureList_4);

        ProductNum productNum_1 = new ProductNum();
        productNum_1.setPnId(11);
        productNum_1.setPnNum(1);
        productNum_1.setProduct(product_1);
        ProductNum productNum_2 = new ProductNum();
        productNum_2.setPnId(12);
        productNum_2.setPnNum(3);
        productNum_2.setProduct(product_2);

        ProductNum productNum_3 = new ProductNum();
        productNum_3.setPnId(13);
        productNum_3.setPnNum(1);
        productNum_3.setProduct(product_3);

        List<ProductNum> productNumList_1 = new ArrayList<ProductNum>(10);
        productNumList_1.add(productNum_1);
        productNumList_1.add(productNum_2);

        List<ProductNum> productNumList_3 = new ArrayList<ProductNum>(10);
        productNumList_3.add(productNum_3);

        List<Room> roomList = new ArrayList<Room>(10);
        Room room_1 = new Room();
        room_1.setRoomId(441);
        room_1.setRoomName("客厅");
        room_1.setProductNumList(productNumList_1);
        roomList.add(room_1);

        for(int i = 0;i<8;i++) {
            Room room_2 = new Room();
            room_2.setRoomId(441 + i + 1);
            room_2.setRoomName("小黑屋");
            room_2.setProductNumList(productNumList_3);
            roomList.add(room_2);
        }

        Solutions solutions_1 = new Solutions();
        solutions_1.setSoluName("一房一厅1号方案");
        solutions_1.setState("设计完毕");
        solutions_1.setRoomList(roomList);

        return solutions_1;
    }

    @RequestMapping("/gotoShowRnd")
    public String gotoShowRnd(Integer soluId, ModelMap mm){
        mm.addAttribute("soluId",soluId);
        return "cus/showrnd";
    }

    @RequestMapping("/querySoluImg")
    @ResponseBody
    public List<Renderings> querySoluImg(Integer soluId){
        //测试数据
        soluId = 100;
        return getSoluImgList();
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public List<Renderings> getSoluImgList(){
        List<Renderings> list = new ArrayList<Renderings>(5);
        for(int i = 0;i<3;i++) {
            Renderings rng = new Renderings();
            rng.setRendName("        一房一厅A方案设计图1/3号\n");
            rng.setRendDesc("    一房一厅A方案设计图1/3号,全房共有3个独立房间，每个房间放置3-5个智能家居产品，报价" + "2000RMB，经济实用，物美价廉。----------------------------------" +
                    "---------------------------------\n" +
                    "--------------------------------------" +

                    "    一房一厅A方案设计图1/3号,全房共有3个独立房间，每个房间放置3-5个智能家居产品，报价" + "2000RMB，经济实用，物美价廉。----------------------------------" +
                    "---------------------------------\n" +
                    "--------------------------------------"+

                    "    一房一厅A方案设计图1/3号,全房共有3个独立房间，每个房间放置3-5个智能家居产品，报价" + "2000RMB，经济实用，物美价廉。----------------------------------" +
                    "---------------------------------\n" +
                    "--------------------------------------");
            rng.setRendPath("http://www.zmdxd.cn:8089/xddz\\images\\2019\\5\\21\\0665a531fd4342fe9ff9a3095d1a862a.thumb.jpg");
            list.add(rng);
        }
        return list;
    }

    @RequestMapping("/gotoShowProd")
    public String gotoShowProd(Integer prodId, ModelMap mm){
        //测试数据
        prodId = 100;
        mm.addAttribute("prodId",prodId);
        return "cus/showprod";
    }

    @RequestMapping("/queryProdProp")
    @ResponseBody
    public Product queryProdProp(Integer prodId){
        return getProdProp();
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public Product getProdProp(){
        Product prod = new Product();
        prod.setProductDesc("    该产品设计简约流畅，光源可自适应调节，以满足" +
                "消费者的不同需求。\n" +
                "    需要打孔15CM，直插220V电源，可连续使用20000小时。\n" +
                "    价格便宜。可广泛用于楼道、休闲娱乐场所、走廊、酒店客" +
                "房、酒店大堂、展厅、家居等场所。" +
                "    该产品设计简约流畅，光源可自适应调节，以满足" +
                "消费者的不同需求。\n" +
                "    需要打孔15CM，直插220V电源，可连续使用20000小时。\n" +
                "    价格便宜。可广泛用于楼道、休闲娱乐场所、走廊、酒店客" +
                "房、酒店大堂、展厅、家居等场所。" +
                "    该产品设计简约流畅，光源可自适应调节，以满足" +
                "消费者的不同需求。\n" +
                "    需要打孔15CM，直插220V电源，可连续使用20000小时。\n" +
                "    价格便宜。可广泛用于楼道、休闲娱乐场所、走廊、酒店客" +
                "房、酒店大堂、展厅、家居等场所。" +
                "    该产品设计简约流畅，光源可自适应调节，以满足" +
                "消费者的不同需求。\n" +
                "    需要打孔15CM，直插220V电源，可连续使用20000小时。\n" +
                "    价格便宜。可广泛用于楼道、休闲娱乐场所、走廊、酒店客" +
                "房、酒店大堂、展厅、家居等场所。" +
                "    该产品设计简约流畅，光源可自适应调节，以满足" +
                "消费者的不同需求。\n" +
                "    需要打孔15CM，直插220V电源，可连续使用20000小时。\n" +
                "    价格便宜。可广泛用于楼道、休闲娱乐场所、走廊、酒店客" +
                "房、酒店大堂、展厅、家居等场所。");
        Picture picture_1 = new Picture();
        picture_1.setPictureId(601);
        picture_1.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/eeb94e7fed66407998860b9e83a6ff91.jpg");
        Picture picture_2 = new Picture();
        picture_2.setPictureId(602);
        picture_2.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/21f35f840c8a46099288fbcc80080a29.jpg");
        Picture picture_3 = new Picture();
        picture_3.setPictureId(603);
        picture_3.setPictureLink("http://www.zmdxd.cn:8089//xddz/ueditor/2020/11/5/d58e092e89054e1bbd60580f1c604d37.jpg");

        List<Picture> pictureList_1 = new ArrayList<Picture>(10);
        pictureList_1.add(picture_1);
        pictureList_1.add(picture_2);
        pictureList_1.add(picture_3);
        prod.setPictureList(pictureList_1);

        List<ProductProperty> list = new ArrayList<ProductProperty>(18);

        ProductProperty pp_1 = new ProductProperty();
        PropertyValue pv_1 = new PropertyValue();
        Property p_1 = new Property();

        p_1.setPropertyName("产品名称");
        p_1.setCommonValue("智能筒灯A款");
        pv_1.setProperty(p_1);
        pp_1.setValue(pv_1);
        list.add(pp_1);

        for(int i = 0;i<13;i ++){
            ProductProperty pp = new ProductProperty();
            PropertyValue pv = new PropertyValue();
            Property p = new Property();
            p.setPropertyName("功率");
            p.setCommonValue("5W");
            pv.setProperty(p);
            pp.setValue(pv);
            list.add(pp);
        }

        prod.setProductPropertyList(list);
        return prod;
    }


    @RequestMapping("/gotoShowQuote")
    public String gotoShowQuote(Integer soluId, ModelMap mm){
        System.out.println("---------" + soluId);
        return "cus/showquote";
    }

    @RequestMapping("/queryQuoteList")
    @ResponseBody
    public List<Quote> queryQuoteList(Integer soluId){
        return getQuoteList();
    }

    //--------------------------------------------------------测试数据，需要改成真的
    public List<Quote> getQuoteList(){
        List<Quote> list = new ArrayList<Quote>(10);
        Quote quote_1 = new Quote();
        quote_1.setRoomName("大门");
        quote_1.setProductName("迅达智能电子锁S9");
        quote_1.setProductNum(1);
        quote_1.setPrice((double) 3000);
        quote_1.setTotalPrice((double) 3000);

        Quote quote_2 = new Quote();
        quote_2.setRoomName("客厅");
        quote_2.setProductName("WiFi_智能筒灯A款");
        quote_2.setProductNum(5);
        quote_2.setPrice((double) 200);
        quote_2.setTotalPrice((double) 1000);

        Quote quote_3 = new Quote();
        quote_3.setRoomName("客厅");
        quote_3.setProductName("ZigBee_智能筒灯Vip款");
        quote_3.setProductNum(2);
        quote_3.setPrice((double) 50);
        quote_3.setTotalPrice((double) 100);

        Quote quote_4 = new Quote();
        quote_4.setRoomName("客厅");
        quote_4.setProductName("ZigBee_智能筒灯Plus款");
        quote_4.setProductNum(2);
        quote_4.setPrice((double) 50);
        quote_4.setTotalPrice((double) 100);

        list.add(quote_1);
        list.add(quote_2);
        list.add(quote_3);
        list.add(quote_4);
        return list;
    }

}
