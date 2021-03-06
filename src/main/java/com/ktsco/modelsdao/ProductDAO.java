package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.factory.ProdDetailModel;
import com.ktsco.models.factory.ProductModel;
import com.ktsco.models.mgmt.ProductHstModal;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductDAO {
	private static final Logger log = LoggerFactory.getLogger(ProductDAO.class);
	private static ResultSet resultSet = null;
	private static ObservableList<ProductModel> list;
	private static String query;
	private static ProductModel prodModel;
	private static PreparedStatement preStmt;
	private static ObservableList<ProdDetailModel> prodDetailList;
	private static ProdDetailModel prodDetailModel;
	private static ResultSetMetaData resultSetMetaData;

	public static int getProductID(String productName) {
		int prodID = 0;
		query = "select prod_id from products where prod_name = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, productName);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				prodID = resultSet.getInt(1);
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

		return prodID;
	}

	/**
	 * Select All Product from database to add to the table view.
	 * 
	 * @return Observable List
	 */

	public static ObservableList<ProductModel> selectAll() {
		list = FXCollections.observableArrayList();

		query = "Select * from products";
		log.info("Executing query {}", query);
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);

		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					int prodId = resultSet.getInt("prod_id");
					int catId = resultSet.getInt("category_id");
					String catName = CategoryDAO.getCategoryName(catId);
					String prodName = resultSet.getString("prod_name");
					String prodUm = resultSet.getString("prod_um");

					prodModel = new ProductModel(prodId, catName, prodName, prodUm);

					list.add(prodModel);

				}
			}

		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.info("Fail to Closing result set {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return list;
	}

	/**
	 * This method will retrieve the last productId and add 1 for next user
	 * 
	 * @return Int product ID
	 */

	public static int getLastProductId() {
		int last = 0;

		query = "select max(prod_id) as prodId from products";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				last = resultSet.getInt("prodId");
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.info("Fail to Closing result set {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return last + 1;
	}

	public static boolean isProductExist(String lookupValue) {
		boolean isExist = false;
		String prodId = null;
		query = "Select prod_id from products where trim(prod_name) = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, lookupValue.trim());
			resultSet = preStmt.executeQuery();

			if (resultSet.isBeforeFirst()) {
				isExist = true;
				while (resultSet.next()) {
					prodId = resultSet.getString(1);
					AlertsUtils.repeatItemAlerts(prodId);
				}
			} else {
				isExist = false;
			}
		} catch (SQLException e) {
			isExist = false;
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
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

	public static boolean insertProducts(int prod_id, int catId, String prodName, String prodUm, int factoryProd) {
		boolean success = false;
		query = "insert into products (prod_id, category_id, prod_name, prod_um, factory_prod) values (?,?,?,?,?)";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, prod_id);
			preStmt.setInt(2, catId);
			preStmt.setString(3, prodName);
			preStmt.setString(4, prodUm);
			preStmt.setInt(5, factoryProd);

			preStmt.execute();

			success = true;
		} catch (SQLException e) {
			success = false;
			log.error("Error in executing query {}", query + " with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return success;

	}

	public static boolean insertProductDetails(int invid, int prodid, double reqQty) {
		boolean success = false;
		query = "insert into productDetail (inv_id, prod_id, req_qty) values (?,?,?)";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, invid);
			preStmt.setInt(2, prodid);
			preStmt.setDouble(3, reqQty);

			preStmt.execute();
			success = true;
		} catch (SQLException e) {
			success = false;
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}
		return success;
	}

	public static boolean deleteProductDetail(int prodid) {
		boolean success = false;

		query = "Delete from productDetail where prod_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, prodid);
			preStmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			success = false;
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return success;
	}

	public static boolean deleteProduct(int prodId) {
		boolean success = false;

		query = "delete from products where prod_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, prodId);
			preStmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			success = false;
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}
		return success;
	}

	public static ObservableList<ProdDetailModel> showProductDetail(int prodId) {
		prodDetailList = FXCollections.observableArrayList();

		String query = "Select * from productDetail where prod_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, prodId);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int invId = resultSet.getInt("inv_id");
				String invName = InventoryDAO.getInvName(invId);
				String invUnit = InventoryDAO.getInventoryUnit(invName);

				double reqQty = resultSet.getDouble("req_qty");

				prodDetailModel = new ProdDetailModel(id, invName, invUnit, reqQty);
				prodDetailList.add(prodDetailModel);
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return prodDetailList;
	}

	public static Map<String, Object> showProduct(int prodId) {

		Map<String, Object> prodMap = new HashMap<String, Object>();
		StringBuffer sbQeury = new StringBuffer();
		sbQeury.append("select prod.prod_id, prod.prod_name, cat.category, prod.prod_um, prod.factory_prod ");
		sbQeury.append("from products prod left outer join category cat ");
		sbQeury.append("on prod.category_id = cat.category_id ");
		sbQeury.append("where prod.prod_id = ?");
		String query = String.valueOf(sbQeury);
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, prodId);
			resultSet = preStmt.executeQuery();
			resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {

					String key = resultSetMetaData.getColumnName(i);
					Object value = resultSet.getObject(i);
					prodMap.put(key, value);
				}
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.info("Fail to Closing PreparedStatement {}", e.getMessage());
				log.error(e.getMessage());
			}
		}

		return prodMap;

	}

	public static void deleteDetailItem(int invId) {
		query = "delete from productDetail where id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, invId);
			preStmt.execute();
		} catch (SQLException e) {
			log.error("Error in Executing queyr " + query + " " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean updateProduct(int catId, String prodName, String prodUm, int prodid) {
		boolean success = false;
		query = "Update products set category_id = ? , prod_name = ? , prod_um = ? where prod_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, catId);
			preStmt.setString(2, prodName);
			preStmt.setString(3, prodUm);
			preStmt.setInt(4, prodid);
			preStmt.executeUpdate();
			success = true;

		} catch (SQLException e) {
			log.error("Error in Executing queyr " + query + " " + e.getMessage());
			AlertsUtils.databaseErrorAlert();
			success = false;
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return success;
	}

	public static List<String> getProductList() {
		List<String> list = new ArrayList<String>();
		query = "select prod_name from products where factory_prod = 1 ";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
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

	public static String getUnitMeasure(String products) {
		log.info("Start Get Unit Measure Service");
		String unit = null;
		query = "Select prod_um from products where prod_name = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, products);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				unit = resultSet.getString(1);
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
		return unit;
	}

	public static ObservableList<String> getProductObservableList() {
		ObservableList<String> list = FXCollections.observableArrayList();
		query = "select concat(prod_id , ':',prod_name) as product from products";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
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

	public static List<String> getProductListWithID() {
		List<String> list = new ArrayList<String>();
		query = "select concat(prod_id, ' - ', prod_name) as products from products where factory_prod = 1";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
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

	public static String getUnitMeasure(int productCode) {
		String unit = null;
		query = "Select prod_um from products where prod_id = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, productCode);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				unit = resultSet.getString(1);
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
		return unit;
	}

	public static List<Map<String, Object>> retrieveProductsSalesReport(String productCode, String startDate,
			String endDate) {
		startDate = (!startDate.equalsIgnoreCase("")) ? DateUtils.convertJalaliToGregory(startDate) : "1900-01-01";
		endDate = (!endDate.equalsIgnoreCase("")) ? DateUtils.convertJalaliToGregory(endDate) : "2900-12-31";
		query = "select p.prod_id as productID , p.prod_name as productName, p.prod_um as unit, \n"
				+ "sum(sd.quantity) as quantity, Sum((sd.unitprice * (select rate from currencies c where c.currency = \n"
				+ "(select currencyType from salebills sb\n" + "where sb.bill_id = sd.bill_id) and \n"
				+ "c.entryDate = (select billdate from salebills sb\n"
				+ "where sb.bill_id = sd.bill_id)) ) * sd.quantity) as usdTotal  from products p \n"
				+ "inner join saledetail sd on sd.product_id = p.prod_id\n"
				+ "inner join salebills sb on sb.bill_id = sd.bill_id\n" + "where prod_id like '%" + productCode
				+ "%' and sb.billdate between '" + startDate + "' and '" + endDate + "'\n" + "group by p.prod_id";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		return DatabaseUtils.convertResultSetToMap(resultSet);
	}

	public static void saveProdHistory(int code, double price) {
		query = "insert into prod_prc_hst (prod_id,price) values (?,?)";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, code);
			preStmt.setDouble(2, price);
			preStmt.execute();
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
	}

	public static ObservableList<ProductHstModal> prodPriceHistoryData(String code, String days) {
		ObservableList<ProductHstModal> list = FXCollections.observableArrayList();
		query = "select p.prod_id, p.prod_name, p.prod_um, pph.price,DATE_FORMAT(pph.dttm_create,'%Y-%m-%d') as date from products p\n"
				+ "inner join prod_prc_hst pph on p.prod_id = pph.prod_id\n" + "where p.prod_id like '%" + code + "%'";
		if (!days.equalsIgnoreCase("")) {
			query = query + " and pph.dttm_create between CURRENT_DATE - INTERVAL " + days + " DAY AND NOW() ";
		}

		query = query + " order by pph.dttm_create desc";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("prod_id");
				String items = resultSet.getString("prod_name");
				String unit = resultSet.getString("prod_um");
				double dolorAmount = resultSet.getDouble("price");
				String date = DateUtils.convertGregoryToJalali(resultSet.getString("date"));
				ProductHstModal model = new ProductHstModal(id, items, unit, dolorAmount, date);
				list.add(model);
			}
		} catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preStmt != null)
					preStmt.close();

			} catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return list;
	}

}
