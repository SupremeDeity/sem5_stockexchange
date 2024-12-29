package com.stockexchangeapp.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import com.stockexchangeapp.MainApp;

/**
 *
 * @author blazej
 */
public class RootLayoutController {

    @FXML
    private TabPane tabPane;

    private MainApp app;

    public void setApp(MainApp app) {
        this.app = app;
    }

    @FXML
    private void handleGoToPrices() {
        app.showStocksPanel();
    }

    @FXML
    private void handleGoToControlPanel() {
        app.showControlPanel();
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(app.getPrimaryStage());

        if (file != null) {
            app.loadDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File personFile = app.getFilePath();
        if (personFile != null) {
            app.saveDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(app.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            app.saveDataToFile(file);
        }
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("StockExchangeApp");
        alert.setHeaderText("About");
        alert.setContentText("Błażej Piaskowski i5 132305");

        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
