package cn.zmdxd.xddesign.admin.handler;

import cn.zmdxd.xddesign.admin.service.GuideService;
import cn.zmdxd.xddesign.admin.service.QuestionService;
import cn.zmdxd.xddesign.admin.service.VideoService;
import cn.zmdxd.xddesign.entity.Guide;
import cn.zmdxd.xddesign.entity.Question;
import cn.zmdxd.xddesign.entity.Video;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

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

    //分页查询产品视频列表
    @RequestMapping(value = "videos")
    public IPage<Video> findVideos(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<Video> page = new Page<>(current,size);
        return videoService.page(page);
    }

    //查看手册列表
    @RequestMapping(value = "guides")
    public IPage<Guide> findGuideList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<Guide> page = new Page<>(current, size);
        return guideService.page(page);
    }

    /**
     * @description: 产品手册预览和下载
     * @param filePath:文件在服务器的存储路径
     * @param isOnLine:预览或下载 true为预览/false为下载
     * @throws IOException
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
        int len = 0;

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
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }


    //查看问题
    @RequestMapping(value = "questions")
    public IPage<Question> findQuestionList(@RequestParam(required = false) String keyword , @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "100") Integer size) {
        Page<Question> page = new Page<>(current,size);
        if (keyword != null) return questionService.page(page, new QueryWrapper<Question>().like("keyword", keyword));
        return questionService.page(page);
    }


}
