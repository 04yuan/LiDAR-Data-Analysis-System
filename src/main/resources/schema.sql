CREATE TABLE IF NOT EXISTS lidar_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    device_name VARCHAR(128) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    vehicle_name VARCHAR(128) NOT NULL,
    device_model VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    lng DECIMAL(10, 6),
    lat DECIMAL(10, 6),
    speed_kph DECIMAL(10, 2) DEFAULT 0,
    heading DECIMAL(10, 2) DEFAULT 0,
    last_report_time_ms BIGINT NOT NULL,
    created_time_ms BIGINT NOT NULL,
    updated_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_device_device_id (device_id)
);

CREATE TABLE IF NOT EXISTS lidar_realtime_target (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    vehicle_name VARCHAR(128) NOT NULL,
    device_id VARCHAR(64) NOT NULL,
    target_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    distance_m DECIMAL(10, 3) NOT NULL,
    intensity INT,
    point_count INT,
    lng DECIMAL(10, 6) NOT NULL,
    lat DECIMAL(10, 6) NOT NULL,
    heading DECIMAL(10, 2),
    timestamp_ms BIGINT NOT NULL,
    created_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_realtime_target_target_id (target_id)
);

CREATE TABLE IF NOT EXISTS lidar_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(64) NOT NULL,
    task_id VARCHAR(64),
    vehicle_id VARCHAR(64) NOT NULL,
    vehicle_name VARCHAR(128) NOT NULL,
    device_id VARCHAR(64) NOT NULL,
    event_type VARCHAR(64) NOT NULL,
    event_name VARCHAR(128) NOT NULL,
    event_level VARCHAR(32) NOT NULL,
    message VARCHAR(512) NOT NULL,
    timestamp_ms BIGINT NOT NULL,
    created_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_event_event_id (event_id)
);

CREATE TABLE IF NOT EXISTS lidar_history_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    vehicle_name VARCHAR(128) NOT NULL,
    start_time_ms BIGINT NOT NULL,
    end_time_ms BIGINT NOT NULL,
    duration_ms BIGINT NOT NULL,
    distance_km DECIMAL(10, 3) NOT NULL,
    event_count INT NOT NULL,
    target_frame_count INT DEFAULT 0,
    avg_point_count INT DEFAULT 0,
    max_distance_m DECIMAL(10, 3) DEFAULT 0,
    created_time_ms BIGINT NOT NULL,
    updated_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_history_task_task_id (task_id)
);

CREATE TABLE IF NOT EXISTS lidar_history_track_point (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    lng DECIMAL(10, 6) NOT NULL,
    lat DECIMAL(10, 6) NOT NULL,
    heading DECIMAL(10, 2),
    speed_kph DECIMAL(10, 2),
    timestamp_ms BIGINT NOT NULL,
    created_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_history_track_point_unique (task_id, timestamp_ms, lng, lat)
);

CREATE TABLE IF NOT EXISTS lidar_video_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    channel VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    camera_name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    updated_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_video_device_channel (channel)
);

CREATE TABLE IF NOT EXISTS lidar_video_segment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    playback_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    channel VARCHAR(64) NOT NULL,
    camera_name VARCHAR(128) NOT NULL,
    start_time_ms BIGINT NOT NULL,
    end_time_ms BIGINT NOT NULL,
    duration_ms BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    stream_url VARCHAR(512),
    created_time_ms BIGINT NOT NULL,
    updated_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_video_segment_playback_id (playback_id)
);

CREATE TABLE IF NOT EXISTS lidar_download_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    download_id VARCHAR(64) NOT NULL,
    vehicle_id VARCHAR(64) NOT NULL,
    channel VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    format VARCHAR(32) NOT NULL,
    created_time_ms BIGINT NOT NULL,
    file_size_mb DECIMAL(10, 2) DEFAULT 0,
    UNIQUE KEY uk_lidar_download_task_download_id (download_id)
);

CREATE TABLE IF NOT EXISTS lidar_analysis_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    snapshot_code VARCHAR(64) NOT NULL,
    scope_type VARCHAR(32) NOT NULL,
    task_id VARCHAR(64),
    target_count INT NOT NULL,
    fusion_success_rate DECIMAL(6, 4) NOT NULL,
    abnormal_frame_count INT NOT NULL,
    stop_point_count INT DEFAULT 0,
    avg_speed_kph DECIMAL(10, 2) DEFAULT 0,
    max_distance_m DECIMAL(10, 3) DEFAULT 0,
    timestamp_ms BIGINT NOT NULL,
    created_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_analysis_snapshot_snapshot_code (snapshot_code)
);

CREATE TABLE IF NOT EXISTS lidar_distance_distribution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bucket_code VARCHAR(64) NOT NULL,
    scope_type VARCHAR(32) NOT NULL,
    bucket_label VARCHAR(64) NOT NULL,
    bucket_order INT NOT NULL,
    bucket_count INT NOT NULL,
    timestamp_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_distance_distribution_bucket_code (bucket_code)
);

CREATE TABLE IF NOT EXISTS lidar_device_stat (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    device_name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    frame_rate_fps DECIMAL(10, 2) NOT NULL,
    point_density_per_frame INT NOT NULL,
    temperature_c DECIMAL(10, 2) NOT NULL,
    last_report_time_ms BIGINT NOT NULL,
    UNIQUE KEY uk_lidar_device_stat_device_id (device_id)
);
