package com.training.lidar.service;

import com.training.lidar.common.PageResult;

import java.util.List;
import java.util.Map;

public interface LidarDataService {

    Map<String, Object> getRealtimeSummary();

    PageResult<Map<String, Object>> getRealtimeTargets(String vehicleId,
                                                       String deviceId,
                                                       String status,
                                                       Long startTimeMs,
                                                       Long endTimeMs,
                                                       Double lng,
                                                       Double lat,
                                                       Double radius,
                                                       String bbox,
                                                       int page,
                                                       int pageSize,
                                                       String sortBy,
                                                       String sortOrder);

    PageResult<Map<String, Object>> getVehicleList(String vehicleId,
                                                   String status,
                                                   int page,
                                                   int pageSize,
                                                   String sortBy,
                                                   String sortOrder);

    PageResult<Map<String, Object>> getRealtimeEvents(String vehicleId,
                                                      String eventType,
                                                      String eventLevel,
                                                      Long startTimeMs,
                                                      Long endTimeMs,
                                                      int page,
                                                      int pageSize,
                                                      String sortBy,
                                                      String sortOrder);

    PageResult<Map<String, Object>> getHistoryTasks(String vehicleId,
                                                    Long startTimeMs,
                                                    Long endTimeMs,
                                                    int page,
                                                    int pageSize,
                                                    String sortBy,
                                                    String sortOrder);

    Map<String, Object> getHistoryDetail(String taskId);

    PageResult<Map<String, Object>> getHistoryEvents(String taskId,
                                                     String vehicleId,
                                                     Long startTimeMs,
                                                     Long endTimeMs,
                                                     int page,
                                                     int pageSize,
                                                     String sortBy,
                                                     String sortOrder);

    Map<String, Object> getHistoryAnalysis(String taskId);

    Map<String, Object> getVideoSummary();

    PageResult<Map<String, Object>> getPlaybackList(String vehicleId,
                                                    String channel,
                                                    Long startTimeMs,
                                                    Long endTimeMs,
                                                    int page,
                                                    int pageSize,
                                                    String sortBy,
                                                    String sortOrder);

    PageResult<Map<String, Object>> getDownloadList(String vehicleId,
                                                    String status,
                                                    int page,
                                                    int pageSize,
                                                    String sortBy,
                                                    String sortOrder);

    Map<String, Object> getAnalysisSummary();

    List<Map<String, Object>> getDistanceDistribution();

    PageResult<Map<String, Object>> getDeviceStats(String deviceId,
                                                   String status,
                                                   int page,
                                                   int pageSize,
                                                   String sortBy,
                                                   String sortOrder);
}
