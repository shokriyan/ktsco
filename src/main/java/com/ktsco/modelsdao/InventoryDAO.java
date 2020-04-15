package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.InventoryModel;
import com.ktsco.models.mgmt.ProductCostModel;
import com.ktsco.models.mgmt.RawMtrlRptModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryDAO {

	private static final Logger log = LoggerFactory.getLogger(InventoryDAO.class);
	private static InventoryModel invModel;

	/**
	 * Retrieve all records from database for inventroy
	 * 
	 * @return Observable list of Invetory Model
	 */

	public static ObservableList<InventoryModel> getAllInvetoryRecord() {
		ObservableList<InventoryModel> list = FXCollections.observableArrayList();

		String query = "select inv.inv_id as inv_id, inv.inv_name as inv_name, inv.inv_um as inv_um , cat.category as category from inventory inv \n"
				+ "left outer join category cat \n" + "on inv.category_id = cat.category_id \n"
				+ "order by inv_id asc;";

		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					int invId = resultSet.getInt("inv_id");
					String invItem = resultSet.getString("inv_name");
					String invCategory = resultSet.getString("category");
					String invUM = resultSet.getString("inv_um");

					invModel = new InventoryModel(invId, invItem, invCategory, invUM);
					list.add(invModel);
				}
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + "with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		return list;
	}

	/**
	 * Insert inventory value to database
	 * 
	 * @param items
	 * @param catId
	 * @param um
	 * @return
	 */

	public static boolean insertInvetoryItems(String items, int catId, String um) {
		boolean success = false;
		StringBuffer query = new StringBuffer();
		query.append("Insert Into inventory (category_id, inv_name, inv_um) ");
		query.append("values (?,?,?)");
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(String.valueOf(query));
		try {
			preStmt.setInt(1, catId);
			preStmt.setString(2, items);
			preStmt.setString(3, um);
			preStmt.executeUpdate();
			success = true;
		} catch (SQLException e) {

			success = false;
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info(e.getMessage());
			}
		}

		return success;
	}

	/**
	 * Check the Inventory Item is exist in database. no duplicate value allowed
	 * 
	 * @param lookup the value to check in query
	 * @return true is value exist
	 */
	public static boolean checkItemisExist(String lookup) {
		boolean isExist = false;

		String query = "select trim(inv_name) from inventory where inv_name = ?";

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setString(1, lookup.trim());
			resultSet = preStmt.executeQuery();
			if (resultSet.next()) {
				isExist = true;
			} else
				isExist = false;
		} catch (SQLException e) {
			log.error("Error Executing query " + query + "with error massage " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return isExist;
	}

	public static boolean deleteInventoryItem(int invID) {
		boolean success = false;
		String query = "delete from inventory where inv_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, invID);
			preStmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			log.error("Error Executing query " + query + "with error massage " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return success;
	}

	public static boolean updateInventoryItem(int invId, String invItem, int catId, String invUm) {
		boolean success = false;

		String query = "Update inventory set inv_name = ?, category_id = ?, inv_um = ? where inv_id = ?";

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, invItem);
			preStmt.setInt(2, catId);
			preStmt.setString(3, invUm);
			preStmt.setInt(4, invId);

			preStmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			log.error("Error Executing query " + query + "with error massage " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
			success = false;
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return success;
	}

	public static ObservableList<InventoryModel> searchInventroyItems(String invItem, String category, String InvUM) {
		ObservableList<InventoryModel> list = FXCollections.observableArrayList();

		StringBuffer query = new StringBuffer();
		query.append("select inv.inv_id, inv.inv_name as invName, inv.inv_um invUm , ");
		query.append("cat.category invCategory from inventory inv ");
		query.append("left outer join category cat ");
		query.append("on inv.category_id = cat.category_id where ");
		query.append("inv.inv_name like ? ");
		query.append("and inv_um like ? ");
		query.append("and category like ? ");
		query.append("order by inv_id asc");

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(String.valueOf(query));
		ResultSet resultSet = null;

		try {

			preStmt.setString(1, "%" + invItem + "%");

			preStmt.setString(2, "%" + InvUM + "%");

			preStmt.setString(3, "%" + category + "%");
			resultSet = preStmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					int invId = resultSet.getInt("inv_id");
					String item = resultSet.getString("invName");
					String invCategory = resultSet.getString("invCategory");
					String invUM = resultSet.getString("invUm");

					invModel = new InventoryModel(invId, item, invCategory, invUM);
					list.add(invModel);
				}
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + "with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		return list;
	}

	/**
	 * Retrieve All inventroy Items to use in Combo Boxes
	 * 
	 * @return List of String
	 */
	public static List<String> getInvItemsForCombo() {
		List<String> list = new ArrayList<String>();
		String query = "select inv_name , category from inventory inner join category on inventory.category_id = category.category_id where category like 'مواد اولیه' order by inv_id;";
		ResultSet result = DatabaseUtils.dbSelectExuteQuery(query);
		log.info("Exectuing query {}", query);
		try {
			while (result.next()) {
				list.add(result.getString(1));
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + "with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return list;
	}

	/**
	 * Retrieve All inventroy Items to use in Combo Boxes
	 * 
	 * @return List of String
	 */
	public static ObservableList<String> getInventoryObservableList() {
		ObservableList<String> list = FXCollections.observableArrayList();
		String query = "select inv_name , category from inventory inner join category on inventory.category_id = category.category_id order by inv_id;";
		ResultSet result = DatabaseUtils.dbSelectExuteQuery(query);
		log.info("Exectuing query {}", query);
		try {
			while (result.next()) {
				list.add(result.getString(1) + " - " + result.getString(2));
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + "with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return list;
	}

	/**
	 * Retrieve the value of Inventroy Id for Inventroy name
	 * 
	 * @param lookupValue
	 * @return Integer Inv ID
	 */
	public static int getInvId(String lookupValue) {
		int invId = 0;
		String query = "select inv_id from inventory where inv_name = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setString(1, lookupValue);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				invId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + " with error massage " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return invId;
	}

	public static String getInvName(int inv_id) {
		String invName = null;
		String query = "Select inv_name from inventory where inv_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setInt(1, inv_id);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				invName = resultSet.getString("inv_name");
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + " with error massage " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return invName;
	}

	public static String getInventoryUnit(String lookUpValue) {
		int invID = getInvId(lookUpValue);
		String invUnit = null;
		String query = "Select inv_um from inventory where inv_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setInt(1, invID);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				invUnit = resultSet.getString(1);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return invUnit;
	}

	public static String getInvUnitMeasure(String lookUpValue) {
		int invID = getInvId(lookUpValue.split("-")[0].trim());
		String invUnit = null;
		String query = "Select inv_um from inventory where inv_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setInt(1, invID);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				invUnit = resultSet.getString(1);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return invUnit;
	}

	public static ObservableList<ProductCostModel> getProductDetailList(int productCode) {
		ObservableList<ProductCostModel> list = FXCollections.observableArrayList();
		String query = "select (pd.id) as id, (inv.inv_name) as item, (inv.inv_um) as unit, (pd.req_qty) as quantity, "
				+ "(select (select rate from currencies c where c.currency = eb.currency and c.entryDate = eb.expns_date)* ed.unitprice "
				+ "as usdunitprice from expenseDetail ed\n" + "inner join expensebill eb on eb.expns_id = ed.expns_id "
				+ "where inv_id = inv.inv_id\n" + "order by eb.expns_date desc\n" + "LIMIT 1) as unitprice "
				+ " from productDetail pd inner join inventory inv on inv.inv_id = pd.inv_id\n" + "where pd.prod_id =  "
				+ productCode;
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		DatabaseUtils.convertResultSetToMap(resultSet).forEach(maps -> {
			int id = (int) maps.get("id");
			String items = maps.get("inv_name").toString();
			String unit = maps.get("inv_um").toString();
			String quantity = maps.get("req_qty").toString();
			String unitPrice = maps.get("unitprice").toString();
			ProductCostModel model = new ProductCostModel(id, items, unit, quantity, unitPrice);
			list.add(model);
		});
		;

		return list;
	}

	public static ObservableList<RawMtrlRptModel> getInventoryReport(String code) {
		ObservableList<RawMtrlRptModel> list = FXCollections.observableArrayList();
		String query = "select inv.inv_id , i.inv_name, i.inv_um, (select totalImport from rawmaterialimport "
				+ "where inv_id = inv.inv_id) as importedRawMaterail , sum(inv.lineTotal) as UsedRawMaterial, "
				+ "(select (select rate from currencies c where c.currency = eb.currency and c.entryDate = eb.expns_date)* ed.unitprice "
				+ "as usdunitprice from expenseDetail ed inner join expensebill eb on eb.expns_id = ed.expns_id\n"
				+ "where inv_id = inv.inv_id order by eb.expns_date desc LIMIT 1) as unitPrice \n"
				+ "from productionrawmaterial inv inner join inventory i on i.inv_id = inv.inv_id\n"
				+ "where inv.inv_id like '%" + code + "%' group by inv_id;";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("inv_id");
				String items = resultSet.getString("inv_name");
				String unit = resultSet.getString("inv_um");
				double imported = resultSet.getDouble("importedRawMaterail");
				double used = resultSet.getDouble("UsedRawMaterial");
				double remained = imported - used;
				double unitPrice = resultSet.getDouble("unitPrice");
				double lineTotal = remained * unitPrice;

				RawMtrlRptModel model = new RawMtrlRptModel(id, items, unit, imported, used, unitPrice, lineTotal);
				list.add(model);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return list;
	}

	public static List<String> getInvItemsWithCode() {
		List<String> list = new ArrayList<String>();
		String query = "select inv_id, inv_name from inventory order by inv_id";
		ResultSet result = DatabaseUtils.dbSelectExuteQuery(query);
		log.info("Exectuing query {}", query);
		try {
			while (result.next()) {
				String items = result.getString("inv_id").concat(" - ").concat(result.getString("inv_name"));
				list.add(items);
			}
		} catch (SQLException e) {
			log.error("Error Executing query {}", query + "with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return list;
	}

}
