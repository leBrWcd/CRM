package test;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName Test
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
public class Test {
    public static void main(String[] args) {
        //验证失效时间
        String expireTime = "2022-01-15 19:32:00";
        int result = expireTime.compareTo(DateTimeUtil.getSysTime());
        System.out.println(result); //5

        String pwd = "zpp0117";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);    //be2f41d50c1da8c8445aa44cb18cfe6c
    }
}
