module com.stockexchangeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires java.logging;
    requires jakarta.xml.bind;
    requires org.json;

    opens com.stockexchangeapp to javafx.fxml;
    opens com.stockexchangeapp.view to javafx.fxml;

    exports com.stockexchangeapp;
}
