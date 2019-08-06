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
				String openingDate = resultSet.getString("accnt_crt_date");
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
	

}
