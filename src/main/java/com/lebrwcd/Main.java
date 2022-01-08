package com.lebrwcd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
	// write your code here
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd" +
                ":HH:mm:ss");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        System.out.println("CRM客户管理系统项目开始" + dateStr);
        System.out.println("this is crm version");
    }
}
