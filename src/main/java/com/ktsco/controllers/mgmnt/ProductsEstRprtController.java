package com.ktsco.controllers.mgmnt;

import java.net.URL;
import java.util.ResourceBundle;

import com.ktsco.utils.Commons;
import com.ktsco.utils.ViewClass;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ProductsEstRprtController implements Initializable {
	private ViewClass views = new ViewClass();
	@FXML
	private Button btnProductPriceEstReport, btnProductSellReports;
	@FXML
	private Text textPageSubtitle;
	@FXML
	private HBox hboxReportResults;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hboxReportResults.getChildren().clear();
	}

	@FXML
	private void allButtonActions(ActionEvent event) {
		if (event.getSource() == btnProductPriceEstReport) {
			textPageSubtitle.setText("محاسبه قیمت کالا");
			setReportBox("ProductsCostEstimation");
		}else if (event.getSource() == btnProductSellReports) {
			textPageSubtitle.setText("گزارش فروش محصولات");
			setReportBox("ProductsSellsReports");
		}
	}

	private void setReportBox(String fxml) {
		hboxReportResults.getChildren().clear();
		hboxReportResults.getChildren().add(views.setVboxFxml(Commons.getFxmlPanel(fxml)));
	}

}
