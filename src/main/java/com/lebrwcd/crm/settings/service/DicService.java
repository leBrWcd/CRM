package com.lebrwcd.crm.settings.service;

import com.lebrwcd.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
