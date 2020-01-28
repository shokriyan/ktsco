package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.BankTransModel;
import com.ktsco.models.csr.DepositModel;
import com.ktsco.models.csr.DepositSearchModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DepositDAO {
	private static Logger log = LoggerFactory.getLogger(AccountsDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<DepositModel> getTableData(String depositType, String currecy) {
		ObservableList<DepositModel> list = FXCollections.observableArrayList();

		if (Commons.getDepositType(depositType) == 0) {
			query = "SELECT ddw.receive_id, sb.currencyType,ddw.bill_id, ddw.amount FROM ktscodb.directdepositview ddw "
					+ "inner join salebills sb on sb.bill_id = ddw.bill_id " + "where sb.currencyType = ? ";
		} else if (Commons.getDepositType(depositType) == 1) {
			query = "SELECT ddw.receive_id, sb.currencyType,ddw.bill_id, ddw.amount FROM undefinddeposit ddw "
					+ "inner join salebills sb on sb.bill_id = ddw.bill_id " + "where sb.currencyType = ? ";
		} else {
			throw new RuntimeException("Deposit type and Currency Can not be null");
		}

		String currencyKey = Commons.getCurrencyKey(currecy);
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, currencyKey);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receiveID = resultSet.getInt("receive_id");
				int billID = resultSet.getInt("bill_id");
				String currencyValue = Commons.getCurrencyValue(resultSet.getString("currencyType"));
				double amount = resultSet.getDouble("amount");
				DepositModel model = new DepositModel(receiveID, billID, currencyValue, amount);
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

	public static boolean insertDataToTable(int receiveID, String amount, String bank, String date, String voucherNo,
			String employee) {
		boolean isSuccess = false;

		double depAmount = Double.parseDouble(amount);
		String gdate = DateUtils.convertJalaliToGregory(date);
		int empID = EmployeeDAO.getEmployeeID(employee);
		int bankID = AccountsDAO.getBankID(bank.split("-")[1].trim());

		query = "insert into bankdeposit (AMOUNT,VOUCHERNO,TRANSACTIONDATE, received_RECEIVE_ID,banks_bank_id,employee_employee_id) "
				+ "values (?,?,?,?,?,?) ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);

		try {
			preStatement.setDouble(1, depAmount);
			preStatement.setString(2, voucherNo);
			preStatement.setString(3, gdate);
			if (receiveID != 0) {
				preStatement.setInt(4, receiveID);
			} else {
				preStatement.setNull(4, Types.INTEGER);
			}
			preStatement.setInt(5, bankID);
			preStatement.setInt(6, empID);

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
	

	public static ObservableList<DepositSearchModel> depositSearchList(String bank, String employee,
			String inputStartDate, String inputEndDate, String voucherNumber) {
		ObservableList<DepositSearchModel> list = FXCollections.observableArrayList();

		String accountID = (!"".equalsIgnoreCase(bank))
				? String.valueOf(AccountsDAO.getBankID(bank.split("-")[1].trim()))
				: "";
		String gStartDate = (!inputStartDate.equalsIgnoreCase("")) ? DateUtils.convertJalaliToGregory(inputStartDate)
				: "1900-01-01";
		String gEndDate = (!inputEndDate.equalsIgnoreCase("")) ? DateUtils.convertJalaliToGregory(inputEndDate)
				: "2900-12-30";

		query = "Select  bd.received_RECEIVE_ID, bd.banks_bank_id, emp.fullname, bd.VOUCHERNO, bd.TRANSACTIONDATE, bd.AMOUNT from BANKDEPOSIT bd "
				+ "inner join employee emp on emp.employee_id = bd.employee_employee_id "
				+ "where bd.VOUCHERNO like ? and emp.fullname like ? and bd.banks_bank_id like ? and bd.TRANSACTIONDATE between ? and ? ";

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + voucherNumber + "%");
			preStatement.setString(2, "%" + employee + "%");
			preStatement.setString(3, "%" + accountID + "%");
			preStatement.setString(4, gStartDate);
			preStatement.setString(5, gEndDate);

			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int receiveID = resultSet.getInt("received_RECEIVE_ID");
				String bankName = AccountsDAO.getBankAccounts(resultSet.getInt("banks_bank_id"));
				String emp = resultSet.getString("fullname");
				String voucher = resultSet.getString("VOUCHERNO");
				String jDepositDate = DateUtils.convertGregoryToJalali(resultSet.getString("TRANSACTIONDATE"));
				double depositAmount = resultSet.getDouble("AMOUNT");

				DepositSearchModel model = new DepositSearchModel(receiveID, bankName, emp, voucher, jDepositDate,
						depositAmount);
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

	public static boolean saveBankTransactions(int receiveID, String amount, String fromBank, String tobank,
			String date, String voucherNo, String employee) {
		boolean isSuccess = false;

		double depAmount = Double.parseDouble(amount);
		String gdate = DateUtils.convertJalaliToGregory(date);
		int empID = EmployeeDAO.getEmployeeID(employee);
		int fromBankID = AccountsDAO.getBankID(fromBank.split("-")[1].trim());
		int bankID = AccountsDAO.getBankID(tobank.split("-")[1].trim());

		query = "insert into bankdeposit (AMOUNT,VOUCHERNO,TRANSACTIONDATE, received_RECEIVE_ID,banks_bank_id,employee_employee_id, banks_fromBank) "
				+ "values (?,?,?,?,?,?,?) ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);

		try {
			preStatement.setDouble(1, depAmount);
			preStatement.setString(2, voucherNo);
			preStatement.setString(3, gdate);
			if (receiveID != 0) {
				preStatement.setInt(4, receiveID);
			} else {
				preStatement.setNull(4, Types.INTEGER);
			}
			preStatement.setInt(5, bankID);
			preStatement.setInt(6, empID);
			preStatement.setInt(7, fromBankID);
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

	public static ObservableList<BankTransModel> getTransactionDetail(String selectedBanksName) {
		ObservableList<BankTransModel> list = FXCollections.observableArrayList();
		int bankid = AccountsDAO.getBankID(selectedBanksName.split("-")[1].trim());

		query = "select bd.bankdepoid as id , bd.voucherno as voucherNo, ac.bank_Accnt as acctNumber, ac.bank_name as bankName, "
				+ "ac.currency as currency, bd.transactiondate as trasactionDate,"
				+ " bd.amount as amount from bankdeposit bd "
				+ "inner join accounts ac on ac.account_id = bd.banks_bank_id " + "where bd.banks_fromBank = " + bankid 
				+ " order by bankdepoid asc";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String voucherNo = resultSet.getString("voucherNo");
				String currency =resultSet.getString("currency");
				
				String accountNo = resultSet.getString("acctNumber"); 
				String bankName = resultSet.getString("bankName");
				String entryDate = DateUtils.convertGregoryToJalali(resultSet.getString("trasactionDate"));
				double amount =resultSet.getDouble("amount");  
				BankTransModel model = new BankTransModel(id, voucherNo, accountNo, bankName, entryDate, currency,
						amount);
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
