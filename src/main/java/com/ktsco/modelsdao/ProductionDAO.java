package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.DetailReportModel;
import com.ktsco.models.factory.ProductionDetailModel;
import com.ktsco.models.factory.ProductionListModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductionDAO {

	private static final Logger log = LoggerFactory.getLogger(ProductionDAO.class);

	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static int retrieveProductionID() {
		int id = 0;
		query = "Select max(production_id) from productionList";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				id = resultSet.getInt(1);
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

		return id + 1;
	}

	public static boolean insertProductionList(int productionID, String inputDate, String employee) {
		boolean isSuccess = false;
		int employeeID = EmployeDAO.getEmployeeID(employee);
		String convertedDate = DateUtils.convertJalaliToGregory(inputDate);
		query = "insert into productionList (production_id, date, employee_id) values (?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, productionID);
			preStatement.setString(2, convertedDate);
			preStatement.setInt(3, employeeID);
			preStatement.execute();
			isSuccess = true;
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return isSuccess;
	}

	public static boolean insertProductionDetail(int productionID, String productName, double quantity) {
		boolean isSuccess = false;
		int productId = ProductDAO.getProductID(productName);
		query = "insert into productionDetail (production_id , product_id, quantity) values (?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, productionID);
			preStatement.setInt(2, productId);
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

	public static ObservableList<ProductionDetailModel> getProductionStockQuantity() {
		ObservableList<ProductionDetailModel> list = FXCollections.observableArrayList();
		query = "Select prodTotal.prod_id , prodTotal.prod_name , prodTotal.prod_um , "
				+ "(prodTotal.productionTotal - exportTotal.ExportTotal) from productionTotalView prodTotal "
				+ "inner join productionExportTotalView exportTotal " + "on prodTotal.prod_id = exportTotal.prod_id";

		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				String productName = resultSet.getString(2);
				String productUnit = resultSet.getString(3);
				String quantity = resultSet.getString(4);
				ProductionDetailModel detailModel = new ProductionDetailModel(productName, productUnit, quantity);
				list.add(detailModel);
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
		return list;
	}

	public static ObservableList<ProductionListModel> retrieveSearchResult(String no, String startDate, String endDate,
			String employeeName) {
		ObservableList<ProductionListModel> list = FXCollections.observableArrayList();
		String convertedStartDate = null;
		if ("".equals(startDate)) {
			convertedStartDate = "1900-01-01";
		} else {
			convertedStartDate = DateUtils.convertJalaliToGregory(startDate);
		}
		String convertedEndDate = null;
		if ("".equals(endDate)) {
			convertedEndDate = "2200-12-30";
		} else {
			convertedEndDate = DateUtils.convertJalaliToGregory(endDate);
		}
		String employeeID = "";
		if (!"".equals(employeeName)) {
			employeeID = String.valueOf(EmployeDAO.getEmployeeID(employeeName));
		}

		query = "select prodList.production_id, prodList.date, emp.fullname from productionList prodList inner join employee emp "
				+ "on prodList.employee_id = emp.employee_id "
				+ "where prodList.production_id like ? AND (prodList.date between ? AND ?) AND emp.employee_id like ? "
				+ "order by production_id ASC";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + no + "%");
			preStatement.setString(2, convertedStartDate);
			preStatement.setString(3, convertedEndDate);
			preStatement.setString(4, "%" + employeeID + "%");
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int productionID = resultSet.getInt(1);
				String convertedOutputDate = DateUtils.convertGregoryToJalali(resultSet.getString(2));
				String empName = resultSet.getString(3);
				ProductionListModel listModel = new ProductionListModel(productionID, convertedOutputDate, empName);
				list.add(listModel);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return list;

	}

	public static ObservableList<ProductionDetailModel> retriveDetailList(int lookupValue) {
		ObservableList<ProductionDetailModel> list = FXCollections.observableArrayList();
		query = "select production.id, production.production_id, prod.prod_name, prod.prod_um, production.quantity from productionDetail production "
				+ "inner join products prod on production.product_id = prod.prod_id where production.production_id = ?";

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, lookupValue);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int detailID = resultSet.getInt(1);
				int productionID = resultSet.getInt(2);
				String productName = resultSet.getString(3);
				String prodUnit = resultSet.getString(4);
				String quantity = resultSet.getString(5);

				ProductionDetailModel detailModel = new ProductionDetailModel(detailID, productionID, productName,
						prodUnit, quantity);
				list.add(detailModel);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return list;
	}

	public static boolean deleteProductionList(int productionID) {
		boolean isSuccess = false;
		query = "delete from productionList where production_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, productionID);
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

	public static boolean updateProdcutionDetailQuantity(int detailID, String quantity) {
		boolean isSuccess = false;
		double convertedQuantity = 0;
		try {
			convertedQuantity = Double.parseDouble(quantity);
		} catch (NumberFormatException e) {
			AlertsUtils.numberEntryFormatErrorAlerts();
			isSuccess = false;
		}
		query = "update productionDetail set quantity = ? where id = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setDouble(1, convertedQuantity);
			preStatement.setInt(2, detailID);
			preStatement.executeUpdate();
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

	public static ObservableList<DetailReportModel> getProductionDetailList(String lookupValue) {
		ObservableList<DetailReportModel> list = FXCollections.observableArrayList();
		query = "SELECT PRODS.PROD_ID,PRODDET.ID, PRODS.PROD_NAME, PRODS.PROD_UM,PRODLIST.DATE ,PRODDET.QUANTITY FROM PRODUCTS PRODS "
				+ "INNER JOIN PRODUCTIONDETAIL PRODDET ON PRODS.PROD_ID = PRODDET.PRODUCT_ID "
				+ "INNER JOIN PRODUCTIONLIST PRODLIST ON PRODDET.PRODUCTION_ID = PRODLIST.PRODUCTION_ID "
				+ "WHERE PRODS.PROD_ID LIKE ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + lookupValue + "%");
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int idnumber = resultSet.getInt(2);
				String item = resultSet.getString(3);
				String unit = resultSet.getString(4);
				String date = DateUtils.convertGregoryToJalali(resultSet.getString(5));
				String quantity = String.valueOf(Commons.setDoubleFormat(resultSet.getDouble(6)));
				DetailReportModel detailModel = new DetailReportModel(idnumber, item, unit, date, quantity);
				list.add(detailModel);
			}
		} catch (SQLException e) {
			Commons.dbExcutionLog(query, e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				Commons.dbClosingLog(e.getMessage());
			}
		}

		return list;
	}

}
