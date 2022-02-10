package com.lebrwcd.crm.utils;/**
 * @author lebrwcd
 * @date 2022/1/10
 * @note
 */

import java.util.UUID;

/**
 * ClassName UUIDutil
 * Description 随机生成UUID工具类
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/10
 */
public class UUIDUtil {
    public static String  getUUID(){

        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
