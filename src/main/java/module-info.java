module net.landofrails {
    requires javafx.controls;
    requires javafx.fxml;

    opens net.landofrails to javafx.fxml;
    exports net.landofrails;
}
