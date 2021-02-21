package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    private List<VBox> currentResultsVboxes = new ArrayList<VBox>();
    private int currentHighlightedVbox = 0;

    public void keyTypedSearchBox(KeyEvent keyEvent) {
        if(keyEvent.getCharacter().equals("\r")){
//            Don't re-run query if enter is pressed. For some reason
//            onKeyTyped is still called even if we consume the key press
//            in the event filter
            return;
        }

        currentResultsVboxes = new ArrayList<VBox>();

        resultsContainer.getChildren().clear();
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
        }
        stage.setHeight(totalResultAmount * resHeight + searchBox.getPrefHeight());
        if(currentResultsVboxes.size() > 0) {
            currentResultsVboxes.get(0).getStyleClass().add("resultsContainerActive");
        }
    }
    @FXML
    public void initialize(){
//        Add event filter to handle selecting the desired vbox
        searchBox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                int numOfResults = currentResultsVboxes.size();
                if (keyEvent.getCode() == KeyCode.UP){
                    System.out.println("UP PRESSED");
                    if(numOfResults > 0){
                        if(currentHighlightedVbox - 1 >= 0){
                            System.out.println(searchBox.getCaretPosition());
                            currentResultsVboxes.get(currentHighlightedVbox).
                                    getStyleClass().clear();
                            currentResultsVboxes.get(currentHighlightedVbox).
                                    getStyleClass().add("resultsContainer");
                            currentResultsVboxes.get(currentHighlightedVbox - 1).
                                    getStyleClass().add("resultsContainerActive");
                            currentHighlightedVbox -= 1;
                        }
                    }
//                    Consuming key event now stops caret being moved around
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == KeyCode.DOWN){
                    System.out.println("DOWN PRESSED");
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
//                    Consuming key event now stops caret being moved around
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == KeyCode.ENTER){
                    System.out.println("ENTER PRESSED");
//                    Run selected result
//                    if(numOfResults > 0) {
//                        keyEvent.consume();
                    keyEvent.consume();
                }
            }
        });
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
