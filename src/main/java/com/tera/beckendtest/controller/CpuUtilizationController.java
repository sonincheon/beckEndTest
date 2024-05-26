package com.tera.beckendtest.controller;

import com.tera.beckendtest.dto.CpuUtilizationDTO;
import com.tera.beckendtest.service.CpuUtilizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cpu")
@RequiredArgsConstructor
public class CpuUtilizationController {

    private final CpuUtilizationService cpuUtilizationService;

    @GetMapping("/minute")
    public ResponseEntity<?> getMinuteData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            List<CpuUtilizationDTO> data = cpuUtilizationService.getMinuteData(dateTime);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error minute data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error minute data");
        }
    }


    @GetMapping("/hourly")
    public ResponseEntity<?> getHourlyData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            List<Map<String, Object>> data = cpuUtilizationService.getHourlyData(dateTime);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error hourly data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error hourly data");
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyData(@RequestParam LocalDate startDate,
                                          @RequestParam LocalDate endDate) {
        try {
            List<Map<String, Object>> data = cpuUtilizationService.getDailyData(startDate, endDate);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error daily data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error daily data");
        }
    }
}
