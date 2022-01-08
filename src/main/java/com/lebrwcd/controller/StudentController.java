package com.lebrwcd.controller;/**
 * @author lebrwcd
 * @date 2022/1/6
 * @note
 */

import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName StudentController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/6
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    static {
        System.out.println("进入学生模块相关操作...");
    }

    @RequestMapping("/save.do")
    public void doSave(){
        System.out.println("添加操作");
    }

    @RequestMapping("/delete.do")
    public void doDelete(){
        System.out.println("删除操作");
    }
}
