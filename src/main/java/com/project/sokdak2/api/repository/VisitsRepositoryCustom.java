package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.common.Visits;

import java.util.List;

public interface VisitsRepositoryCustom {
    List<Visits> selectUselessVisits();
    long deleteUselessVisits();
}
