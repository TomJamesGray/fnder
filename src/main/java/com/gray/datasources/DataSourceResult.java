package com.gray.datasources;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSourceResult {
    public String title;
    public DataSourceResult(String title){
        this.title = title;
    }
    public void openResult(){

    }
    public VBox genResultBox(){
        VBox container = new VBox();
        container.getStyleClass().add("resultsContainer");
        Label lbl = new Label(this.title);
        container.getChildren().add(lbl);
        return(container);
    }
}
