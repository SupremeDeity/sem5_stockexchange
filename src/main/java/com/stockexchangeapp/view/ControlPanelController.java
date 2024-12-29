package com.stockexchangeapp.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.stockexchangeapp.MainApp;
import com.stockexchangeapp.model.Commodity;
import com.stockexchangeapp.model.Company;
import com.stockexchangeapp.model.Currency;
import com.stockexchangeapp.model.Investor;
import com.stockexchangeapp.model.StockExchange;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class ControlPanelController implements Initializable {

    private MainApp app;

    @FXML
    Button newStockExchange;
    @FXML
    Button newCompany;
    @FXML
    Button newInvestor;
    @FXML
    Button newCurrency;
    @FXML
    Button newCommodity;

    @FXML
    private TableView<StockExchange> stockExchangeTable;
    @FXML
    private TableColumn<StockExchange, String> stockExchangeNameColumn;

    @FXML
    private TableView<Company> companyTable;
    @FXML
    private TableColumn<Company, String> companyNameColumn;
    @FXML
    private TableColumn<Company, Integer> companySharesForSaleColumn;

    @FXML
    private TableView<Investor> investorTable;
    @FXML
    private TableColumn<Investor, String> investorFirstNameColumn;
    @FXML
    private TableColumn<Investor, String> investorLastNameColumn;
    @FXML
    private TableColumn<Investor, String> investorIdColumn;
    @FXML
    private TableColumn<Investor, Double> investorBudgetColumn;

    @FXML
    private TableView<Currency> currencyTable;
    @FXML
    private TableColumn<Currency, String> currencyCodeColumn;
    @FXML
    private TableColumn<Currency, String> currencyNameColumn;

    @FXML
    private TableView<Commodity> commodityTable;
    @FXML
    private TableColumn<Commodity, String> commodityNameColumn;
    @FXML
    private TableColumn<Commodity, Integer> commodityUnitsForSale;

    public void setApp(MainApp app) {
        this.app = app;
        stockExchangeTable.setItems(app.getStockExchangeData());
        companyTable.setItems(app.getCompanyData());
        investorTable.setItems(app.getInvestorData());
        currencyTable.setItems(app.getCurrencyData());
        commodityTable.setItems(app.getCommodityData());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        stockExchangeNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        companySharesForSaleColumn
                .setCellValueFactory(cellData -> cellData.getValue().sharesForSaleProperty().asObject());
        investorIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        investorFirstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        investorLastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        investorBudgetColumn.setCellValueFactory(cellData -> cellData.getValue().budgetProperty().asObject());
        currencyCodeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        currencyNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        commodityNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        commodityUnitsForSale
                .setCellValueFactory(cellData -> cellData.getValue().commoditiesForSaleProperty().asObject());
    }

    @FXML
    private void handleNewCurrency() {
        Currency tempCurrency = new Currency();
        boolean okClicked = showCurrencyFormDialog(tempCurrency);
        if (okClicked) {
            app.getCurrencyData().add(tempCurrency);
            System.out.println(app.getCurrencyData());
        }
    }

    @FXML
    private void handleEditCurrency() {
        Currency selectedCurrency = currencyTable.getSelectionModel().getSelectedItem();
        if (selectedCurrency != null) {
            showCurrencyFormDialog(selectedCurrency);
        } else
            showAlert("Please select a Currency in the table.");
    }

    @FXML
    private void handleDeleteCurrency() {
        Currency selectedCurrency = currencyTable.getSelectionModel().getSelectedItem();
        if (selectedCurrency != null) {
            app.getCurrencyData().remove(selectedCurrency);
        } else
            showAlert("Please select a Currency in the table.");
    }

    @FXML
    private void handleNewCompany() {
        Company tempCompany = new Company();
        boolean okClicked = showCompanyFormDialog(tempCompany, "new");
        if (okClicked) {
            app.getCompanyData().add(tempCompany);
            app.getCompanyAbbreviationSet().add(tempCompany.getAbbreviation());
            new Thread(tempCompany).start();
            System.out.println("added company: " + tempCompany.getName());
        }
    }

    @FXML
    private void handleEditCompany() {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        if (selectedCompany != null) {
            showCompanyFormDialog(selectedCompany, "edit");
        } else
            showAlert("Please select a Company in the table.");
    }

    @FXML
    private void handleDeleteCompany() {
        Company selectedCompany = companyTable.getSelectionModel().getSelectedItem();
        if (selectedCompany != null) {
            app.getCompanyData().remove(selectedCompany);
            selectedCompany.getStockExchangeBelonging().getCompanies().remove(selectedCompany);
            selectedCompany.buyAllShares();
            selectedCompany.terminate();
        } else
            showAlert("Please select a Company in the table.");
    }

    @FXML
    private void handleNewStockExchange() {
        StockExchange tempStockExchange = new StockExchange();
        boolean okClicked = showStockExchangeFormDialog(tempStockExchange);
        if (okClicked) {
            app.getStockExchangeData().add(tempStockExchange);
            System.out.println(app.getStockExchangeData());
        }
    }

    @FXML
    private void handleEditStockExchange() {
        StockExchange selectedStockExchange = stockExchangeTable.getSelectionModel().getSelectedItem();
        if (selectedStockExchange != null) {
            showStockExchangeFormDialog(selectedStockExchange);
        } else
            showAlert("Please select a StockExchange in the table.");
    }

    @FXML
    private void handleDeleteStockExchange() {
        StockExchange selectedStockExchange = stockExchangeTable.getSelectionModel().getSelectedItem();
        if (selectedStockExchange != null) {

            selectedStockExchange.getCompanies().forEach(company -> {
                app.getCompanyData().remove(company);
                company.terminate();
            });

            selectedStockExchange.getInvestors().forEach(investor -> {
                app.getInvestorData().remove(investor);
                investor.terminate();
            });

            app.getStockExchangeData().remove(selectedStockExchange);
        } else
            showAlert("Please select a StockExchange in the table.");
    }

    @FXML
    private void handleNewInvestor() {
        Investor tempInvestor = new Investor();
        boolean okClicked = showInvestorFormDialog(tempInvestor);
        if (okClicked) {
            app.getInvestorData().add(tempInvestor);
            new Thread(tempInvestor).start();
            System.out.println("added investor: " + tempInvestor.getId() + " " + tempInvestor.getFirstName()
                    + tempInvestor.getLastName());
        }
    }

    @FXML
    private void handleEditInvestor() {
        Investor selectedInvestor = investorTable.getSelectionModel().getSelectedItem();
        if (selectedInvestor != null) {
            showInvestorFormDialog(selectedInvestor);
        } else
            showAlert("Please select a Investor in the table.");
    }

    @FXML
    private void handleDeleteInvestor() {
        Investor selectedInvestor = investorTable.getSelectionModel().getSelectedItem();
        if (selectedInvestor != null) {
            app.getInvestorData().remove(selectedInvestor);
            selectedInvestor.sellAllShares();
            selectedInvestor.sellAllCommodities();
            selectedInvestor.terminate();
        } else
            showAlert("Please select a Investor in the table.");
    }

    @FXML
    private void handleNewCommodity() {
        Commodity tempCommodity = new Commodity();
        boolean okClicked = showCommodityFormDialog(tempCommodity, "new");
        if (okClicked) {
            app.getCommodityData().add(tempCommodity);
            new Thread(tempCommodity).start();
            System.out.println("added commodity: " + tempCommodity.getName());
        }
    }

    @FXML
    private void handleEditCommodity() {
        Commodity selectedCommodity = commodityTable.getSelectionModel().getSelectedItem();
        if (selectedCommodity != null) {
            showCommodityFormDialog(selectedCommodity, "edit");
        } else
            showAlert("Please select a Commodity in the table.");
    }

    @FXML
    private void handleDeleteCommodity() {
        Commodity selectedCommodity = commodityTable.getSelectionModel().getSelectedItem();
        if (selectedCommodity != null) {
            app.getCommodityData().remove(selectedCommodity);
            selectedCommodity.getCommodityMarketBelonging().getCommodities().remove(selectedCommodity);
            selectedCommodity.buyAllCommodities();
            selectedCommodity.terminate();
        } else
            showAlert("Please select a Commodity in the table.");
    }

    public boolean showCurrencyFormDialog(Currency currency) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CurrencyFormDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Currency Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the currency into the controller.
            CurrencyFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCurrency(currency);

            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showCompanyFormDialog(Company company, String type) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CompanyFormDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Company Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the currency into the controller.
            CompanyFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);
            controller.setType(type);
            controller.setCompanyFields(company);

            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showStockExchangeFormDialog(StockExchange stockExchange) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StockExchangeFormDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("StockExchange Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the currency into the controller.
            StockExchangeFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);
            controller.setStockExchangeFields(stockExchange);

            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showInvestorFormDialog(Investor investor) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/InvestorFormDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Investor Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the currency into the controller.
            InvestorFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);

            controller.setInvestorFields(investor);

            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(app.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText(message);

        alert.showAndWait();
    }

    public boolean showCommodityFormDialog(Commodity commodity, String type) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CommodityFormDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Commodity Form");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(app.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the currency into the controller.
            CommodityFormDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApp(app);
            controller.setType(type);
            controller.setCommodityFields(commodity);

            // Set the dialog icon.
            // dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
