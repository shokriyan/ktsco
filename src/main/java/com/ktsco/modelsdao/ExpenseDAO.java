package com.ktsco.modelsdao;

import java.math.BigDecimal;
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
import com.ktsco.models.mgmt.SalesDetailModel;
import com.ktsco.models.mgmt.SellSummaryModel;
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
	

	public static boolean deleteBillDetail(int id) {
		boolean isSuccess = false;
		query = "Delete from expenseDetail where id = ?";
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
	
	public static ObservableList<SellSummaryModel> getExpenseSummaryReport(String code, String fromDate, String toDate,
			String currency) {
		
		ObservableList<SellSummaryModel> list = FXCollections.observableArrayList();
		String currKey = (currency.equalsIgnoreCase("")) ? "" : Commons.getCurrencyKey(currency);
		String convFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(fromDate);
		String convToDate = (toDate.equalsIgnoreCase("")) ? "2900-12-31" : DateUtils.convertJalaliToGregory(toDate);

		query = "SELECT ex.expns_id,eb.expns_date ,eb.vendor_id, v.company, eb.currency,cu.rate, ex.billtotal FROM expnsbilltotal ex " + 
				"inner join expenseBill eb on eb.expns_id = ex.expns_id " + 
				"inner join vendors v on v.vendor_id = eb.vendor_id " + 
				"inner join currencies cu on cu.currency = eb.currency " + 
				"where cu.entryDate = eb.expns_date and eb.vendor_id like ? and eb.currency like ? and eb.expns_date between ? and ? \n" + 
				"order by ex.expns_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, code + "%");
			preStatement.setString(2, "%" + currKey + "%");
			preStatement.setString(3, convFromDate);
			preStatement.setString(4, convToDate);
			resultSet = preStatement.executeQuery();

			while (resultSet.next()) {
				int billID = resultSet.getInt("expns_id");
				String customer = resultSet.getString("company");
				String billdate = DateUtils.convertGregoryToJalali(resultSet.getString("expns_date"));
				String currencyType = Commons.getCurrencyValue(resultSet.getString("currency"));
				BigDecimal rateBigDecimal = resultSet.getBigDecimal("rate");
				double currencyRate = rateBigDecimal.doubleValue();
				double originalPrice = resultSet.getDouble("billtotal");
				double usdTotal = originalPrice * currencyRate;
				SellSummaryModel model = new SellSummaryModel(billID, customer, currencyType, billdate, rateBigDecimal.toPlainString(),
						originalPrice, 0, usdTotal);
				list.add(model);
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

	public static ObservableList<SellSummaryModel> getExpensePaymentSummaryReport(String customerCode, String fromDate,
			String toDate) {
		ObservableList<SellSummaryModel> list = FXCollections.observableArrayList();
		String convFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(fromDate);
		String convToDate = (toDate.equalsIgnoreCase("")) ? "2900-12-31" : DateUtils.convertJalaliToGregory(toDate);

		query = "SELECT ex.expns_id,eb.expns_date ,eb.vendor_id, v.company, (ex.billtotal * cu.rate) as billTotal , " + 
				"IFNULL(tpd.paiddoloramount,0) as payTotal FROM expnsbilltotal ex " + 
				"inner join expenseBill eb on eb.expns_id = ex.expns_id " + 
				"left join totalpaiddolor tpd on tpd.expns_id = eb.expns_id " + 
				"inner join vendors v on v.vendor_id = eb.vendor_id " + 
				"inner join currencies cu on cu.currency = eb.currency " + 
				"where cu.entryDate = eb.expns_date and eb.vendor_id like ? and eb.expns_date between ? and ?" + 
				"order by ex.expns_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, customerCode + "%");
			preStatement.setString(2, convFromDate);
			preStatement.setString(3, convToDate);
			resultSet = preStatement.executeQuery();

			while (resultSet.next()) {
				int billID = resultSet.getInt("expns_id");
				String customer = resultSet.getString("company");
				String billdate = DateUtils.convertGregoryToJalali(resultSet.getString("expns_date"));
				double billTotal = resultSet.getDouble("billTotal");
				double receivedTotal = resultSet.getDouble("payTotal");
				double remainedTotal = billTotal - receivedTotal;
				SellSummaryModel model = new SellSummaryModel(billID, customer, "", billdate, "0", billTotal,
						receivedTotal, remainedTotal);
				list.add(model);
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

	public static ObservableList<SalesDetailModel> getExpenseDetailReport(String productName, String currencyType,
			String fromDate, String toDate) {
		
		ObservableList<SalesDetailModel> list = FXCollections.observableArrayList();
		String productCode = (productName.equalsIgnoreCase("")) ? "" : productName.split("-")[0].trim();
		
		String convertyCurrencyType = Commons.getCurrencyKey(currencyType);
		String convertFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01" : DateUtils.convertJalaliToGregory(fromDate);
		String convertToDate = (fromDate.equalsIgnoreCase("")) ? "2999-12-30" : DateUtils.convertJalaliToGregory(toDate);
		
		query = "select eb.expns_id, eb.expns_date, eb.currency, ed.inv_id, concat(inv.inv_name,' - ',inv.inv_um) as item, " + 
				"ed.unitprice, ed.quantity, cu.rate " + 
				"from expensebill eb " + 
				"inner join expenseDetail ed on ed.expns_id = eb.expns_id " + 
				"inner join inventory inv on inv.inv_id = ed.inv_id " + 
				"inner join currencies cu on cu.currency = eb.currency where cu.entrydate = eb.expns_date " + 
				"and ed.inv_id like ? and cu.currency like ? and eb.expns_date between ? and ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, productCode + "%");
			preStatement.setString(2, "%" + convertyCurrencyType + "%");
			preStatement.setString(3,  convertFromDate );
			preStatement.setString(4, convertToDate);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int billCode = resultSet.getInt("expns_id");
				int prodCode = resultSet.getInt("inv_id");
				String prodName = resultSet.getString("item");
				String saleDate = DateUtils.convertGregoryToJalali(resultSet.getString("expns_date"));
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				BigDecimal currencyRate = resultSet.getBigDecimal("rate");
				double quantity = resultSet.getDouble("quantity");
				double unitPrice = resultSet.getDouble("unitprice");
				double originalLinePrice = quantity * unitPrice; 
				double usdLinePrice = originalLinePrice * currencyRate.doubleValue(); 
				
				SalesDetailModel model = new SalesDetailModel(billCode, prodCode, prodName, saleDate, currency, currencyRate.toPlainString(), quantity, unitPrice, originalLinePrice, usdLinePrice);
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
