package com.gray.datasources;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSourceResult {
    public String title;
    private VBox resultBox;
    public static final boolean writesToGui = false;
    public DataSourceResult(String title){
        this.title = title;
    }
    public void openResult(VBox container){

    }

    public VBox getResultBox() {
        if (resultBox == null){
            this.resultBox = new VBox();
            resultBox.getStyleClass().add("resultsContainer");
            Label lbl = new Label(this.title);
            resultBox.getChildren().add(lbl);
        }
        return resultBox;
    }
}
