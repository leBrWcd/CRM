package com.lebrwcd.crm.vo;/**
 * @author lebrwcd
 * @date 2022/1/18
 * @note
 */

import java.util.List;

/**
 * ClassName PaginationVo
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/18
 */
/*页面对象Vo*/
public class PaginationVo<T> {
    private int total;
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
