package com.lebrwcd.crm.settings.service.impl;/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */

import com.lebrwcd.crm.settings.dao.DicTypeDao;
import com.lebrwcd.crm.settings.dao.DicValueDao;
import com.lebrwcd.crm.settings.domain.DicType;
import com.lebrwcd.crm.settings.domain.DicValue;
import com.lebrwcd.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName DicServiceImpl
 * Description TODO
 *
 * @author lebr7wcd
 * @version 1.0
 * @date 2022/1/26
 */
@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicValueDao valueDao;
    @Resource
    private DicTypeDao typeDao;

    Map<String,List<DicValue>> map = new HashMap<String,List<DicValue>>();

    @Override
    public Map<String, List<DicValue>> getAll() {
        //按字典类型列表去吃
        List<DicType> typeList = typeDao.getTypeList();

        //将字典类型便利
        for(DicType type : typeList){
            //取得每一种类型的字典类型编码,取tbl_dic_value表中取得对应的值
            String code = type.getCode();
            List<DicValue> valueList = valueDao.getValueByCode(code);
            map.put(code,valueList);
        }
        return map;
    }
}
