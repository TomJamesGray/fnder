package com.gray.datasources;

import javafx.scene.layout.VBox;

import java.io.Serializable;

public class GuiDataSourceResult extends DataSourceResult implements Serializable {
    public String title;
    public VBox resultBox;
    public static final boolean writesToGui = true;

    public GuiDataSourceResult(String title) {
        super(title);
    }
    public VBox genVboxResult(){
        return new VBox();
    }
}
