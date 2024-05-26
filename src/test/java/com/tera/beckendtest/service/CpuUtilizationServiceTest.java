package com.tera.beckendtest.service;

import com.tera.beckendtest.dto.CpuUtilizationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CpuUtilizationServiceTest {

    @Autowired
    private CpuUtilizationService service;

    @Test
    @DisplayName("시간별 출력 테스트")
    void testGetHourlyData() {
        LocalDate date = LocalDate.now();
        List<Map<String, Object>> hourlyData = service.getHourlyData(date);

        for (Map<String, Object> dataPoint : hourlyData) {
            System.out.println("시간: " + dataPoint.get("time"));
            System.out.println("최소값: " + dataPoint.get("min"));
            System.out.println("최대값: " + dataPoint.get("max"));
            System.out.println("평균값: " + dataPoint.get("avg"));
        }
    }

    @Test
    @DisplayName("분 단위 출력 테스트")
    void testGetMinuteData() {
        LocalDate date = LocalDate.now();
        List<CpuUtilizationDTO> minuteData = service.getMinuteData(date.atStartOfDay());

        for (CpuUtilizationDTO dataPoint : minuteData) {
            System.out.println(dataPoint);
            System.out.println("------------------------");
        }
    }

    @Test
    @DisplayName("일 단위 출력 테스트")
    void testGetDailyData() {
        LocalDate startDate = LocalDate.now().minusDays(7); // 7일 전부터
        LocalDate endDate = LocalDate.now();
        List<Map<String, Object>> dailyData = service.getDailyData(startDate, endDate);

        for (Map<String, Object> dataPoint : dailyData) {
            System.out.println("날짜: " + dataPoint.get("date"));
            System.out.println("최소값: " + dataPoint.get("min"));
            System.out.println("최대값: " + dataPoint.get("max"));
            System.out.println("평균값: " + dataPoint.get("avg"));
            System.out.println("------------------------");
        }
    }
}
