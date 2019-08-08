package com.ktsco.modelsdao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktsco.models.csr.ReceivableModel;
import com.ktsco.utils.AlertsUtils;
import com.ktsco.utils.Commons;
import com.ktsco.utils.DatabaseUtils;
import com.ktsco.utils.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceivableDAO {

	private static Logger log = LoggerFactory.getLogger(ReceivableDAO.class);
	private static String query;
	private static ResultSet resultSet;
	private static ResultSetMetaData rsMetaData;
	private static PreparedStatement preStatement;

	public static ObservableList<ReceivableModel> retriveReceivableList() {
		ObservableList<ReceivableModel> list = FXCollections.observableArrayList();
		query = "SELECT ar.bill_id, c.company, ar.billdate, ar.duedate, ar.billtotal, c.currency FROM ktscodb.accountreceivable ar "
				+ "inner join customers c on c.customer_id = ar.customer_id order by ar.duedate;";
		resultSet = DatabaseUtils.dbSelectExuteQuery(query);
		try {
			while (resultSet.next()) {
				int code = resultSet.getInt("bill_id");
				String company = resultSet.getString("company");
				String billDate = DateUtils.convertGregoryToJalali(resultSet.getString("billdate"));
				String duedate = DateUtils.convertGregoryToJalali(resultSet.getString("duedate"));
				double billTotal = resultSet.getDouble("billtotal");
				String currency = Commons.getCurrencyValue(resultSet.getString("currency"));
				ReceivableModel model = new ReceivableModel(code, company, billDate, duedate, billTotal, currency);
				list.add(model);
			}
			
		}catch (SQLException e) {
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
	
	public static Map<String, Object> reteivedBillDetail(int code){
		Map<String, Object> billDetail = new HashMap<String, Object>();
		query = "select sb.bill_id, c.company , sb.billdate, sb.currencyType,\n" + 
				"sum(sd.quantity * sd.unitprice) as billTotal , sum(r.amount) as totalReceived, \n" + 
				"(sum(sd.quantity * sd.unitprice) - sum(r.amount)) as remainAmount\n" + 
				"from salebills sb\n" + 
				"inner join customers c on sb.customer_id = c.customer_id\n" + 
				"inner join saledetail sd on sb.bill_id = sd.bill_id\n" + 
				"left outer join received r on r.bill_id = sb.bill_id\n" + 
				"where sb.bill_id = ?\n" + 
				"group by sb.bill_id";
		preStatement = DatabaseUtils.dbPreparedStatment(query);
		try {
			preStatement.setInt(1, code);
			resultSet = preStatement.executeQuery();
			while(resultSet.next()) {
				billDetail.put("billID", resultSet.getInt("bill_id"));
				billDetail.put("company", resultSet.getString("company"));
				billDetail.put("billdate", resultSet.getString("billdate"));
				billDetail.put("currency", resultSet.getString("currencyType"));
				billDetail.put("billTotal", resultSet.getDouble("billTotal"));
				billDetail.put("totalReceived", resultSet.getDouble("totalReceived"));
				billDetail.put("remainAmount", resultSet.getDouble("remainAmount"));	
			}
			
		}catch (SQLException e) {
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
		return billDetail;
		
	}

}
