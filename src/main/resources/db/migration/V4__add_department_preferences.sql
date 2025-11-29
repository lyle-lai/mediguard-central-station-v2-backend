-- ============================
-- V4: 添加科室偏好设置字段
-- ============================

ALTER TABLE `sys_department`
ADD COLUMN `is_demo_mode` TINYINT(1) DEFAULT 0 COMMENT '是否演示模式' AFTER `capacity`,
ADD COLUMN `simulation_speed` INT DEFAULT 1 COMMENT '模拟速度（倍数）' AFTER `is_demo_mode`,
ADD COLUMN `night_mode` TINYINT(1) DEFAULT 0 COMMENT '夜间模式' AFTER `simulation_speed`,
ADD COLUMN `audio_enabled` TINYINT(1) DEFAULT 1 COMMENT '音频启用' AFTER `night_mode`;
