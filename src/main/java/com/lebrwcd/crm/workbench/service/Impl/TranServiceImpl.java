package com.lebrwcd.crm.workbench.service.Impl;/**
 * @author lebrwcd
 * @date 2022/2/7
 * @note
 */

import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.UUIDUtil;
import com.lebrwcd.crm.workbench.dao.CustomerDao;
import com.lebrwcd.crm.workbench.dao.TranDao;
import com.lebrwcd.crm.workbench.dao.TranHistoryDao;
import com.lebrwcd.crm.workbench.domain.Customer;
import com.lebrwcd.crm.workbench.domain.Tran;
import com.lebrwcd.crm.workbench.domain.TranHistory;
import com.lebrwcd.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName TranServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/2/7
 */
@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran, String customerName) {

        boolean flag = true;
        /*
            交易添加业务
            在做添加交易之前， 参数tran中少了一项信息，那就是customerId
            先处理客户相关的需求：
                1）判断customerName,根据客户名称在客户表中进行精准查询
                        如果有这个客户，则取出这个客户的id，封装到tran对象中
                        如果没有，则在客户表中新建一条客户信息，然后将新建的客户的id取出，封装到tran对象中
                2）经过以上操作后，tran对象中的信息已经完毕，需要进行添加交易的操作
                3）添加交易完毕后，需要创建一条交易历史

         */
        Customer customer = customerDao.getCustomerByName(customerName);

        if(customer==null){
            customer = new Customer();
            //如果没有这个客户，新建
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setOwner(tran.getOwner());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setContactSummary(tran.getContactSummary());

            //添加客户
            int count1 = customerDao.save(customer);
            if(count1!=1){
                flag = false;
            }
        }

        //到这里已经能够获得customerId了，不管是已有的客户还是新建的客户
        tran.setCustomerId(customer.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(tran.getId());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setExpectedDate(tran.getExpectedDate());

        int count3 = tranHistoryDao.save(tranHistory);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran tran = tranDao.detail(id);

        return tran;
    }

    @Override
    public List<TranHistory> showHistoryList(String tranId) {


        return tranHistoryDao.showHistoryList(tranId);
    }

    @Override
    public boolean changeStage(Tran tran) {

        boolean flag = true;

        int count1  = tranDao.changeStage(tran);
        if(count1!=1){
            flag = false;
        }


        //交易阶段改变后，生成一条交易历史

        TranHistory history = new TranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setCreateBy(tran.getEditBy());
        history.setCreateTime(tran.getEditTime());
        history.setMoney(tran.getMoney());
        history.setTranId(tran.getId());
        history.setExpectedDate(tran.getExpectedDate());
        history.setStage(tran.getStage());

        int count2 = tranHistoryDao.save(history);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //获得total
        int total = tranDao.getTotal();

        //获得dataList
        List<Map<String,Object>> dataList = tranDao.getDataList();

        //封装dataList
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

}
