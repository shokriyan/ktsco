package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.CategoryModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryDAO {
	private static final Logger log = LoggerFactory.getLogger(CategoryDAO.class);

	private static CategoryModel catModel;

	public static ObservableList<CategoryModel> selectAllItems() {
		ObservableList<CategoryModel> listObservable = FXCollections.observableArrayList();
		String query = "Select * from category";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);

		try {
			while (resultSet.next()) {
				int categoryID = resultSet.getInt("category_id");
				String category = resultSet.getString("category");

				catModel = new CategoryModel(categoryID, category);
				listObservable.add(catModel);
			}

		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			log.info("list can be null at this point {}", listObservable);
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));

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
		String query = "Select * from category where category like ?";

		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		ResultSet resultSet = null;
		try {
			preStmt.setString(1, "%"+searchContex+"%");
			resultSet = preStmt.executeQuery();

			while (resultSet.next()) {
				int categoryID = resultSet.getInt("category_id");
				String category = resultSet.getString("category");

				catModel = new CategoryModel(categoryID, category);
				listObservable.add(catModel);
			}

		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			log.info("list can be null at this point {}", listObservable);
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));

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

	public static void addCategory(String category) {

		String query = "Insert into category (category) values (?)";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, category);
			preStmt.execute();
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در اضافه کردن اطلاعات به دیتابیس" + "\n" + e.getMessage()));
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
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در اضافه کردن اطلاعات به دیتابیس" + "\n" + e.getMessage()));
		} finally {
			try {
				preStmt.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	public static void modifyCategory(int categoryId, String newValue) {
		String query = "Update category set category = ? where category_id = ?";
		PreparedStatement preStmt = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStmt.setString(1, newValue);
			preStmt.setInt(2, categoryId);
			preStmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error while executing query {}", query);
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در اضافه کردن اطلاعات به دیتابیس" + "\n" + e.getMessage()));
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
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));

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
			AlertsUtils.ErrorAlert("خطا در دیتابیس", ("خطا در درخواست اطلاعات از دیتابیس" + "\n" + e.getMessage()));
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
	
	
	public static List<String> getCategoryItemsForCombo(){
		List<String> list = new ArrayList<String>();
		String query = "Select category from category";
		ResultSet resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					list.add(resultSet.getString(1));
				}
			}
		}catch (SQLException e) {
			log.error("Error in executing query {}" , query + " with error massage {}" , e.getMessage());
			AlertsUtils.ErrorAlert("Database Error", "خطا در ارتباط با دیتابیس");
		}
		return list; 
	}

}
