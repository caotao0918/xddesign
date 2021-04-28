package cn.zmdxd.xddesign.design.service;

import cn.zmdxd.xddesign.design.dao.*;
import cn.zmdxd.xddesign.entity.*;
import cn.zmdxd.xddesign.util.CookieUtil;
import cn.zmdxd.xddesign.util.ReturnResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.thymeleaf.util.StringUtils;

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
@Transactional
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
    @Autowired
    private RenderingsDao renderingsDao;
    @Autowired
    private HouseDao houseDao;

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
        Integer oldSoluId = null;
        String workName = null;
        Double workPrice = null;
        String soluDesc = null;
        Timestamp workTime = null;
        String state = null;

        if (solutions.getSoluId() != null) {
            // 修改方案

            // 删除方案之前要先得到该方案对应的模板方案信息
            template = templateDao.selectTemplateBySoluId(solutions.getSoluId());
            // 得到方案id，以更新效果图t_renderings表
            oldSoluId = solutions.getSoluId();
            Solutions solu = solutionsDao.selectOne(new QueryWrapper<Solutions>().eq("solu_id", oldSoluId).select("work_name", "work_time", "work_price", "solu_desc", "state"));
            workName = solu.getWorkName();
            workTime = solu.getWorkTime();
            workPrice = solu.getWorkPrice();
            soluDesc = solu.getSoluDesc();
            state = solu.getState();

            /*
                删除此id对应的方案
                因为数据库中设置的级联删除，所以这个方案的房间信息、房间内的产品信息、方案对应的报价单信息也一并删除
             */
            int removeById = solutionsDao.deleteById(solutions.getSoluId());
            if (removeById != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                result.setStatus(0);
                result.setMsg("添加失败，请稍后重试");
                return result;
            }
        }
        Integer designId = Integer.valueOf(CookieUtil.getCookieValue(request, "userId"));//从cookie中找到设计人员的id
        User design = new User();
        design.setId(designId);
        solutions.setDesign(design);
        if (state == null) {
            solutions.setState(SolutionsStateEnum.DESIGNING.getMsg());
        }else {
            solutions.setState(state);
        }
        solutions.setWorkName(workName);
        solutions.setWorkTime(workTime);
        solutions.setWorkPrice(workPrice);
//        solutions.setSoluDesc(soluDesc);
        solutions.setAddTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        Integer customerId = houseDao.selectById(solutions.getHouseId()).getCustomerId();

        // 拼接报价单号
        String num1 = String.format("%04d", designId);
        String num2 = String.format("%04d", customerId);
        String num3 = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).toString();
        num3 = StringUtils.substringBefore(num3, ".").replaceAll("[[\\s-:punct:]]","");
        solutions.setQuoteNum(num1 + num2 + num3);

        int saveSolutions =  solutionsDao.insertSolutions(solutions);//将方案基本信息插入t_solutions

        // 将更新后的方案id同步更新到模板方案表里
        if (template != null) {
            Map<String,Object> map = new HashMap<>();
            template.setSolutions(solutions);
            map.put("template",template);
            map.put("typeId",template.getHouseType().getTypeId());
            map.put("designId",template.getDesign().getId());
            int insertTemplate = templateDao.insertTemplate(map);
            if (insertTemplate != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                result.setStatus(0);
                result.setMsg("添加失败，请稍后重试");
                return result;
            }
        }

        // 将更新后的方案id同步更新到方案效果图表里
        if (oldSoluId != null) {
            Renderings renderings = new Renderings();
            renderings.setSoluId(solutions.getSoluId());
            int updateRenderingsBySoluId = renderingsDao.update(renderings, new UpdateWrapper<Renderings>().eq("solu_id", oldSoluId));
        }

        if (saveSolutions != 1) {
            // 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setMsg("添加失败，请稍后重试");
            result.setStatus(0);
            return result;
        }
        List<Room> roomList = solutions.getRoomList();
        Quote quote = new Quote();
        int saveRoom,saveProductNum,saveQuote;
        for (Room room:roomList) {
            room.setSoluId(solutions.getSoluId());
            // 将房间信息插入表t_room
            saveRoom = roomDao.insertRoom(room);
            if (saveRoom != 1) {
                // 事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setMsg("添加失败，请稍后重试");
                result.setStatus(0);
                return result;
            }
            List<ProductNum> productNumList = room.getProductNumList();
            // list去除重复并统计产品数量
            productNumList = getProductNumList(productNumList);

            for (ProductNum productNum:productNumList) {
                productNum.setRoomId(room.getRoomId());
                // 将房间内的产品及数量插入表t_product_num
                saveProductNum = productNumDao.insertProductNum(productNum);
                quote.setSoluId(solutions.getSoluId());
                quote.setRoomId(room.getRoomId());
                quote.setRoomName(room.getRoomName());
                quote.setProductId(productNum.getProduct().getProductId());
                quote.setProductName(productNum.getProduct().getProductName());
                quote.setProductNum(productNum.getPnNum());
                quote.setPrice(productNum.getProduct().getPrice());
                // 将报价单保存到t_quote
                saveQuote = quoteDao.insert(quote);
                if (saveProductNum != 1 || saveQuote != 1) {
                    // 事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
