package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.common.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
