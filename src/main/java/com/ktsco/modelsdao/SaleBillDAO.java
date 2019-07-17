package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

public class SaleBillDAO {

	private static Logger log = LoggerFactory.getLogger(SaleBillDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static int getLastBillID() {
		int billID = 0;
		query = "SELECT MAX(BILL_ID) FROM SALEBILLS";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				billID = resultSet.getInt(1);
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

		return billID + 1;
	}

	public static int getBillDetailID() {
		int id = 0;
		query = "SELECT MAX(ID) FROM SALEDETAIL";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				id = resultSet.getInt(1);
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

		return id + 1;
	}

	public static boolean insertIntoSaleBill(int bill_id, int customerId, String billDate, int payTerm,
			String currencyType, String billMemo) {
		boolean isSuccess = false;
		query = "INSERT INTO SALEBILLS (BILL_ID, CUSTOMER_ID, BILLDATE, DUEDATE, CURRENCYTYPE, BILLMEMO) VALUES "
				+ "(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE CUSTOMER_ID = ? , BILLDATE = ? , DUEDATE = ? , CURRENCYTYPE = ? , BILLMEMO = ?";
		String convBillDate = "";
		String calDueDate = "";
		if (DateUtils.checkEntryDateFormat(billDate)) {
			LocalDate convertedDate = LocalDate.parse(DateUtils.convertJalaliToGregory(billDate),
					DateTimeFormatter.ISO_DATE);
			convBillDate = String.valueOf(convertedDate);
			calDueDate = Commons.calculateDueDate(convBillDate, payTerm);

		} else {
			log.error("Wrong Date Entry " + billDate);
			throw new RuntimeException("Wrong Date Entry");
		}

		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, bill_id);
			preStatement.setInt(2, customerId);
			preStatement.setString(3, convBillDate);
			preStatement.setString(4, calDueDate);
			preStatement.setString(5, currencyType);
			preStatement.setString(6, billMemo);
			preStatement.setInt(7, customerId);
			preStatement.setString(8, convBillDate);
			preStatement.setString(9, calDueDate);
			preStatement.setString(10, currencyType);
			preStatement.setString(11, billMemo);
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

}
