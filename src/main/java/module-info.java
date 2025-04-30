module org.example.automationsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.example.automationsystem to javafx.fxml;
    exports org.example.automationsystem;
}