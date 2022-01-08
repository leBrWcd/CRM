package test;/**
 * @author lebrwcd
 * @date 2022/1/6
 * @note
 */

import java.util.UUID;

/**
 * ClassName UUIDtest
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/6
 */
public class UUIDtest {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        System.out.println(str);
        System.out.println(str.length());

        String newstr = str.replaceAll("-","");
        System.out.println(newstr);
        System.out.println(newstr.length());
    }
}
