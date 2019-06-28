package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

public class ProductionExportDAO {
	private static Logger log = LoggerFactory.getLogger(ProductionExportDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static int getExportID() {
		int exportID = 0;
		query = "select max(export_id) from productExport";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				exportID = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return exportID + 1;
	}

	public static boolean insertExport(int exportID, String date, int employeeID, String memo) {
		boolean isSuccess = false;

		query = "Insert into productExport (export_id, date, employee_id, memo) values (?,?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);

		try {
			preStatement.setInt(1, exportID);
			preStatement.setString(2, date);
			preStatement.setInt(3, employeeID);
			preStatement.setString(4, memo);
			preStatement.execute();
			isSuccess = true;
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return isSuccess;

	}
	
	public static boolean insertIntoExportDetail(int exportID, int productID, double quantity) {
		boolean isSuccess = false; 
		
		query = "INSERT INTO PRODUCTEXPORTDETAIL (EXPORT_ID, PRODUCT_ID, QUANTITY) VALUES (?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setInt(1, exportID);
			preStatement.setInt(2, productID);
			preStatement.setDouble(3, quantity);
			preStatement.execute();
			isSuccess = true; 
		}catch (SQLException e ) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStatement.close();
			}catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		return isSuccess; 
		
	}

}
