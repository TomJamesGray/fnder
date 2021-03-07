package com.gray.frontend;

import com.gray.datasources.*;
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
        ServerResultList[] searchResults;
        int numOfResults = 0;
        try {
            numOfResults = sendSearchQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final int resHeight = 90;
        final int maxResults = 2;
        int totalResultAmount = 0;
        currentResults = new ArrayList<DataSourceResult>();
        for (int j = 0; j < numOfResults; j++){
            Object objIn;
            ServerResultList resultsForDSource;
            try {
                objIn = socketIn.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }

            if (objIn instanceof ServerResultList){
                resultsForDSource = (ServerResultList) objIn;
            }
            else{
                System.out.println("Expected ServerResultList. Got " + objIn.getClass());
                return;
            }

            if(resultsForDSource.getResults().length > 0){
//                Add label describing the data source
                Label dSourceLabel = new Label(resultsForDSource.getdSourceTitle());
                dSourceLabel.getStyleClass().add("dSourceLabel");
                resultsContainer.getChildren().add(dSourceLabel);
            }

            totalResultAmount += resultsForDSource.getResults().length;
            for (int i = 0; i < resultsForDSource.getResults().length; i++){
                VBox resVbox = resultsForDSource.getResults()[i].getResultBox();
                resultsContainer.getChildren().add(resVbox);
//                Add onto currentResults list so we can access it later
                currentResults.add(resultsForDSource.getResults()[i]);
            }
        }
        closeConnection();
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
                    openResult(currentResults.get(currentHighlightedVbox));
                    keyEvent.consume();
                }
            }
        });
    }

    private void openResult(DataSourceResult res){
        if (res instanceof GuiDataSourceResult){
            searchRoot.getChildren().clear();
            GuiDataSourceResult guiRes = (GuiDataSourceResult) res;
            searchRoot.getChildren().add(guiRes.genVboxResult());
        }
        else{
            res.openResult();
            stage.close();
        }
    }

    /**
     * Starts connection to server
     */
    private int initConnection(){
        try {
            socket = new Socket("localhost",socketPort);
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new DataOutputStream(socket.getOutputStream());
            Object numOfResults = socketIn.readObject();
            if(numOfResults instanceof Integer){
                return (int) numOfResults;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Closes connection to server
     */
    private void closeConnection(){
        try{
            socket.close();
            socketIn.close();
            socketOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends search queries to the server and returns the result object
     * @param msg Search query, provided by the user
     * @return ServerResultList array
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private int sendSearchQuery(String msg) throws IOException {
        int numOfResults = initConnection();
        socketOut.writeUTF(msg);
        return numOfResults;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage property and adds an event filter to catch presses
     * of the escape key which close the program
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
//                    Close program
                    stage.close();
                }
            }
        });
    }
}
