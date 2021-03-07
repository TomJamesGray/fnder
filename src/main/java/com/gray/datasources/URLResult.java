package com.gray.datasources;

import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.Serializable;

public class URLResult extends DataSourceResult implements Serializable {
    public String url;
    public URLResult(String title, String url) {
        super(title);
        this.url = url;
    }
    public String toString(){
        return String.format("%s[title=%s]",getClass().getSimpleName(),title);
    }
    public void openResult(){
        try{
            Runtime.getRuntime().exec(new String[] { "xdg-open", this.url });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
