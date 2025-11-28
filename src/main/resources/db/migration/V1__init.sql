/*
 * MediGuard CMS Database Initialization Script
 * Version: 1.0
 * Description: 初始化数据库表结构，包含用户、科室、患者、设备、报警及生命体征数据表
 */

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `role` varchar(20) DEFAULT NULL COMMENT '角色',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` varchar(32) NOT NULL COMMENT '科室ID',
  `code` varchar(20) NOT NULL COMMENT '科室代码',
  `name` varchar(50) NOT NULL COMMENT '科室名称',
  `capacity` int DEFAULT 0 COMMENT '床位容量',
  `bed_labels` json DEFAULT NULL COMMENT '床位标签配置(JSON)',
  `device_configs` json DEFAULT NULL COMMENT '设备显示模板配置(JSON)',
  `alarm_thresholds` json DEFAULT NULL COMMENT '报警阈值策略(JSON)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室配置表';

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category` varchar(50) NOT NULL COMMENT '字典分类',
  `code` varchar(50) NOT NULL COMMENT '字典代码',
  `label` varchar(50) NOT NULL COMMENT '字典标签',
  `sort` int DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典表';

-- ----------------------------
-- Table structure for dev_device
-- ----------------------------
DROP TABLE IF EXISTS `dev_device`;
CREATE TABLE `dev_device` (
  `device_id` varchar(50) NOT NULL COMMENT '设备ID',
  `serial_number` varchar(50) NOT NULL COMMENT '序列号',
  `device_type` varchar(20) NOT NULL COMMENT '设备类型',
  `department_code` varchar(20) DEFAULT NULL COMMENT '所属科室',
  `status` varchar(20) DEFAULT 'OFFLINE' COMMENT '状态(ONLINE/OFFLINE)',
  `ip_address` varchar(20) DEFAULT NULL COMMENT 'IP地址',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

-- ----------------------------
-- Table structure for pat_patient
-- ----------------------------
DROP TABLE IF EXISTS `pat_patient`;
CREATE TABLE `pat_patient` (
  `id` varchar(32) NOT NULL COMMENT '患者ID',
  `admission_id` varchar(50) NOT NULL COMMENT '住院号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `age` int DEFAULT NULL COMMENT '年龄',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `department_code` varchar(20) DEFAULT NULL COMMENT '所属科室',
  `bed_number` varchar(20) DEFAULT NULL COMMENT '床位号',
  `device_id` varchar(50) DEFAULT NULL COMMENT '绑定设备ID',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型',
  `status` varchar(20) DEFAULT 'NORMAL' COMMENT '状态(NORMAL/WARNING/CRITICAL)',
  `display_config` json DEFAULT NULL COMMENT '个性化显示配置(JSON)',
  `admission_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '入科时间',
  `discharge_time` datetime DEFAULT NULL COMMENT '出科时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept` (`department_code`),
  KEY `idx_device` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者信息表';

-- ----------------------------
-- Table structure for alm_alarm
-- ----------------------------
DROP TABLE IF EXISTS `alm_alarm`;
CREATE TABLE `alm_alarm` (
  `id` varchar(32) NOT NULL COMMENT '报警ID',
  `alarm_time` datetime NOT NULL COMMENT '报警时间',
  `patient_id` varchar(32) DEFAULT NULL COMMENT '患者ID',
  `patient_name` varchar(50) DEFAULT NULL COMMENT '患者姓名',
  `bed_number` varchar(20) DEFAULT NULL COMMENT '床位号',
  `department_code` varchar(20) DEFAULT NULL COMMENT '科室代码',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型',
  `category` varchar(20) DEFAULT NULL COMMENT '报警分类(PHYSIOLOGICAL/TECHNICAL)',
  `level` varchar(20) DEFAULT NULL COMMENT '报警级别(CRITICAL/WARNING/NORMAL)',
  `message` varchar(255) DEFAULT NULL COMMENT '报警信息',
  `acknowledged` tinyint(1) DEFAULT 0 COMMENT '是否确认',
  `snapshot` json DEFAULT NULL COMMENT '报警快照(JSON)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`alarm_time`),
  KEY `idx_patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警历史表';

-- ----------------------------
-- Table structure for pat_vital_sign
-- ----------------------------
DROP TABLE IF EXISTS `pat_vital_sign`;
CREATE TABLE `pat_vital_sign` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `patient_id` varchar(32) NOT NULL COMMENT '患者ID',
  `timestamp` datetime NOT NULL COMMENT '采集时间',
  `hr` int DEFAULT NULL COMMENT '心率',
  `spo2` int DEFAULT NULL COMMENT '血氧',
  `nibp_sys` int DEFAULT NULL COMMENT '收缩压',
  `nibp_dia` int DEFAULT NULL COMMENT '舒张压',
  `resp` int DEFAULT NULL COMMENT '呼吸率',
  `temp` decimal(4,1) DEFAULT NULL COMMENT '体温',
  PRIMARY KEY (`id`),
  KEY `idx_patient_time` (`patient_id`, `timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生命体征趋势表';

-- ----------------------------
-- Initial Data
-- ----------------------------

INSERT INTO `sys_dictionary` (`category`, `code`, `label`, `sort`) VALUES 
('DEVICE_TYPE', 'MONITOR', '监护仪', 1),
('DEVICE_TYPE', 'VENTILATOR', '呼吸机', 2),
('DEVICE_TYPE', 'ANESTHESIA', '麻醉机', 3),
('ALARM_LEVEL', 'CRITICAL', '危急', 1),
('ALARM_LEVEL', 'WARNING', '警告', 2),
('ALARM_LEVEL', 'NORMAL', '正常', 3);

INSERT INTO `sys_department` (`id`, `code`, `name`, `capacity`) VALUES 
('d1', 'ICU', '重症医学科', 20),
('d2', 'OR', '手术室', 8),
('d3', 'ER', '急诊科', 12);
