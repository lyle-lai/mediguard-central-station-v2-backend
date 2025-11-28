/*
 * MediGuard CMS Test Data Script
 * Version: 3.0
 * Description: 添加测试数据用于前端联调
 */

-- ----------------------------
-- 1. 插入测试科室数据
-- ----------------------------
INSERT INTO `sys_department` (`id`, `code`, `name`, `capacity`) VALUES
('dept_001', 'ICU', '重症监护室', 12),
('dept_002', 'CCU', '心脏监护室', 8),
('dept_003', 'NICU', '新生儿监护室', 10)
AS new_dept
ON DUPLICATE KEY UPDATE 
    `name` = new_dept.name, 
    `capacity` = new_dept.capacity;

-- ----------------------------
-- 2. 插入测试用户数据
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `role`) VALUES
('user_001', 'admin', 'admin123', '系统管理员', 'ADMIN'),
('user_002', 'doctor_icu', 'doctor123', '张医生', 'DOCTOR'),
('user_003', 'nurse_icu', 'nurse123', '李护士', 'NURSE'),
('user_004', 'doctor_ccu', 'doctor123', '王医生', 'DOCTOR')
AS new_user
ON DUPLICATE KEY UPDATE 
    `password` = new_user.password, 
    `name` = new_user.name, 
    `role` = new_user.role;

-- ----------------------------
-- 2.1 插入用户-科室关联数据（多对多）
-- ----------------------------
INSERT IGNORE INTO `sys_user_department` (`user_id`, `department_code`) VALUES
('user_001', 'ICU'),   -- admin 可以访问 ICU
('user_001', 'CCU'),   -- admin 可以访问 CCU
('user_001', 'NICU'),  -- admin 可以访问 NICU
('user_002', 'ICU'),   -- doctor_icu 访问 ICU
('user_003', 'ICU'),   -- nurse_icu 访问 ICU
('user_004', 'CCU'),   -- doctor_ccu 访问 CCU
('user_004', 'ICU');   -- doctor_ccu 也可以访问 ICU

-- ----------------------------
-- 3. 插入测试设备数据
-- ----------------------------
INSERT INTO `dev_device` (`device_id`, `serial_number`, `device_type`, `department_code`, `status`, `ip_address`) VALUES
('MONITOR_001', 'SN-M-10001', 'MONITOR', 'ICU', 'ONLINE', '192.168.1.101'),
('MONITOR_002', 'SN-M-10002', 'MONITOR', 'ICU', 'ONLINE', '192.168.1.102'),
('MONITOR_003', 'SN-M-10003', 'MONITOR', 'ICU', 'ONLINE', '192.168.1.103'),
('MONITOR_004', 'SN-M-10004', 'MONITOR', 'CCU', 'ONLINE', '192.168.1.104'),
('VENTILATOR_001', 'SN-V-20001', 'VENTILATOR', 'ICU', 'ONLINE', '192.168.1.201'),
('VENTILATOR_002', 'SN-V-20002', 'VENTILATOR', 'ICU', 'OFFLINE', '192.168.1.202'),
('ANESTHESIA_001', 'SN-A-30001', 'ANESTHESIA', 'ICU', 'ONLINE', '192.168.1.301')
AS new_device
ON DUPLICATE KEY UPDATE 
    `status` = new_device.status, 
    `ip_address` = new_device.ip_address;

-- ----------------------------
-- 4. 插入测试患者数据
-- ----------------------------
INSERT INTO `pat_patient` (`id`, `name`, `age`, `gender`, `admission_id`, `bed_number`, `department_code`, `device_id`, `device_type`, `status`, `admission_time`) VALUES
('patient_001', '张三', 45, '男', 'ADM-2024-001', 'ICU-01', 'ICU', 'MONITOR_001', 'MONITOR', 'NORMAL', '2024-11-20 08:00:00'),
('patient_002', '李四', 62, '女', 'ADM-2024-002', 'ICU-02', 'ICU', 'MONITOR_002', 'MONITOR', 'WARNING', '2024-11-21 10:30:00'),
('patient_003', '王五', 38, '男', 'ADM-2024-003', 'ICU-03', 'ICU', 'MONITOR_003', 'MONITOR', 'CRITICAL', '2024-11-22 14:20:00'),
('patient_004', '赵六', 55, '女', 'ADM-2024-004', 'CCU-01', 'CCU', 'MONITOR_004', 'MONITOR', 'NORMAL', '2024-11-23 09:15:00')
AS new_patient
ON DUPLICATE KEY UPDATE 
    `status` = new_patient.status,
    `device_id` = new_patient.device_id;

-- ----------------------------
-- 5. ICU 监护仪波形配置（如果不存在）
-- ----------------------------
INSERT IGNORE INTO `cfg_device_waveform` (`department_code`, `device_type`, `waveform_id`, `label`, `visible`, `color`, `display_order`) VALUES
('ICU', 'MONITOR', 'ecg', 'ECG I', 1, '#00ff41', 1),
('ICU', 'MONITOR', 'spo2', 'SpO2', 1, '#00d0ff', 2),
('ICU', 'MONITOR', 'resp', 'Resp', 1, '#ffea00', 3),
('ICU', 'MONITOR', 'ibp', 'IBP', 1, '#ff0000', 4);

-- ----------------------------
-- 6. ICU 监护仪参数配置（如果不存在）
-- ----------------------------
INSERT IGNORE INTO `cfg_device_parameter` (`department_code`, `device_type`, `parameter_id`, `label`, `unit`, `visible`, `display_order`) VALUES
('ICU', 'MONITOR', 'hr', 'HR', 'bpm', 1, 1),
('ICU', 'MONITOR', 'spo2', 'SpO2', '%', 1, 2),
('ICU', 'MONITOR', 'nibp', 'NIBP', 'mmHg', 1, 3),
('ICU', 'MONITOR', 'rr', 'RR', 'rpm', 1, 4),
('ICU', 'MONITOR', 'temp', 'Temp', '°C', 1, 5);

-- ----------------------------
-- 7. ICU 报警阈值配置（如果不存在）
-- ----------------------------
INSERT IGNORE INTO `cfg_alarm_threshold` (`department_code`, `param_id`, `label`, `min_value`, `max_value`, `delay_seconds`, `priority`, `enabled`) VALUES
('ICU', 'hr', '心率', 50, 120, 2, 'CRITICAL', 1),
('ICU', 'spo2', '血氧', 90, 100, 5, 'CRITICAL', 1),
('ICU', 'rr', '呼吸率', 10, 30, 3, 'WARNING', 1),
('ICU', 'temp', '体温', 36, 38, 10, 'WARNING', 1);

-- ----------------------------
-- 8. CCU 配置（如果不存在）
-- ----------------------------
INSERT IGNORE INTO `cfg_device_waveform` (`department_code`, `device_type`, `waveform_id`, `label`, `visible`, `color`, `display_order`) VALUES
('CCU', 'MONITOR', 'ecg', 'ECG II', 1, '#00ff41', 1),
('CCU', 'MONITOR', 'spo2', 'SpO2', 1, '#00d0ff', 2);

INSERT IGNORE INTO `cfg_device_parameter` (`department_code`, `device_type`, `parameter_id`, `label`, `unit`, `visible`, `display_order`) VALUES
('CCU', 'MONITOR', 'hr', 'HR', 'bpm', 1, 1),
('CCU', 'MONITOR', 'spo2', 'SpO2', '%', 1, 2),
('CCU', 'MONITOR', 'nibp', 'NIBP', 'mmHg', 1, 3);

INSERT IGNORE INTO `cfg_alarm_threshold` (`department_code`, `param_id`, `label`, `min_value`, `max_value`, `delay_seconds`, `priority`, `enabled`) VALUES
('CCU', 'hr', '心率', 45, 110, 2, 'CRITICAL', 1),
('CCU', 'spo2', '血氧', 92, 100, 5, 'CRITICAL', 1);
