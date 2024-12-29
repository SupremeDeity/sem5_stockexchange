package com.stockexchangeapp.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import com.stockexchangeapp.MainApp;
import com.stockexchangeapp.model.Commodity;
import com.stockexchangeapp.model.Company;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class StatisticsPanelController implements Initializable {
    private MainApp app;

    @FXML
    private ListView<Company> companyList;
    @FXML
    private ListView<Commodity> commodityList;

    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    public void setApp(MainApp app) {
        this.app = app;
        companyList.setItems(app.getCompanyData());
        commodityList.setItems(app.getCommodityData());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        commodityList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void handleCompareAssets() {

        // clear old values
        if (!lineChart.getData().isEmpty()) {
            lineChart.getData().remove(0, lineChart.getData().size());
        }

        companyList.getSelectionModel().getSelectedItems().forEach(company -> {
            XYChart.Series series = new XYChart.Series();

            company.getTransactionList().forEach((transaction) -> {
                series.getData().add(new XYChart.Data<String, Double>(transaction.getTime(), transaction.getPrice()));
            });
            series.setName(company.getName());
            lineChart.getData().add(series);
        });

        commodityList.getSelectionModel().getSelectedItems().forEach(commodity -> {
            XYChart.Series series = new XYChart.Series();

            commodity.getTransactionList().forEach((transaction) -> {
                series.getData().add(new XYChart.Data<String, Double>(transaction.getTime(), transaction.getPrice()));
            });
            series.setName(commodity.getName());
            lineChart.getData().add(series);
        });
    }
}
