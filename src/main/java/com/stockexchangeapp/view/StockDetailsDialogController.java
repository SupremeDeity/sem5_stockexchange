package com.stockexchangeapp.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.stockexchangeapp.model.Company;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class StockDetailsDialogController implements Initializable {

    private Stage dialogStage;
    private Company company;
    private boolean okClicked = false;

    @FXML
    private Label companyName;
    @FXML
    private Label currentPrice;
    @FXML
    private Label changeFXML;
    @FXML
    private Label min;
    @FXML
    private Label max;
    @FXML
    private Label volume;
    @FXML
    private Label turnover;
    @FXML
    private Label sharesNumber;
    @FXML
    private Label marketValue;
    @FXML
    private Label chairman;
    @FXML
    private Label abbreviation;
    @FXML
    private Label firstListingDate;
    @FXML
    private Label currency;
    @FXML
    private Label memberOfStockExchange;

    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCompanyFields(Company company) {
        this.company = company;

        companyName.setText(company.getName());
        currentPrice.setText(String.valueOf(company.getCurrent()));

        double change = company.getChange();
        change = (double) Math.round(change * 100) / 100;
        if (change > 0) {
            String changeText = "+" + String.valueOf(change) + "%";
            changeFXML.setText(changeText);
            changeFXML.setStyle("-fx-text-fill: green;");
        } else {
            String changeText = String.valueOf(change) + "%";
            changeFXML.setText(changeText);
            changeFXML.setStyle("-fx-text-fill: red;");
        }

        min.setText(String.valueOf(company.getMin()));
        max.setText(String.valueOf(company.getMax()));
        turnover.setText(String.valueOf(company.getTurnoverValue()));
        volume.setText(String.valueOf(company.getVolume()));
        sharesNumber.setText(String.valueOf(company.getSharesCount()));
        marketValue.setText(String.valueOf(company.getMarketValue()));

        chairman.setText(company.getChairman());
        abbreviation.setText(company.getAbbreviation());
        firstListingDate.setText(company.getFirstListingDate());
        currency.setText(company.getStockExchangeBelonging().getCurrency().getCode());
        memberOfStockExchange.setText(company.getStockExchangeBelonging().getName());

        XYChart.Series series = new XYChart.Series();

        company.getTransactionList().forEach((transaction) -> {
            series.getData().add(new XYChart.Data<String, Double>(transaction.getTime(), transaction.getPrice()));
        });

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
    }

    @FXML
    private void handleOk() {
        dialogStage.close();
    }
}