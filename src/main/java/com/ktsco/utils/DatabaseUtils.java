package com.ktsco.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ktsco.utils.AlertsUtils;
import com.mysql.cj.jdbc.MysqlDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils {

	private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
	

	private static DataSource getMySqlDataSource() throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(Commons.getConfigurationPropertyValue("serverName"));
		dataSource.setPort(Integer.parseInt(Commons.getConfigurationPropertyValue("serverPort")));
		dataSource.setDatabaseName(Commons.getConfigurationPropertyValue("databaseName"));
		dataSource.setUser(Commons.getConfigurationPropertyValue("user"));
		dataSource.setPassword(Commons.getConfigurationPropertyValue("password"));
		dataSource.setServerTimezone(Commons.getConfigurationPropertyValue("serverTimeZon"));
		return dataSource;
	}

	private static Connection connection;

	/**
	 * this method will create the connection for database and return the Connection
	 * for other methods.
	 * 
	 * @param sqlUrl
	 * @return
	 */
	public static void OpenConnection() {

		
		try {
			log.info("Opening Database Connection");
			connection = getMySqlDataSource().getConnection();

		} catch (SQLException e) {
			log.error("Error in creating connection with error massage {} " + e.getMessage());
			log.info("returning null value");
			AlertsUtils.databaseErrorAlert();
			connection = null;
		}

	}

	public static void closeConnection() {
		if (connection != null) {
			try {
				log.info("Closing Database Connection");
				connection.close();
			} catch (SQLException e) {
				log.error("Error in Closing connection with error massage {} " + e.getMessage());
			}
		}
	}

	/**
	 * this method will take only queries with parameters <br>
	 * and return a PreparedStatement
	 * 
	 * @param query
	 * @return PreparedStatement
	 */

	public static PreparedStatement dbPreparedStatment(String query) {
		Connection conn = connection;
		PreparedStatement preStmt = null;
		try {
			if (conn != null) {
				
				preStmt = conn.prepareStatement(query);
				
			}
		} catch (SQLException e) {
			log.error("Error in creating connection with error massage {} ", e.getMessage());
			log.info("returning null value");
			AlertsUtils.databaseErrorAlert();
			return null;
		}
		return preStmt;
	}
	

	/**
	 * This method with queries without parameter and return <br>
	 * a ResultSet
	 * 
	 * @param query
	 * @return ResultSet
	 */
	public static ResultSet dbSelectExuteQuery(String query) {

		ResultSet resultSet = null;

		Connection conn = connection;
		Statement stmt = null;
		try {
			if (conn != null) {
				stmt = conn.createStatement();
				resultSet = stmt.executeQuery(query);
			} else {
				log.error("Connection received as null check connection");
				stmt = null;
			}
		} catch (SQLException e) {
			log.error("Error in creating the statment check the connection {}" + e.getMessage());
			stmt = null;

		}
		return resultSet;

	}

	public static boolean ddlQueryExecution(String query) {
		boolean result = false;
		Connection conn = connection;
		Statement stmt = null;
		try {
			if (conn != null) {
				stmt = conn.createStatement();
				stmt.execute(query);
			} else {
				log.error("Connection received as null check connection");
				stmt = null;
			}
		} catch (SQLException e) {
			log.error("Error in creating the statment check the connection {}" + e.getMessage());
			stmt = null;

		}

		return result;
	}

	public static List<Map<String, Object>> convertResultSetToMap(ResultSet resultSet) {
		List<Map<String, Object>> list = null;
		Map<String, Object> rowMap = null;
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int numOfCols = rsmd.getColumnCount();
			list = new ArrayList<Map<String, Object>>();

			while (resultSet.next()) {
				
				rowMap = new HashMap<String, Object>();
				for (int i = 1; i <= numOfCols; i++) {
					rowMap.put(rsmd.getColumnName(i), resultSet.getObject(i));
				}
				list.add(rowMap);

			}

			log.info("Records converted to List" + list);
			return list;
		} catch (SQLException e) {
			log.error("Fail to convert result to map " + e.getMessage());
			return list;
		}
	}

}
