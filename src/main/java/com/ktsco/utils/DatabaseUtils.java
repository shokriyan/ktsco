package com.ktsco.utils;

import java.sql.*;

import javax.sql.DataSource;

import com.ktsco.utils.AlertsUtils;
import com.mysql.cj.jdbc.MysqlDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils {

	private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
	
	private static DataSource getMySqlDataSource() throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setPort(3306);
		dataSource.setDatabaseName("ktscodb");
		dataSource.setUser("root");
		dataSource.setPassword("");
		dataSource.setServerTimezone("EST");
		return dataSource;
	}

	/**
	 * this method will create the connection for database and return the Connection
	 * for other methods.
	 * 
	 * @param sqlUrl
	 * @return
	 */
	public static Connection connection() {
		
		Connection conn;
		try {
			conn = getMySqlDataSource().getConnection();
			
		} catch (SQLException e) {
			log.error("Error in creating connection with error massage {} " + e.getMessage());
			log.info("returning null value");
			AlertsUtils.ErrorAlert("خطا در دیتابیس", "ارتباط با دیتابیس موجود نمیباشد \n با ادمین در تماس شوید");
			return null;
		}
		return conn;

	}
	
	/**
	 * this method will take only queries with paramenet 
	 * <br> and return a PreparedStatement
	 * 
	 * @param query
	 * @return PreparedStatement
	 */
	
	public static PreparedStatement dbPreparedStatment(String query) {
		Connection conn = connection();
		PreparedStatement preStmt = null; 
		try {
			if(conn != null) {
				log.debug("Initializing Prepared query for {}", query);
				preStmt = conn.prepareStatement(query);
			}
		}catch (SQLException e) {
			log.error("Error in creating connection with error massage {} ", e.getMessage());
			log.debug("returning null value");
			AlertsUtils.ErrorAlert("خطا در دیتابیس", "ارتباط با دیتابیس موجود نمیباشد" + "\n" + e.getMessage());
			return null;
		}
		return preStmt;
	}

	/**
	 * This method with queries without parameter and return 
	 * <br> a ResultSet
	 * @param query
	 * @return ResultSet
	 */
	public static ResultSet dbSelectExuteQuery(String query) {

		ResultSet resultSet = null;

		Connection conn = connection();
		Statement stmt = null;
		try {
			if (conn != null) {
				stmt = conn.createStatement();
				resultSet = stmt.executeQuery(query);
			}else {
				log.error("Connection received as null check connection");
				stmt = null; 
			}
		} catch (SQLException e) {
			log.error("Error in creating the statment check the connection {}" + e.getMessage());
			stmt = null;

		}
		return resultSet;

	}

}
