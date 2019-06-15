package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.EmpListModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeDAO {
	private static final Logger log = LoggerFactory.getLogger(EmployeDAO.class);
	private static ResultSet resultSet;
	private static String query;
	private static PreparedStatement preStmt;
	private static ObservableList<EmpListModel> empList;
	private static EmpListModel empModel;

	private static String dbClosingLog(String message) {
		String logMessage = "Error at Closing ResultSet or PreparedStatement with error message" + message;
		return logMessage;
	}

	private static String dbExceptionLog(String query, String message) {
		String logMessage = "Error at Executing query " + query + " With Error Message " + message;
		return logMessage;
	}
	
	

	public static int getMaxEmpID() {
		int employeeID = 0;
		query = "Select Max(employee_id) from employee";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				employeeID = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}

		return employeeID + 1;
	}

	public static ObservableList<EmpListModel> retreiveAllEmpRecord() {
		empList = FXCollections.observableArrayList();
		query = "Select * from employee";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				int empId = resultSet.getInt("employee_id");
				String empFullName = resultSet.getString("fullname");
				String empPosition = resultSet.getString("position");
				empModel = new EmpListModel(empId, empFullName, empPosition);
				empList.add(empModel);
			}
		} catch (SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}
		return empList;
	}

	public static boolean addEmployee(int empId, String empFullName, String empPosition) {
		boolean success = false;
		StringBuffer sb = new StringBuffer();
		sb.append("Insert into employee (employee_id, fullname, position) values ");
		sb.append("(? , ? , ?) on duplicate key update fullname = ? , position = ?");
		query = String.valueOf(sb);

		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, empId);
			preStmt.setString(2, empFullName);
			preStmt.setString(3, empPosition);
			preStmt.setString(4, empFullName);
			preStmt.setString(5, empPosition);
			preStmt.execute();
			success = true;
		} catch (SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			success = false;
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}

		return success;
	}
	
	public static boolean checkEmployeeNameExist(String lookupValue) {
		boolean exist = false; 
		query = "Select trim(employee_id) from employee where fullname = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, lookupValue.trim());
			resultSet = preStmt.executeQuery();
			if (resultSet.isBeforeFirst()) {
				exist = true;
				while(resultSet.next()) {
					int empID = resultSet.getInt(1);
					AlertsUtils.repeatItemAlerts(String.valueOf(empID));
				}
			}else {
				exist = false; 
			}
		}catch(SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch(SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}
		return exist; 
	}
	
	public static boolean deletEmployee(int empID) {
		boolean success = false; 
		query = "delete from employee where employee_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, empID);
			preStmt.executeUpdate();
			success = true; 
		}catch (SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			success = false;
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
			}catch(SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}
		return success; 
	}
	
	public static ObservableList<EmpListModel> searchForEmployee(String lookupValue){
		empList = FXCollections.observableArrayList();
		String query = "Select * from employee where fullname like ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, "%" + lookupValue.trim() + "%");
			resultSet = preStmt.executeQuery();
			while(resultSet.next()) {
				int empID = resultSet.getInt(1);
				String empName = resultSet.getString(2);
				String position = resultSet.getString(3);
				empModel = new EmpListModel(empID, empName, position);
				empList.add(empModel);
			}
		}catch(SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch(SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}
		return empList;
	}
	
	public static List<String> getEmployeeName(){
		List<String> empNameList = new ArrayList<String>(); 
		String query = "Select fullname from employee";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while(resultSet.next()) {
				 String empName = resultSet.getString(1);
				 empNameList.add(empName);
			}
		}catch(SQLException e) {
			log.error(dbExceptionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				resultSet.close();
			}catch(SQLException e) {
				log.error(dbClosingLog(e.getMessage()));
			}
		}
		
		return empNameList;
	}
	
	public static int getEmployeeID (String lookupValue) {
		int empid = 0; 
		query = "select employee_id from employee where fullname = ?"; 
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, lookupValue);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				empid = resultSet.getInt(1);
			}
		}catch(SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch(SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return empid;
	}

}
