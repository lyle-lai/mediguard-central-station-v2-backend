-- =====================================================
-- Flyway 修复脚本
-- 用于解决 V2 迁移 checksum 不匹配问题
-- =====================================================

-- 方法1：删除并重建数据库（推荐，最干净）
-- DROP DATABASE mediguard;
-- CREATE DATABASE mediguard CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 方法2：删除失败的迁移记录
-- DELETE FROM flyway_schema_history WHERE version IN ('2', '3') AND success = 0;

-- 方法3：修复 checksum（如果 V2 已成功执行但 checksum 变了）
-- UPDATE flyway_schema_history SET checksum = NULL WHERE version = '2';

-- =====================================================
-- 推荐操作步骤：
-- =====================================================
-- 1. 备份当前数据库（如果有重要数据）
--    mysqldump -h 159.75.205.149 -P 20008 -u root -p mediguard > backup.sql
--
-- 2. 删除并重建数据库
--    DROP DATABASE mediguard;
--    CREATE DATABASE mediguard CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
--
-- 3. 重启 Spring Boot 应用
--    Flyway 将自动执行 V1, V2, V3 迁移
