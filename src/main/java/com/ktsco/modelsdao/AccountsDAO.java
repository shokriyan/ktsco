package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.AccountsModel;
import com.ktsco.models.csr.BankBalanceModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccountsDAO {

	private static Logger log = LoggerFactory.getLogger(AccountsDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<AccountsModel> reterieveAllRecords() {
		ObservableList<AccountsModel> list = FXCollections.observableArrayList();
		query = "Select * from accounts";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			
			while (resultSet.next()) {
				int code = resultSet.getInt("account_id");
				String bankAccount = resultSet.getString("bank_Accnt");
				String bankName = resultSet.getString("bank_name");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				String openingDate = DateUtils.convertGregoryToJalali(resultSet.getString("accnt_crt_date"));
				double openingBalance = resultSet.getDouble("opening_balance");

				AccountsModel model = new AccountsModel(code, bankAccount, bankName, currency, openingDate,
						openingBalance);
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

	public static boolean insertOrUpdateIntoAccountTableAPI(int code, String bankAccount, String bankName,
			String currency, String openingDate, double openingBalance) {
		boolean isSuccess = false;

		int id = (code != 0) ? code : 0;
		String gregoryDate = DateUtils.convertJalaliToGregory(openingDate);
		String currencyKey = Commons.getCurrencyKey(currency);
		query = "INSERT INTO ACCOUNTS (account_id, bank_Accnt, bank_name, accnt_crt_date, opening_balance, currency) "
				+ "values (?,?,?,?,?,?) on duplicate key update bank_Accnt = ? , bank_name = ? , accnt_crt_date = ?, "
				+ "opening_balance = ? , currency = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);

		try {
			preStatement.setInt(1, id);
			preStatement.setString(2, bankAccount);
			preStatement.setString(3, bankName);
			preStatement.setString(4, gregoryDate);
			preStatement.setDouble(5, openingBalance);
			preStatement.setString(6, currencyKey);
			preStatement.setString(7, bankAccount);
			preStatement.setString(8, bankName);
			preStatement.setString(9, gregoryDate);
			preStatement.setDouble(10, openingBalance);
			preStatement.setString(11, currencyKey);

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

	public static int generateAccountID() {
		int code = 0;
		query = "select max(account_id) from accounts";

		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				code = resultSet.getInt(1);
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

		return code + 1;
	}

	public static boolean deleteRecord(int code) {
		boolean isSuccess = false;
		query = "delete from accounts where account_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, code);
			preStatement.executeUpdate();
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

	public static List<String> getBankAccounts(String currency) {
		List<String> accountList = new ArrayList<String>();
		String currencyKey = Commons.getCurrencyKey(currency);
		 query = "Select bank_name, bank_accnt, currency from accounts where currency = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, currencyKey);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				String accounts = resultSet.getString(1) + " - " + resultSet.getString(2) + " - "
						+ Commons.getCurrencyValue(resultSet.getString(3));
				accountList.add(accounts);
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

		return accountList;
	}

	public static String getBankAccounts(int accountID) {
		String bank = ""; 
		query = "Select bank_name, bank_accnt, currency from accounts where account_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, accountID);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				bank = resultSet.getString(1) + " - " + resultSet.getString(2) + " - "
						+ Commons.getCurrencyValue(resultSet.getString(3));
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

		return bank;
	}

	public static int getBankID(String accountNo) {
		int id = 0;
		query = "select account_id from accounts where bank_accnt = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, accountNo);
			resultSet = preStatement.executeQuery();
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
		return id;
	}

	public static ObservableList<BankBalanceModel> getBankBalance() {
		ObservableList<BankBalanceModel> list = FXCollections.observableArrayList();
		query = "select a.account_id,a.bank_Accnt,a.bank_name,a.currency,a.opening_balance, brtv.accountBalance from accounts a\n"
				+ "left outer join bankReceiveTotalView brtv on a.account_id = brtv.banks_bank_id";

		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int accountID = resultSet.getInt("account_id");
				String account = resultSet.getString("bank_accnt");
				String bankName = resultSet.getString("bank_name");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				double openingAmount = resultSet.getDouble("opening_balance");
				double depositedAmount = resultSet.getDouble("accountBalance");
				double accountBalance = openingAmount + depositedAmount;

				BankBalanceModel model = new BankBalanceModel(accountID, account, bankName, currency, accountBalance);
				list.add(model);

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
	

	public static List<String> getBankAccounts() {
		List<String> accountList = new ArrayList<String>();
		 query = "Select bank_name, bank_accnt, currency from accounts";
		 resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				String accounts = resultSet.getString(1) + " - " + resultSet.getString(2) + " - "
						+ Commons.getCurrencyValue(resultSet.getString(3));
				accountList.add(accounts);
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

		return accountList;
		
	}
	

}
