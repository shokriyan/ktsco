package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.FactoryExportModal;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

	public static ObservableList<FactoryExportModal> retrieveFactoryExportSearchItems(String fromDate, String toDate) {
		ObservableList<FactoryExportModal> list = FXCollections.observableArrayList();

		fromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01" : DateUtils.convertJalaliToGregory(fromDate);
		toDate = (toDate.equalsIgnoreCase("")) ? "2900-12-31" : DateUtils.convertJalaliToGregory(toDate);

		query = "select pe.export_id, pe.date, pe.employee_id, e.fullname, ped.id as sequenceId, ped.product_id, p.prod_name, ped.quantity from productexport pe "
				+ "inner join employee e on e.employee_id = pe.employee_id "
				+ "inner join productexportdetail ped on ped.export_id = pe.export_id "
				+ "inner join products p on p.prod_id = ped.product_id " + "where pe.date between ? and ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, fromDate);
			preStatement.setString(2, toDate);

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				FactoryExportModal exportModal = new FactoryExportModal();
				exportModal.setExportId(resultSet.getInt("export_id"));
				exportModal.setExportDate(resultSet.getString("date"));
				exportModal.setEmployeeId(resultSet.getInt("employee_id"));
				exportModal.setEmployeeName(resultSet.getString("fullname"));
				exportModal.setSequenceId(resultSet.getInt("sequenceId"));
				exportModal.setProductId(resultSet.getInt("product_id"));
				exportModal.setProductName(resultSet.getString("prod_name"));
				exportModal.setExportQuantity(resultSet.getDouble("quantity"));

				list.add(exportModal);

			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preStatement != null)
					preStatement.close();

			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return list;
	}

	public static void updateExport(FactoryExportModal exportModal) {
		try {

			query = "update productexportdetail set product_id = ? , quantity = ? where id = ?";
			preStatement = DatabaseUtils.dbPreparedStatment(query);
			preStatement.setInt(1, exportModal.getProductId());
			preStatement.setDouble(2, exportModal.getExportQuantity());
			preStatement.setInt(3, exportModal.getSequenceId());
			preStatement.executeUpdate();

			preStatement.close();

			query = "update productexport set employee_id = ? , date = ? where export_id = ? ";
			preStatement = DatabaseUtils.dbPreparedStatment(query);
			preStatement.setInt(1, exportModal.getEmployeeId());
			preStatement.setString(2, exportModal.getExportDate());
			preStatement.setInt(3, exportModal.getExportId());
			preStatement.executeUpdate();

		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preStatement != null)
					preStatement.close();

			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
	}

	public static boolean deleteExportData(FactoryExportModal data) {
		boolean isSuccess = false;
		try {
			query = "delete from productExportDetail where id = ? ";
			preStatement = DatabaseUtils.dbPreparedStatment(query);
			preStatement.setInt(1, data.getSequenceId());
			preStatement.execute();
			
			query = "select * from productExportDetail where export_id = ? ";
			preStatement = DatabaseUtils.dbPreparedStatment(query);
			preStatement.setInt(1, data.getExportId());
			resultSet = preStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) {
				query = "delete from productExport where export_id = ? ";
				preStatement = DatabaseUtils.dbPreparedStatment(query);
				preStatement.setInt(1, data.getExportId());
				preStatement.execute();
			}
			isSuccess = true;
		} catch (SQLException e) {
			isSuccess = false;
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				if (resultSet != null || !resultSet.isClosed())
					resultSet.close();
				if (preStatement != null || !preStatement.isClosed())
					preStatement.close();

			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		return isSuccess;
	}

}
