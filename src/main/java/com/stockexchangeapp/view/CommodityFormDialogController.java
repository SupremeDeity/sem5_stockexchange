/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockexchangeapp.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.stockexchangeapp.MainApp;
import com.stockexchangeapp.model.Commodity;
import com.stockexchangeapp.model.Company;
import com.stockexchangeapp.model.StockExchange;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class CommodityFormDialogController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField unitsField;

    private Stage dialogStage;
    private Commodity commodity;
    private boolean okClicked = false;
    private String type;

    private MainApp app;

    public void setApp(MainApp app) {
        this.app = app;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Set the dialog icon.
        // this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCommodityFields(Commodity commodity) {
        this.commodity = commodity;

        nameField.setText(commodity.getName());
        unitsField.setText(commodity.getUnit());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            commodity.setName(nameField.getText());
            commodity.setUnit(unitsField.getText());
            commodity.setCommodityMarketBelonging(app.getCommodityMarket());
            app.getCommodityMarket().getCommodities().add(commodity);

            if (type.equalsIgnoreCase("new")) {

                int min = 1;
                int max = 50;
                double random = ThreadLocalRandom.current().nextDouble(min, max);
                random = (double) Math.round(random * 100) / 100;
                int randomInt = ThreadLocalRandom.current().nextInt(min, max);

                commodity.setMin(random - 0.5);
                commodity.setMax(random + 0.5);
                commodity.setCurrent(random);
                commodity.setVolume(randomInt);
                commodity.setCommoditiesCount(randomInt);
                commodity.setCommoditiesForSale(randomInt);
                commodity.setTurnoverValue(commodity.getCurrent() * commodity.getVolume());
                commodity.setChange(1.0);
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Commodity name can't be empty\n";
        }

        if (unitsField.getText() == null || unitsField.getText().length() == 0) {
            errorMessage += "Commodity units can't be empty\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
