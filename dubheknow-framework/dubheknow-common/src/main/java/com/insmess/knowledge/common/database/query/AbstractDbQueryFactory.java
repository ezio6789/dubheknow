package com.insmess.knowledge.common.database.query;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import com.insmess.knowledge.common.database.DbDialect;
import com.insmess.knowledge.common.database.DbQuery;
import com.insmess.knowledge.common.database.constants.DbQueryProperty;
import com.insmess.knowledge.common.database.constants.DbType;
import com.insmess.knowledge.common.database.core.DbColumn;
import com.insmess.knowledge.common.database.core.DbTable;
import com.insmess.knowledge.common.database.core.PageResult;
import com.insmess.knowledge.common.database.dialect.OracleDialect;
import com.insmess.knowledge.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Setter
public abstract class AbstractDbQueryFactory implements DbQuery {

    protected DataSource dataSource;

    protected JdbcTemplate jdbcTemplate;

    protected DbDialect dbDialect;

    private DbQueryProperty dbQueryProperty;

    @Override
    public Connection getConnection() {
        try {
            DbType dbType = DbType.getDbType(dbQueryProperty.getDbType());
            String jdbcUrl = trainToJdbcUrl(dbQueryProperty);
            if (DbType.DM8.getDb().equals(dbType.getDb()) && jdbcUrl.startsWith("jdbc:dm")) {
                return DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword());
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataQueryException("获取数据库连接出错");
        }
    }

    @Override
    public boolean valid() {
        Connection conn = null;
        try {
            DbType dbType = DbType.getDbType(dbQueryProperty.getDbType());
            String jdbcUrl = trainToJdbcUrl(dbQueryProperty);
            if (DbType.DM8.getDb().equals(dbType.getDb()) && jdbcUrl.startsWith("jdbc:dm")) {
                conn = DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                        dbQueryProperty.getPassword());
            } else {
                conn = dataSource.getConnection();
            }
            return conn.isValid(0);
        } catch (SQLException e) {
            throw new DataQueryException("检测连通性出错");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DataQueryException("关闭数据库连接出错");
                }
            }
        }

    }

    @Override
    public void close() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        } else {
            throw new DataQueryException("不合法数据源类型");
        }
    }

    @Override
    public List<DbColumn> getTableColumns(String dbName, String tableName) {
        String sql = dbDialect.columns(dbName, tableName);
        System.out.println(sql);
        if (dbDialect instanceof OracleDialect) {
            List<DbColumn> longColumns = jdbcTemplate.query(sql, dbDialect.columnLongMapper());
            List<DbColumn> queryColumns = jdbcTemplate.query(sql, dbDialect.columnMapper());
            for (int i = 0; i < longColumns.size(); i++) {
                DbColumn longColumn = longColumns.get(i);
                DbColumn otherColumn = queryColumns.get(i);
                otherColumn.setDataDefault(longColumn.getDataDefault());
            }
            return queryColumns;
        }
        return jdbcTemplate.query(sql, dbDialect.columnMapper());
    }

    @Override
    public List<DbColumn> getTableColumns(DbQueryProperty dbQueryProperty, String tableName) {
        String sql = dbDialect.columns(dbQueryProperty, tableName);
        System.out.println(sql);
        if (dbDialect instanceof OracleDialect) {
            List<DbColumn> longColumns = jdbcTemplate.query(sql, dbDialect.columnLongMapper());
            List<DbColumn> queryColumns = jdbcTemplate.query(sql, dbDialect.columnMapper());
            for (int i = 0; i < longColumns.size(); i++) {
                DbColumn longColumn = longColumns.get(i);
                DbColumn otherColumn = queryColumns.get(i);
                otherColumn.setDataDefault(longColumn.getDataDefault());
            }
            return queryColumns;
        }
        return jdbcTemplate.query(sql, dbDialect.columnMapper());
    }

    @Override
    public int generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        String sql = dbDialect.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        System.out.println(sql);
        return this.isTableExists(sql);
    }

    @Override
    public List<String> generateCreateTableSQL(DbQueryProperty dbQueryProperty, String tableName, String tableComment,
            List<DbColumn> dbColumnList) {
        List<String> sql = dbDialect.someInternalSqlGenerator(dbQueryProperty, tableName, tableComment, dbColumnList);
        System.out.println(sql.toString());
        return sql;
    }

    @Override
    public List<DbTable> getTables(String dbName) {
        String sql = dbDialect.tables(dbName);
        return jdbcTemplate.query(sql, dbDialect.tableMapper());
    }

    @Override
    public List<DbTable> getTables(DbQueryProperty dbQueryProperty) {
        String sql = dbDialect.tables(dbQueryProperty);
        return jdbcTemplate.query(sql, dbDialect.tableMapper());
    }

    @Override
    public int count(String sql) {
        return jdbcTemplate.queryForObject(dbDialect.count(sql), Integer.class);
    }

    @Override
    public int count(String sql, Object[] args) {
        return jdbcTemplate.queryForObject(dbDialect.count(sql), args, Integer.class);
    }

    @Override
    public int count(String sql, Map<String, Object> params) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.queryForObject(dbDialect.count(sql), params, Integer.class);
    }

    @Override
    public int countNew(String sql, Map<String, Object> params) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedJdbcTemplate.queryForObject(dbDialect.countNew(sql, params), params, Integer.class);
    }

    @Override
    public List<Map<String, Object>> queryList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> queryWithAliasPrefix(String tableA, String tableB, String joinCondition) {

        List<String> colsA = getTableColumns(dbQueryProperty, tableA).stream().map(DbColumn::getColName).toList();
        List<String> colsB = getTableColumns(dbQueryProperty, tableB).stream().map(DbColumn::getColName).toList();
        // 构建 SELECT 子句
        String selectA = buildSelectColumns("a", colsA);
        String selectB = buildSelectColumns("b", colsB);
        //拼接完整sql
        // 拼接完整 SQL
        String sql = String.format(
                "SELECT %s, %s FROM %s a INNER JOIN %s b ON %s",
                selectA, selectB, tableA, tableB, joinCondition
        );
        // 执行查询
        List<Map<String, Object>> resultList = new ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                ConcurrentHashMap<String, Object> row = new ConcurrentHashMap<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        });
        return resultList;
    }

    /** 获取表的所有字段名 */
    private List<String> getColumnNames(String tableName) {
        List<String> cols = new ArrayList<>();
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                cols.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("获取表字段失败：" + tableName, e);
        }
        return cols;
    }

    /** 构建带前缀的 SELECT 片段，如 a.id AS a_id, a.name AS a_name */
    private String buildSelectColumns(String alias, List<String> columns) {
        String prefix = alias + "_";
        List<String> list = new ArrayList<>();
        for (String c : columns) {
            list.add(alias + "." + c + " AS " + prefix + c);
        }
        return String.join(", ", list);
    }


    @Override
    public List<Map<String, Object>> queryDbColumnByList(List<DbColumn> columns,String tableName,DbQueryProperty dbQueryProperty,long offset, long size) {
        String sql = dbDialect.buildQuerySqlFields(columns,tableName,dbQueryProperty);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        return jdbcTemplate.queryForList(pageSql);
    }
    @Override
    public List<Map<String, Object>> queryList(String sql, Object[] args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, long offset, long size) {
        int total = count(sql);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(pageSql);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Object[] args, long offset, long size) {
        int total = count(sql, args);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        List<Map<String, Object>> records = jdbcTemplate.queryForList(pageSql, args);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<Map<String, Object>> queryByPage(String sql, Map<String, Object> params, long offset, long size,
            Integer cache) {
        int total = count(sql, params);
        String pageSql = dbDialect.buildPaginationSql(sql, offset, size);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(pageSql, params);
        return new PageResult<>(total, records);
    }

    /**
     * 查询结果列表带查询参数
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> queryList(String sql, Map<String, Object> params, Integer cache) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(sql, params);
        return records;
    }

    /**
     * 查询详情结果带查询参数
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> queryOne(String sql, Map<String, Object> params, Integer cache) {
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> records = namedJdbcTemplate.queryForList(sql, params);
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    @Override
    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    @Override
    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

    @Override
    public int[] batchUpdate(String sql) {
        jdbcTemplate.execute(sql);

        return jdbcTemplate.batchUpdate(sql);
    }

    @Override
    public int isTableExists(String sql) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        return count == null ? 0 : count;
    }

    protected String trainToJdbcUrl(DbQueryProperty property) {
        String url = DbType.getDbType(property.getDbType()).getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new DataQueryException("无效数据库类型!");
        }
        url = url.replace("${host}", property.getHost());
        url = url.replace("${port}", String.valueOf(property.getPort()));
        if (DbType.ORACLE.getDb().equals(property.getDbType())
                || DbType.ORACLE_12C.getDb().equals(property.getDbType())) {
            url = url.replace("${sid}", property.getSid());
        } else {
            url = url.replace("${dbName}", property.getDbName());
        }
        return url;
    }

    @Override
    public Integer getDataStorageSize() {
        // 获取数据库名或模式名
        String dbNameSql = dbDialect.getDbName();
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> dbNameResult = namedJdbcTemplate.queryForList(dbNameSql, new HashMap<>());
        String dbName = null;
        if (dbNameResult.size() > 0) {
            Map<String, Object> data = dbNameResult.get(0);
            if (data.containsKey("databaseName")) {
                dbName = String.valueOf(dbNameResult.get(0).get("databaseName"));
            }
        }
        if (StringUtils.isEmpty(dbName)) {
            throw new DataQueryException("无效数据库类型!");
        }
        String dataStorageSizeSql = dbDialect.getDataStorageSize(dbName);
        List<Map<String, Object>> dataStorageSizeResult = namedJdbcTemplate.queryForList(dataStorageSizeSql,
                new HashMap<>());

        // 获取存储量
        Integer dataStorageSize = 0;
        if (dataStorageSizeResult.size() > 0) {
            Map<String, Object> data = dataStorageSizeResult.get(0);
            if (data.containsKey("usedSizeMb")) {
                dataStorageSize = new BigDecimal(String.valueOf(data.get("usedSizeMb"))).intValue();
            }
        }
        return dataStorageSize;
    }
}
