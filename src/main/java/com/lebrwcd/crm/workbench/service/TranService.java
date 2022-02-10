package com.lebrwcd.crm.workbench.service;

import com.lebrwcd.crm.workbench.domain.Tran;
import com.lebrwcd.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * @author lebrwcd
 * @date 2022/2/7
 * @note
 */
public interface TranService {
    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> showHistoryList(String tranId);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
