package com.tera.beckendtest.repository;

import com.tera.beckendtest.entity.CpuUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CpuUtillizationRepository extends JpaRepository<CpuUtilization, Long> {
    List<CpuUtilization> findByTimesBetweenOrderByTimes(LocalDateTime startTime, LocalDateTime endTime);
}
