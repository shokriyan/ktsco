package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ktsco.models.csr.MainStockModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainStockDAO {
	private static Logger log = Logger.getLogger(SaleBillDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static PreparedStatement preStatement;

	public static ObservableList<MainStockModel> retrieveStockItems() {
		ObservableList<MainStockModel> list = FXCollections.observableArrayList();
		query = "SELECT P.PROD_ID, P.PROD_NAME, P.PROD_UM, PV.EXPORTTOTAL, SV.TOTALSALES FROM PRODUCTS P "
				+ "LEFT OUTER JOIN productionexporttotalview PV ON P.PROD_ID = PV.PROD_ID "
				+ "LEFT OUTER JOIN  SALESTOTALQUANTITY SV ON SV.PRODUCT_ID = P.PROD_ID;";

		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int productCode = resultSet.getInt("prod_id");
				String productItem = resultSet.getString("prod_name");
				String unit = resultSet.getString("prod_um");
				double importQty = resultSet.getDouble("exporttotal");
				double saleQty = resultSet.getDouble("totalsales");
				double remain = importQty - saleQty;
				MainStockModel model = new MainStockModel(productCode, productItem, unit, importQty, saleQty, remain);
				list.add(model);
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

		return list;
	}

	public static List<Map<String, Object>> getCenteralStockReport(String code) {
		query = "SELECT P.PROD_ID, P.PROD_NAME, P.PROD_UM, PV.EXPORTTOTAL, SV.TOTALSALES \n"
				+ ", (select price from prod_prc_hst where prod_id = p.prod_id order by dttm_create desc limit 1) as unitPrice "
				+ "FROM PRODUCTS P " + "LEFT OUTER JOIN productionexporttotalview PV ON P.PROD_ID = PV.PROD_ID "
				+ "LEFT OUTER JOIN  SALESTOTALQUANTITY SV ON SV.PRODUCT_ID = P.PROD_ID where p.prod_id like '%"+code+"%'";
		
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		return DatabaseUtils.convertResultSetToMap(resultSet);
	}
	
	public static List<Map<String, Object>> getFactoryStockReport(String code) {
		query = "SELECT P.PROD_ID, P.PROD_NAME, P.PROD_UM,ptv.productionTotal , PV.EXPORTTOTAL\n" + 
				", (select price from prod_prc_hst where prod_id = p.prod_id order by dttm_create desc limit 1) as unitPrice\n" + 
				"FROM PRODUCTS P \n" + 
				"LEFT OUTER JOIN productionexporttotalview PV ON P.PROD_ID = PV.PROD_ID \n" + 
				"LEFT OUTER JOIN  productiontotalview ptv ON ptv.prod_id = P.PROD_ID "+" where p.prod_id like '%"+code+"%'";
		
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		return DatabaseUtils.convertResultSetToMap(resultSet);
	}

}
