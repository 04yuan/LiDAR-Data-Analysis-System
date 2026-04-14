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

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/lidar/realtime")
public class LidarRealtimeController {

    private final LidarDataService lidarDataService;

    public LidarRealtimeController(LidarDataService lidarDataService) {
        this.lidarDataService = lidarDataService;
    }

    @GetMapping("/summary")
    public ApiResponse<Map<String, Object>> summary() {
        return ApiResponse.success(lidarDataService.getRealtimeSummary());
    }

    @GetMapping("/targets")
    public ApiResponse<PageResult<Map<String, Object>>> targets(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String bbox,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getRealtimeTargets(
                vehicleId, deviceId, status, startTimeMs, endTimeMs, lng, lat, radius, bbox,
                page, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/vehicle-list")
    public ApiResponse<PageResult<Map<String, Object>>> vehicleList(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getVehicleList(
                vehicleId, status, page, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/events")
    public ApiResponse<PageResult<Map<String, Object>>> events(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String eventLevel,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(lidarDataService.getRealtimeEvents(
                vehicleId, eventType, eventLevel, startTimeMs, endTimeMs, page, pageSize, sortBy, sortOrder));
    }
}
