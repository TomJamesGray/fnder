package com.gray.datasources;

import javafx.scene.layout.VBox;

public class URLResult extends DataSourceResult {
    public String url;
    public URLResult(String title, String url) {
        super(title);
        this.url = url;
    }
    public void openResult(VBox container){
        System.out.println("Opening " + title + url);
    }
}
