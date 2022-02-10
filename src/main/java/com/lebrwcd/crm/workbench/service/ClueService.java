package com.lebrwcd.crm.workbench.service;

import com.lebrwcd.crm.workbench.domain.Activity;
import com.lebrwcd.crm.workbench.domain.Clue;
import com.lebrwcd.crm.workbench.domain.Tran;

import java.util.List;

/**
 * @author lebrwcd
 * @date 2022/1/26
 * @note
 */
public interface ClueService {
    boolean saveClue(Clue clue);

    Clue detail(String id);

    boolean unbind(String id);

    boolean bind(String cid, String[] aids);

    boolean convert(String clueId, Tran tran, String createBy);
}
