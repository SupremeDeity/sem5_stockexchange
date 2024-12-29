/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockexchangeapp.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.stockexchangeapp.model.Currency;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class CurrencyFormDialogController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;

    private Stage dialogStage;
    private Currency currency;
    private boolean okClicked = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // Set the dialog icon.
        // this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
        nameField.setText(currency.getName());
        codeField.setText(currency.getCode());

    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            currency.setName(nameField.getText());
            currency.setCode(codeField.getText());

            okClicked = true;
            dialogStage.close();
        }

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Currency name can't be empty\n";
        }
        if (codeField.getText() == null || codeField.getText().length() == 0) {
            errorMessage += "Currency code can't be empty\n";

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