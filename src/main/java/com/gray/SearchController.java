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

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    public TextField searchBox;
    public VBox searchRoot;
    public VBox resultsContainer;
    private BaseSource[] dataSources;
    private Stage stage;
    private DataSourceResult[] currentResults = new DataSourceResult[0];
    private int currentHighlightedVbox = 0;

    /**
     * Handles key press in the search box. Runs searches for
     * all data sources for every key press and then
     * draws the results to the stage
     * @param keyEvent
     */
    public void keyTypedSearchBox(KeyEvent keyEvent) {
        if(keyEvent.getCharacter().equals("\r")){
//            Don't re-run query if enter is pressed. For some reason
//            onKeyTyped is still called even if we consume the key press
//            in the event filter
            return;
        }


        resultsContainer.getChildren().clear();
        String query = searchBox.getText() + keyEvent.getText();
        final int resHeight = 90;
        int totalResultAmount = 0;
        for (BaseSource dSource : dataSources){
            currentResults = dSource.searchFor(query,2);

            totalResultAmount += currentResults.length;
            for (int i = 0; i < currentResults.length; i++){
                VBox resVbox = currentResults[i].resultBox;
                resultsContainer.getChildren().add(resVbox);
//                System.out.println(i+1 + ") " + results[i]);

            }
        }
        stage.setHeight(totalResultAmount * resHeight + searchBox.getPrefHeight());
        if(currentResults.length > 0) {
            currentResults[0].resultBox.getStyleClass().add("resultsContainerActive");
            for(int i = 1;i < currentResults.length; i++){
//                Clear all other result boxes
                currentResults[i].resultBox.getStyleClass().clear();
                currentResults[i].resultBox.getStyleClass().add("resultsContainer");
            }
            currentHighlightedVbox = 0;
        }
    }

    /**
     * Called just after the FXML file is loaded and drawn. Adds
     * an event filter to the search box to handle, up, down and enter keys
     */
    @FXML
    public void initialize(){
//        Add event filter to handle selecting the desired vbox
        searchBox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                int numOfResults = currentResults.length;
                if (keyEvent.getCode() == KeyCode.UP){
                    if(numOfResults > 0){
                        if(currentHighlightedVbox - 1 >= 0){
                            System.out.println(searchBox.getCaretPosition());
                            currentResults[currentHighlightedVbox].resultBox.
                                    getStyleClass().clear();
                            currentResults[currentHighlightedVbox].resultBox.
                                    getStyleClass().add("resultsContainer");
                            currentResults[currentHighlightedVbox - 1].resultBox.
                                    getStyleClass().add("resultsContainerActive");
                            currentHighlightedVbox -= 1;
                        }
                    }
//                    Consuming key event now stops caret being moved around
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == KeyCode.DOWN){
                    if(numOfResults > 0){
                        if(currentHighlightedVbox + 1 < numOfResults){
                            currentResults[currentHighlightedVbox + 1].resultBox.
                                    getStyleClass().add("resultsContainerActive");
                            currentResults[currentHighlightedVbox].resultBox.
                                    getStyleClass().clear();
                            currentResults[currentHighlightedVbox].resultBox.
                                    getStyleClass().add("resultsContainer");
                            currentHighlightedVbox += 1;
                        }
                    }
//                    Consuming key event now stops caret being moved around
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == KeyCode.ENTER){
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
