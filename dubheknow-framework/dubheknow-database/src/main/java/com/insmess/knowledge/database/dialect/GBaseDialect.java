package com.insmess.knowledge.database.dialect;

import com.insmess.knowledge.database.constants.DbQueryProperty;
import com.insmess.knowledge.database.core.DbColumn;
import com.insmess.knowledge.database.core.DbTable;
import com.insmess.knowledge.database.utils.MD5Util;
import com.insmess.knowledge.common.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 南大通用 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class GBaseDialect extends AbstractDbDialect {
    // 定义南大通用数据库保留关键字集合
    private static final String[] NANDA_RESERVED_WORDS = {
            "ABSOLUTE", "ACTION", "ADD", "ADMIN", "AFTER", "AGGREGATE", "ALIAS", "ALL", "ALLOCATE",
            "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASSERTION", "ASYMMETRIC", "AT",
            "AUTHORIZATION", "AVG", "BACKWARD", "BEFORE", "BEGIN", "BETWEEN", "BIGINT", "BINARY",
            "BIT", "BIT_LENGTH", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BY", "CALL", "CALLED",
            "CARDINALITY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER",
            "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLASS", "CLOB", "CLOSE", "COALESCE",
            "COLLATE", "COLLATION", "COLUMN", "COMMIT", "COMPLETION", "CONNECT", "CONNECTION",
            "CONSTRAINT", "CONSTRAINTS", "CONSTRUCTOR", "CONTAINS", "CONTINUE", "CONVERT", "CORRESPONDING",
            "COUNT", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP",
            "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE",
            "CURRENT_USER", "CURSOR", "CYCLE", "DATA", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL",
            "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DEPTH", "DEREF", "DESC", "DESCRIBE",
            "DESCRIPTOR", "DESTROY", "DESTRUCTOR", "DETERMINISTIC", "DIAGNOSTICS", "DICTIONARY", "DISCONNECT",
            "DISTINCT", "DOMAIN", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC",
            "EQUALS", "ESCAPE", "EVERY", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNAL",
            "EXTRACT", "FALSE", "FETCH", "FILTER", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FREE", "FROM",
            "FULL", "FUNCTION", "GENERAL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "GROUPING", "HAVING",
            "HOST", "HOUR", "IDENTITY", "IGNORE", "IMMEDIATE", "IN", "INDICATOR", "INITIALIZE", "INITIALLY",
            "INNER", "INOUT", "INPUT", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS",
            "ISOLATION", "ITERATE", "JOIN", "KEY", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEFT",
            "LESS", "LEVEL", "LIKE", "LIMIT", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATOR", "MAP", "MATCH",
            "MAX", "MEMBER", "MERGE", "METHOD", "MIN", "MINUTE", "MODIFIES", "MODIFY", "MODULE", "MONTH",
            "MULTISET", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NEXT", "NO", "NONE", "NOT",
            "NULL", "NUMERIC", "OBJECT", "OCTET_LENGTH", "OF", "OFF", "OLD", "ON", "ONLY", "OPEN", "OPERATION",
            "OPTION", "OR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "PAD", "PARAMETER", "PARAMETERS",
            "PARTIAL", "PATH", "POSTFIX", "PRECEDES", "PRECISION", "PREFIX", "PREORDER", "PREPARE", "PRESERVE",
            "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "READS", "REAL", "RECURSIVE",
            "REF", "REFERENCES", "REFERENCING", "RELATIVE", "RELEASE", "RESTRICT", "RESULT", "RETURN",
            "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROW", "ROWS", "SAVEPOINT",
            "SCHEMA", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SECTION", "SELECT", "SEQUENCE", "SESSION",
            "SESSION_USER", "SET", "SETS", "SIZE", "SMALLINT", "SOME", "SPACE", "SPECIFIC", "SPECIFICTYPE",
            "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATE", "STATEMENT", "STATIC",
            "STRUCTURE", "SUBMULTISET", "SUBSTRING", "SUM", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE",
            "TABLESAMPLE", "TEMPORARY", "TERMINATE", "THAN", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR",
            "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TREAT",
            "TRIGGER", "TRUE", "UNDER", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "USAGE", "USER",
            "USING", "VALUE", "VALUES", "VARCHAR", "VARIABLE", "VARYING", "VIEW", "WHEN", "WHENEVER",
            "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN", "WITHOUT", "WORK", "WRITE", "YEAR", "ZONE"
    };

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("PRI".equals(rs.getString("COLKEY")));
            entity.setNullable("YES".equals(rs.getString("NULLABLE")));
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT column_name AS COLNAME, ordinal_position AS COLPOSITION, " +
                "column_default AS DATADEFAULT, is_nullable AS NULLABLE, data_type AS DATATYPE, " +
                "character_maximum_length AS DATALENGTH, numeric_precision AS DATAPRECISION, " +
                "numeric_scale AS DATASCALE, column_key AS COLKEY, column_comment AS COLCOMMENT " +
                "FROM information_schema.columns " +
                "WHERE table_schema = '" + dbQueryProperty.getDbName() + "' " +
                "AND table_name = '" + tableName + "' " +
                "ORDER BY ordinal_position";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = '" + dbQueryProperty.getDbName() + "' " +
                "AND table_name = '" + tableName + "'";
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName,
                                                 String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        // 生成CREATE TABLE语句
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");

        for (DbColumn column : dbColumnList) {
            String columnType = column.getDataType();
            String colName = column.getColName();

            sql.append("  ").append(this.escapeReservedKeyword(colName)).append(" ");

            // 转换数据类型为南大通用数据库支持的类型
            switch (columnType.toLowerCase()) {
                case "varchar":
                case "varchar2":
                    sql.append("VARCHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    }
                    break;
                case "char":
                    sql.append("CHAR");
                    if (StringUtils.isNotEmpty(column.getDataLength())) {
                        sql.append("(").append(column.getDataLength()).append(")");
                    }
                    break;
                case "text":
                    sql.append("CLOB");
                    break;
                case "int":
                case "integer":
                    sql.append("INTEGER");
                    break;
                case "bigint":
                    sql.append("BIGINT");
                    break;
                case "smallint":
                    sql.append("SMALLINT");
                    break;
                case "tinyint":
                    sql.append("SMALLINT"); // 映射为 SMALLINT
                    break;
                case "decimal":
                    sql.append(generateColumnSQLNanda("DECIMAL", column.getDataLength(),
                            column.getDataScale(), 38, 10));
                    break;
                case "float":
                    sql.append("FLOAT");
                    break;
                case "double":
                    sql.append("DOUBLE PRECISION");
                    break;
                case "date":
                    sql.append("DATE");
                    break;
                case "datetime":
                    sql.append("TIMESTAMP");
                    break;
                case "timestamp":
                    sql.append("TIMESTAMP");
                    break;
                case "time":
                    sql.append("TIME");
                    break;
                default:
                    sql.append(columnType.toUpperCase()); // 默认处理未知类型
                    break;
            }

            // 检查是否必填
            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            // 默认值处理
            if (StringUtils.isNotEmpty(column.getDataDefault())) {
                if (columnType.equalsIgnoreCase("VARCHAR") ||
                        columnType.equalsIgnoreCase("CHAR") ||
                        columnType.equalsIgnoreCase("CLOB")) {
                    sql.append(" DEFAULT '").append(column.getDataDefault()).append("'");
                } else {
                    sql.append(" DEFAULT ").append(column.getDataDefault());
                }
            }

            // 添加字段备注（COMMENT）
            if (StringUtils.isNotEmpty(column.getColComment())) {
                sql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(column.getColComment())).append("'");
            }

            // 加入字段到主键列表，如果是主键
            if (column.getColKey()) {
                primaryKeys.add(column.getColName());
            }

            sql.append(",\n");
        }

        // 移除最后的逗号和换行
        sql.setLength(sql.length() - 2);
        sql.append("\n");

        // 添加主键约束
        if (!primaryKeys.isEmpty()) {
            sql.append(",\n  PRIMARY KEY (");
            for (int i = 0; i < primaryKeys.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append(this.escapeReservedKeyword(primaryKeys.get(i)));
            }
            sql.append(")");
        }

        sql.append("\n)");

        // 添加表备注
        if (StringUtils.isNotEmpty(tableComment)) {
            sql.append(" COMMENT='").append(MD5Util.escapeSingleQuotes(tableComment)).append("'");
        }

        sql.append(";\n");
        sqlList.add(sql.toString());

        return sqlList;
    }

    /**
     * 转义保留关键字
     */
    public static String escapeReservedKeyword(String colName) {
        if (colName == null || colName.isEmpty()) {
            return colName;
        }
        for (String reserved : NANDA_RESERVED_WORDS) {
            if (reserved.equalsIgnoreCase(colName)) {
                return "\"" + colName + "\"";
            }
        }
        return colName;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    /**
     * 生成列SQL定义
     */
    public static String generateColumnSQLNanda(String columnType, String columnLength,
                                                String columnScale, int maxLength, int maxScale) {
        StringBuilder sql = new StringBuilder(columnType);

        // 处理需要长度和小数位数的类型
        if (columnType.equalsIgnoreCase("DECIMAL") || columnType.equalsIgnoreCase("NUMERIC")) {
            if (StringUtils.isNotEmpty(columnLength)) {
                int length = Integer.parseInt(columnLength);
                // 限制长度不超过最大长度
                if (length > maxLength) {
                    length = maxLength;
                }
                sql.append("(").append(length);

                // 如果提供了小数位数，则附加小数位
                if (StringUtils.isNotEmpty(columnScale)) {
                    int scale = Integer.parseInt(columnScale);
                    // 限制小数位数不超过最大值
                    if (scale > maxScale) {
                        scale = maxScale;
                    }
                    sql.append(", ").append(scale);
                }

                sql.append(")");
            }
        }

        return sql.toString();
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT table_name AS TABLENAME, table_comment AS TABLECOMMENT " +
                "FROM information_schema.tables " +
                "WHERE table_schema = '" + dbQueryProperty.getDbName() + "'";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName,
                                      DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // 根据传入的 DbColumn 列表获取所有字段名，并用逗号分隔
        String fields = columns.stream()
                .map(column -> escapeReservedKeyword(column.getColName()))
                .collect(Collectors.joining(", "));

        // 构造最终的 SQL 查询语句
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + tableName;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT SUM(data_length) / 1024 / 1024 AS usedSizeMb " +
                "FROM information_schema.tables " +
                "WHERE table_schema = '" + dbName + "' " +
                "GROUP BY table_schema";
    }

    @Override
    public String getDbName() {
        return "SELECT CURRENT_SCHEMA AS databaseName";
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbTable entity = new DbTable();
            entity.setTableName(rs.getString("TABLENAME"));
            entity.setTableComment(rs.getString("TABLECOMMENT"));
            return entity;
        };
    }
}
