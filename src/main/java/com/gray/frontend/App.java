package com.gray.frontend;

import com.gray.datasources.BaseSource;
import com.gray.datasources.GithubRepos;
import com.gray.datasources.LocalWiki;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class App extends Application {
    private SearchController searchController;
    private BaseSource[] dataSources;
    public final boolean background = false;
    public final int serverPort = 6000;

    public static void main( String[] args ) throws Exception {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(100);
        stage.setWidth(500);

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/searchInterface.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        searchController = loader.getController();
        searchController.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static String getInput(String prompt){
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        String val = input.nextLine();
        return(val);
    }
}
