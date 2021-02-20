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
        container.setStyle("width:100%");
        container.setStyle("height:50px");
        Label lbl = new Label(this.title);
        System.out.println(this.title);
        container.getChildren().add(lbl);
        return(container);
    }
}
