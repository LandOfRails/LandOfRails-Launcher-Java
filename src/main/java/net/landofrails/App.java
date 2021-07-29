package net.landofrails;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.landofrails.utils.FXMLUtils;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage stage;
    private static Scene scene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        stage.setOnCloseRequest((eventus) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login");
            alert.setHeaderText("Are you sure?");
            ButtonType buttonTypeOne = new ButtonType("Yes");
            alert.initModality(Modality.NONE);
            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
            if (alert.showAndWait().get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
                eventus.consume();
            }
        });
        startProgram(primaryStage);
    }

    public static void startProgram(Stage stage) throws Exception {
        scene = new Scene(FXMLUtils.loadFXML("login").load());

        stage.setScene(scene);
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

}