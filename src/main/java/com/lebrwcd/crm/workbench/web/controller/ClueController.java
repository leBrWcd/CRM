package com.lebrwcd.crm.workbench.web.controller;/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.settings.service.UserService;
import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.UUIDUtil;
import com.lebrwcd.crm.vo.CommonVo;
import com.lebrwcd.crm.workbench.domain.Activity;
import com.lebrwcd.crm.workbench.domain.Clue;
import com.lebrwcd.crm.workbench.domain.Tran;
import com.lebrwcd.crm.workbench.service.ActivityService;
import com.lebrwcd.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName ClueController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/26
 */
@Controller
@RequestMapping("/clue")
public class ClueController {

    @Resource
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Resource
    private ActivityService activityService;
    private final CommonVo vo = new CommonVo();

    /*获取用户列表*/
    @RequestMapping("/getUserList.do")
    @ResponseBody
    public Object getUsetList(){

        List<User> user = userService.getUser();
        return user;
    }

    /*添加线索*/
    @RequestMapping("/save.do")
    @ResponseBody
    public CommonVo saveClue(Clue clue, HttpServletRequest request){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = clueService.saveClue(clue);
        if(flag){
            vo.setSuccess(true);
            vo.setMsg("添加成功");
        }else{
            vo.setSuccess(false);
            vo.setMsg("添加失败");
        }
        return vo;
    }

    /*跳转到详细页*/
    @RequestMapping("/detail.do")
    public ModelAndView TodetailPage(String id){

        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);

        mv.addObject("c",clue);
        mv.setViewName("forward:/workbench/clue/detail.jsp");

        return mv;
    }

    /*详细页中展现关联的市场活动列表*/
    @RequestMapping("/getActivityListByclueId.do")
    @ResponseBody
    public Object getActivityListByClueId(String clueId){
        List<Activity> aList = activityService.getActivityListByclueId(clueId);
        return aList;
    }

    /*解除关联*/
    @RequestMapping("/unbindById.do")
    @ResponseBody
    public Object unbind(String id){
        boolean flag = clueService.unbind(id);
        if (flag){
            vo.setSuccess(true);
        }
        return vo;
    }

    /*关联市场活动搜索市场活动列表*/
    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public Object getActivityListByNameAndNotByClueId(String aname,String clueId){

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    /*关联市场活动*/
    @RequestMapping("/bind.do")
    @ResponseBody
    public CommonVo bind(HttpServletRequest request){

        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");

        boolean flag = clueService.bind(cid,aids);

        if(flag){
            vo.setSuccess(true);
            vo.setMsg("关联市场活动成功");
        }else{
            vo.setSuccess(false);
            vo.setMsg("关联市场活动失败");
        }
        return this.vo;
    }

    /*根据名字模糊查询获得市场获得列表*/
    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    public Object getActivityListByName(String aname){

        List<Activity> aList = activityService.getActivityListByName(aname);
        return aList;

    }

    /*执行线索转换的操作*/
    @RequestMapping("/convert.do")
    public ModelAndView convert(String clueId,Tran tran,HttpServletRequest request){

        String flag = request.getParameter("flag");
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();

        //接收参数flag,判断是否需要创建交易
        if("need".equals(flag)){
            //需要创建交易，那么完善Tran对象中的属性
            //前端传递过来的参数中。tran已经有:money,name,expectedDate,stage,stage
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);

        }

        boolean flag1 = clueService.convert(clueId,tran,createBy);

        if(flag1){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }

        return mv;
    }
}
