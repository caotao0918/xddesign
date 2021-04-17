package cn.zmdxd.xddesign.common.handler;

import cn.zmdxd.xddesign.admin.service.*;
import cn.zmdxd.xddesign.common.easyexcel.MyWriteHandler;
import cn.zmdxd.xddesign.common.easyexcel.MyWriteHandler3;
import cn.zmdxd.xddesign.design.service.CustomerService;
import cn.zmdxd.xddesign.design.service.QuoteService;
import cn.zmdxd.xddesign.design.service.RenderingsService;
import cn.zmdxd.xddesign.design.service.SolutionsService;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.CookieUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.property.OnceAbsoluteMergeProperty;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹涛
 * @date 2021/1/26 9:04
 * @description: 客户模块(公共模块)
 */
@RestController
@RequestMapping(value = "/public/", produces = {"application/json;charset=UTF-8;" })
public class PublicController {

    @Autowired
    private VideoService videoService;//视频接口
    @Autowired
    private GuideService guideService;//产品手册接口
    @Autowired
    private QuestionService questionService;//常见问题接口
    @Autowired
    private SolutionsService solutionsService;//方案接口
    @Autowired
    private CustomerService customerService;//客户接口
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private RenderingsService renderingsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SecondLevelService secondLevelService;

    // 售后中心-左边侧栏 二级分类&产品列表
    @RequestMapping(value = "secondlevels/products")
    public Map<String, List<Product>> findSecondLevelList() {
        List<SecondLevel> secondLevelList = secondLevelService.list(new QueryWrapper<SecondLevel>().select("second_id", "second_name"));
        List<Product> productList;
        Map<String, List<Product>> map = new HashMap<>();
        for (SecondLevel secondLevel:secondLevelList) {
            productList = productService.list(new QueryWrapper<Product>().select("product_name","product_id").eq("second_id", secondLevel.getSecondId()));
            for (Product product:productList) {
                product.setSecondLevel(secondLevel);
            }
            map.put(secondLevel.getSecondName(), productList);
        }
        return map;
    }

    // 售后中心-常见问题
    @RequestMapping(value = "questions")
    public List<Question> findQuestionList(Integer productId) {
        if (productId == null) {
            return questionService.list();
        }
        return questionService.list(new QueryWrapper<Question>().eq("product_id", productId));
    }
    // 售后中心-安装视频
    @RequestMapping(value = "video")
    public List<Video> findVideo(Integer productId) {
        if (productId != null) {
            return videoService.list(new QueryWrapper<Video>().eq("product_id", productId));
        }
        return videoService.list();
    }
    //售后中心-产品手册
    @RequestMapping(value = "guide")
    public List<Guide> findGuideList(Integer productId) {
        if (productId == null) {
            return guideService.list();
        }
        return guideService.list(new QueryWrapper<Guide>().eq("product_id", productId));
    }

    /**
     * @description: 产品手册预览和下载
     * @param filePath:文件在服务器的存储路径
     * @param isOnLine:预览或下载 true为预览/false为下载
     */
    @RequestMapping(value="guide/download",method= RequestMethod.GET)
    public void downloadPrivacyAgreement(HttpServletResponse response, String filePath, boolean isOnLine) throws IOException {
        // 判断文件是否存在
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "该文件不存在");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len;

        // 设置页面不缓存清除首部空白行
        response.reset();
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(f.getName().getBytes("utf-8"), "ISO8859-1"));
//            response.addHeader("Content-Disposition","attachment;filename=" + new String(filename.getBytes("utf-8"),"ISO8859-1")); //文件名解决中文乱码
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }

    /**
     * @description: 查询所有产品
     * @param current:当前页
     * @param size:每页条数
     */
    @RequestMapping(value = "products", method = RequestMethod.GET)
    public Object findProducts(ProductVo productVo, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(current, size);
        String productName = productVo.getProductName();
        if (productName != null) {
            // 搜索时，去除输入的产品名称左右两边的空格
            productVo.setProductName(productName.trim());
        }
        return productService.findProducts(page, productVo);
    }

    // 根据产品id查询产品详情
    @RequestMapping(value = "product")
    public Product findProduct(Integer productId) {
        return productService.findProduct(productId);
    }


    // 客户-查询自己的信息
    @RequestMapping("customer")
    public Customer findCustomer(HttpServletRequest request) {
        Integer id = Integer.valueOf(CookieUtil.getCookieValue(request, "customerId"));
        return customerService.findCustomer(id);
    }

    // 客户-根据房子id找到方案列表
    @RequestMapping(value = "customer/house/solutions")
    public IPage<Solutions> findSolutionsByHouseId(Integer houseId, @RequestParam(value = "pageNum",defaultValue = "1") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {
        Page<Solutions> page = new Page<>(current,size);
        return solutionsService.page(page,new QueryWrapper<Solutions>().eq("house_id",houseId).select("solu_id","solu_name","state","add_time"));
    }

    // 根据方案id查询方案详情
    @RequestMapping(value = "customer/solution")
    public Solutions findSolution(Integer soluId) {
        return solutionsService.findSolutions(soluId);
    }

    /**
     * @description: 查看效果图
     * @param soluId:方案id
     * @return List
     */
    @RequestMapping(value = "customer/renderings")
    public List<Renderings> findRenderingsList(Integer soluId) {
        return renderingsService.list(new QueryWrapper<Renderings>().eq("solu_id",soluId));
    }

    // 根据方案id查看方案报价单
    @RequestMapping(value = "customer/quotes")
    public List<Quote> findQuoteList(Integer soluId) {
        List<Quote> quoteList = quoteService.list(new QueryWrapper<Quote>().eq("solu_id", soluId));
        for (Quote quote:quoteList) {
            quote.setTotalPrice(quote.getPrice()*quote.getProductNum());
        }
        return quoteList;
    }

    // 查询报价单基本信息
    @RequestMapping(value = "customer/quotes/info")
    public QuoteVo findQuoteInfo(Integer soluId) {
        return quoteService.findQuoteInfo(soluId);
    }

    // 根据报价单生成Excel
    @RequestMapping(value = "customer/quote/toexcel")
    public void quoteToExcelTest(Integer soluId, HttpServletResponse response) throws IOException {
        List<Quote> quoteList = quoteService.list(new QueryWrapper<Quote>().eq("solu_id",soluId));
        int i = 1;
        Quote quote = new Quote();
        double totalPrice = 0,price = 0;//价格总计-报价单总价
        Integer productNum = 0;
        for (Quote quote1:quoteList) {
            quote1.setId(i);//设置序号
            quote1.setTotalPrice(quote1.getPrice() * quote1.getProductNum());//每行合计
            totalPrice += quote1.getTotalPrice();//所有产品价格总计
            productNum += quote1.getProductNum();//产品总数
            price += quote1.getPrice();//单件产品价格总计
            i = i + 1;
        }

        QuoteVo quoteInfo = quoteService.findQuoteInfo(soluId);

        String templateFileName ="xlsx" + File.separator + "template.xlsx";
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("智能家居方案报价单", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(new ClassPathResource(templateFileName).getInputStream()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(quoteList, fillConfig, writeSheet);
            Map<String, Object> map = new HashMap<>();
            map.put("date", quoteInfo.getAddTime());
            map.put("quoteNum", quoteInfo.getQuoteNum());
            map.put("designName", quoteInfo.getDesignName());
            map.put("designMobile", quoteInfo.getDesignMobile());
            map.put("cusName", quoteInfo.getCusName());
            map.put("cusMobile", quoteInfo.getCusMobile());
            map.put("descr", quoteInfo.getDescr());
            // 产品合计
            map.put("total", totalPrice);
            // 施工费用称呼
            map.put("workPriceName", quoteInfo.getWorkPriceName());
            // 施工（其他）费用
            if (quoteInfo.getWorkPrice() == 0) {
                map.put("workPrice", totalPrice * 0.1);
            }else {
                map.put("workPrice", quoteInfo.getWorkPrice());
            }
            // 总价
            map.put("total2", quoteInfo.getWorkPrice() + totalPrice);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "0");
            map.put("msg", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }

    }


//    @RequestMapping(value = "")
//    public void quoteToExcel(Integer soluId, HttpServletResponse response) throws IOException {
//        List<List<String>> header = new ArrayList<>();
//
//        List<String> cellContain1 = new ArrayList<>();
//        cellContain1.add("智能家居方案报价单");
//        cellContain1.add("报价单号：11111111111");
//        cellContain1.add("设计师：xxx");
//        cellContain1.add("联系方式：xx333x");
//        cellContain1.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain1.add("序号");
//        header.add(cellContain1);
//        List<String> cellContain1_2 = new ArrayList<>();
//        cellContain1_2.add("智能家居方案报价单");
//        cellContain1_2.add("报价单号：11111111111");
//        cellContain1_2.add("设计师：xxx");
//        cellContain1_2.add("联系方式：xx333x");
//        cellContain1_2.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain1_2.add("房间名称");
//        header.add(cellContain1_2);
//        List<String> cellContain1_3 = new ArrayList<>();
//        cellContain1_3.add("智能家居方案报价单");
//        cellContain1_3.add("报价单号：11111111111");
//        cellContain1_3.add("设计师：xxx");
//        cellContain1_3.add("联系方式：xx333x");
//        cellContain1_3.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain1_3.add("产品名称");
//        header.add(cellContain1_3);
//
//        List<String> cellContain2 = new ArrayList<>();
//        cellContain2.add("智能家居方案报价单");
//        cellContain2.add("日期：2021-04-21");
//        cellContain2.add("客户：xxx");
//        cellContain2.add("联系方式：x444444xx");
//        cellContain2.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain2.add("产品数量/个");
//        header.add(cellContain2);
//        List<String> cellContain2_2 = new ArrayList<>();
//        cellContain2_2.add("智能家居方案报价单");
//        cellContain2_2.add("日期：2021-04-21");
//        cellContain2_2.add("客户：xxx");
//        cellContain2_2.add("联系方式：x444444xx");
//        cellContain2_2.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain2_2.add("产品单价/元");
//        header.add(cellContain2_2);
//        List<String> cellContain2_3 = new ArrayList<>();
//        cellContain2_3.add("智能家居方案报价单");
//        cellContain2_3.add("日期：2021-04-21");
//        cellContain2_3.add("客户：xxx");
//        cellContain2_3.add("联系方式：x444444xx");
//        cellContain2_3.add("以下为智能家居方案产品明细，请详阅，如有疑问及时与设计师联系，谢谢");
//        cellContain2_3.add("合计/元");
//        header.add(cellContain2_3);
//
//        List<Quote> quoteList = quoteService.list(new QueryWrapper<Quote>().eq("solu_id",soluId));
//        int i = 1;
//        Quote quote = new Quote();
//        double totalPrice = 0,price = 0;//价格总计-报价单总价
//        Integer productNum = 0;
//        for (Quote quote1:quoteList) {
//            quote1.setId(i);//设置序号
//            quote1.setTotalPrice(quote1.getPrice() * quote1.getProductNum());//每行合计
//            totalPrice += quote1.getTotalPrice();//所有产品价格总计
//            productNum += quote1.getProductNum();//产品总数
//            price += quote1.getPrice();//单件产品价格总计
//            i = i + 1;
//        }
//        quote.setRoomName("价格总计");
//        quote.setId(quoteList.size()+1);
//        quote.setPrice(price);
//        quote.setProductNum(productNum);
//        quote.setProductName("");
//        quote.setTotalPrice(totalPrice);
//        quoteList.add(quote);//额外增加一行总计
//        quote.setRoomName("备注");
//        quoteList.add(quote);// 额外增加一行备注
//        try {
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            // 这里URLEncoder.encode可以防止中文乱码
//            String fileName = URLEncoder.encode("方案报价表", "UTF-8").replaceAll("\\+", "%20");
//            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//
//            //设置合并单元格
//            OnceAbsoluteMergeProperty onceAbsoluteMergeProperty = new OnceAbsoluteMergeProperty(quoteList.size()+5, quoteList.size()+5, 1, 4);
//            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty);
//
//            // 这里需要设置不关闭流
//            EasyExcel.write(response.getOutputStream(), Quote.class).autoCloseStream(Boolean.FALSE)
//                    .head(header)
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
//                    .registerWriteHandler(onceAbsoluteMergeStrategy)//合并单元格
////                    .registerWriteHandler(new MyWriteHandler2())//自定义头拦截器
//                    .registerWriteHandler(new MyWriteHandler())//自定义内容拦截器
////                    .registerWriteHandler(new MyWriteHandler3())
//                    .sheet("方案报价表")
//                    .doWrite(quoteList);
//        } catch (Exception e) {
//            // 重置response
//            response.reset();
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//            Map<String, String> map = new HashMap<>();
//            map.put("status", "0");
//            map.put("msg", "下载文件失败" + e.getMessage());
//            response.getWriter().println(JSON.toJSONString(map));
//        }
//
//    }


}
