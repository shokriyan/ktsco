package com.ktsco.modelsdao;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.admin.UsersModels;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsersDAO {
	private static final Logger log = LoggerFactory.getLogger(UsersDAO.class);

	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static boolean insertUserDate(int userID, String fullname, String username, String password, int adminAccess,
			int csrAccess, int factoryAccess, int mgmntAccess) {
		boolean isSuccess = false;
		// user_ID,fullname,username,password,admin_access,csr_access,factory_access,mgmt_access
		query = "insert into users (user_ID, fullname,username,password,admin_access,csr_access,factory_access,mgmt_access) "
				+ "values (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE fullname = ?, password = ? , admin_access = ? "
				+ ", csr_access = ? , factory_access = ? , mgmt_access = ? ";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, userID);
			preStatement.setString(2, fullname);
			preStatement.setString(3, username);
			preStatement.setString(4, password);
			preStatement.setInt(5, adminAccess);
			preStatement.setInt(6, csrAccess);
			preStatement.setInt(7, factoryAccess);
			preStatement.setInt(8, mgmntAccess);
			preStatement.setString(9, fullname);
			preStatement.setString(10, password);
			preStatement.setInt(11, adminAccess);
			preStatement.setInt(12, csrAccess);
			preStatement.setInt(13, factoryAccess);
			preStatement.setInt(14, mgmntAccess);
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

	public static ObservableList<UsersModels> getUsersList() {
		ObservableList<UsersModels> list = FXCollections.observableArrayList();
		// user_ID,fullname,username,password,admin_access,csr_access,factory_access,mgmt_access
		query = "Select * from users";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);

		List<Map<String, Object>> dataMap = DatabaseUtils.convertResultSetToMap(resultSet);
		for (Map<String, Object> map : dataMap) {
			int code = Integer.parseInt(map.get("user_ID").toString());
			String fullname = map.get("fullname").toString();
			String username = map.get("username").toString();
			String password = map.get("password").toString();
			String admin = Commons.accessSymployes(Integer.parseInt(map.get("admin_access").toString()));
			String csr = Commons.accessSymployes(Integer.parseInt(map.get("csr_access").toString()));
			String mgmnt = Commons.accessSymployes(Integer.parseInt(map.get("mgmt_access").toString()));
			String factory = Commons.accessSymployes(Integer.parseInt(map.get("factory_access").toString()));

			UsersModels model = new UsersModels(code, fullname, username, password, admin, csr, factory, mgmnt);
			list.add(model);
		}
		return list;
	}

	public static boolean deleteUser(int userID) {
		boolean isSuccess = false;
		query = "delete from users where user_ID = ?";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, userID);
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

	public static Map<String, Object> getUserInfoByUsername(String username) {
		Map<String, Object> userDetail = new HashMap<String, Object>();
		query = "select * from users where username = '" + username + "'";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			if (resultSet.isBeforeFirst())
				userDetail = DatabaseUtils.convertResultSetToMap(resultSet).get(0);
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
		return userDetail;
	}

}
