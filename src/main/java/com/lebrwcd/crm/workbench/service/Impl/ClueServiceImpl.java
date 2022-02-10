package com.lebrwcd.crm.workbench.service.Impl;/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */

import com.lebrwcd.crm.utils.DateTimeUtil;
import com.lebrwcd.crm.utils.UUIDUtil;
import com.lebrwcd.crm.workbench.dao.*;
import com.lebrwcd.crm.workbench.domain.*;
import com.lebrwcd.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * ClassName ClueServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/26
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = true;
        int count = clueDao.saveClue(clue);
        if(count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unbind(String id) {
        return clueActivityRelationDao.unbind(id);
    }

    @Override
    public boolean bind(String cid, String[] aids) {

        boolean flag = true;

        for(String aid : aids){

            ClueActivityRelation relation = new ClueActivityRelation();
            relation.setId(UUIDUtil.getUUID());
            relation.setClueId(cid);
            relation.setActivityId(aid);

            int count = clueActivityRelationDao.bind(relation);
            if(count!=1){
                flag = false;
            }
        }
        return  flag;
    }

    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {

        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）(tbl_clue)
        Clue c  = clueDao.getById(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）(tbl_customer)
        String company = c.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        //如果客户不存在，新建客户
        if(customer==null) {

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(c.getAddress());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setOwner(c.getOwner());
            customer.setDescription(c.getDescription());
            customer.setContactSummary(c.getContactSummary());
            customer.setName(company);
            customer.setPhone(c.getPhone());
            customer.setWebsite(c.getWebsite());
            customer.setNextContactTime(c.getNextContactTime());
            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }
            //  (3) 通过线索对象提取联系人信息，保存联系人(tbl_contacts)
            Contacts contacts = new Contacts();
            contacts.setId(UUIDUtil.getUUID());
            contacts.setAddress(c.getAddress());
            contacts.setAppellation(c.getAppellation());
            contacts.setContactSummary(c.getContactSummary());
            contacts.setCustomerId(customer.getId());
            contacts.setOwner(c.getOwner());
            contacts.setDescription(c.getDescription());
            contacts.setCreateBy(createBy);
            contacts.setCreateTime(createTime);
            contacts.setFullname(c.getFullname());
            contacts.setEmail(c.getEmail());
            contacts.setSource(c.getSource());
            contacts.setNextContactTime(c.getNextContactTime());
            contacts.setMphone(c.getMphone());
            contacts.setJob(c.getJob());
            //保存联系人
            int count2 = contactsDao.save(contacts);
            if (count2 != 1) {
                flag = false;
            }

            //(4) 线索备注转换到客户备注以及联系人备注(tbl_clue_remark、tbl_customer_remark、tbl_contacts_remark)
            //通过线索id查出与该线索关联的备注信息列表
            List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
            //取出每一条线索的备注
            for (ClueRemark clueRemark : clueRemarkList) {

                //依此转移到客户备注，联系人备注,主要转换的是备注信息noteContent,id可以随机生
                String noteContent = clueRemark.getNoteContent();

                //创建客户备注对象，添加客户备注
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag("0");
                customerRemark.setNoteContent(noteContent);
                customerRemark.setCustomerId(customer.getId());

                int count3 = customerRemarkDao.save(customerRemark);
                if (count3 != 1) {
                    flag = false;
                }
                //创建联系人备注对象，添加联系人备注
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag("0");
                contactsRemark.setNoteContent(noteContent);
                contactsRemark.setContactsId(contacts.getId());

                int count4 = contactsRemarkDao.save(contactsRemark);
                if (count4 != 1) {
                    flag = false;
                }
            }
            // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系(tbl_clue_activity_relation、tbl_contacts_activity_relation)
            //查询出与该线索关联的市场活动，查询与市场活动的关联关系列表
            List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);

            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {

                //从每一条遍历出来的记录中取出关联的市场活动id
                String activityId = clueActivityRelation.getActivityId();

                //创建 联系人与市场活动的关联关系对象,让第三步生成的联系人与市场活动做关联
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                //添加联系人与市场活动的关联关系
                int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
                if (count5 != 1) {
                    flag = false;
                }
            }

            //(6) 如果有创建交易需求，创建一条交易(tbl_tran)
            if (tran != null) {
                    /*
                        tran对象在控制器已经封装好一些信息：
                        id,money,name,expectedDate,stage,activityId,createBy,createTime
                     */
                tran.setSource(c.getSource());
                tran.setOwner(c.getOwner());
                tran.setCreateBy(createBy);
                tran.setCreateTime(createTime);
                tran.setDescription(c.getDescription());
                tran.setContactSummary(c.getContactSummary());
                tran.setNextContactTime(c.getNextContactTime());
                tran.setContactsId(contacts.getId());
                tran.setCustomerId(customer.getId());

                //添加交易
                int count6 = tranDao.save(tran);
                if (count6 != 1) {
                    flag = false;
                }

                // (7) 如果创建了交易，则创建一条该交易下的交易历史(tbl_tran_history)
                TranHistory tranHistory = new TranHistory();
                tranHistory.setId(UUIDUtil.getUUID());
                tranHistory.setCreateBy(createBy);
                tranHistory.setCreateTime(createTime);
                tranHistory.setExpectedDate(tran.getExpectedDate());
                tranHistory.setStage(tran.getStage());
                tranHistory.setMoney(tran.getMoney());
                tranHistory.setTranId(tran.getId());
                //添加交易历史
                int count7 = tranHistoryDao.save(tranHistory);
                if (count7 != 1) {
                    flag = false;
                }
            }
            // (8) 删除线索备注(tbl_clue_remark)
            for (ClueRemark clueRemark : clueRemarkList) {


                int count8 = clueRemarkDao.delete(clueRemark);
                if(count8!=1){
                    flag = false;
                }
            }
            //  (9) 删除线索和市场活动的关系(tbl_clue_activity)
            for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
                int count9 = clueActivityRelationDao.delete(clueActivityRelation);
                if(count9!=1){
                    flag = false;
                }
            }
            //    (10) 删除线索(tbl_clue)
            int count10 = clueDao.delete(clueId);
            if(count10!=1){
                flag = false;
            }

        return flag;
    }
}
