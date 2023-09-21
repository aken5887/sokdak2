package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
