package com.tera.beckendtest.service;

import com.tera.beckendtest.dto.CpuUtilizationDTO;
import com.tera.beckendtest.entity.CpuUtilization;
import com.tera.beckendtest.repository.CpuUtillizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CpuUtilizationService {
    private final CpuUtillizationRepository cpuUtillizationRepository;
    private final SystemInfo systemInfo = new SystemInfo();
    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private long[] prevTicks = new long[CentralProcessor.TickType.values().length];

    // 1분단위로 cpu사용률 저장
    @Scheduled(fixedRate = 60000)
    public void collectCpuUsage() {
        long[] currTicks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = currTicks;
        CpuUtilization cpuUtilization = new CpuUtilization();
        cpuUtilization.setUtilization(cpuLoad);
        cpuUtilization.setTimes(LocalDateTime.now());
        cpuUtillizationRepository.save(cpuUtilization);
    }

    public List<CpuUtilization> getHourlyUsage(LocalDateTime date) {
        LocalDateTime startDateTime = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDateTime = startDateTime.plusHours(24);
        return cpuUtillizationRepository.findByTimesBetweenOrderByTimes(startDateTime, endDateTime);
    }

    public List<CpuUtilization> getDailyUsage(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime startDateTime = startDate.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDateTime = endDate.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return cpuUtillizationRepository.findByTimesBetweenOrderByTimes(startDateTime, endDateTime);
    }

    public double calculateMinUtilization(List<CpuUtilization> cpuUtilizations) {
        return cpuUtilizations.stream()
                .mapToDouble(CpuUtilization::getUtilization)
                .min()
                .orElse(0.0);
    }

    public double calculateMaxUtilization(List<CpuUtilization> cpuUtilizations) {
        return cpuUtilizations.stream()
                .mapToDouble(CpuUtilization::getUtilization)
                .max()
                .orElse(0.0);
    }

    public double calculateAvgUtilization(List<CpuUtilization> cpuUtilizations) {
        OptionalDouble average = cpuUtilizations.stream()
                .mapToDouble(CpuUtilization::getUtilization)
                .average();
        return average.orElse(0.0);
    }

    // 분 단위 데이터
    public List<CpuUtilizationDTO> getMinuteData(LocalDateTime dateTime) {
        LocalDateTime startDateTime = dateTime.minusWeeks(1); // 입력된 일시에서 1주 전까지
        LocalDateTime endDateTime = dateTime;
        List<CpuUtilization> cpuUtilizations = cpuUtillizationRepository.findByTimesBetweenOrderByTimes(startDateTime, endDateTime);
        List<CpuUtilizationDTO> result = new ArrayList<>();
        for (CpuUtilization utilization : cpuUtilizations) {
            result.add(CpuUtilizationDTO.fromEntity(utilization));
        }
        return result;
    }


    // 시 단위 데이터(최대, 최소, 평균 포함)
    public List<Map<String, Object>> getHourlyData(LocalDateTime dateTime ) {
        LocalDateTime startDateTime = LocalDateTime.now().minusMonths(3); //3달 전까지
        LocalDateTime endDateTime = dateTime;
        List<CpuUtilization> cpuUtilizations = cpuUtillizationRepository.findByTimesBetweenOrderByTimes(startDateTime, endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        Map<String, List<CpuUtilization>> groupedData = new HashMap<>();
        for (CpuUtilization utilization : cpuUtilizations) {
            String formattedTime = utilization.getTimes().format(formatter);
            groupedData.putIfAbsent(formattedTime, new ArrayList<>());
            groupedData.get(formattedTime).add(utilization);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<CpuUtilization>> entry : groupedData.entrySet()) {
            Map<String, Object> dataPoint = new HashMap<>();
            List<CpuUtilization> utilizations = entry.getValue();
            dataPoint.put("time", entry.getKey());
            dataPoint.put("min", calculateMinUtilization(utilizations)); // 최소값
            dataPoint.put("max", calculateMaxUtilization(utilizations)); // 최대값
            dataPoint.put("avg", calculateAvgUtilization(utilizations)); // 평균값
            result.add(dataPoint);
        }
        return result;
    }

    // 일 단위 데이터 (최대, 최소, 평균 포함)
    public List<Map<String, Object>> getDailyData(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.now().minusYears(1); //1년 전까지
        LocalDateTime endDateTime = LocalDateTime.now();
        List<CpuUtilization> cpuUtilizations = cpuUtillizationRepository.findByTimesBetweenOrderByTimes(startDateTime, endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<String, List<CpuUtilization>> groupedData = new HashMap<>();
        for (CpuUtilization utilization : cpuUtilizations) {
            String formattedDate = utilization.getTimes().toLocalDate().format(formatter);
            groupedData.putIfAbsent(formattedDate, new ArrayList<>());
            groupedData.get(formattedDate).add(utilization);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<CpuUtilization>> entry : groupedData.entrySet()) {
            Map<String, Object> dataPoint = new HashMap<>();
            List<CpuUtilization> utilizations = entry.getValue();
            dataPoint.put("date", entry.getKey());
            dataPoint.put("min", calculateMinUtilization(utilizations)); // 최소값
            dataPoint.put("max", calculateMaxUtilization(utilizations)); // 최대값
            dataPoint.put("avg", calculateAvgUtilization(utilizations)); // 평균값
            result.add(dataPoint);
        }
        return result;
    }
}
