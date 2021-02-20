package com.gray;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class GUIApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(100);
        stage.setWidth(500);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/searchInterface.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
