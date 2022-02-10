package com.lebrwcd.crm.workbench.service.Impl;/**
 * @author lebrwcd
 * @date 2022/2/8
 * @note
 */

import com.lebrwcd.crm.workbench.dao.CustomerDao;
import com.lebrwcd.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName CustomerServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/2/8
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;
    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }
}
