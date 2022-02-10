package com.lebrwcd.crm.settings.dao;

import com.lebrwcd.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */
public interface DicValueDao {
    List<DicValue> getValueByCode(String code);
}
