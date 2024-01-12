package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.common.Visits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitsRepository extends JpaRepository<Visits, Long>, VisitsRepositoryCustom {
    List<Visits> findAllByUriContaining(String uri);
}
