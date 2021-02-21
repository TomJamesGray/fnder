package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SearchController {
    public TextField searchBox;
    public VBox searchRoot;
    public VBox resultsContainer;
    private BaseSource[] dataSources;
    private Stage stage;
    private List<VBox> currentResultsVboxes;
    private int currentHighlightedVbox = 0;

    public void keyTypedSearchBox(KeyEvent keyEvent) {
        currentResultsVboxes = new ArrayList<VBox>();

        resultsContainer.getChildren().clear();
        System.out.println(searchBox.getScene().getWindow());
        System.out.println("Do Search");
        String query = searchBox.getText() + keyEvent.getText();
        final int resHeight = 90;
        int totalResultAmount = 0;
        for (BaseSource dSource : dataSources){
            DataSourceResult[] results = dSource.searchFor(query,2);

            totalResultAmount += results.length;
            for (int i = 0; i < results.length; i++){
                VBox resVbox = results[i].genResultBox();
                currentResultsVboxes.add(resVbox);
                resultsContainer.getChildren().add(resVbox);
//                System.out.println(i+1 + ") " + results[i]);

            }
            System.out.println();
        }
        stage.setHeight(totalResultAmount * resHeight + searchBox.getPrefHeight());
        if(currentResultsVboxes.size() > 0) {
            currentResultsVboxes.get(0).getStyleClass().add("resultsContainerActive");
        }
    }

    public void keyPressedSearchBox(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP){
            int numOfResults = currentResultsVboxes.size();
            if(numOfResults > 0){
                if(currentHighlightedVbox - 1 >= 0){
                    currentResultsVboxes.get(currentHighlightedVbox).
                            getStyleClass().clear();
                    currentResultsVboxes.get(currentHighlightedVbox).
                            getStyleClass().add("resultsContainer");
                    currentResultsVboxes.get(currentHighlightedVbox - 1).
                            getStyleClass().add("resultsContainerActive");
                    currentHighlightedVbox -= 1;
                }
            }
        }
        else if (keyEvent.getCode() == KeyCode.DOWN){
            int numOfResults = currentResultsVboxes.size();
            if(numOfResults > 0){
                if(currentHighlightedVbox + 1 < numOfResults){
                    currentResultsVboxes.get(currentHighlightedVbox + 1).
                            getStyleClass().add("resultsContainerActive");
                    currentResultsVboxes.get(currentHighlightedVbox).
                            getStyleClass().clear();
                    currentResultsVboxes.get(currentHighlightedVbox).
                            getStyleClass().add("resultsContainer");
                    currentHighlightedVbox += 1;
                }
            }
        }
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
