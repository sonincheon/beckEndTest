package com.tera.beckendtest.service;

import com.tera.beckendtest.entity.CpuUtilization;
import com.tera.beckendtest.repository.CpuUtillizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CpuUtilizationService {
    private final CpuUtillizationRepository cpuUtillizationRepository;

    private final SystemInfo systemInfo = new SystemInfo();
    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private long[] prevTicks = new long[CentralProcessor.TickType.values().length];

    @Scheduled(fixedRate = 60000) // 1분단위로 cpu사용률 저장
    public void collectCpuUsage() {
        long[] currTicks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = currTicks;
        CpuUtilization cpuUtilization = new CpuUtilization();
        cpuUtilization.setUtilization(cpuLoad);
        cpuUtilization.setTimes(LocalDateTime.now());
        cpuUtillizationRepository.save(cpuUtilization);
    }


}
