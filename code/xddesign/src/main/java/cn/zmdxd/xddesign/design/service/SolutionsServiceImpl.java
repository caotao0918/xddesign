package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.CookieUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 曹涛
 * @date 2021/1/21 15:47
 * @description:
 */
@Service
public class SolutionsServiceImpl extends ServiceImpl<SolutionsDao, Solutions> implements SolutionsService {

    @Autowired
    private SolutionsDao solutionsDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ProductNumDao productNumDao;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private TemplateDao templateDao;

//    @Override
//    public Integer saveSolutions(Solutions solutions) {
//        solutions.setState(SolutionsStateEnum.DESIGNING.getMsg());
//        solutions.setAddTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//        return solutionsDao.insertSolutions(solutions);
//    }

    @Override
    public IPage<Solutions> findSolutionsList(Page<Solutions> page, Solutions solutions) {
        return solutionsDao.selectSolutionsList(page,solutions);
    }

    @Override
    public Solutions findSolutions(Integer soluId) {
        return solutionsDao.selectSolutions(soluId);
    }

    @Override
    public List<Solutions> findSolutionsByHouseId(Integer houseId) {
        return solutionsDao.selectSolutionsByHouseId(houseId);
    }

    //list去重
    public static List<ProductNum> getProductNumList(List<ProductNum> list) {
        Map<Integer, ProductNum> map = new HashMap<>();
        int num;
        for (ProductNum pn : list) {
            Integer id = pn.getProduct().getProductId();
            if (map.get(id) != null) {
                num = map.get(id).getPnNum() + 1;
            } else {
                num = pn.getPnNum();
            }
            pn.setPnNum(num);
            map.put(id, pn);
        }
        Collection<ProductNum> values = map.values();
        return new ArrayList<>(values);
    }

    @Override
    public ReturnResult saveOrUpdateSolution(Solutions solutions, HttpServletRequest request) {
        ReturnResult result = new ReturnResult();
        Template template = null;
        if (solutions.getSoluId() != null) {

            //删除方案之前要先得到该方案对应的模板方案信息
            template = templateDao.selectTemplateBySoluId(solutions.getSoluId());

            /*
            删除此id对应的方案
            因为数据库中设置的级联删除，所以这个方案的房间信息、房间内的产品信息、方案对应的报价单信息也一并删除
             */
            int removeById = solutionsDao.deleteById(solutions.getSoluId());
            if (removeById != 1) {
                result.setStatus(0);
                result.setMsg("添加失败，请稍后重试");
                return result;
            }
        }
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));//从cookie中找到设计人员的id
        User design = new User();
        design.setId(designId);
        solutions.setDesign(design);
        solutions.setState(SolutionsStateEnum.DESIGNING.getMsg());
        solutions.setAddTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        int saveSolutions =  solutionsDao.insertSolutions(solutions);//将方案基本信息插入t_solutions

        //将更新后的方案id同步更新到模板方案表里
        if (template != null) {
            Map<String,Object> map = new HashMap<>();
            template.setSolutions(solutions);
            map.put("template",template);
            map.put("typeId",template.getHouseType().getTypeId());
            map.put("designId",template.getDesign().getId());
            int insertTemplate = templateDao.insertTemplate(map);
            if (insertTemplate != 1) {
                result.setStatus(0);
                result.setMsg("添加失败，请稍后重试");
                return result;
            }
        }
        if (saveSolutions != 1) {
            result.setMsg("添加失败，请稍后重试");
            result.setStatus(0);
            return result;
        }
        List<Room> roomList = solutions.getRoomList();
        Quote quote = new Quote();
        int saveRoom,saveProductNum,saveQuote;
        for (Room room:roomList) {
            room.setSoluId(solutions.getSoluId());
            saveRoom = roomDao.insertRoom(room);//将房间信息插入表t_room
            if (saveRoom != 1) {
                result.setMsg("添加失败，请稍后重试");
                result.setStatus(0);
                return result;
            }
            List<ProductNum> productNumList = room.getProductNumList();
            productNumList = getProductNumList(productNumList);//list去除重复并统计产品数量

            for (ProductNum productNum:productNumList) {
                productNum.setRoomId(room.getRoomId());
                saveProductNum = productNumDao.insertProductNum(productNum);//将房间内的产品及数量插入表t_product_num
                quote.setSoluId(solutions.getSoluId());
                quote.setRoomName(room.getRoomName());
                quote.setProductName(productNum.getProduct().getProductName());
                quote.setProductNum(productNum.getPnNum());
                quote.setPrice(productNum.getProduct().getPrice());
                saveQuote = quoteDao.insert(quote);//将报价单保存到t_quote
                if (saveProductNum != 1 || saveQuote != 1) {
                    result.setMsg("添加失败，请稍后重试");
                    result.setStatus(0);
                    return result;
                }
            }
        }
        result.setStatus(1);
        result.setMsg("添加成功");
        return result;
    }

}
