package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resource/main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CodeNamer");
        primaryStage.setScene(new Scene(root, 250, 140));
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.initSettingsWindow();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
