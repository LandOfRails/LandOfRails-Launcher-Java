package net.landofrails.utils;

import javafx.fxml.FXMLLoader;
import net.landofrails.App;

import java.io.IOException;

public class FXMLUtils {
    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }
}
