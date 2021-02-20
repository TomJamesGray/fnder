package com.gray;

import com.gray.datasources.BaseSource;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SearchController {
    public TextField searchBox;
    private BaseSource[] dataSources;
    public void keyPressSearchBox(KeyEvent keyEvent) {
        System.out.println(searchBox.getScene().getWindow());
        System.out.println("Do Search");
    }

    public BaseSource[] getDataSources() {
        return dataSources;
    }

    public void setDataSources(BaseSource[] dataSources) {
        this.dataSources = dataSources;
    }
}
