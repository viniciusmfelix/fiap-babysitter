package br.com.fiap.babysitter.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private String plSqlDisableConstraints 
    	= "	BEGIN "
    	+ "	FOR i IN "
    	+ "	(SELECT "
    	+ "		constraint_name, "
    	+ "		table_name "
    	+ "	FROM "
    	+ "		user_constraints"
    	+ "	WHERE "
    	+ "		table_name NOT LIKE '%VIEW' "
    	+ "	AND	"
    	+ "		constraint_type = 'R') "
    	+ " LOOP "
    	+ "	EXECUTE IMMEDIATE "
    	+ "	'ALTER TABLE '||i.table_name||' DISABLE CONSTRAINT '||constraint_name||''; "
    	+ "	END LOOP; "
    	+ "	END; "
    	+ "	/ ";
    		
    private String plSqlEnableConstraints 
    	= "	BEGIN "
    	+ "	FOR i IN "
    	+ "	(SELECT "
    	+ "		constraint_name, "
    	+ "		table_name "
    	+ "	FROM "
    	+ "		user_constraints"
    	+ "	WHERE "
    	+ "		table_name NOT LIKE '%VIEW' "
    	+ "	AND	"
    	+ "		constraint_type = 'R') "
    	+ " LOOP "
    	+ "	EXECUTE IMMEDIATE "
    	+ "	'ALTER TABLE '||i.table_name||' ENABLE CONSTRAINT '||constraint_name||''; "
    	+ "	END LOOP; "
    	+ "	END; "
    	+ "	/ ";

    @Autowired
    private DataSource dataSource;

    private Connection connection;

    public void clearTables() {
        try (Connection connection = dataSource.getConnection()) {
            this.connection = connection;

            checkTestDatabase();
            tryToClearTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.connection = null;
        }
    }

    private void checkTestDatabase() throws SQLException {
        String catalog = connection.getCatalog();

        if (catalog == null || !catalog.endsWith("test")) {
            throw new RuntimeException(
                    "Cannot clear database tables because '" + catalog + "' is not a test database (suffix 'test' not found).");
        }
    }

    private void tryToClearTables() throws SQLException {
        List<String> tableNames = getTableNames();
        clear(tableNames);
    }

    private List<String> getTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(connection.getCatalog(), null, null, new String[] { "TABLE" });

        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }

        tableNames.remove("flyway_schema_history");

        return tableNames;
    }

    private void clear(List<String> tableNames) throws SQLException {
        Statement statement = buildSqlStatement(tableNames);

        logger.debug("Executing SQL");
        statement.executeBatch();
    }

    private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
        Statement statement = connection.createStatement();

        statement.addBatch(sql(plSqlDisableConstraints));
        addTruncateSatements(tableNames, statement);
        statement.addBatch(sql(plSqlEnableConstraints));

        return statement;
    }

    private void addTruncateSatements(List<String> tableNames, Statement statement) {
        tableNames.forEach(tableName -> {
            try {
                statement.addBatch(sql("TRUNCATE TABLE " + tableName));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String sql(String sql) {
        logger.debug("Adding SQL: {}", sql);
        return sql;
    }
}
