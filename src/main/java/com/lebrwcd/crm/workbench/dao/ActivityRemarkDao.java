package com.lebrwcd.crm.workbench.dao;

import com.lebrwcd.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author lebrwcd
 * @date 2022/1/16
 * @note
 */
public interface ActivityRemarkDao {
    int selectByAids(String[] ids);

    int deleteByAids(String[] ids);


    List<ActivityRemark> getRemarkListByAid(String aid);

    boolean deleteRemarkById(String id);

    boolean saveRemark(ActivityRemark remark);

    boolean updateRemark(ActivityRemark remark);
}
