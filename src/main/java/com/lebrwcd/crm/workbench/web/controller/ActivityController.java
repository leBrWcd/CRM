package com.lebrwcd.crm.workbench.web.controller;/**
 * @author lebrwcd
 * @date 2022/1/15
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.settings.service.UserService;
import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.UUIDUtil;
import com.lebrwcd.crm.vo.CommonVo;
import com.lebrwcd.crm.vo.PaginationVo;
import com.lebrwcd.crm.workbench.domain.Activity;
import com.lebrwcd.crm.workbench.domain.ActivityRemark;
import com.lebrwcd.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName ActivityController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/15
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired(required = false)
    private ActivityService activityService;
    private final CommonVo commonVo = new CommonVo();
    @Resource
    private UserService userService;

    /*查询所有用户信息*/
    @RequestMapping("/user/getUserList.do")
    @ResponseBody
    public Object getAllUser(){
        List<User> userList = userService.getUser();
        return userList;
    }

    /*保存市场活动添加操作*/
    @RequestMapping("/saveActivity.do")
    @ResponseBody
    public CommonVo saveActivity(Activity activity, HttpServletRequest request){

        //接收请求参数
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = activityService.save(activity);
        if(flag){
            commonVo.setSuccess(true);
            commonVo.setMsg("添加成功");
        }else{
            commonVo.setSuccess(false);
            commonVo.setMsg("添加失败");
        }
        return commonVo;
    }

    /*显示市场活动信息列表*/
    @RequestMapping("/pageList.do")
    @ResponseBody
    public Object pageList(Activity activity,HttpServletRequest request){
        //接收参数
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        /*select * from tbl limit 0,5
        *  表示略过0个数据显示5个数据      1，2，3，4，5
        * select * from tbl limit 5,5
        * 表示略过5个数据显示5个数据       6，7，8，9，10
        * */
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        //需要6个参数给service,将6个参数封装成map
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",activity.getName());
        map.put("owner",activity.getOwner());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        //控制器分发任务给service
        PaginationVo<Activity> vo = activityService.pageList(map);

        return vo;
    }

    /*删除市场活动*/
    @RequestMapping("/delete.do")
    @ResponseBody
    public CommonVo deleteActivity(HttpServletRequest request){

        //接收参数
        String[] ids = request.getParameterValues("id");
        //分发给service
        boolean flag = activityService.delete(ids);

        if(flag){
            commonVo.setSuccess(true);
            commonVo.setMsg("删除成功!");
        }else{
            commonVo.setSuccess(false);
            commonVo.setMsg("删除失败");
        }

        return commonVo;
    }

    /*获取市场活动*/
    @RequestMapping("/getActivityAndUserList.do")
    @ResponseBody
    public Object getActivityAndUserList(String id){

        //分发给service去获取两项信息:uList,Activity
        Map<String,Object> map = activityService.getUserListAndActivity(id);
        return map;
    }

    /*更新市场活动*/
    @RequestMapping("/update.do")
    @ResponseBody
    public CommonVo delete(Activity activity,HttpServletRequest request){

        //接收请求参数
        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = activityService.update(activity);
        if(flag){
            commonVo.setSuccess(true);
            commonVo.setMsg("更新成功");
        }else{
            commonVo.setSuccess(false);
            commonVo.setMsg("更新失败");
        }
        return commonVo;
    }

    /*跳转到详细页*/
    @RequestMapping("/detail.do")
    public ModelAndView TodetailPage(String id){
        ModelAndView mv = new ModelAndView();
        //根据接收参数中id调用service层返回一个activity
        Activity activity = activityService.getActivityByIdJoinUser(id);

        //将数据保存到request作用域中
        mv.addObject("a",activity);
        //转发
        mv.setViewName("forward:/workbench/activity/detail.jsp");
        return mv;
    }

    /*显示详细页备注信息*/
    @RequestMapping("/getRemarkListByAid.do")
    @ResponseBody
    public Object getRemarkListByAid(String activityId){
        //前端要什么，就返回什么:[{备注1}，{备注2}],很明显这是个List集合，解析成json就是数组形式
        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);

        return arList;
    }

    /*删除单条备注信息*/
    @RequestMapping("/removeRemark.do")
    @ResponseBody
    public CommonVo deleteRemarkById(String id){
        //通过前端传过来的id删除对应的单条备注信息
        boolean flag = activityService.deleteRemarkById(id);
        if (flag){
            commonVo.setSuccess(true);
            commonVo.setMsg("删除成功");
        }else{
            commonVo.setSuccess(false);
            commonVo.setMsg("删除失败");
        }
        return commonVo;
    }

    /*添加单条备注信息*/
    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Object saveRemark(ActivityRemark remark,HttpServletRequest request){
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setCreateTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("0");

        boolean flag  = activityService.saveRemark(remark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRm",remark);
        return map;
    }

    /*修改单条备注信息*/
    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Object updateRemark(ActivityRemark remark,HttpServletRequest request){

        remark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setEditTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("1");

        boolean flag  = activityService.updateRemark(remark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",remark);
        return map;
    }

}
