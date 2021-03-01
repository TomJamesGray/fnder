package com.gray.datasources;

import javafx.scene.layout.VBox;

public class GuiDataSourceResult extends DataSourceResult{
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
