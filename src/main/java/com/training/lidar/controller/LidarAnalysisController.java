package com.training.lidar.controller;

import com.training.lidar.common.ApiResponse;
import com.training.lidar.common.PageResult;
import com.training.lidar.service.LidarDataService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/lidar/analysis")
public class LidarAnalysisController {

    private final LidarDataService lidarDataService;

    public LidarAnalysisController(LidarDataService lidarDataService) {
        this.lidarDataService = lidarDataService;
    }

    @GetMapping("/summary")
    public ApiResponse<Map<String, Object>> summary() {
        return ApiResponse.success(lidarDataService.getAnalysisSummary());
    }

    @GetMapping("/distance-distribution")
    public ApiResponse<List<Map<String, Object>>> distanceDistribution() {
        return ApiResponse.success(lidarDataService.getDistanceDistribution());
    }

    @GetMapping("/device-stats")
    public ApiResponse<PageResult<Map<String, Object>>> deviceStats(
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getDeviceStats(
                deviceId, status, page, pageSize, sortBy, sortOrder));
    }
}
