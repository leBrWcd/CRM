package com.lebrwcd.crm.workbench.web.controller;/**
 * @author lebrwcd
 * @date 2022/2/7
 * @note
 */

import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.settings.service.UserService;
import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.UUIDUtil;
import com.lebrwcd.crm.workbench.domain.Tran;
import com.lebrwcd.crm.workbench.domain.TranHistory;
import com.lebrwcd.crm.workbench.service.CustomerService;
import com.lebrwcd.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName TranController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/2/7
 */
@Controller
@RequestMapping("/transaction")
public class TranController {


    @Autowired(required = false)
    private TranService tranService;
    @Resource
    private UserService userService;
    @Resource
    private CustomerService customerService;

    /*获得用户列表*/
    @RequestMapping("/add.do")
    public ModelAndView add(){

        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUser();

        mv.addObject("uList",userList);
        mv.setViewName("forward:/workbench/transaction/save.jsp");

        return mv;
    }

    /*自动补全获得模糊查询获得客户名称*/
    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public Object getCustomerName(String name){

        List<String> cList = customerService.getCustomerName(name);
        return cList;
    }

    /*添加交易*/
    @RequestMapping("/save.do")
    public ModelAndView save(Tran tran, HttpServletRequest request){

        String customerName = request.getParameter("customerName");
        ModelAndView mv = new ModelAndView();
        //完善tran对象中的信息，前端传过来，后端接收的参数有：
        //owner,money,name,expectedDate,stage,type,source,activityId,contactsId,description,contactSummary,nextContactTime
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        //tran.setCustomerId(),我们只知道customerName,可以让业务层根据customerName查出customerId

        boolean flag = tranService.save(tran,customerName);

        if(flag){
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }

        return mv;
    }

    /*跳转到详细信息页*/
    @RequestMapping("/detail.do")
    public ModelAndView detail(String id,HttpServletRequest request){

        ModelAndView mv = new ModelAndView();

        Tran tran = tranService.detail(id);

        ServletContext context = request.getSession().getServletContext();
        //处理可能性
        String stage = tran.getStage();
        Map<String,String> pMap = (Map<String, String>) context.getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);

        mv.addObject("t",tran);
        mv.setViewName("forward:/workbench/transaction/detail.jsp");

        return mv;
    }

    /*详细页展现阶段历史*/
    @RequestMapping("/showHistoryList.do")
    @ResponseBody
    public Object showHistoryList(String tranId,HttpServletRequest request){

        List<TranHistory> historyList = tranService.showHistoryList(tranId);

        ServletContext context = request.getSession().getServletContext();
        Map<String,String> pMap = (Map<String, String>) context.getAttribute("pMap");

        //遍历historyList取出每一条历史的stage，然后拿着stage去map对应关系中得到可能性
        for(TranHistory history: historyList){
            String stage = history.getStage();
            String possibility = pMap.get(stage);
            history.setPossibility(possibility);
        }

        return historyList;

    }

    /*更改阶段*/
    @RequestMapping("/changeStage.do")
    @ResponseBody
    public Object changeStage(Tran tran,HttpServletRequest request){

        //前端传给tran对象的参数有：id,money,expectedDate,stage
        //根据id修改单条记录
        tran.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());

        boolean flag = tranService.changeStage(tran);

        //可能性的处理
        Map<String,String> pMap = (Map<String, String>) request.getSession().getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",tran);

        return map;

    }

    /*获得交易阶段统计*/
    @RequestMapping("/getCharts.do")
    @ResponseBody
    public Object getCharts(){

        Map<String,Object> map = tranService.getCharts();

        return map;

        
    }

}
