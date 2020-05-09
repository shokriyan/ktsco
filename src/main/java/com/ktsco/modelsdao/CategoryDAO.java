package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.admin.MainCatModel;
import com.ktsco.models.factory.CategoryModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryDAO {
	private static final Logger log = LoggerFactory.getLogger(CategoryDAO.class);

	private static CategoryModel catModel;

	public static ObservableList<CategoryModel> selectAllItems() {
		ObservableList<CategoryModel> listObservable = FXCollections.observableArrayList();
		String query = "Select category_id, category, (select concat(id , ' - ' , name )from maincategory where id = c.maincategory_id) as mainCategory, "
				+ "cd_category from category c";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);

		try {

			while (resultSet.next()) {
				int categoryID = resultSet.getInt("category_id");
				String category = resultSet.getString("category");
				String mainCatItem = resultSet.getString("maincategory");
				String catCode = resultSet.getString("cd_category");
				
				catModel = new CategoryModel(categoryID, mainCatItem, category, catCode);
				listObservable.add(catModel);
			}

		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			log.info("list can be null at this point {}", listObservable);
			AlertsUtils.databaseErrorAlert();

		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return listObservable;
	}

	public static ObservableList<CategoryModel> retrieveSearchItems(String searchContex) {
		ObservableList<CategoryModel> listObservable = FXCollections.observableArrayList();
		String query = "Select category_id, category, (select concat(id , ' - ' , name )from maincategory where id = c.maincategory_id) as mainCategory, "
				+ "cd_category from category c where category like ?";

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setString(1, "%" + searchContex + "%");
			resultSet = preStmt.executeQuery();

			while (resultSet.next()) {
				int categoryID = resultSet.getInt("category_id");
				String category = resultSet.getString("category");
				String mainCatItem = resultSet.getString("maincategory");
				String categoryCode = resultSet.getString("cd_category");
				catModel = new CategoryModel(categoryID, mainCatItem, category, categoryCode);
				listObservable.add(catModel);
			}

		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			log.info("list can be null at this point {}", listObservable);
			AlertsUtils.databaseErrorAlert();

		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return listObservable;

	}

	public static void addCategory(String category, int mainCat, String categoryCode) {

		String query = "Insert into category (category, maincategory_id,cd_category ) values (?, ?,?)";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, category);
			preStmt.setInt(2, mainCat);
			preStmt.setString(3, categoryCode);
			preStmt.execute();
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

	}

	public static void deleteCategory(int categoryId) {
		String query = "Delete from category where category_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, categoryId);
			preStmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	public static void modifyCategory(int categoryId, String newValue, int mainCatID ,String categoryCode) {
		String query = "Update category set category = ? , maincategory_id = ? , cd_category = ? where category_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, newValue);
			preStmt.setInt(2, mainCatID);
			preStmt.setString(3, categoryCode);
			preStmt.setInt(4, categoryId);
			preStmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	public static boolean checkExistance(String lookupValue) {

		boolean exist = false;

		String query = "Select * from category";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);

		try {
			while (resultSet.next()) {
				String category = resultSet.getString("category");
				if (category.trim().equalsIgnoreCase(lookupValue.trim())) {
					exist = true;
					break;
				} else
					exist = false;
			}

		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.databaseErrorAlert();

		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return exist;

	}

	public static Integer getCategoryID(String lookupValue) {
		int catId = 0;
		String query = "Select category_id from category where category = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setString(1, lookupValue);
			resultSet = preStmt.executeQuery();

			while (resultSet.next()) {
				catId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return catId;
	}

	public static List<String> getCategoryItemsForCombo() {
		List<String> list = new ArrayList<String>();
		String query = "Select category from category";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					list.add(resultSet.getString(1));
				}
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		}
		return list;
	}

	public static String getCategoryName(int categoryId) {
		String catName = null;

		String query = "select category from category where category_id = ? ";

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setInt(1, categoryId);
			resultSet = preStmt.executeQuery();
			while (resultSet.next()) {
				catName = resultSet.getString("category");
			}
		} catch (SQLException e) {
			log.error("Error in executing query {}", query + " with error massage {}", e.getMessage());
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return catName;
	}

	public static ObservableList<MainCatModel> getMainCatItems() {
		ObservableList<MainCatModel> list = FXCollections.observableArrayList();

		String query = "select * from mainCategory order by id";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		List<Map<String, Object>> mapData = DatabaseUtils.convertResultSetToMap(resultSet);
		for (Map<String, Object> map : mapData) {
			int code = Integer.parseInt(map.get("id").toString());
			String desc = map.get("name").toString();
			MainCatModel model = new MainCatModel(code, desc);
			list.add(model);
		}

		return list;

	}

	public static boolean insertMainCatData(int code, String desc) {
		boolean isSuccess = false;
		String query = "Insert into mainCategory (id, name) values (? , ?)";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, code);
			preStmt.setString(2, desc);

			preStmt.execute();
			isSuccess = true;
		} catch (SQLException e) {
			log.error("Error in executing query " + query + " with error massage " + e.getMessage());
			isSuccess = false;
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return isSuccess;
	}

	public static boolean deleceMainCatItems(int code) {
		boolean isSuccess = false;
		String query = "delete from mainCategory where id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setInt(1, code);
			preStmt.execute();
			isSuccess = true;
		} catch (SQLException e) {
			log.error("Error in executing query " + query + " with error massage " + e.getMessage());
			isSuccess = false;
			AlertsUtils.databaseErrorAlert();
		} finally {
			try {
				preStmt.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		return isSuccess;
	}

	public static List<String> comboMainCatItems() {
		List<String> list = new ArrayList<String>();

		String query = "select * from mainCategory order by id";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		List<Map<String, Object>> mapData = DatabaseUtils.convertResultSetToMap(resultSet);
		for (Map<String, Object> map : mapData) {
			int code = Integer.parseInt(map.get("id").toString());
			String desc = map.get("name").toString();
			list.add(code + " - " + desc);
		}

		return list;

	}

}
