module com.example.bignumsjavafx261009 {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.example.bignumsjavafx261009 to javafx.fxml;
    exports com.example.bignumsjavafx261009;
}