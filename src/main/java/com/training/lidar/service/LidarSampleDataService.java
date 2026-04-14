package com.training.lidar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.lidar.common.ApiException;
import com.training.lidar.common.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("!mysql")
@Service
public class LidarSampleDataService implements LidarDataService {

    private final ObjectMapper objectMapper;
    private final Map<String, Object> dataStore;

    public LidarSampleDataService(ObjectMapper objectMapper,
                                  @Value("classpath:sample-data/lidar-sample-data.json") Resource resource)
            throws IOException {
        this.objectMapper = objectMapper;
        try (InputStream inputStream = resource.getInputStream()) {
            this.dataStore = objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() { });
        }
    }

    public Map<String, Object> getRealtimeSummary() {
        return copyMap(getMap("realtimeSummary"));
    }

    public PageResult<Map<String, Object>> getRealtimeTargets(String vehicleId, String deviceId, String status,
                                                              Long startTimeMs, Long endTimeMs, Double lng, Double lat,
                                                              Double radius, String bbox, int page, int pageSize,
                                                              String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("realtimeTargets"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterEquals(filtered, "deviceId", deviceId);
        filtered = filterEquals(filtered, "status", status);
        filtered = filterTime(filtered, startTimeMs, endTimeMs, "timestampMs");
        filtered = filterByGeo(filtered, lng, lat, radius, bbox);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "timestampMs");
    }

    public PageResult<Map<String, Object>> getVehicleList(String vehicleId, String status, int page, int pageSize,
                                                          String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("vehicleList"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterEquals(filtered, "status", status);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "lastReportTimeMs");
    }

    public PageResult<Map<String, Object>> getRealtimeEvents(String vehicleId, String eventType, String eventLevel,
                                                             Long startTimeMs, Long endTimeMs, int page, int pageSize,
                                                             String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("events"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterEquals(filtered, "eventType", eventType);
        filtered = filterEquals(filtered, "eventLevel", eventLevel);
        filtered = filterTime(filtered, startTimeMs, endTimeMs, "timestampMs");
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "timestampMs");
    }

    public PageResult<Map<String, Object>> getHistoryTasks(String vehicleId, Long startTimeMs, Long endTimeMs,
                                                           int page, int pageSize, String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("historyTasks"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterTaskWindow(filtered, startTimeMs, endTimeMs);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "startTimeMs");
    }

    public Map<String, Object> getHistoryDetail(String taskId) {
        Map<String, Object> detail = getHistoryMap("historyDetails").get(taskId);
        if (detail == null) {
            throw new ApiException(40400, "history task not found");
        }
        return copyMap(detail);
    }

    public PageResult<Map<String, Object>> getHistoryEvents(String taskId, String vehicleId, Long startTimeMs,
                                                            Long endTimeMs, int page, int pageSize, String sortBy,
                                                            String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("historyEvents"));
        filtered = filterEquals(filtered, "taskId", taskId);
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterTime(filtered, startTimeMs, endTimeMs, "timestampMs");
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "timestampMs");
    }

    public Map<String, Object> getHistoryAnalysis(String taskId) {
        Map<String, Object> analysis = getHistoryMap("historyAnalysis").get(taskId);
        if (analysis == null) {
            throw new ApiException(40400, "history analysis not found");
        }
        return copyMap(analysis);
    }

    public Map<String, Object> getVideoSummary() {
        return copyMap(getMap("videoSummary"));
    }

    public PageResult<Map<String, Object>> getPlaybackList(String vehicleId, String channel, Long startTimeMs,
                                                           Long endTimeMs, int page, int pageSize, String sortBy,
                                                           String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("playbackList"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterEquals(filtered, "channel", channel);
        filtered = filterTaskWindow(filtered, startTimeMs, endTimeMs);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "startTimeMs");
    }

    public PageResult<Map<String, Object>> getDownloadList(String vehicleId, String status, int page, int pageSize,
                                                           String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("downloadList"));
        filtered = filterEquals(filtered, "vehicleId", vehicleId);
        filtered = filterEquals(filtered, "status", status);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "createdTimeMs");
    }

    public Map<String, Object> getAnalysisSummary() {
        return copyMap(getMap("analysisSummary"));
    }

    public List<Map<String, Object>> getDistanceDistribution() {
        return copyList(getList("distanceDistribution"));
    }

    public PageResult<Map<String, Object>> getDeviceStats(String deviceId, String status, int page, int pageSize,
                                                          String sortBy, String sortOrder) {
        List<Map<String, Object>> filtered = new ArrayList<>(getList("deviceStats"));
        filtered = filterEquals(filtered, "deviceId", deviceId);
        filtered = filterEquals(filtered, "status", status);
        return paginateAndSort(filtered, page, pageSize, sortBy, sortOrder, "lastReportTimeMs");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMap(String key) {
        Object value = dataStore.get(key);
        if (!(value instanceof Map<?, ?> map)) {
            throw new ApiException(50000, "invalid sample data section: " + key);
        }
        return (Map<String, Object>) map;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Object>> getHistoryMap(String key) {
        Object value = dataStore.get(key);
        if (!(value instanceof Map<?, ?> map)) {
            throw new ApiException(50000, "invalid sample data section: " + key);
        }
        return (Map<String, Map<String, Object>>) map;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getList(String key) {
        Object value = dataStore.get(key);
        if (!(value instanceof List<?> list)) {
            throw new ApiException(50000, "invalid sample data section: " + key);
        }
        return (List<Map<String, Object>>) list;
    }

    private Map<String, Object> copyMap(Map<String, Object> source) {
        return objectMapper.convertValue(source, new TypeReference<Map<String, Object>>() { });
    }

    private List<Map<String, Object>> copyList(List<Map<String, Object>> source) {
        return objectMapper.convertValue(source, new TypeReference<List<Map<String, Object>>>() { });
    }

    private List<Map<String, Object>> filterEquals(List<Map<String, Object>> source, String field, String value) {
        if (!StringUtils.hasText(value)) {
            return source;
        }
        return source.stream().filter(item -> value.equalsIgnoreCase(String.valueOf(item.get(field))))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> filterTime(List<Map<String, Object>> source, Long startTimeMs, Long endTimeMs,
                                                 String field) {
        if (startTimeMs == null && endTimeMs == null) {
            return source;
        }
        return source.stream().filter(item -> {
            Long current = toLong(item.get(field));
            if (current == null) {
                return false;
            }
            if (startTimeMs != null && current < startTimeMs) {
                return false;
            }
            return endTimeMs == null || current <= endTimeMs;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> filterTaskWindow(List<Map<String, Object>> source, Long startTimeMs,
                                                       Long endTimeMs) {
        if (startTimeMs == null && endTimeMs == null) {
            return source;
        }
        return source.stream().filter(item -> {
            Long currentStart = toLong(item.get("startTimeMs"));
            Long currentEnd = toLong(item.get("endTimeMs"));
            if (currentStart == null || currentEnd == null) {
                return false;
            }
            if (startTimeMs != null && currentEnd < startTimeMs) {
                return false;
            }
            return endTimeMs == null || currentStart <= endTimeMs;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> filterByGeo(List<Map<String, Object>> source, Double lng, Double lat,
                                                  Double radius, String bbox) {
        if (StringUtils.hasText(bbox)) {
            String[] parts = bbox.split(",");
            if (parts.length != 4) {
                throw new ApiException(40000, "bbox must be minLng,minLat,maxLng,maxLat");
            }
            double minLng = Double.parseDouble(parts[0]);
            double minLat = Double.parseDouble(parts[1]);
            double maxLng = Double.parseDouble(parts[2]);
            double maxLat = Double.parseDouble(parts[3]);
            return source.stream().filter(item -> insideBox(item, minLng, minLat, maxLng, maxLat))
                    .collect(Collectors.toList());
        }
        if (lng == null || lat == null || radius == null) {
            return source;
        }
        return source.stream().filter(item -> withinRadius(item, lng, lat, radius)).collect(Collectors.toList());
    }

    private boolean insideBox(Map<String, Object> item, double minLng, double minLat, double maxLng, double maxLat) {
        Double itemLng = toDouble(item.get("lng"));
        Double itemLat = toDouble(item.get("lat"));
        return itemLng != null && itemLat != null && itemLng >= minLng && itemLng <= maxLng
                && itemLat >= minLat && itemLat <= maxLat;
    }

    private boolean withinRadius(Map<String, Object> item, double lng, double lat, double radiusMeters) {
        Double itemLng = toDouble(item.get("lng"));
        Double itemLat = toDouble(item.get("lat"));
        if (itemLng == null || itemLat == null) {
            return false;
        }
        double earthRadius = 6371000.0;
        double latDistance = Math.toRadians(itemLat - lat);
        double lngDistance = Math.toRadians(itemLng - lng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(itemLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c <= radiusMeters;
    }

    private PageResult<Map<String, Object>> paginateAndSort(List<Map<String, Object>> source, int page, int pageSize,
                                                            String sortBy, String sortOrder, String defaultSortField) {
        if (page < 1 || pageSize < 1) {
            throw new ApiException(40000, "page and pageSize must be greater than 0");
        }
        String sortField = StringUtils.hasText(sortBy) ? sortBy : defaultSortField;
        boolean desc = !"asc".equalsIgnoreCase(sortOrder);
        List<Map<String, Object>> copied = copyList(source);
        copied.sort(buildComparator(sortField, desc));
        int fromIndex = Math.min((page - 1) * pageSize, copied.size());
        int toIndex = Math.min(fromIndex + pageSize, copied.size());
        return new PageResult<>(copied.subList(fromIndex, toIndex), copied.size(), page, pageSize);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Comparator<Map<String, Object>> buildComparator(String field, boolean desc) {
        Comparator<Map<String, Object>> comparator = Comparator.comparing(
                item -> comparableValue(item.get(field)),
                Comparator.nullsLast(Comparator.naturalOrder())
        );
        return desc ? comparator.reversed() : comparator;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Comparable comparableValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof Comparable comparable) {
            return comparable;
        }
        return String.valueOf(value);
    }

    private Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String stringValue && StringUtils.hasText(stringValue)) {
            return Long.parseLong(stringValue);
        }
        return null;
    }

    private Double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String stringValue && StringUtils.hasText(stringValue)) {
            return Double.parseDouble(stringValue);
        }
        return null;
    }
}
