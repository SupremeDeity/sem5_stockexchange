package com.stockexchangeapp.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.stockexchangeapp.MainApp;
import com.stockexchangeapp.model.Commodity;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class CommoditiesPanelController implements Initializable {

    private MainApp app;

    @FXML
    private TableView<Commodity> commodityTable;
    @FXML
    private TableColumn<Commodity, String> nameColumn;
    @FXML
    private TableColumn<Commodity, String> unitColumn;
    @FXML
    private TableColumn<Commodity, Integer> numberOfUnitsColumn;
    @FXML
    private TableColumn<Commodity, Double> currentPriceColumn;
    @FXML
    private TableColumn<Commodity, Double> changeColumn;

    public void setApp(MainApp app) {
        this.app = app;
        commodityTable.setItems(app.getCommodityData());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        numberOfUnitsColumn.setCellValueFactory(cellData -> cellData.getValue().commoditiesCountProperty().asObject());
        currentPriceColumn.setCellValueFactory(cellData -> cellData.getValue().currentProperty().asObject());
        changeColumn.setCellValueFactory(cellData -> cellData.getValue().changeProperty().asObject());

        commodityTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/CommodityDetailsDialog.fxml"));
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Commodity details");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(app.getPrimaryStage());
                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    // Set the currency into the controller.
                    CommodityDetailsDialogController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setCommodityFields(commodityTable.getSelectionModel().getSelectedItem());

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}