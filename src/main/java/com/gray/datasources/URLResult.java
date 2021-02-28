package com.gray.datasources;

import javafx.scene.layout.VBox;

import java.io.IOException;

public class URLResult extends DataSourceResult {
    public String url;
    public URLResult(String title, String url) {
        super(title);
        this.url = url;
    }
    public void openResult(VBox container){
        System.out.println("Opening " + title + " " + url);
        try{
            Runtime.getRuntime().exec(new String[] { "xdg-open", this.url });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
