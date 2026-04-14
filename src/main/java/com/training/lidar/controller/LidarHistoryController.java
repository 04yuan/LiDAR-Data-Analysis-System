package com.training.lidar.controller;

import com.training.lidar.common.ApiResponse;
import com.training.lidar.common.PageResult;
import com.training.lidar.service.LidarDataService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/lidar/history")
public class LidarHistoryController {

    private final LidarDataService lidarDataService;

    public LidarHistoryController(LidarDataService lidarDataService) {
        this.lidarDataService = lidarDataService;
    }

    @GetMapping("/tasks")
    public ApiResponse<PageResult<Map<String, Object>>> tasks(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getHistoryTasks(
                vehicleId, startTimeMs, endTimeMs, page, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/detail")
    public ApiResponse<Map<String, Object>> detail(@RequestParam @NotBlank String taskId) {
        return ApiResponse.success(lidarDataService.getHistoryDetail(taskId));
    }

    @GetMapping("/events")
    public ApiResponse<PageResult<Map<String, Object>>> events(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getHistoryEvents(
                taskId, vehicleId, startTimeMs, endTimeMs, page, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/analysis")
    public ApiResponse<Map<String, Object>> analysis(@RequestParam @NotBlank String taskId) {
        return ApiResponse.success(lidarDataService.getHistoryAnalysis(taskId));
    }
}
