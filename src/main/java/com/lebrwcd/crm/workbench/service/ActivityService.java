package com.lebrwcd.crm.workbench.service;

import com.lebrwcd.crm.vo.PaginationVo;
import com.lebrwcd.crm.workbench.domain.Activity;
import com.lebrwcd.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/1/15
 * @note
 */
public interface ActivityService {
    boolean save(Activity activity);

    PaginationVo pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity getActivityByIdJoinUser(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemarkById(String id);

    boolean saveRemark(ActivityRemark remark);

    boolean updateRemark(ActivityRemark remark);

    List<Activity> getActivityListByclueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
