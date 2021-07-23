package net.landofrails;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LandOfRails Launcher");

        primaryStage.show();
    }
    
}