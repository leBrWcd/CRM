package com.lebrwcd.crm.workbench.dao;

import com.lebrwcd.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    boolean unbind(String id);

    int bind(ClueActivityRelation relation);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
