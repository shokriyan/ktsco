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
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

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
	
	public static int getProductID (String productName) {
		int prodID = 0;
		query = "select prod_id from products where prod_name = ?";
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, productName);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				prodID = resultSet.getInt(1);
			}
		}catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch (SQLException e) {
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
		log.debug("Executing query {}", query);
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
				log.debug("Fail to Closing result set {}", e.getMessage());
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
				log.debug("Fail to Closing result set {}", e.getMessage());
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
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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

				prodDetailModel = new ProdDetailModel(id, invName,invUnit, reqQty);
				prodDetailList.add(prodDetailModel);
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}" + e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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
				log.debug("Fail to Closing PreparedStatement {}", e.getMessage());
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
		}finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return success; 
	}

	
	public static List<String> getProductList(){
		List<String> list = new ArrayList<String>();
		query = "select prod_name from products where factory_prod = 1 ";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
			}
		}catch(SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				resultSet.close();
			}catch(SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		
		return list; 
	}
	
	public static String getUnitMeasure (String products) {
		String unit = null; 
		query = "Select prod_um from products where prod_name = ?"; 
		preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, products);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				unit = resultSet.getString(1);
			}
		}catch (SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				preStmt.close();
				resultSet.close();
			}catch (SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		return unit;
	}
	
	public static ObservableList<String> getProductObservableList(){
		ObservableList<String> list = FXCollections.observableArrayList();
		query = "select prod_name from products";
		try {
			resultSet = DatabaseUtils.dbSelectExuteQuery(query);
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
			}
		}catch(SQLException e) {
			log.error(Commons.dbExcutionLog(query, e.getMessage()));
			AlertsUtils.databaseErrorAlert();
		}finally {
			try {
				resultSet.close();
			}catch(SQLException e) {
				log.error(Commons.dbClosingLog(e.getMessage()));
			}
		}
		
		
		return list; 
	}
	
}
