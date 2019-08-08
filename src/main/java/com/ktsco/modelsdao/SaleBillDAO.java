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

public class SaleBillDAO {

	private static Logger log = LoggerFactory.getLogger(SaleBillDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static int getLastBillID() {
		int billID = 0;
		query = "SELECT MAX(BILL_ID) FROM SALEBILLS";
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

	public static int getBillDetailID() {
		int id = 0;
		query = "SELECT MAX(ID) FROM SALEDETAIL";
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
				if (resultSet != null)
					resultSet.close();
				if (preStatement != null)
					preStatement.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return id + 1;
	}

	public static boolean insertIntoSaleBill(int bill_id, int customerId, String billDate, int payTerm,
			String currencyType, String billMemo) {
		boolean isSuccess = false;
		query = "INSERT INTO SALEBILLS (BILL_ID, CUSTOMER_ID, BILLDATE, DUEDATE, CURRENCYTYPE, BILLMEMO) VALUES "
				+ "(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE CUSTOMER_ID = ? , BILLDATE = ? , DUEDATE = ? , CURRENCYTYPE = ? , BILLMEMO = ?";
		String convBillDate = "";
		String calDueDate = "";
		if (DateUtils.checkEntryDateFormat(billDate)) {
			LocalDate convertedDate = LocalDate.parse(DateUtils.convertJalaliToGregory(billDate),
					DateTimeFormatter.ISO_DATE);
			convBillDate = String.valueOf(convertedDate);
			calDueDate = Commons.calculateDueDate(convBillDate, payTerm);

		} else {
			log.error("Wrong Date Entry " + billDate);
			throw new RuntimeException("Wrong Date Entry");
		}

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, bill_id);
			preStatement.setInt(2, customerId);
			preStatement.setString(3, convBillDate);
			preStatement.setString(4, calDueDate);
			preStatement.setString(5, currencyType);
			preStatement.setString(6, billMemo);
			preStatement.setInt(7, customerId);
			preStatement.setString(8, convBillDate);
			preStatement.setString(9, calDueDate);
			preStatement.setString(10, currencyType);
			preStatement.setString(11, billMemo);
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
		int productID = ProductDAO.getProductID(product);
		double quantityValue = Double.parseDouble(quantity.replace(",", ""));
		double unitpriceValue = Double.parseDouble(unitPrice.replace(",", ""));

		query = "INSERT INTO SALEDETAIL (ID, BILL_ID, PRODUCT_ID, QUANTITY, UNITPRICE) VALUES (?,?,?,?,?)"
				+ " ON DUPLICATE KEY UPDATE PRODUCT_ID = ? , QUANTITY = ? , UNITPRICE = ?";
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

	public static ObservableList<BillDetailModel> retrieveSaleDateAfterSave(int billID) {
		ObservableList<BillDetailModel> list = FXCollections.observableArrayList();
		int lineNumber = 1;

		query = "SELECT ID, P.PROD_NAME AS PRODUCT, QUANTITY, UNITPRICE FROM SALEDETAIL SD "
				+ "INNER JOIN PRODUCTS P ON SD.PRODUCT_ID = P.PROD_ID WHERE BILL_ID = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String product = resultSet.getString("product");
				String unit = ProductDAO.getUnitMeasure(product);
				String quantity = String.valueOf(resultSet.getDouble("quantity"));
				String unitPrice = String.valueOf(resultSet.getDouble("unitprice"));
				String lineTotal = Commons.calculateLineTotal(quantity, unitPrice);
				BillDetailModel model = new BillDetailModel(id, lineNumber, product, unit, quantity, unitPrice,
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

	public static Map<String, Object> retieveSaleBillDate(int billID) {
		Map<String, Object> data = new HashMap<String, Object>();
		query = "SELECT * FROM SALEBILLS WHERE BILL_ID = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int customerCode = resultSet.getInt("customer_id");
				data.put("customerCode", customerCode);
				String billDate = resultSet.getString("billDate");
				data.put("billDate", billDate);
				String dueDate = resultSet.getString("dueDate");
				data.put("dueDate", dueDate);
				String billMemo = resultSet.getString("billmemo");
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
		query = "Delete from salebills where bill_id = ?";
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

	public static boolean deleteSaleDetail(int id) {
		boolean isSuccess = false;
		query = "Delete from saleDetail where id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, id);
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

	public static ObservableList<SalesSearchModel> searchForSalesBill(String code, String company, String startDate,
			String endDate) {
		ObservableList<SalesSearchModel> list = FXCollections.observableArrayList();
		
		String billID = (null == code || "".equalsIgnoreCase(code)) ? "":code;
		String customerID = (null == company || "".equalsIgnoreCase(company)) ? "":company;
		String convertedStartDate = (null == startDate || "".equalsIgnoreCase(startDate)) ? "1900-01-01" : DateUtils.convertJalaliToGregory(startDate);
		String convertedEndDate = (null == endDate || "".equalsIgnoreCase(endDate)) ? "2900-12-31" : DateUtils.convertJalaliToGregory(endDate);
		query = "SELECT SB.BILL_ID , C.COMPANY, C.CURRENCY, SB.BILLDATE, SB.DUEDATE , SUM(SD.QUANTITY * SD.UNITPRICE ) AS BILLTOTAL "
				+ "FROM SALEBILLS SB INNER JOIN CUSTOMERS C ON SB.CUSTOMER_ID = C.CUSTOMER_ID "
				+ "INNER JOIN SALEDETAIL SD ON SB.BILL_ID = SD.BILL_ID "
				+ "WHERE SB.BILL_ID LIKE ? AND C.CUSTOMER_ID LIKE ? AND SB.BILLDATE BETWEEN ? AND ? "
				+ "GROUP BY SB.BILL_ID ORDER BY BILL_ID";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setString(1, "%"+billID+"%");
			preStatement.setString(2, "%"+customerID+"%");
			preStatement.setString(3, convertedStartDate);
			preStatement.setString(4, convertedEndDate);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int billCode = resultSet.getInt("bill_ID");
				String billCompany = resultSet.getString("company");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				String billDate = DateUtils.convertGregoryToJalali(resultSet.getString("billDate"));
				String dueDate = DateUtils.convertGregoryToJalali(resultSet.getString("duedate"));
				String billTotal = resultSet.getString("billtotal");
				SalesSearchModel model = new SalesSearchModel(billCode, billCompany, currency, billDate, dueDate, billTotal);
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
