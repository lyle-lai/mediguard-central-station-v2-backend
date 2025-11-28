/*
 * MediGuard CMS Database Refactoring Script
 * Version: 2.0
 * Description: 重构数据库表结构
 * 1. 使用枚举替代字符串
 * 2. Department 配置改为关联表
 * 3. 显示配置绑定床位而非患者
 * 4. VitalSign 参数动态化
 */

-- ----------------------------
-- 1. 修改 sys_department 表（移除 JSON 字段）
-- ----------------------------
-- 注意：如果这些列不存在会报错，但不影响后续执行
ALTER TABLE `sys_department` DROP COLUMN `bed_labels`;
ALTER TABLE `sys_department` DROP COLUMN `device_configs`;
ALTER TABLE `sys_department` DROP COLUMN `alarm_thresholds`;

-- ----------------------------
-- 2. 修改 pat_patient 表（移除 display_config，使用枚举）
-- ----------------------------
ALTER TABLE `pat_patient` DROP COLUMN `display_config`;
ALTER TABLE `pat_patient` MODIFY COLUMN `device_type` VARCHAR(20) COMMENT '设备类型(枚举: MONITOR/VENTILATOR/ANESTHESIA)';
ALTER TABLE `pat_patient` MODIFY COLUMN `status` VARCHAR(20) COMMENT '状态(枚举: NORMAL/WARNING/CRITICAL)';

-- ----------------------------
-- 2.1 创建用户-科室关联表（多对多）
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_department`;
CREATE TABLE `sys_user_department` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_dept` (`user_id`, `department_code`),
  KEY `idx_user` (`user_id`),
  KEY `idx_dept` (`department_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户科室关联表';

-- ----------------------------
-- 3. 修改 dev_device 表（使用枚举）
-- ----------------------------
ALTER TABLE `dev_device` MODIFY COLUMN `device_type` VARCHAR(20) COMMENT '设备类型(枚举: MONITOR/VENTILATOR/ANESTHESIA)';
ALTER TABLE `dev_device` MODIFY COLUMN `status` VARCHAR(20) COMMENT '状态(枚举: ONLINE/OFFLINE)';

-- ----------------------------
-- 4. 修改 alm_alarm 表（使用枚举）
-- ----------------------------
ALTER TABLE `alm_alarm` MODIFY COLUMN `device_type` VARCHAR(20) COMMENT '设备类型(枚举: MONITOR/VENTILATOR/ANESTHESIA)';
ALTER TABLE `alm_alarm` MODIFY COLUMN `category` VARCHAR(20) COMMENT '报警分类(枚举: PHYSIOLOGICAL/TECHNICAL)';
ALTER TABLE `alm_alarm` MODIFY COLUMN `level` VARCHAR(20) COMMENT '报警级别(枚举: CRITICAL/WARNING/NORMAL)';

-- ----------------------------
-- 5. 创建设备波形配置表
-- ----------------------------
DROP TABLE IF EXISTS `cfg_device_waveform`;
CREATE TABLE `cfg_device_waveform` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `device_type` varchar(20) NOT NULL COMMENT '设备类型(枚举)',
  `waveform_id` varchar(50) NOT NULL COMMENT '波形ID',
  `label` varchar(50) NOT NULL COMMENT '波形标签',
  `visible` tinyint(1) DEFAULT 1 COMMENT '是否可见',
  `color` varchar(20) DEFAULT NULL COMMENT '颜色',
  `display_order` int DEFAULT 0 COMMENT '显示顺序',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept_device` (`department_code`, `device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备波形配置表';

-- ----------------------------
-- 6. 创建设备参数配置表
-- ----------------------------
DROP TABLE IF EXISTS `cfg_device_parameter`;
CREATE TABLE `cfg_device_parameter` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `device_type` varchar(20) NOT NULL COMMENT '设备类型(枚举)',
  `parameter_id` varchar(50) NOT NULL COMMENT '参数ID',
  `label` varchar(50) NOT NULL COMMENT '参数标签',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `visible` tinyint(1) DEFAULT 1 COMMENT '是否可见',
  `display_order` int DEFAULT 0 COMMENT '显示顺序',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept_device` (`department_code`, `device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备参数配置表';

-- ----------------------------
-- 7. 创建报警阈值配置表
-- ----------------------------
DROP TABLE IF EXISTS `cfg_alarm_threshold`;
CREATE TABLE `cfg_alarm_threshold` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `param_id` varchar(50) NOT NULL COMMENT '参数ID',
  `label` varchar(50) NOT NULL COMMENT '参数标签',
  `min_value` int DEFAULT NULL COMMENT '最小值',
  `max_value` int DEFAULT NULL COMMENT '最大值',
  `delay_seconds` int DEFAULT 0 COMMENT '延迟时间(秒)',
  `priority` varchar(20) DEFAULT NULL COMMENT '优先级(枚举: CRITICAL/WARNING/NORMAL)',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept` (`department_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警阈值配置表';

-- ----------------------------
-- 8. 创建床位标签配置表
-- ----------------------------
DROP TABLE IF EXISTS `cfg_bed_label`;
CREATE TABLE `cfg_bed_label` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `bed_number` varchar(20) NOT NULL COMMENT '床位号',
  `custom_label` varchar(50) DEFAULT NULL COMMENT '自定义标签',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_bed` (`department_code`, `bed_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位标签配置表';

-- ----------------------------
-- 9. 创建床位显示配置表（绑定床位而非患者）
-- ----------------------------
DROP TABLE IF EXISTS `cfg_bed_display`;
CREATE TABLE `cfg_bed_display` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `department_code` varchar(20) NOT NULL COMMENT '科室代码',
  `bed_number` varchar(20) NOT NULL COMMENT '床位号',
  `waveform_id` varchar(50) DEFAULT NULL COMMENT '波形ID',
  `parameter_id` varchar(50) DEFAULT NULL COMMENT '参数ID',
  `visible` tinyint(1) DEFAULT 1 COMMENT '是否可见',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept_bed` (`department_code`, `bed_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位显示配置表';

-- ----------------------------
-- 10. 重建生命体征数据表（动态参数）
-- ----------------------------
DROP TABLE IF EXISTS `pat_vital_sign`;
DROP TABLE IF EXISTS `pat_vital_sign_data`;
CREATE TABLE `pat_vital_sign_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `patient_id` varchar(32) NOT NULL COMMENT '患者ID',
  `timestamp` datetime NOT NULL COMMENT '采集时间',
  `parameter_id` varchar(50) NOT NULL COMMENT '参数ID',
  `parameter_value` varchar(50) NOT NULL COMMENT '参数值',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_patient_time` (`patient_id`, `timestamp`),
  KEY `idx_param` (`parameter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生命体征数据表(动态参数)';

-- ----------------------------
-- 11. 初始化配置数据（示例）
-- ----------------------------
-- ICU 监护仪波形配置
INSERT INTO `cfg_device_waveform` (`department_code`, `device_type`, `waveform_id`, `label`, `visible`, `color`, `display_order`) VALUES
('ICU', 'MONITOR', 'ecg', 'ECG I', 1, '#00ff41', 1),
('ICU', 'MONITOR', 'spo2', 'SpO2', 1, '#00d0ff', 2),
('ICU', 'MONITOR', 'resp', 'Resp', 1, '#ffea00', 3);

-- ICU 监护仪参数配置
INSERT INTO `cfg_device_parameter` (`department_code`, `device_type`, `parameter_id`, `label`, `unit`, `visible`, `display_order`) VALUES
('ICU', 'MONITOR', 'hr', 'HR', 'bpm', 1, 1),
('ICU', 'MONITOR', 'spo2', 'SpO2', '%', 1, 2),
('ICU', 'MONITOR', 'nibp', 'NIBP', 'mmHg', 1, 3);

-- ICU 报警阈值配置
INSERT INTO `cfg_alarm_threshold` (`department_code`, `param_id`, `label`, `min_value`, `max_value`, `delay_seconds`, `priority`, `enabled`) VALUES
('ICU', 'hr', '心率', 50, 120, 2, 'CRITICAL', 1),
('ICU', 'spo2', '血氧', 90, 100, 5, 'CRITICAL', 1);
