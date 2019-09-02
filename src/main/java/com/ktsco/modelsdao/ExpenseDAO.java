package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.BillDetailModel;
import com.ktsco.models.csr.SalesSearchModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExpenseDAO {
	private static Logger log = LoggerFactory.getLogger(SaleBillDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static int getLastBillID() {
		int billID = 0;
		query = "SELECT MAX(expns_id) FROM expenseBill";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				billID = resultSet.getInt(1);
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

		return billID + 1;
	}

	public static boolean insertIntoExpnsBill(int expense_id, int vendorID, String billDate, String currencyType,
			String billMemo) {
		
		boolean isSuccess = false;
		query = "INSERT INTO EXPENSEBILL (EXPNS_ID, vendor_id, expns_date, currency, memo) VALUES "
				+ "(?,?,?,?,?) ON DUPLICATE KEY UPDATE vendor_id = ? , expns_date = ? , currency = ? , memo = ?";
		String convBillDate = "";
		if (DateUtils.checkEntryDateFormat(billDate)) {
			LocalDate convertedDate = LocalDate.parse(DateUtils.convertJalaliToGregory(billDate),
					DateTimeFormatter.ISO_DATE);
			convBillDate = String.valueOf(convertedDate);
		} else {
			log.error("Wrong Date Entry " + billDate);
			throw new RuntimeException("Wrong Date Entry");
		}

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, expense_id);
			preStatement.setInt(2, vendorID);
			preStatement.setString(3, convBillDate);
			preStatement.setString(4, currencyType);
			preStatement.setString(5, billMemo);
			preStatement.setInt(6, vendorID);
			preStatement.setString(7, convBillDate);
			preStatement.setString(8, currencyType);
			preStatement.setString(9, billMemo);
			preStatement.execute();
			isSuccess = true;

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
		return isSuccess;
	}

	public static boolean insertBillDetail(int id, int billID, String product, String quantity, String unitPrice) {
		boolean isSuccess = false;
		int detailID = (id > 0) ? id : 0;
		int productID = InventoryDAO.getInvId(product);
		double quantityValue = Double.parseDouble(quantity.replace(",", ""));
		double unitpriceValue = Double.parseDouble(unitPrice.replace(",", ""));

		query = "INSERT INTO expenseDetail (ID, expns_id, inv_id, QUANTITY, UNITPRICE) VALUES (?,?,?,?,?)"
				+ " ON DUPLICATE KEY UPDATE inv_id = ? , QUANTITY = ? , UNITPRICE = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {

			preStatement.setInt(1, detailID);
			preStatement.setInt(2, billID);
			preStatement.setInt(3, productID);
			preStatement.setDouble(4, quantityValue);
			preStatement.setDouble(5, unitpriceValue);
			preStatement.setInt(6, productID);
			preStatement.setDouble(7, quantityValue);
			preStatement.setDouble(8, unitpriceValue);
			preStatement.execute();
			isSuccess = true;

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
		return isSuccess;
	}

	public static Map<String, Object> getBillData(int billID) {
		Map<String, Object> data = new HashMap<String, Object>();
		query = "SELECT * FROM expenseBill WHERE expns_id = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int customerCode = resultSet.getInt("vendor_id");
				data.put("vendorID", customerCode);
				String billDate = resultSet.getString("expns_date");
				data.put("billDate", billDate);
				String billMemo = resultSet.getString("memo");
				data.put("billMemo", billMemo);
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

		return data;		
	}
	
	public static boolean deleteSaleBill(int billID) {
		boolean isSuccess = false;
		query = "Delete from expensebill where expns_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);
			preStatement.execute();
			isSuccess = true;
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

		return isSuccess;
	}
	
	public static ObservableList<BillDetailModel> retrieveSaleDateAfterSave(int billID) {
		ObservableList<BillDetailModel> list = FXCollections.observableArrayList();
		int lineNumber = 1;

		query = "SELECT ID, concat(inv.inv_name, ' - ', cat.category) AS Item, ex.QUANTITY, ex.UNITPRICE FROM expenseDetail ex "
				+ "INNER JOIN inventory inv ON ex.inv_id = inv.inv_id "
				+"inner join category cat on inv.category_id = cat.category_id "
				+ "WHERE expns_id = ?";
		
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String items = resultSet.getString("item");
				String unit =InventoryDAO.getInvUnitMeasure(items);
				String quantity = String.valueOf(resultSet.getDouble("quantity"));
				String unitPrice = String.valueOf(resultSet.getDouble("unitprice"));
				String lineTotal = Commons.calculateLineTotal(quantity, unitPrice);
				BillDetailModel model = new BillDetailModel(id, lineNumber, items, unit, quantity, unitPrice,
						lineTotal);
				list.add(model);
				lineNumber++;
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
	
	public static ObservableList<SalesSearchModel> searchExpense(String code, String company, String startDate,
			String endDate) {
		ObservableList<SalesSearchModel> list = FXCollections.observableArrayList();
		
		String billID = (null == code || "".equalsIgnoreCase(code)) ? "":code;
		String customerID = (null == company || "".equalsIgnoreCase(company)) ? "":company;
		String convertedStartDate = (null == startDate || "".equalsIgnoreCase(startDate)) ? "1900-01-01" : DateUtils.convertJalaliToGregory(startDate);
		String convertedEndDate = (null == endDate || "".equalsIgnoreCase(endDate)) ? "2900-12-31" : DateUtils.convertJalaliToGregory(endDate);
		query = "SELECT EB.EXPNS_ID , V.COMPANY, V.CURRENCY, EB.EXPNS_DATE , SUM(ED.QUANTITY * ED.UNITPRICE ) AS BILLTOTAL "
				+ "FROM EXPENSEBILL EB INNER JOIN VENDORS V ON EB.VENDOR_ID = V.VENDOR_ID "
				+ "INNER JOIN EXPENSEDETAIL ED ON EB.EXPNS_ID = ED.EXPNS_ID "
				+ "WHERE EB.EXPNS_ID LIKE ? AND V.VENDOR_ID LIKE ? AND EB.EXPNS_DATE BETWEEN ? AND ? "
				+ "GROUP BY EB.EXPNS_ID ORDER BY EB.EXPNS_ID";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setString(1, "%"+billID+"%");
			preStatement.setString(2, "%"+customerID+"%");
			preStatement.setString(3, convertedStartDate);
			preStatement.setString(4, convertedEndDate);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int billCode = resultSet.getInt("expns_id");
				String billCompany = resultSet.getString("company");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				String billDate = DateUtils.convertGregoryToJalali(resultSet.getString("expns_date"));
				String billTotal = resultSet.getString("billtotal");
				SalesSearchModel model = new SalesSearchModel(billCode, billCompany, currency, billDate, billTotal);
				list.add(model);
				
			}
		}catch (SQLException e) {
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
}
