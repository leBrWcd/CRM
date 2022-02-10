package com.lebrwcd.crm.utils;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName DataTimeUtil
 * Description 日期工具类
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
public class DateTimeUtil {

    public static String getSysTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
