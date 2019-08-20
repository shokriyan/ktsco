package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.ReceivableModel;
import com.ktsco.models.csr.ReceiveModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceivableDAO {

	private static Logger log = LoggerFactory.getLogger(ReceivableDAO.class);
	
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<ReceivableModel> retriveReceivableList() {
		ObservableList<ReceivableModel> list = FXCollections.observableArrayList();
		query = "SELECT ar.bill_id, c.company, ar.billdate, ar.duedate, ar.billtotal, c.currency FROM ktscodb.accountreceivable ar "
				+ "inner join customers c on c.customer_id = ar.customer_id order by ar.duedate;";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("bill_id");
				String company = resultSet.getString("company");
				String billDate = DateUtils.convertGregoryToJalali(resultSet.getString("billdate"));
				String duedate = DateUtils.convertGregoryToJalali(resultSet.getString("duedate"));
				double billTotal = resultSet.getDouble("billtotal");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				ReceivableModel model = new ReceivableModel(code, company, billDate, duedate, billTotal, currency);
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
	
	public static Map<String, Object> reteivedBillDetail(int code){
		Map<String, Object> billDetail = new HashMap<String, Object>();
		query = "SELECT sbt.bill_id, sbt.company, sbt.billdate, sbt.currencyType, sbt.billtotal, sum(r.amount) as totalReceived "
				+ "FROM ktscodb.salesBillTotal  sbt "
				+"left outer join received r on sbt.bill_id = r.bill_id "
				+"where sbt.bill_id = ? "
				+"group by bill_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, code);
			resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				billDetail.put("billID", resultSet.getInt("bill_id"));
				billDetail.put("company", resultSet.getString("company"));
				billDetail.put("billdate", DateUtils.convertGregoryToJalali(resultSet.getString("billdate")));
				billDetail.put("currency", Commons.getCurrencyValue(resultSet.getString("currencyType")));
				billDetail.put("billTotal", resultSet.getDouble("billTotal"));
				billDetail.put("totalReceived", resultSet.getDouble("totalReceived"));
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
		return billDetail;
		
	}
	
	public static ObservableList<ReceiveModel> getReceiveDetailByID(int billID){
		ObservableList<ReceiveModel> list = FXCollections.observableArrayList();
		query = "select receive_id, receive_date, amount from received where bill_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, billID);
			
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receiveID = resultSet.getInt("receive_id");
				String receiveDate = DateUtils.convertGregoryToJalali(resultSet.getString("receive_date"));
				double amount = resultSet.getDouble("amount");
				
				ReceiveModel model = new ReceiveModel(receiveID, receiveDate, amount);
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
	
	public static boolean insertIntoReceiveTable (int billID, String receiveDate, String employee, String amount, String depositeType) {
		boolean isSuccess = false; 
		
		query = "insert into received (bill_id, receive_date, emp_id, amount, deposittype) values (?,?,?,?,?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setInt(1, billID);
			preStatement.setString(2, DateUtils.convertJalaliToGregory(receiveDate));
			preStatement.setInt(3, EmployeeDAO.getEmployeeID(employee));
			preStatement.setDouble(4, Double.parseDouble(amount));
			preStatement.setInt(5, Commons.getDepositType(depositeType));
			
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
	
	public static void updateBillPaidFlagSale(boolean flag, int bill_id) {
		query = "update salebills set paidflag = ? where bill_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			int paidFlag = (flag) ? 1 :0;
			preStatement.setInt(1, paidFlag);
			preStatement.setInt(2, bill_id);
			preStatement.executeUpdate();
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
	
	public static void updateReceiveAmount(String newAmount , int recieve_id) {
		query = "update received set amount = ? where receive_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setDouble(1, Double.parseDouble(newAmount));
			preStatement.setInt(2, recieve_id);
			preStatement.executeUpdate();
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
	
	public static void deleteReceiveRecord(int recieve_id) {
		query = "delete from received where receive_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, recieve_id);
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
	
	public static ObservableList<ReceiveModel> searchReceiveRecords(String billID, String startDate, String endDate, String employee){
		ObservableList<ReceiveModel> list = FXCollections.observableArrayList();
		billID = (!"".equalsIgnoreCase(billID)) ? billID : "";
		String gstartDate = (!"".equalsIgnoreCase(startDate)) ? DateUtils.convertJalaliToGregory(startDate) : "1900-01-01";
		String gendDate = (!"".equalsIgnoreCase(endDate)) ?DateUtils.convertJalaliToGregory(endDate): "2900-12-31"; 
		String empID = (!"".equalsIgnoreCase(employee))? String.valueOf(EmployeeDAO.getEmployeeID(employee)):"";
		
		query = "select r.receive_id, r.bill_id, e.fullname, r.receive_date, r.amount , r.deposittype " + 
				"from received r inner join employee e on e.employee_id = r.emp_id " + 
				"where r.bill_id like ? AND r.receive_date between ? and ? AND r.emp_id like ? " + 
				"order by r.receive_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%"+billID+"%");
			preStatement.setString(2, gstartDate);
			preStatement.setString(3, gendDate);
			preStatement.setString(4, "%"+empID+"%");
			
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receiveID = resultSet.getInt("receive_id");
				int bill_id = resultSet.getInt("bill_id");
				String receiveDate = DateUtils.convertGregoryToJalali(resultSet.getString("receive_date"));
				String employeeName = resultSet.getString("fullname");
				double receiveAmount = resultSet.getDouble("amount");
				String depositType = Constants.depositTypeList.get(resultSet.getInt("deposittype"));
				
				ReceiveModel model = new ReceiveModel(receiveID, bill_id, receiveDate, employeeName, depositType, receiveAmount);
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
	
	public static boolean getDepositFlag(int receiveID) {
		boolean isDeposit = false; 
		query = "select received_flag from received where receive_id = ?"; 
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, receiveID);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int flagCheck = resultSet.getInt("received_flag");
				if (flagCheck == 0)
					isDeposit = false; 
				else
					isDeposit = true; 
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
		
		return isDeposit; 
	}

}
