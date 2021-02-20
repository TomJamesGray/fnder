package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchController {
    public TextField searchBox;
    public VBox searchRoot;
    public VBox resultsContainer;
    private BaseSource[] dataSources;
    private Stage stage;

    public void keyPressSearchBox(KeyEvent keyEvent) {
        resultsContainer.getChildren().clear();
        System.out.println(searchBox.getScene().getWindow());
        System.out.println("Do Search");
        String query = searchBox.getText() + keyEvent.getText();
        final int resHeight = 90;
        int totalResultAmount = 0;
        for (BaseSource dSource : dataSources){
            DataSourceResult[] results = dSource.searchFor(query,2);

//            System.out.println(dSource.getSourceName() + ":");

            totalResultAmount += results.length;
            for (int i = 0; i < results.length; i++){
                resultsContainer.getChildren().add(results[i].genResultBox());
//                System.out.println(i+1 + ") " + results[i]);

            }
            System.out.println();
        }
        stage.setHeight(totalResultAmount * resHeight + searchBox.getPrefHeight());
    }

    public BaseSource[] getDataSources() {
        return dataSources;
    }

    public void setDataSources(BaseSource[] dataSources) {
        this.dataSources = dataSources;
    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
