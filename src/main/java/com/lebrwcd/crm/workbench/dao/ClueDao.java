package com.lebrwcd.crm.workbench.dao;

import com.lebrwcd.crm.workbench.domain.Clue;

public interface ClueDao {


    int saveClue(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
