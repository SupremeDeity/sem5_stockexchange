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
import com.stockexchangeapp.model.Commodity;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class CommodityDetailsDialogController implements Initializable {

    private Stage dialogStage;
    private Commodity commodity;

    @FXML
    private Label commodityName;
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
    private Label unitsNumber;
    @FXML
    private Label unit;

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
        // TODO
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCommodityFields(Commodity commodity) {
        this.commodity = commodity;

        commodityName.setText(commodity.getName());
        currentPrice.setText(String.valueOf(commodity.getCurrent()));

        double change = commodity.getChange();
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

        min.setText(String.valueOf(commodity.getMin()));
        max.setText(String.valueOf(commodity.getMax()));
        turnover.setText(String.valueOf(commodity.getTurnoverValue()));
        volume.setText(String.valueOf(commodity.getVolume()));
        unit.setText(commodity.getUnit());
        unitsNumber.setText(String.valueOf(commodity.getCommoditiesCount()));

        XYChart.Series series = new XYChart.Series();

        commodity.getTransactionList().forEach((transaction) -> {
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