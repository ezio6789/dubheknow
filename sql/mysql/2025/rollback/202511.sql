-- ----------------------------
-- 系统表中属性和关系数据类型变更
-- ----------------------------
--附件类型：
DELETE FROM `system_dict_data` WHERE `dict_type` = 'ext_data_type' AND `dict_value` = '6';
--对象类型：
DELETE FROM `system_dict_data` WHERE `dict_type` = 'ext_data_type' AND `dict_value` = '7';
--地理位置：
DELETE FROM `system_dict_data` WHERE `dict_type` = 'ext_data_type' AND `dict_value` = '8';

-- ----------------------------
-- 关系表中数据类型字段的变更
-- ----------------------------
ALTER TABLE `ext_schema_relation` DROP COLUMN `data_type`;