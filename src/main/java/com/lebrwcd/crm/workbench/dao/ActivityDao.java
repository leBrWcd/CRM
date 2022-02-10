package com.lebrwcd.crm.workbench.dao;

import com.lebrwcd.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/1/15
 * @note
 */
public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getDataListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity getActivityByIdJoinUser(String id);

    List<Activity> getActivityListByclueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
