package com.tera.beckendtest.dto;


import com.tera.beckendtest.entity.CpuUtilization;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CpuUtilizationDTO {
    private double utilization; // CPU 사용률
    private LocalDateTime times; // 시간

    public static CpuUtilizationDTO fromEntity(CpuUtilization entity) {
        CpuUtilizationDTO dto = new CpuUtilizationDTO();
        dto.setUtilization(entity.getUtilization());
        dto.setTimes(entity.getTimes());
        return dto;
    }
}

