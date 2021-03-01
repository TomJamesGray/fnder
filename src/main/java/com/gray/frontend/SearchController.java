package com.gray.frontend;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import com.gray.datasources.ServerResultList;
import com.gray.datasources.URLResult;
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

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    public TextField searchBox;
    public VBox searchRoot;
    public VBox resultsContainer;
    private BaseSource[] dataSources;
    private Stage stage;
    private List<DataSourceResult> currentResults = new ArrayList<DataSourceResult>();
    private int currentHighlightedVbox = 0;

    private Socket socket;
    private ObjectInputStream socketIn;
    private DataOutputStream socketOut;
    public static final int socketPort = 6000;

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
        Object out;
        try {
            out = sendSearchQuery(query);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        final int resHeight = 90;
        final int maxResults = 2;
        int totalResultAmount = 0;
        currentResults = new ArrayList<DataSourceResult>();
        for (BaseSource dSource : dataSources){
            DataSourceResult[] resultsForDSource = dSource.searchFor(query,maxResults);

            if(resultsForDSource.length > 0){
//                Add label describing the data source
                Label dSourceLabel = new Label(dSource.getSourceName());
                dSourceLabel.getStyleClass().add("dSourceLabel");
                resultsContainer.getChildren().add(dSourceLabel);
            }

            totalResultAmount += resultsForDSource.length;
            for (int i = 0; i < resultsForDSource.length; i++){
                VBox resVbox = resultsForDSource[i].getResultBox();
                resultsContainer.getChildren().add(resVbox);
//                Add onto currentResults list so we can access it later
                currentResults.add(resultsForDSource[i]);
            }
        }
        stage.setHeight(totalResultAmount * resHeight + searchBox.getPrefHeight());
        if(currentResults.size() > 0) {
            currentResults.get(0).getResultBox().getStyleClass().add("resultsContainerActive");
            for(int i = 1;i < currentResults.size(); i++){
//                Clear all other result boxes
                currentResults.get(i).getResultBox().getStyleClass().clear();
                currentResults.get(i).getResultBox().getStyleClass().add("resultsContainer");
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
                int numOfResults = currentResults.size();
                if (keyEvent.getCode() == KeyCode.UP){
                    if(numOfResults > 0){
                        if(currentHighlightedVbox - 1 >= 0){
                            System.out.println(searchBox.getCaretPosition());
                            currentResults.get(currentHighlightedVbox).getResultBox().
                                    getStyleClass().clear();
                            currentResults.get(currentHighlightedVbox).getResultBox().
                                    getStyleClass().add("resultsContainer");
                            currentResults.get(currentHighlightedVbox - 1).getResultBox().
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
                            currentResults.get(currentHighlightedVbox + 1).getResultBox().
                                    getStyleClass().add("resultsContainerActive");
                            currentResults.get(currentHighlightedVbox).getResultBox().
                                    getStyleClass().clear();
                            currentResults.get(currentHighlightedVbox).getResultBox().
                                    getStyleClass().add("resultsContainer");
                            currentHighlightedVbox += 1;
                        }
                    }
//                    Consuming key event now stops caret being moved around
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == KeyCode.ENTER){
                    searchRoot.getChildren().clear();
                    currentResults.get(currentHighlightedVbox).openResult(searchRoot);
                    keyEvent.consume();
                }
            }
        });
//        initConnection();
    }

    /**
     * Starts connection to server
     */
    private void initConnection(){
        try {
            socket = new Socket("localhost",socketPort);
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection(){
        try{
            socket.close();
            socketIn.close();
            socketOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object sendSearchQuery(String msg) throws IOException, ClassNotFoundException {
        initConnection();
        socketOut.writeUTF(msg);
        Object reply = socketIn.readObject();
        closeConnection();
        return (reply);
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
