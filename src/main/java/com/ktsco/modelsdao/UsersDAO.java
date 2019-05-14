package com.ktsco.modelsdao;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.UsersModels;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Constants;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class UsersDAO {
	private static final Logger log = LoggerFactory.getLogger(UsersDAO.class);

	private static UsersModels users;

	/**
	 * This method will retrieve all data from database and <br>
	 * and return it as Observable List of UsersModel
	 * 
	 * @return ObservableList<UsersModels>
	 */
	public static ObservableList<UsersModels> selectAllRows() {

		ObservableList<UsersModels> list = FXCollections.observableArrayList();
		String query = "select * from users";
		log.info("Executing SQl query {}" + query);
		ResultSet result = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (result.next()) {
				int userID = result.getInt("user_ID");
				String username = result.getString("username");
				String password = result.getString("password");
				String accesskey = result.getString("access");
				String accessType = Constants.accessValue.get(accesskey);
				users = new UsersModels(userID, username, password, accessType);
				list.add(users);
			}

		} catch (SQLException e) {
			log.error("Error while looping on results of Query " + e.getMessage());
			log.info("list can be null at this point" + list);
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}

		return list;
	}

	/**
	 * This method is used for executing query to <br>
	 * to Insert users info to Users table of database
	 * 
	 * @param username
	 * @param password
	 * @param accessType
	 */
	public static void createNewUser(String username, String password, String accessType) {
		String query = "Insert into users (username, password, access) values (?,?,?)";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		log.info("Initializing PreparedStatment for {}" + query);

		try {
			log.info("Set the parameters for {}" + username);
			preStmt.setString(1, username);
			log.info("Set the parameters for {}" + password);
			preStmt.setString(2, password);
			log.info("Set the parameters for {}" + accessType);
			preStmt.setString(3, accessType);
			log.info("Execute Query");
			preStmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());

		}

	}

	/**
	 * This method with Populate Usersname in ComboBox <br>
	 * drop down
	 * 
	 * @param comboUsername
	 */

	public static void populateUsersList(ComboBox<String> comboUsername) {
		String query = "Select username from users order by user_ID ASC";
		ObservableList<String> comboList = FXCollections.observableArrayList();
		log.info("Executing query " + query);
		ResultSet result = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (result.next()) {
				log.info("adding value of result to ComboList " + comboList);
				String value = result.getString(1);
				comboList.add(value);
			}
		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}

		comboUsername.setItems(comboList);
	}

	/**
	 * this method will return the userid of selected value in combobox
	 * 
	 * @param comboUser
	 * @return
	 */
	public static Integer getUserIDFromComboValue(ComboBox<String> comboUser) {
		int userID = 0;

		String selectedUser = comboUser.getValue();

		String query = "Select user_ID from users where username = ?";
		try {
			PreparedStatement prestmt = DatabaseUtils.dbPreparedStatment(query);
			prestmt.setString(1, selectedUser);
			ResultSet result = prestmt.executeQuery();
			while (result.next()) {
				userID = result.getInt(1);
			}
		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}

		return userID;
	}

	/**
	 * This method will result result set of edit panel
	 * 
	 * @param userid
	 * @return
	 */
	public static ResultSet getPasswordAndAccessByUserID(int userid) {
		ResultSet result = null;
		String query = "Select password, access from users where user_ID = ?";
		try {
			PreparedStatement prestmt = DatabaseUtils.dbPreparedStatment(query);
			prestmt.setInt(1, userid);
			result = prestmt.executeQuery();

		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}
		return result;

	}

	/**
	 * This method is used for executing query to <br>
	 * to Updates users info to Users table of database
	 * 
	 * @param userid
	 * @param password
	 * @param access
	 */
	public static void updatePasswordAccess(int userid, String password, String access) {
		String query = "UPDATE users SET password = ? , access = ? WHERE user_ID = ?";

		PreparedStatement prestmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			prestmt.setString(1, password);
			prestmt.setString(2, access);
			prestmt.setInt(3, userid);
			prestmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}
	}
	
	/**
	 * This method is used for executing query to <br>
	 * to Delete users info to Users table of database
	 * 
	 * @param userid
	 */

	public static void deleteSelectedUser(int userid) {
		String query = "Delete from users where user_ID = ?";
		PreparedStatement prestmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			prestmt.setInt(1, userid);
			prestmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Execution failed with error massage {}" + e.getMessage());
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
		}
	}
	
}
