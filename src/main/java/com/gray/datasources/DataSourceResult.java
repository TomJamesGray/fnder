package com.gray.datasources;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.Serializable;

public class DataSourceResult extends RootDataSourceResult implements Serializable {
    public String title;
    private VBox resultBox;
    public static final boolean writesToGui = false;
    public DataSourceResult(String title){
        super();
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
