package com.tera.beckendtest.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class CpuUtilization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double utilization; //cpu 사용률
    private LocalDateTime times; // 시간
}
