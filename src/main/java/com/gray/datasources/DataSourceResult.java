package com.gray.datasources;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSourceResult {
    public String title;
    public VBox resultBox;
    public DataSourceResult(String title){
        this.title = title;
        this.resultBox = new VBox();
        resultBox.getStyleClass().add("resultsContainer");
        Label lbl = new Label(this.title);
        resultBox.getChildren().add(lbl);
    }
    public void openResult(){

    }
}
