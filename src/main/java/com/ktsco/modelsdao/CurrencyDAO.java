package com.ktsco.modelsdao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.CurrencyModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrencyDAO {
	

	private static Logger log = LoggerFactory.getLogger(CurrencyDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<CurrencyModel> retrieveCurrencyRecords(String date) {
		ObservableList<CurrencyModel> list = FXCollections.observableArrayList();

		query = "Select * from currencies";
		try {
			if ("".equalsIgnoreCase(date)) {
				resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			} else {
				if (DateUtils.checkEntryDateFormat(date)) {
					query += " where entryDate = ?";
					String gregoryDate = DateUtils.convertJalaliToGregory(date);
					preStatement = DatabaseUtils.dbPreparedStatment(query);
					preStatement.setString(1, gregoryDate);
					resultSet = preStatement.executeQuery();
				} else
					throw new RuntimeException("Wrong Date Entry");
			}
			while (resultSet.next()) {
				int idNumber = resultSet.getInt("id");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				String jalaliDate = DateUtils.convertGregoryToJalali(resultSet.getString("entryDate"));
				@SuppressWarnings("deprecation")
				BigDecimal bg = resultSet.getBigDecimal("rate", 9);
				String rate = bg.toPlainString();
				CurrencyModel model = new CurrencyModel(idNumber, currency, jalaliDate, rate);
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

	public static boolean insertCurrencyRecords(String inputCurrency, String inputDate, String inputRate) {
		boolean isSuccess = false;

		query = "INSERT INTO CURRENCIES (CURRENCY, ENTRYDATE, RATE) VALUES (?,?,TRUNCATE(?,9))";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		String currency = Commons.getCurrencyKey(inputCurrency);
		String gregoryDate = "";
		double rate = Double.parseDouble(inputRate);

		if (DateUtils.checkEntryDateFormat(inputDate)) {
			gregoryDate = DateUtils.convertJalaliToGregory(inputDate);
		} else
			throw new RuntimeException("Wrong Date Entry");

		try {
			preStatement.setString(1, currency);
			preStatement.setString(2, gregoryDate);
			preStatement.setDouble(3, rate);
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

	public static boolean checkExistingDate(String inputDate) {
		boolean isExist = false;
		query = "Select * from currencies where entryDate = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, DateUtils.convertJalaliToGregory(inputDate));
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				isExist = true;
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

		return isExist;
	}

	public static boolean deleteCurrencyRecord(int id) {
		boolean isSuccess = false;
		query = "delete from currencies where id = ?";
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
	
	public static boolean updateCurrencyRecord(int id , String inputRate) {
		boolean isSuccess = false; 
		double rate = Double.parseDouble(inputRate);
		query = "Update currencies set rate = TRUNCATE(?,9) where id = ?"; 
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setDouble(1, rate);
			preStatement.setInt(2, id);
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
	
	public static String getCurrencyRate (String currency, String date) {
		String value = ""; 
		query = "SELECT RATE FROM CURRENCIES WHERE CURRENCY LIKE ? AND ENTRYDATE = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%"+currency+"%");
			preStatement.setString(2, date);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				@SuppressWarnings("deprecation")
				BigDecimal bg = resultSet.getBigDecimal(1, 9);
				value = bg.toPlainString();
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
		
		return value; 
	}
	public static String getCurrencyRate (String date) {
		String value = ""; 
		query = "SELECT RATE FROM CURRENCIES WHERE ENTRYDATE = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			
			preStatement.setString(1, date);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				@SuppressWarnings("deprecation")
				BigDecimal bg = resultSet.getBigDecimal(1, 9);
				value = bg.toPlainString();
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
		
		return value; 
	}
	
	

}
