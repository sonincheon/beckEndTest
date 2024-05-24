package com.tera.beckendtest.controller;

import com.tera.beckendtest.entity.CpuUtilization;
import com.tera.beckendtest.repository.CpuUtillizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cpu")
@RequiredArgsConstructor
public class CpuUtilizationController {
    private final CpuUtillizationRepository cpuUtillizationRepository;

    @GetMapping("/utilization")
    public ResponseEntity<List<CpuUtilization>> getCpuUtilization() {
        try {
            List<CpuUtilization> cpuUtilizations = cpuUtillizationRepository.findAll();
            return new ResponseEntity<>(cpuUtilizations, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving CPU utilization data: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
