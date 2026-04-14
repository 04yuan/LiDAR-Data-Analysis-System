package com.training.lidar.service;

import com.training.lidar.common.ApiException;
import com.training.lidar.common.PageResult;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Profile("mysql")
@Service
public class LidarMysqlService implements LidarDataService {

    private final JdbcTemplate jdbcTemplate;

    public LidarMysqlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getRealtimeSummary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("onlineVehicleCount", queryCount("SELECT COUNT(DISTINCT vehicle_id) FROM lidar_device WHERE status = 'online'"));
        result.put("onlineDeviceCount", queryCount("SELECT COUNT(*) FROM lidar_device WHERE status = 'online'"));
        result.put("eventCount", queryCount("SELECT COUNT(*) FROM lidar_event WHERE task_id IS NULL"));
        result.put("targetCount", queryCount("SELECT COUNT(*) FROM lidar_realtime_target"));
        result.put("timestampMs", jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(timestamp_ms), UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000) FROM lidar_realtime_target",
                Long.class
        ));
        return result;
    }

    public PageResult<Map<String, Object>> getRealtimeTargets(String vehicleId, String deviceId, String status,
                                                              Long startTimeMs, Long endTimeMs, Double lng, Double lat,
                                                              Double radius, String bbox, int page, int pageSize,
                                                              String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_realtime_target WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendEquals(from, params, "device_id", deviceId);
        appendEquals(from, params, "status", status);
        appendTimeRange(from, params, startTimeMs, endTimeMs, "timestamp_ms");
        appendGeoRange(from, params, lng, lat, radius, bbox);
        String select = "SELECT target_id AS targetId, vehicle_id AS vehicleId, vehicle_name AS vehicleName, "
                + "device_id AS deviceId, status, target_type AS targetType, distance_m AS distanceM, "
                + "intensity, point_count AS pointCount, lng, lat, heading, timestamp_ms AS timestampMs";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("targetId", "target_id", "vehicleId", "vehicle_id", "deviceId", "device_id",
                        "status", "status", "distanceM", "distance_m", "timestampMs", "timestamp_ms"),
                "timestamp_ms");
    }

    public PageResult<Map<String, Object>> getVehicleList(String vehicleId, String status, int page, int pageSize,
                                                          String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_device WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendEquals(from, params, "status", status);
        String select = "SELECT vehicle_id AS vehicleId, vehicle_name AS vehicleName, status, lng, lat, "
                + "speed_kph AS speedKph, heading, device_id AS deviceId, last_report_time_ms AS lastReportTimeMs";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("vehicleId", "vehicle_id", "status", "status", "speedKph", "speed_kph",
                        "lastReportTimeMs", "last_report_time_ms"),
                "last_report_time_ms");
    }

    public PageResult<Map<String, Object>> getRealtimeEvents(String vehicleId, String eventType, String eventLevel,
                                                             Long startTimeMs, Long endTimeMs, int page, int pageSize,
                                                             String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_event WHERE task_id IS NULL");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendEquals(from, params, "event_type", eventType);
        appendEquals(from, params, "event_level", eventLevel);
        appendTimeRange(from, params, startTimeMs, endTimeMs, "timestamp_ms");
        String select = "SELECT event_id AS eventId, event_type AS eventType, event_name AS eventName, "
                + "event_level AS eventLevel, vehicle_id AS vehicleId, vehicle_name AS vehicleName, "
                + "message, timestamp_ms AS timestampMs";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("eventId", "event_id", "eventType", "event_type", "eventLevel", "event_level",
                        "vehicleId", "vehicle_id", "timestampMs", "timestamp_ms"),
                "timestamp_ms");
    }

    public PageResult<Map<String, Object>> getHistoryTasks(String vehicleId, Long startTimeMs, Long endTimeMs,
                                                           int page, int pageSize, String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_history_task WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendTaskWindow(from, params, startTimeMs, endTimeMs);
        String select = "SELECT task_id AS taskId, vehicle_id AS vehicleId, vehicle_name AS vehicleName, "
                + "start_time_ms AS startTimeMs, end_time_ms AS endTimeMs, duration_ms AS durationMs, "
                + "distance_km AS distanceKm, event_count AS eventCount";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("taskId", "task_id", "vehicleId", "vehicle_id", "startTimeMs", "start_time_ms",
                        "endTimeMs", "end_time_ms", "distanceKm", "distance_km"),
                "start_time_ms");
    }

    public Map<String, Object> getHistoryDetail(String taskId) {
        List<Map<String, Object>> tasks = jdbcTemplate.queryForList(
                "SELECT task_id AS taskId, vehicle_id AS vehicleId, vehicle_name AS vehicleName, "
                        + "start_time_ms AS startTimeMs, end_time_ms AS endTimeMs, duration_ms AS durationMs, "
                        + "distance_km AS distanceKm, event_count AS eventCount, target_frame_count AS targetFrameCount, "
                        + "avg_point_count AS avgPointCount, max_distance_m AS maxDistanceM "
                        + "FROM lidar_history_task WHERE task_id = ?",
                taskId
        );
        if (tasks.isEmpty()) {
            throw new ApiException(40400, "history task not found");
        }
        Map<String, Object> detail = new LinkedHashMap<>(tasks.get(0));
        List<Map<String, Object>> trackPoints = jdbcTemplate.queryForList(
                "SELECT lng, lat, timestamp_ms AS timestampMs FROM lidar_history_track_point "
                        + "WHERE task_id = ? ORDER BY timestamp_ms ASC",
                taskId
        );
        detail.put("trackPoints", trackPoints);
        return detail;
    }

    public PageResult<Map<String, Object>> getHistoryEvents(String taskId, String vehicleId, Long startTimeMs,
                                                            Long endTimeMs, int page, int pageSize, String sortBy,
                                                            String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_event WHERE task_id IS NOT NULL");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "task_id", taskId);
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendTimeRange(from, params, startTimeMs, endTimeMs, "timestamp_ms");
        String select = "SELECT task_id AS taskId, event_id AS eventId, event_type AS eventType, "
                + "event_name AS eventName, event_level AS eventLevel, vehicle_id AS vehicleId, "
                + "vehicle_name AS vehicleName, message, timestamp_ms AS timestampMs";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("taskId", "task_id", "eventId", "event_id", "eventType", "event_type",
                        "eventLevel", "event_level", "timestampMs", "timestamp_ms"),
                "timestamp_ms");
    }

    public Map<String, Object> getHistoryAnalysis(String taskId) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT task_id AS taskId, target_count AS targetCount, fusion_success_rate AS fusionSuccessRate, "
                        + "abnormal_frame_count AS abnormalFrameCount, stop_point_count AS stopPointCount, "
                        + "avg_speed_kph AS avgSpeedKph, max_distance_m AS maxDistanceM, timestamp_ms AS timestampMs "
                        + "FROM lidar_analysis_snapshot WHERE scope_type = 'TASK' AND task_id = ?",
                taskId
        );
        if (list.isEmpty()) {
            throw new ApiException(40400, "history analysis not found");
        }
        return list.get(0);
    }

    public Map<String, Object> getVideoSummary() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("onlineVideoDeviceCount", queryCount("SELECT COUNT(*) FROM lidar_video_device WHERE status = 'online'"));
        result.put("onlineChannelCount", queryCount("SELECT COUNT(*) FROM lidar_video_device WHERE status = 'online'"));
        result.put("playbackTaskCount", queryCount("SELECT COUNT(*) FROM lidar_video_segment"));
        result.put("downloadTaskCount", queryCount("SELECT COUNT(*) FROM lidar_download_task"));
        result.put("timestampMs", jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(updated_time_ms), UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000) FROM lidar_video_segment",
                Long.class
        ));
        return result;
    }

    public PageResult<Map<String, Object>> getPlaybackList(String vehicleId, String channel, Long startTimeMs,
                                                           Long endTimeMs, int page, int pageSize, String sortBy,
                                                           String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_video_segment WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendEquals(from, params, "channel", channel);
        appendTaskWindow(from, params, startTimeMs, endTimeMs);
        String select = "SELECT playback_id AS playbackId, vehicle_id AS vehicleId, channel, "
                + "camera_name AS cameraName, start_time_ms AS startTimeMs, end_time_ms AS endTimeMs, "
                + "duration_ms AS durationMs, status, stream_url AS streamUrl";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("playbackId", "playback_id", "vehicleId", "vehicle_id", "channel", "channel",
                        "startTimeMs", "start_time_ms", "status", "status"),
                "start_time_ms");
    }

    public PageResult<Map<String, Object>> getDownloadList(String vehicleId, String status, int page, int pageSize,
                                                           String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_download_task WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "vehicle_id", vehicleId);
        appendEquals(from, params, "status", status);
        String select = "SELECT download_id AS downloadId, vehicle_id AS vehicleId, channel, status, "
                + "format, created_time_ms AS createdTimeMs, file_size_mb AS fileSizeMb";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("downloadId", "download_id", "vehicleId", "vehicle_id", "status", "status",
                        "createdTimeMs", "created_time_ms"),
                "created_time_ms");
    }

    public Map<String, Object> getAnalysisSummary() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT target_count AS targetCount, fusion_success_rate AS fusionSuccessRate, "
                        + "abnormal_frame_count AS abnormalFrameCount, timestamp_ms AS timestampMs "
                        + "FROM lidar_analysis_snapshot WHERE scope_type = 'SUMMARY' LIMIT 1"
        );
        if (list.isEmpty()) {
            throw new ApiException(40400, "analysis summary not found");
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getDistanceDistribution() {
        return jdbcTemplate.queryForList(
                "SELECT bucket_label AS bucket, bucket_count AS count "
                        + "FROM lidar_distance_distribution WHERE scope_type = 'SUMMARY' ORDER BY bucket_order ASC"
        );
    }

    public PageResult<Map<String, Object>> getDeviceStats(String deviceId, String status, int page, int pageSize,
                                                          String sortBy, String sortOrder) {
        StringBuilder from = new StringBuilder(" FROM lidar_device_stat WHERE 1=1");
        List<Object> params = new ArrayList<>();
        appendEquals(from, params, "device_id", deviceId);
        appendEquals(from, params, "status", status);
        String select = "SELECT device_id AS deviceId, device_name AS deviceName, status, "
                + "frame_rate_fps AS frameRateFps, point_density_per_frame AS pointDensityPerFrame, "
                + "temperature_c AS temperatureC, last_report_time_ms AS lastReportTimeMs";
        return queryPage(select, from.toString(), params, page, pageSize, sortBy, sortOrder,
                mapOf("deviceId", "device_id", "status", "status", "frameRateFps", "frame_rate_fps",
                        "lastReportTimeMs", "last_report_time_ms"),
                "last_report_time_ms");
    }

    private PageResult<Map<String, Object>> queryPage(String selectClause, String fromClause, List<Object> params,
                                                      int page, int pageSize, String sortBy, String sortOrder,
                                                      Map<String, String> allowedSortColumns, String defaultSortColumn) {
        if (page < 1 || pageSize < 1) {
            throw new ApiException(40000, "page and pageSize must be greater than 0");
        }
        String resolvedSortColumn = allowedSortColumns.getOrDefault(sortBy, defaultSortColumn);
        String resolvedSortOrder = "asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC";
        int offset = (page - 1) * pageSize;
        long total = queryCount("SELECT COUNT(*)" + fromClause, params);
        List<Object> dataParams = new ArrayList<>(params);
        dataParams.add(pageSize);
        dataParams.add(offset);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                selectClause + fromClause + " ORDER BY " + resolvedSortColumn + " " + resolvedSortOrder
                        + " LIMIT ? OFFSET ?",
                dataParams.toArray()
        );
        return new PageResult<>(list, total, page, pageSize);
    }

    private void appendEquals(StringBuilder sql, List<Object> params, String column, String value) {
        if (StringUtils.hasText(value)) {
            sql.append(" AND ").append(column).append(" = ?");
            params.add(value);
        }
    }

    private void appendTimeRange(StringBuilder sql, List<Object> params, Long startTimeMs, Long endTimeMs, String column) {
        if (startTimeMs != null) {
            sql.append(" AND ").append(column).append(" >= ?");
            params.add(startTimeMs);
        }
        if (endTimeMs != null) {
            sql.append(" AND ").append(column).append(" <= ?");
            params.add(endTimeMs);
        }
    }

    private void appendTaskWindow(StringBuilder sql, List<Object> params, Long startTimeMs, Long endTimeMs) {
        if (startTimeMs != null) {
            sql.append(" AND end_time_ms >= ?");
            params.add(startTimeMs);
        }
        if (endTimeMs != null) {
            sql.append(" AND start_time_ms <= ?");
            params.add(endTimeMs);
        }
    }

    private void appendGeoRange(StringBuilder sql, List<Object> params, Double lng, Double lat, Double radius, String bbox) {
        if (StringUtils.hasText(bbox)) {
            String[] parts = bbox.split(",");
            if (parts.length != 4) {
                throw new ApiException(40000, "bbox must be minLng,minLat,maxLng,maxLat");
            }
            sql.append(" AND lng BETWEEN ? AND ? AND lat BETWEEN ? AND ?");
            params.add(Double.parseDouble(parts[0]));
            params.add(Double.parseDouble(parts[2]));
            params.add(Double.parseDouble(parts[1]));
            params.add(Double.parseDouble(parts[3]));
            return;
        }
        if (lng != null && lat != null && radius != null) {
            double lngDelta = radius / 111000.0;
            double latDelta = radius / 111000.0;
            sql.append(" AND lng BETWEEN ? AND ? AND lat BETWEEN ? AND ?");
            params.add(lng - lngDelta);
            params.add(lng + lngDelta);
            params.add(lat - latDelta);
            params.add(lat + latDelta);
        }
    }

    private long queryCount(String sql) {
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null ? 0L : count;
    }

    private long queryCount(String sql, List<Object> params) {
        Long count = jdbcTemplate.queryForObject(sql, Long.class, params.toArray());
        return count == null ? 0L : count;
    }

    private Map<String, String> mapOf(String... values) {
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            result.put(values[i], values[i + 1]);
        }
        return result;
    }
}
