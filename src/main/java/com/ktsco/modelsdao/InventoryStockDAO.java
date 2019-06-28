package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.DetailReportModel;
import com.ktsco.models.factory.InvStockDetailModel;
import com.ktsco.models.factory.InvStockModel;
import com.ktsco.models.factory.InventoryImportModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryStockDAO {
	private static final Logger log = LoggerFactory.getLogger(InventoryStockDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStmt;
	private static ObservableList<InvStockDetailModel> invStockDetailList;
	private static ObservableList<InvStockModel> rawMaterialList;
	private static ObservableList<InventoryImportModel> importList;
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
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}

		return rawMaterialList;
	}

	public static ObservableList<InventoryImportModel> getSearchRecords(String importID, String startString,
			String endDate, String employeeID) {
		importList = FXCollections.observableArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append("select imp.import_id, imp.import_date, imp.employee_id, emp.fullname from invImport imp ");
		sb.append("inner join employee emp on imp.employee_id = emp.employee_id ");
		sb.append("where imp.import_id like ? AND (imp.import_date between ? AND ?) AND emp.employee_id like ?");
		query = String.valueOf(sb);
		InventoryImportModel importModel;
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, "%" + importID + "%");
			preStmt.setString(2, startString);
			preStmt.setString(3, endDate);
			preStmt.setString(4, "%" + employeeID + "%");
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String date = DateUtils.convertGregoryToJalali(resultSet.getString(2));
				String responsible = resultSet.getString(4);
				importModel = new InventoryImportModel(id, date, responsible);
				importList.add(importModel);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return importList;
	}

	public static ObservableList<InvStockModel> getImportDetailList(int importID) {
		rawMaterialList = FXCollections.observableArrayList();
		query = "select detail.detail_id , inv.inv_name, inv.inv_um, detail.import_qty "
				+ "from invImportDetail detail inner join inventory inv "
				+ "on detail.inv_item = inv.inv_id where detail.invImportID = ? " + "order by detail.detail_id ASC ";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, importID);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				int detailID = resultSet.getInt(1);
				String invName = resultSet.getString(2);
				String invUm = resultSet.getString(3);
				String importQty = resultSet.getString(4);
				rawModel = new InvStockModel(detailID, importID, invName, invUm, importQty);
				rawMaterialList.add(rawModel);

			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return rawMaterialList;
	}

	public static void deleteImport(int importID) {
		query = "delete from invImport where import_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, importID);
			preStmt.execute();
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
	}

	public static boolean updateInventoryImportQty(double newImportQty, int detailID) {
		boolean success = false;
		query = "update invImportDetail set import_qty = ? where detail_id = ?";

		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setDouble(1, newImportQty);
			preStmt.setInt(2, detailID);
			preStmt.executeUpdate();
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

	public static ObservableList<InvStockModel> retrieveRawMaterialStock() {
		ObservableList<InvStockModel> list = FXCollections.observableArrayList();
		query = "select inv.inv_id, inv.inv_name, inv.inv_um, rmi.totalImport, tpr.totalUsage " + "from inventory inv "
				+ "left outer join rawmaterialimport rmi on inv.inv_id = rmi.inv_id "
				+ "left outer join tatalproductionraw tpr on inv.inv_id = tpr.inv_id "
				+ "where rmi.totalImport is not null";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				String invName = resultSet.getString("inv_name");
				String invUnit = resultSet.getString("inv_um");
				double totalImport = Commons.setDoubleFormat(resultSet.getDouble("totalImport"));
				double totalUsage = Commons.setDoubleFormat(resultSet.getDouble("totalUsage"));
				double stockQuantity = totalImport - totalUsage; 
				rawModel = new InvStockModel(invName, invUnit, String.valueOf(Commons.setDoubleFormat(stockQuantity)));
				list.add(rawModel);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
		}

		return list;

	}

	public static ObservableList<DetailReportModel> getDetailItemRecord(String lookupValue) {
		ObservableList<DetailReportModel> list = FXCollections.observableArrayList();
		query = "SELECT INV.INV_ID,INVD.DETAIL_ID, INV.INV_NAME, INV.INV_UM, INVI.IMPORT_DATE, INVD.IMPORT_QTY FROM INVENTORY INV " + 
				"INNER JOIN INVIMPORTDETAIL INVD ON INV.INV_ID = INVD.INV_ITEM " + 
				"INNER JOIN invImport INVI ON INVD.INVIMPORTID = INVI.IMPORT_ID " + 
				"WHERE INV.INV_ID LIKE ? AND INVD.IMPORT_QTY != 0";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, "%"+lookupValue+"%");
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				int idnumber = resultSet.getInt(2);
				String item = resultSet.getString(3);
				String unit = resultSet.getString(4);
				String date = DateUtils.convertGregoryToJalali(resultSet.getString(5));
				String quantity = String.valueOf(Commons.setDoubleFormat(resultSet.getDouble(6)));
				DetailReportModel detailModel = new DetailReportModel(idnumber, item, unit, date, quantity);
				list.add(detailModel);
			}
		}catch (SQLException e) {
			Commons.dbExcutionLog(query, e.getMessage());
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch (SQLException e) {
				Commons.dbClosingLog(e.getMessage());
			}
		}
		
		return list;
	}

}
