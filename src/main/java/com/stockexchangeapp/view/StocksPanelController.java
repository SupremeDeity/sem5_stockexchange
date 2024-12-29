package com.stockexchangeapp.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.stockexchangeapp.MainApp;
import com.stockexchangeapp.model.Company;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class StocksPanelController implements Initializable {

    private MainApp app;

    @FXML
    private TableView<Company> companyTable;
    @FXML
    private TableColumn<Company, String> nameColumn;
    @FXML
    private TableColumn<Company, String> abbreviationColumn;
    @FXML
    private TableColumn<Company, Integer> numberOfSharesColumn;
    @FXML
    private TableColumn<Company, Double> currentPriceColumn;
    @FXML
    private TableColumn<Company, Double> changeColumn;

    public void setApp(MainApp app) {
        this.app = app;
        companyTable.setItems(app.getCompanyData());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        abbreviationColumn.setCellValueFactory(cellData -> cellData.getValue().abbreviationProperty());
        numberOfSharesColumn.setCellValueFactory(cellData -> cellData.getValue().sharesCountProperty().asObject());
        currentPriceColumn.setCellValueFactory(cellData -> cellData.getValue().currentProperty().asObject());
        changeColumn.setCellValueFactory(cellData -> cellData.getValue().changeProperty().asObject());

        companyTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/StockDetailsDialog.fxml"));
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Company details");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(app.getPrimaryStage());
                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    // Set the currency into the controller.
                    StockDetailsDialogController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    System.out.println(companyTable.getSelectionModel().getSelectedItem().getStockExchangeBelonging());
                    controller.setCompanyFields(companyTable.getSelectionModel().getSelectedItem());

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
