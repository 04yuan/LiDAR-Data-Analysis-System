INSERT INTO lidar_device (device_id, device_name, vehicle_id, vehicle_name, device_model, status, lng, lat, speed_kph, heading, last_report_time_ms, created_time_ms, updated_time_ms) VALUES
('lidar-c32-01', 'LeiShen C32 #01', 'vehicle-lidar-01', 'Training Car 01', 'C32', 'online', 106.354816, 29.549742, 0.03, 13.38, 1768206563238, 1768206500000, 1768206563238),
('lidar-c32-02', 'LeiShen C32 #02', 'vehicle-lidar-02', 'Training Car 02', 'C32', 'online', 106.354773, 29.549771, 1.42, 11.82, 1768206563440, 1768206400000, 1768206563440),
('lidar-c32-03', 'LeiShen C32 #03', 'vehicle-lidar-03', 'Training Car 03', 'C32', 'offline', 106.354701, 29.549824, 0.00, 8.30, 1768206500000, 1768206200000, 1768206500000)
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_time_ms = VALUES(updated_time_ms);

INSERT INTO lidar_realtime_target (target_id, vehicle_id, vehicle_name, device_id, target_type, status, distance_m, intensity, point_count, lng, lat, heading, timestamp_ms, created_time_ms) VALUES
('lidar-target-001', 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'vehicle', 'online', 7.996, 67, 1280, 106.354816, 29.549742, 13.38, 1768206563238, 1768206563238),
('lidar-target-002', 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'pedestrian', 'online', 8.504, 68, 640, 106.354809, 29.549748, 13.49, 1768206563250, 1768206563250),
('lidar-target-003', 'vehicle-lidar-02', 'Training Car 02', 'lidar-c32-02', 'cone', 'online', 12.640, 54, 220, 106.354773, 29.549771, 11.82, 1768206563380, 1768206563380),
('lidar-target-004', 'vehicle-lidar-02', 'Training Car 02', 'lidar-c32-02', 'vehicle', 'online', 17.300, 72, 1560, 106.354739, 29.549794, 10.40, 1768206563440, 1768206563440),
('lidar-target-005', 'vehicle-lidar-03', 'Training Car 03', 'lidar-c32-03', 'vehicle', 'offline', 25.110, 44, 980, 106.354701, 29.549824, 9.90, 1768206500000, 1768206500000),
('lidar-target-006', 'vehicle-lidar-03', 'Training Car 03', 'lidar-c32-03', 'barrier', 'offline', 31.424, 39, 410, 106.354667, 29.549851, 8.30, 1768206499000, 1768206499000)
ON DUPLICATE KEY UPDATE status = VALUES(status), timestamp_ms = VALUES(timestamp_ms);

INSERT INTO lidar_event (event_id, task_id, vehicle_id, vehicle_name, device_id, event_type, event_name, event_level, message, timestamp_ms, created_time_ms) VALUES
('lidar-event-001', NULL, 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'point-cloud-drop', 'Point Cloud Drop', 'warning', 'Point density dropped below the configured threshold.', 1768206563200, 1768206563200),
('lidar-event-002', NULL, 'vehicle-lidar-02', 'Training Car 02', 'lidar-c32-02', 'frame-rate-low', 'Frame Rate Low', 'warning', 'Frame rate temporarily dropped to 8.7 fps.', 1768206563390, 1768206563390),
('lidar-event-003', NULL, 'vehicle-lidar-03', 'Training Car 03', 'lidar-c32-03', 'sensor-occlusion', 'Sensor Occlusion', 'danger', 'Front-left field of view is partially blocked.', 1768206500100, 1768206500100),
('lidar-event-004', NULL, 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'tracking-delay', 'Tracking Delay', 'info', 'Object association latency exceeded 30 ms once.', 1768206563300, 1768206563300),
('lidar-history-event-001', 'lidar-task-20260112-01', 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'point-cloud-drop', 'Point Cloud Drop', 'warning', 'Point density dipped during low-speed maneuver.', 1768206563200, 1768206563200),
('lidar-history-event-002', 'lidar-task-20260112-01', 'vehicle-lidar-01', 'Training Car 01', 'lidar-c32-01', 'tracking-delay', 'Tracking Delay', 'info', 'Object association latency exceeded 30 ms once.', 1768206563300, 1768206563300),
('lidar-history-event-003', 'lidar-task-20260112-02', 'vehicle-lidar-02', 'Training Car 02', 'lidar-c32-02', 'frame-rate-low', 'Frame Rate Low', 'warning', 'Frame rate dropped during a sharp turn.', 1768206563390, 1768206563390),
('lidar-history-event-004', 'lidar-task-20260112-03', 'vehicle-lidar-03', 'Training Car 03', 'lidar-c32-03', 'sensor-occlusion', 'Sensor Occlusion', 'danger', 'Front-left field of view was blocked for 14 seconds.', 1768206300100, 1768206300100)
ON DUPLICATE KEY UPDATE message = VALUES(message), timestamp_ms = VALUES(timestamp_ms);

INSERT INTO lidar_history_task (task_id, vehicle_id, vehicle_name, start_time_ms, end_time_ms, duration_ms, distance_km, event_count, target_frame_count, avg_point_count, max_distance_m, created_time_ms, updated_time_ms) VALUES
('lidar-task-20260112-01', 'vehicle-lidar-01', 'Training Car 01', 1768206500000, 1768206680000, 180000, 0.31, 2, 320, 1180, 31.424, 1768206680000, 1768206680000),
('lidar-task-20260112-02', 'vehicle-lidar-02', 'Training Car 02', 1768206400000, 1768206610000, 210000, 0.52, 1, 410, 1324, 28.764, 1768206610000, 1768206610000),
('lidar-task-20260112-03', 'vehicle-lidar-03', 'Training Car 03', 1768206200000, 1768206380000, 180000, 0.18, 3, 205, 910, 25.110, 1768206380000, 1768206380000)
ON DUPLICATE KEY UPDATE updated_time_ms = VALUES(updated_time_ms), distance_km = VALUES(distance_km);

INSERT INTO lidar_history_track_point (task_id, vehicle_id, lng, lat, heading, speed_kph, timestamp_ms, created_time_ms) VALUES
('lidar-task-20260112-01', 'vehicle-lidar-01', 106.354818, 29.549742, 13.38, 0.10, 1768206500000, 1768206500000),
('lidar-task-20260112-01', 'vehicle-lidar-01', 106.354816, 29.549742, 13.38, 0.03, 1768206563238, 1768206563238),
('lidar-task-20260112-01', 'vehicle-lidar-01', 106.354810, 29.549747, 13.20, 0.20, 1768206620000, 1768206620000),
('lidar-task-20260112-01', 'vehicle-lidar-01', 106.354802, 29.549751, 13.00, 0.40, 1768206680000, 1768206680000),
('lidar-task-20260112-02', 'vehicle-lidar-02', 106.354790, 29.549760, 12.00, 3.20, 1768206400000, 1768206400000),
('lidar-task-20260112-02', 'vehicle-lidar-02', 106.354783, 29.549766, 11.90, 4.50, 1768206460000, 1768206460000),
('lidar-task-20260112-02', 'vehicle-lidar-02', 106.354773, 29.549771, 11.82, 1.42, 1768206563440, 1768206563440),
('lidar-task-20260112-02', 'vehicle-lidar-02', 106.354761, 29.549779, 11.10, 2.60, 1768206610000, 1768206610000),
('lidar-task-20260112-03', 'vehicle-lidar-03', 106.354722, 29.549810, 9.20, 0.80, 1768206200000, 1768206200000),
('lidar-task-20260112-03', 'vehicle-lidar-03', 106.354715, 29.549817, 8.90, 0.50, 1768206280000, 1768206280000),
('lidar-task-20260112-03', 'vehicle-lidar-03', 106.354701, 29.549824, 8.30, 0.00, 1768206380000, 1768206380000)
ON DUPLICATE KEY UPDATE speed_kph = VALUES(speed_kph), heading = VALUES(heading);

INSERT INTO lidar_video_device (channel, vehicle_id, camera_name, status, updated_time_ms) VALUES
('front-center', 'vehicle-lidar-01', 'Front Center Camera', 'online', 1768206565000),
('front-left', 'vehicle-lidar-01', 'Front Left Camera', 'online', 1768206565000),
('front-center-02', 'vehicle-lidar-02', 'Front Center Camera', 'online', 1768206568000),
('rear-center', 'vehicle-lidar-03', 'Rear Center Camera', 'offline', 1768206320000)
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_time_ms = VALUES(updated_time_ms);

INSERT INTO lidar_video_segment (playback_id, vehicle_id, channel, camera_name, start_time_ms, end_time_ms, duration_ms, status, stream_url, created_time_ms, updated_time_ms) VALUES
('lidar-playback-001', 'vehicle-lidar-01', 'front-center', 'Front Center Camera', 1768206500000, 1768206565000, 65000, 'ready', 'http://localhost:8080/mock/video/front-center', 1768206565000, 1768206565000),
('lidar-playback-002', 'vehicle-lidar-01', 'front-left', 'Front Left Camera', 1768206500000, 1768206565000, 65000, 'ready', 'http://localhost:8080/mock/video/front-left', 1768206565000, 1768206565000),
('lidar-playback-003', 'vehicle-lidar-02', 'front-center', 'Front Center Camera', 1768206460000, 1768206568000, 108000, 'ready', 'http://localhost:8080/mock/video/front-center', 1768206568000, 1768206568000),
('lidar-playback-004', 'vehicle-lidar-03', 'rear-center', 'Rear Center Camera', 1768206200000, 1768206320000, 120000, 'processing', 'http://localhost:8080/mock/video/rear-center', 1768206320000, 1768206320000)
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_time_ms = VALUES(updated_time_ms);

INSERT INTO lidar_download_task (download_id, vehicle_id, channel, status, format, created_time_ms, file_size_mb) VALUES
('lidar-download-001', 'vehicle-lidar-01', 'front-center', 'success', 'mp4', 1768206600000, 82.40),
('lidar-download-002', 'vehicle-lidar-03', 'rear-center', 'processing', 'mp4', 1768206340000, 0.00)
ON DUPLICATE KEY UPDATE status = VALUES(status), file_size_mb = VALUES(file_size_mb);

INSERT INTO lidar_analysis_snapshot (snapshot_code, scope_type, task_id, target_count, fusion_success_rate, abnormal_frame_count, stop_point_count, avg_speed_kph, max_distance_m, timestamp_ms, created_time_ms) VALUES
('summary-001', 'SUMMARY', NULL, 5048, 0.9100, 41, 0, 0.00, 31.424, 1768206563238, 1768206563238),
('task-001', 'TASK', 'lidar-task-20260112-01', 1880, 0.9300, 12, 1, 6.20, 31.424, 1768206680000, 1768206680000),
('task-002', 'TASK', 'lidar-task-20260112-02', 2214, 0.9100, 8, 0, 8.90, 28.764, 1768206610000, 1768206610000),
('task-003', 'TASK', 'lidar-task-20260112-03', 954, 0.8500, 21, 2, 3.60, 25.110, 1768206380000, 1768206380000)
ON DUPLICATE KEY UPDATE timestamp_ms = VALUES(timestamp_ms), target_count = VALUES(target_count);

INSERT INTO lidar_distance_distribution (bucket_code, scope_type, bucket_label, bucket_order, bucket_count, timestamp_ms) VALUES
('bucket-001', 'SUMMARY', '0-5m', 1, 162, 1768206563238),
('bucket-002', 'SUMMARY', '5-10m', 2, 624, 1768206563238),
('bucket-003', 'SUMMARY', '10-20m', 3, 1186, 1768206563238),
('bucket-004', 'SUMMARY', '20-30m', 4, 921, 1768206563238),
('bucket-005', 'SUMMARY', '30m+', 5, 213, 1768206563238)
ON DUPLICATE KEY UPDATE bucket_count = VALUES(bucket_count), timestamp_ms = VALUES(timestamp_ms);

INSERT INTO lidar_device_stat (device_id, device_name, status, frame_rate_fps, point_density_per_frame, temperature_c, last_report_time_ms) VALUES
('lidar-c32-01', 'LeiShen C32 #01', 'online', 9.80, 1180, 42.10, 1768206563238),
('lidar-c32-02', 'LeiShen C32 #02', 'online', 8.70, 1324, 44.50, 1768206563440),
('lidar-c32-03', 'LeiShen C32 #03', 'offline', 0.00, 910, 39.00, 1768206500000)
ON DUPLICATE KEY UPDATE status = VALUES(status), last_report_time_ms = VALUES(last_report_time_ms);
