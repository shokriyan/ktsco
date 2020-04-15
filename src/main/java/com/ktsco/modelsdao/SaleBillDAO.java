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
import com.ktsco.models.mgmt.AmountOweModal;
import com.ktsco.models.mgmt.SalesDetailModel;
import com.ktsco.models.mgmt.SellSummaryModel;
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

	public static boolean insertBillDetail(int id, int billID, String product, double quantity, double unitPrice) {
		boolean isSuccess = false;
		int detailID = (id > 0) ? id : 0;
		int productID = ProductDAO.getProductID(product);

		query = "INSERT INTO SALEDETAIL (ID, BILL_ID, PRODUCT_ID, QUANTITY, UNITPRICE) VALUES (?,?,?,?,?)"
				+ " ON DUPLICATE KEY UPDATE PRODUCT_ID = ? , QUANTITY = ? , UNITPRICE = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {

			preStatement.setInt(1, detailID);
			preStatement.setInt(2, billID);
			preStatement.setInt(3, productID);
			preStatement.setDouble(4, quantity);
			preStatement.setDouble(5, unitPrice);
			preStatement.setInt(6, productID);
			preStatement.setDouble(7, quantity);
			preStatement.setDouble(8, unitPrice);
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

		query = "SELECT ID,SD.PRODUCT_ID, P.PROD_NAME AS PRODUCT, QUANTITY, UNITPRICE FROM SALEDETAIL SD "
				+ "INNER JOIN PRODUCTS P ON SD.PRODUCT_ID = P.PROD_ID WHERE BILL_ID = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String prodCode = resultSet.getString("PRODUCT_ID");
				String product = resultSet.getString("product");
				String unit = ProductDAO.getUnitMeasure(product);
				double quantity = resultSet.getDouble("quantity");
				double unitPrice = resultSet.getDouble("unitprice");
				BigDecimal lineTotal = BigDecimal.valueOf(Commons.calculateLineTotal(quantity, unitPrice));
				BillDetailModel model = new BillDetailModel(id, prodCode, lineNumber, product, unit, quantity, unitPrice,
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

		String billID = (null == code || "".equalsIgnoreCase(code)) ? "" : code;
		String customerID = (null == company || "".equalsIgnoreCase(company)) ? "" : company;
		String convertedStartDate = (null == startDate || "".equalsIgnoreCase(startDate)) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(startDate);
		String convertedEndDate = (null == endDate || "".equalsIgnoreCase(endDate)) ? "2900-12-31"
				: DateUtils.convertJalaliToGregory(endDate);
		query = "SELECT SB.BILL_ID , C.COMPANY, C.CURRENCY, SB.BILLDATE, SB.DUEDATE , SUM(SD.QUANTITY * SD.UNITPRICE ) AS BILLTOTAL "
				+ "FROM SALEBILLS SB INNER JOIN CUSTOMERS C ON SB.CUSTOMER_ID = C.CUSTOMER_ID "
				+ "INNER JOIN SALEDETAIL SD ON SB.BILL_ID = SD.BILL_ID "
				+ "WHERE SB.BILL_ID LIKE ? AND C.CUSTOMER_ID LIKE ? AND SB.BILLDATE BETWEEN ? AND ? "
				+ "GROUP BY SB.BILL_ID ORDER BY BILL_ID";
		preStatement = DatabaseUtils.dbPreparedStatment(query);

		try {
			preStatement.setString(1, "%" + billID + "%");
			preStatement.setString(2, "%" + customerID + "%");
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
				SalesSearchModel model = new SalesSearchModel(billCode, billCompany, currency, billDate, dueDate,
						billTotal);
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

	public static ObservableList<SellSummaryModel> getSalesSummaryReport(String code, String fromDate, String toDate,
			String currency) {
		
		ObservableList<SellSummaryModel> list = FXCollections.observableArrayList();
		String currKey = (currency.equalsIgnoreCase("")) ? "" : Commons.getCurrencyKey(currency);
		String convFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(fromDate);
		String convToDate = (toDate.equalsIgnoreCase("")) ? "2900-12-31" : DateUtils.convertJalaliToGregory(toDate);

		query = "SELECT sb.bill_id,c.customer_id, sb.company, sb.billdate, sb.currencytype, "
				+ "(select rate from currencies where currency = sb.currencytype and entryDate = sb.billdate) as currencyrate, "
				+ "sb.billtotal FROM salesbilltotal sb left outer join received r on r.bill_id = sb.bill_id "
				+ "inner join customers c on c.customer_id = (select customer_id from salebills where bill_id = sb.bill_id) "
				+ "where c.customer_id like ? and sb.currencytype like ? and sb.billdate between ? and ? "
				+ "group by sb.bill_id, c.customer_id order by sb.bill_id";

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + code + "%");
			preStatement.setString(2, "%" + currKey + "%");
			preStatement.setString(3, convFromDate);
			preStatement.setString(4, convToDate);
			resultSet = preStatement.executeQuery();

			while (resultSet.next()) {
				int billID = resultSet.getInt("bill_id");
				String customer = resultSet.getString("company");
				String billdate = DateUtils.convertGregoryToJalali(resultSet.getString("billdate"));
				String currencyType = Commons.getCurrencyValue(resultSet.getString("currencytype"));
				BigDecimal rateBig = resultSet.getBigDecimal("currencyrate");
				double currencyRate = rateBig.doubleValue();
				double originalPrice = resultSet.getDouble("billtotal");
				double usdTotal = originalPrice * currencyRate;
				SellSummaryModel model = new SellSummaryModel(billID, customer, currencyType, billdate, rateBig.toPlainString(),
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

	public static ObservableList<SellSummaryModel> getSalesUSDSummaryReport(String customerCode, String fromDate,
			String toDate) {

		ObservableList<SellSummaryModel> list = FXCollections.observableArrayList();
		String convFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01"
				: DateUtils.convertJalaliToGregory(fromDate);
		String convToDate = (toDate.equalsIgnoreCase("")) ? "2900-12-31" : DateUtils.convertJalaliToGregory(toDate);

		query = "SELECT sb.bill_id,c.customer_id, sb.company, sb.billdate, (sb.billtotal * cu.rate) as billtotal, ifnull(urt.receivedtotal,0) as receivedtotal "
				+ "FROM salesbilltotal sb\n" + "left outer join usdreceivedtotal urt on urt.bill_id = sb.bill_id "
				+ "inner join customers c on (c.customer_id = (select customer_id from salebills where bill_id = sb.bill_id)) "
				+ "inner join currencies cu on cu.currency = sb.currencytype "
				+ "where sb.billdate = cu.entryDate and c.customer_id like ? and sb.billdate between ? and ? "
				+ "order by sb.bill_id asc";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + customerCode + "%");
			preStatement.setString(2, convFromDate);
			preStatement.setString(3, convToDate);
			resultSet = preStatement.executeQuery();

			while (resultSet.next()) {
				int billID = resultSet.getInt("bill_id");
				String customer = resultSet.getString("company");
				String billdate = DateUtils.convertGregoryToJalali(resultSet.getString("billdate"));
				double billTotal = resultSet.getDouble("billtotal");
				double receivedTotal = resultSet.getDouble("receivedtotal");
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

	public static ObservableList<SalesDetailModel>  getSalesDetailReport(String productName, String currencyType, String fromDate, String toDate) {
		ObservableList<SalesDetailModel> list = FXCollections.observableArrayList();
		String productCode = (productName.equalsIgnoreCase("")) ? "" : String.valueOf(ProductDAO.getProductID(productName));
		
		String convertyCurrencyType = Commons.getCurrencyKey(currencyType);
		String convertFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01" : DateUtils.convertJalaliToGregory(fromDate);
		String convertToDate = (fromDate.equalsIgnoreCase("")) ? "2999-12-30" : DateUtils.convertJalaliToGregory(toDate);
		
		query = "SELECT sb.bill_id, sb.billdate, sd.product_id, concat(pd.prod_name, ' - ' , prod_um) as product, sb.currencytype,cu.rate, "
				+ "sd.quantity, sd.unitprice From salebills sb inner join saledetail sd on sd.bill_id = sb.bill_id "
				+ "inner join products pd on pd.prod_id = sd.product_id inner join currencies cu on cu.currency = sb.currencytype "
				+ "where cu.entrydate = sb.billdate and sd.product_id like ? and sb.currencyType like ? and sb.billdate between ? and ? "
				+ "order by sb.bill_id asc ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + productCode + "%");
			preStatement.setString(2, "%" + convertyCurrencyType + "%");
			preStatement.setString(3,  convertFromDate );
			preStatement.setString(4, convertToDate);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int billCode = resultSet.getInt("bill_id");
				int prodCode = resultSet.getInt("product_id");
				String prodName = resultSet.getString("product");
				String saleDate = DateUtils.convertGregoryToJalali(resultSet.getString("billdate"));
				String currency = Commons.getCurrencyValue(resultSet.getString("currencytype"));
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
	
	public static ObservableList<AmountOweModal>  getSalesAmountOweReport(String cusomterCode, String currencyType, String fromDate, String toDate) {
		ObservableList<AmountOweModal> list = FXCollections.observableArrayList(); 
		String customerId = (cusomterCode.equalsIgnoreCase("")) ? "" : cusomterCode;
		String convertyCurrencyType = Commons.getCurrencyKey(currencyType);
		String convertFromDate = (fromDate.equalsIgnoreCase("")) ? "1900-01-01" : DateUtils.convertJalaliToGregory(fromDate);
		String convertToDate = (fromDate.equalsIgnoreCase("")) ? "2999-12-30" : DateUtils.convertJalaliToGregory(toDate);
		
		query = "select sb.customer_id,c.company, c.currency, SUM(sbt.billTotal) as saleTotal , IFNULL(SUM(r.amount) , 0) as receivedTotal  from salebills sb " + 
				"inner join salesbilltotal sbt on sbt.bill_id = sb.bill_id " + 
				"left outer join received r on r.bill_id = sb.bill_id " + 
				"inner join customers c on c.customer_id = sb.customer_id " + 
				"where sb.customer_id like ? and c.currency like ? and sb.billdate between ? and ? " + 
				"group by sb.customer_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, customerId + "%");
			preStatement.setString(2, "%" + convertyCurrencyType + "%");
			preStatement.setString(3,  convertFromDate );
			preStatement.setString(4, convertToDate);
			
			resultSet = preStatement.executeQuery(); 
			while (resultSet.next()) {
				int id = resultSet.getInt("customer_id");
				String company = resultSet.getString("company");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				double billTotal = resultSet.getDouble("saleTotal");
				double receivedTotal = resultSet.getDouble("receivedTotal");
				double amountRemained = billTotal - receivedTotal; 
				
				AmountOweModal model = new AmountOweModal(id, company, currency, billTotal, receivedTotal, 0, amountRemained);
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
