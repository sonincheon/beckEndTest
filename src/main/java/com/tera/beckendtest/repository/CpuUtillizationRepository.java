package com.tera.beckendtest.repository;

import com.tera.beckendtest.entity.CpuUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuUtillizationRepository extends JpaRepository<CpuUtilization, Long> {

}
