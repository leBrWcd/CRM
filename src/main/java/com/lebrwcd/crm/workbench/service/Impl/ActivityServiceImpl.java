package com.lebrwcd.crm.workbench.service.Impl;/**
 * @author lebrwcd
 * @date 2022/1/15
 * @note
 */

import com.lebrwcd.crm.settings.dao.UserDao;
import com.lebrwcd.crm.settings.domain.User;
import com.lebrwcd.crm.vo.PaginationVo;
import com.lebrwcd.crm.workbench.dao.ActivityDao;
import com.lebrwcd.crm.workbench.dao.ActivityRemarkDao;
import com.lebrwcd.crm.workbench.domain.Activity;
import com.lebrwcd.crm.workbench.domain.ActivityRemark;
import com.lebrwcd.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName ActivityServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/15
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao remarkDao;
    @Resource
    private UserDao userDao;
    private boolean flag = true;
    private PaginationVo<Activity> vo;
    @Override
    public boolean save(Activity activity) {
        int saveResult = activityDao.save(activity);
        if(saveResult != 1){
            flag = false;
        }
        return flag;
    }


    @Override
    public PaginationVo pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);
        //取得dataList
        List<Activity> dataList = activityDao.getDataListByCondition(map);
        //将total和dataList封装到vo
        vo = new PaginationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //根据传过来的ids数组去activiy_remark表中查找是否有对应的关联数据，查出数量
        int count1 = remarkDao.selectByAids(ids);

        //删除activity_remark表中关联的数据
        int count2 = remarkDao.deleteByAids(ids);

        if(count1 != count2){
            flag = false;
        }
        //count1 == count2
        //删除activity表中的数据
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            //如果删除的数据与传递过来的参数数量不一致，证明删除没有完全成功
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //获取UserList
        List<User> uList = userDao.getUser();

        //获取Activity
        Activity activity = activityDao.getActivityById(id);

        //将UserList和Activity封装到map中
        Map<String, Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("activity",activity);

        //返回Map
        return map;
    }

    @Override
    public boolean update(Activity activity) {

        int updateResult = activityDao.update(activity);
        if(updateResult != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity getActivityByIdJoinUser(String id) {

        //根据id获得activity
        Activity activity = activityDao.getActivityByIdJoinUser(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = remarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemarkById(String id) {
        return remarkDao.deleteRemarkById(id);

    }

    @Override
    public boolean saveRemark(ActivityRemark remark) {
        boolean flag = remarkDao.saveRemark(remark);
        return  flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark remark) {
        return remarkDao.updateRemark(remark);
    }

    @Override
    public List<Activity> getActivityListByclueId(String clueId) {
        List<Activity> aList = activityDao.getActivityListByclueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {


        List<Activity> aList = activityDao.getActivityListByName(aname);
        return aList;

    }
}
