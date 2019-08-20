package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.AccountsModel;
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
	
	public static ObservableList<AccountsModel> reterieveAllRecords(){
		ObservableList<AccountsModel> list = FXCollections.observableArrayList();
		query = "Select * from accounts";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("account_id");
				String bankAccount = resultSet.getString("bank_Accnt");
				String bankName = resultSet.getString("bank_name");
				String openingDate = DateUtils.convertGregoryToJalali(resultSet.getString("accnt_crt_date"));
				double openingBalance = resultSet.getDouble("opening_balance");
				AccountsModel model = new AccountsModel(code, bankAccount, bankName, openingDate, openingBalance);
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
	
	public static boolean insertOrUpdateIntoAccountTableAPI(int code, String bankAccount, String bankName, String openingDate, double openingBalance) {
		boolean isSuccess = false; 
		
		int id = (code != 0) ? code : 0; 
		String gregoryDate = DateUtils.convertJalaliToGregory(openingDate);
		
		query = "INSERT INTO ACCOUNTS (account_id, bank_Accnt, bank_name, accnt_crt_date, opening_balance) "
				+ "values (?,?,?,?,?) on duplicate key update bank_Accnt = ? , bank_name = ? , accnt_crt_date = ?, "
				+ "opening_balance = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		
		try {
			preStatement.setInt(1, id);
			preStatement.setString(2, bankAccount);
			preStatement.setString(3, bankName);
			preStatement.setString(4, gregoryDate);
			preStatement.setDouble(5, openingBalance);
			preStatement.setString(6, bankAccount);
			preStatement.setString(7, bankName);
			preStatement.setString(8, gregoryDate);
			preStatement.setDouble(9, openingBalance);
			
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
	
	public static int generateAccountID() {
		int code = 0; 
		query = "select max(account_id) from accounts";
		
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				code = resultSet.getInt(1);
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
		
		return code +1; 
	}
	
	public static boolean deleteRecord(int code) {
		boolean isSuccess = false; 
		query = "delete from accounts where account_id = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, code);
			preStatement.executeUpdate();
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
	
	

}
