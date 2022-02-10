package com.lebrwcd.crm.workbench.dao;

import com.lebrwcd.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> showHistoryList(String tranId);
}
