package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.CustomerModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDAO {

	private static Logger log = LoggerFactory.getLogger(CustomerDAO.class);
	private static String query;
	private static PreparedStatement preStatement;
	private static ResultSet resultSet;

	public static int stablishCustomerCode() {
		int code = 0;
		query = "Select max(customer_id) from customers";

		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				code = resultSet.getInt(1);
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
		return code + 1;
	}

	public static ObservableList<CustomerModel> getAllCustomerRecord() {
		ObservableList<CustomerModel> list = FXCollections.observableArrayList();
		query = "Select * from customers order by customer_id";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("customer_id");
				String company = resultSet.getString("company");
				String poc = resultSet.getString("poc");
				String phone = resultSet.getString("phone");
				String address = resultSet.getString("address");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				CustomerModel model = new CustomerModel(code, company, poc, phone, address, currency);
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

	
	public static boolean insertCustomerRecord(int code, String company, String poc, String phone, String address,
			String currency) {
		boolean isSuccess = false;
		query = "Insert into customers (customer_id, company, poc, phone, address, currency) "
				+ "Values(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE "
				+ "company =? , poc = ? , phone = ? , address = ? , currency = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, code);
			preStatement.setString(2, company);
			preStatement.setString(3, poc);
			preStatement.setString(4, phone);
			preStatement.setString(5, address);
			preStatement.setString(6, currency);
			preStatement.setString(7, company);
			preStatement.setString(8, poc);
			preStatement.setString(9, phone);
			preStatement.setString(10, address);
			preStatement.setString(11, currency);

			preStatement.executeUpdate();
			isSuccess = true;

		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return isSuccess;
	}

	public static boolean checkForDuplicateCustomer(String company, String currency) {
		boolean isExist = false;
		query = "select customer_id, trim(company) as company, trim(currency) as currency from customers "
				+ "where company = trim(?) AND currency = trim(?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, company);
			preStatement.setString(2, currency);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				isExist = true;
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return isExist;
	}

	public static boolean checkForDuplicateCustomer(String currency) {
		boolean isExist = false;
		query = "select customer_id, trim(company) as company, trim(currency) as currency from customers "
				+ "where currency = trim(?)";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, currency);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				isExist = true;
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return isExist;
	}

	public static boolean deleteCustomerRecord(int code) {
		boolean isSuccess = false;
		query = "delete from customers where customer_id = ?";
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
				preStatement.close();

			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return isSuccess;
	}

	public static ObservableList<CustomerModel> searchCustomerRecord(String company, String poc, String phone,
			String address, String currency) {
		ObservableList<CustomerModel> list = FXCollections.observableArrayList();
		query = "Select * from customers where company like ? and poc like ? and phone like ? and address like ? and currency like ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setString(1, "%" + company + "%");
			preStatement.setString(2, "%" + poc + "%");
			preStatement.setString(3, "%" + phone + "%");
			preStatement.setString(4, "%" + address + "%");
			preStatement.setString(5, "%" + Commons.getCurrencyKey(currency) + "%");
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				int getCode = resultSet.getInt("customer_id");
				String getCompany = resultSet.getString("company");
				String getPOC = resultSet.getString("poc");
				String getPhone = resultSet.getString("phone");
				String getAddress = resultSet.getString("address");
				String getCurrency = Commons.getCurrencyValue(resultSet.getString("currency"));
				CustomerModel model = new CustomerModel(getCode, getCompany, getPOC, getPhone, getAddress, getCurrency);
				list.add(model);
				
			}
		}catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStatement.close();
				resultSet.close();
			}catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return list;

	}
	
	public static List<String> getCustomersList(){
		List<String> list = new ArrayList<String>();
		query = "SELECT CUSTOMER_ID, COMPANY, CURRENCY FROM CUSTOMERS";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("customer_id");
				String company = resultSet.getString("company");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				
				//Customer name should be : id + company + currency
				String customerNameForCombo = String.valueOf(code) + " - " + company +" - " + currency;
				list.add(customerNameForCombo);
			}
		}catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				resultSet.close();
			}catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		return list;
	}
	
	public static String getCurrency(String lookupValue){
		String currency = new String();
		query = "SELECT CURRENCY FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			 int code = Integer.parseInt(lookupValue.split("-")[0].trim());
			preStatement.setInt(1, code);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				
				currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				
				
			}
		}catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStatement.close();
				resultSet.close();
			}catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		return currency;
	}

}
