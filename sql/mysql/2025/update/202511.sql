-- ----------------------------
-- 系统表中属性和关系数据类型变更
-- ----------------------------
--附件类型：
INSERT INTO `system_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark` )
VALUES
    ( 6, '附件', '6', 'ext_data_type', NULL, 'default', 'N', '0', '星小图', NOW(), '星小图', NOW(), '附件' );
--对象类型：
INSERT INTO `system_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark` )
VALUES
    ( 7, '对象', '7', 'ext_data_type', NULL, 'default', 'N', '0', '星小图', NOW(), '星小图', NOW(), '对象' );
--地理位置：
INSERT INTO `system_dict_data` ( `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark` )
VALUES
    ( 8, '地理位置', '8', 'ext_data_type', NULL, 'default', 'N', '0', '星小图', NOW(), '星小图', NOW(), '地理位置' );

-- ----------------------------
-- 关系表中数据类型字段的变更
-- ----------------------------
ALTER TABLE `ext_schema_relation` ADD COLUMN `data_type` TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '关系类型;0：文本，1：整数，2：小数，3：时间，4：字节类型，5：布尔值，6：附件，7：对象，8：地理位置' AFTER `start_schema_id`;