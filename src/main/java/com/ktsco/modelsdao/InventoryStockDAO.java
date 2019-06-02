package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.InvStockDetailModel;
import com.ktsco.models.InvStockModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryStockDAO {
	private static final Logger log = LoggerFactory.getLogger(InventoryStockDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStmt;
	private static ObservableList<InvStockDetailModel> invStockDetailList;
	private static ObservableList<InvStockModel> rawMaterialList;
	private static InvStockDetailModel invStockModel;
	private static InvStockModel rawModel; 

	public static ObservableList<InvStockDetailModel> populateInvList() {
		invStockDetailList = FXCollections.observableArrayList();
		query = "select inv.inv_name,inv.inv_um, cat.category from inventory inv inner join category cat on inv.category_id = cat.category_id where cat.category = 'مواد اولیه'";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				String invName = resultSet.getString(1);
				String um = resultSet.getString(2);
				String defualtImporQty = "0";
				invStockModel = new InvStockDetailModel(invName, um, defualtImporQty);
				invStockDetailList.add(invStockModel);
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
		return invStockDetailList;
	}

	public static int generateImportID() {
		int importid = 0;
		query = "select max(import_id) from invImport";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				importid = resultSet.getInt(1);
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
		return importid + 1;
	}

	public static boolean addImportList(int importId, String importdate, int empID) {
		boolean success = false;
		query = "Insert into invImport (import_id, import_date, employee_id) values (?,?,?)";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, importId);
			preStmt.setString(2, importdate);
			preStmt.setInt(3, empID);
			preStmt.execute();
			success = true;
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return success;
	}

	public static boolean addImportDetailList(int inv_id, double importQty, int import_id) {
		boolean success = false;

		query = "insert into invImportDetail (inv_item , import_qty , invImportID) values (? , ? , ?)";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, inv_id);
			preStmt.setDouble(2, importQty);
			preStmt.setInt(3, import_id);
			preStmt.execute();
			success = true;
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return success;
	}

	public static ObservableList<InvStockModel> getRawMaterialInventory() {
		rawMaterialList = FXCollections.observableArrayList();

		StringBuilder sb = new StringBuilder();
		sb.append("select inv.inv_id, inv.inv_um , inv.inv_name , cat.category , ");
		sb.append("sum(detail.import_qty) as invTotalQty from inventory inv inner join category cat on ");
		sb.append("inv.category_id = cat.category_id left join invImportDetail detail on ");
		sb.append("inv.inv_id = detail.inv_item where cat.category = 'مواد اولیه' group by inv.inv_id");

		query = String.valueOf(sb);

		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				String invName = resultSet.getString("inv_name");
				String invUm = resultSet.getString("inv_um");
				String invTotalQty = String.valueOf(resultSet.getDouble("invTotalQty"));
				rawModel = new InvStockModel(invName, invUm, invTotalQty);
				rawMaterialList.add(rawModel);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				resultSet.close();
			}catch(SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return rawMaterialList;
	}
}
