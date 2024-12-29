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
import com.stockexchangeapp.model.Currency;
import com.stockexchangeapp.model.StockExchange;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class StockExchangeFormDialogController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField symbolField;
    @FXML
    private TextField feeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<Currency> currencyComboBox;

    private Stage dialogStage;
    private StockExchange stockExchange;
    private boolean okClicked = false;

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

    public void setStockExchangeFields(StockExchange stockExchange) {
        this.stockExchange = stockExchange;

        nameField.setText(stockExchange.getName());
        symbolField.setText(stockExchange.getSymbol());
        feeField.setText(String.valueOf(stockExchange.getFee()));
        countryField.setText(stockExchange.getCountry());
        cityField.setText(stockExchange.getCity());
        addressField.setText(stockExchange.getAddress());

        currencyComboBox.setItems(app.getCurrencyData());
        currencyComboBox.getSelectionModel().selectFirst();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            stockExchange.setName(nameField.getText());
            stockExchange.setSymbol(symbolField.getText());
            stockExchange.setFee(Double.parseDouble(feeField.getText()));
            stockExchange.setCountry(countryField.getText());
            stockExchange.setCity(cityField.getText());
            stockExchange.setAddress(addressField.getText());
            stockExchange.setCurrency(currencyComboBox.getSelectionModel().getSelectedItem());

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
            errorMessage += "Stock Exchange name can't be empty\n";
        }

        if (symbolField.getText() == null || symbolField.getText().length() == 0) {
            errorMessage += "Stock Exchange symbol can't be empty\n";
        }

        if (feeField.getText() == null || Double.parseDouble(feeField.getText()) == 0.0) {
            errorMessage += "Stock Exchange fee can't be empty\n";
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "Stock Exchange city can't be empty\n";
        }

        if (countryField.getText() == null || countryField.getText().length() == 0) {
            errorMessage += "Stock Exchange country can't be empty\n";
        }

        if (addressField.getText() == null || addressField.getText().length() == 0) {
            errorMessage += "Stock Exchange address can't be empty\n";
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
