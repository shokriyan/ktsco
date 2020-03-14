package com.ktsco.modelsdao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.PaybillModel;
import com.ktsco.models.csr.ReceivableModel;
import com.ktsco.models.mgmt.ReceivedDetailModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PayableDAO {

	private static Logger log = LoggerFactory.getLogger(ReceivableDAO.class);

	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<ReceivableModel> retrievePayableList() {
		ObservableList<ReceivableModel> list = FXCollections.observableArrayList();
		query = "select eb.expns_id, eb.expns_date, eb.currency, v.company, Sum(ed.unitprice*ed.quantity) as billTotal from expensebill eb "
				+ "inner join vendors v on eb.vendor_id = v.vendor_id "
				+ "inner join expenseDetail ed on eb.expns_id = ed.expns_id "
				+ "where eb.paidflag = 0 group by eb.expns_id";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("expns_id");
				String company = resultSet.getString("company");
				String billDate = DateUtils.convertGregoryToJalali(resultSet.getString("expns_date"));
				double billTotal = resultSet.getDouble("billtotal");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				ReceivableModel model = new ReceivableModel(code, company, billDate, billTotal, currency);
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

	public static void updatePayAmount(String newAmount, int payid) {
		query = "update paybills set amount = ? where pay_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setDouble(1, Double.parseDouble(newAmount));
			preStatement.setInt(2, payid);
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

	public static void updateExpensePaidFlagSale(boolean flag, int expnsID) {
		query = "update expensebill set paidflag = ? where expns_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			int paidFlag = (flag) ? 1 : 0;
			preStatement.setInt(1, paidFlag);
			preStatement.setInt(2, expnsID);
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

	public static Map<String, Object> reteivedBillDetail(int code) {
		Map<String, Object> billDetail = new HashMap<String, Object>();
		query = "select ebt.expns_id, eb.expns_date, v.company , eb.currency, ebt.billtotal, ept.totalPaid from expnsbilltotal ebt "
				+ "inner join expenseBill eb on ebt.expns_id = eb.expns_id "
				+ "inner join vendors v on v.vendor_id = eb.vendor_id "
				+ "left outer join expnspaytotal ept on ebt.expns_id = ept.expns_id "
				+ "where ebt.expns_id = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setInt(1, code);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				billDetail.put("expns_id", resultSet.getInt("expns_id"));
				billDetail.put("company", resultSet.getString("company"));
				billDetail.put("expns_date", DateUtils.convertGregoryToJalali(resultSet.getString("expns_date")));
				billDetail.put("currency", Commons.getCurrencyValue(resultSet.getString("currency")));
				billDetail.put("billtotal", resultSet.getDouble("billtotal"));
				billDetail.put("totalPaid", resultSet.getDouble("totalPaid"));
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
		return billDetail;

	}
	
	public static ObservableList<PaybillModel> getPaymentsDetail(int billID){
		ObservableList<PaybillModel> list = FXCollections.observableArrayList();
		query = "select pay_id,paydate,amount, bank_name from paybills p inner join accounts a on a.account_id = p.account_id where expns_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);
			
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receiveID = resultSet.getInt("pay_id");
				String receiveDate = DateUtils.convertGregoryToJalali(resultSet.getString("paydate"));
				String bankAccount = resultSet.getString("bank_name");
				double amount = resultSet.getDouble("amount");
				
				PaybillModel model = new PaybillModel(receiveID, receiveDate, bankAccount, amount);
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
	
	public static boolean insertIntoPayment (int expnsID, String payDate, String employee, String amount, String bankAccount) {
		boolean isSuccess = false; 
		
		query = "insert into paybills (expns_id,account_id,employee,paydate,amount) values (?,?,?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setInt(1, expnsID);
			preStatement.setInt(2, AccountsDAO.getBankID(bankAccount.split("-")[1].trim()));
			preStatement.setInt(3, EmployeeDAO.getEmployeeID(employee));
			preStatement.setString(4, DateUtils.convertJalaliToGregory(payDate));
			preStatement.setDouble(5, Double.parseDouble(amount));
			
			preStatement.execute();
			isSuccess = true;
			
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
		
		return isSuccess;
	}
	
	public static void deletePaybillRecord(int pay_id) {
		query = "delete from paybills where pay_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, pay_id);
			preStatement.execute();
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
	}
	
	public static ObservableList<PaybillModel> searchPayments(String billID, String startDate, String endDate, String employee){
		ObservableList<PaybillModel> list = FXCollections.observableArrayList();
		billID = (!"".equalsIgnoreCase(billID)) ? billID : "";
		String gstartDate = (!"".equalsIgnoreCase(startDate)) ? DateUtils.convertJalaliToGregory(startDate) : "1900-01-01";
		String gendDate = (!"".equalsIgnoreCase(endDate)) ?DateUtils.convertJalaliToGregory(endDate): "2900-12-31"; 
		String empID = (!"".equalsIgnoreCase(employee))? String.valueOf(EmployeeDAO.getEmployeeID(employee)):"";
		
		query = "select p.pay_id, p.expns_id, p.paydate, p.amount, e.fullname,concat(a.bank_name ,' - ' ,a.bank_Accnt) as bankAccount from paybills p\n" + 
				"inner join employee e on e.employee_id = p.employee\n" + 
				"inner join accounts a on a.account_id = p.account_id\n" + 
				"where p.expns_id like ? AND p.paydate between ? and ? AND p.employee like ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%"+billID+"%");
			preStatement.setString(2, gstartDate);
			preStatement.setString(3, gendDate);
			preStatement.setString(4, "%"+empID+"%");
			
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int payID = resultSet.getInt("pay_id");
				int expnsID = resultSet.getInt("expns_id");
				String payDate = DateUtils.convertGregoryToJalali(resultSet.getString("paydate"));
				String employeeName = resultSet.getString("fullname");
				double amount = resultSet.getDouble("amount");
				String bankAccount = resultSet.getString("bankAccount");
				
				PaybillModel model = new PaybillModel(payID, expnsID, payDate, employeeName, bankAccount, amount);
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

	public static ObservableList<ReceivedDetailModel> getPaymentsReport(String employee, String currencyType,
			String fromDate, String toDate) {
		
		ObservableList<ReceivedDetailModel> list = FXCollections.observableArrayList();

		query = "select pb.pay_id, pb.expns_id, eb.currency, cu.rate, pb.employee,em.fullname, pb.paydate, pb.amount " + 
				"from paybills pb " + 
				"inner join expensebill eb on eb.expns_id = pb.expns_id " + 
				"inner join employee em on em.employee_id = pb.employee " + 
				"inner join currencies cu on cu.currency = eb.currency where cu.entrydate = pb.paydate " + 
				"and pb.employee like ? and eb.currency like ? and pb.paydate between ? and ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, employee + "%");
			preStatement.setString(2, "%" + currencyType + "%");
			preStatement.setString(3, fromDate);
			preStatement.setString(4, toDate);
			log.info("Execute Received Detail Query " + preStatement);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receivedId = resultSet.getInt("pay_id");
				int billId = resultSet.getInt("expns_id");
				int employeeId = resultSet.getInt("employee");
				String employeeName = resultSet.getString("fullname");
				String currency =Commons.getCurrencyValue(resultSet.getString("currency"));
				String receivedDate = DateUtils.convertGregoryToJalali(resultSet.getString("paydate"));
				BigDecimal currencyRate = resultSet.getBigDecimal("rate");
				double originalAmount = resultSet.getDouble("amount");
				double dolarAmount = originalAmount * currencyRate.doubleValue();

				ReceivedDetailModel model = new ReceivedDetailModel(receivedId, billId, employeeId, employeeName,
						currency, currencyRate.toPlainString(), receivedDate, originalAmount, dolarAmount);
				
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
	

}
