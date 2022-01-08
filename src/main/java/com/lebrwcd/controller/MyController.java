package com.lebrwcd.controller;/**
 * @author lebrwcd
 * @date 2022/1/5
 * @note
 */

import com.lebrwcd.domain.Student;
import com.sun.deploy.net.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * ClassName MyController
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/5
 */
@Controller
public class MyController {

    @RequestMapping(value = "/mytest01.do")
    public void doAjax(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.print("wcd");
        out.close();
    }

    @RequestMapping(value = "/mytest02.do")
    @ResponseBody
    public Object doJson() throws IOException {
        ArrayList<Student> list = new ArrayList();
        Student s1 = new Student(001,"wcd",18);
        Student s2 = new Student(002,"zpp",17);
        list.add(s1);
        list.add(s2);
        return list;
    }
}
