package com.gray;

import com.gray.datasources.BaseSource;
import com.gray.datasources.DataSourceResult;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SearchController {
    public TextField searchBox;
    private BaseSource[] dataSources;

    public void keyPressSearchBox(KeyEvent keyEvent) {
        System.out.println(searchBox.getScene().getWindow());
        System.out.println("Do Search");
        String query = searchBox.getText() + keyEvent.getText();
        for (BaseSource dSource : dataSources){
            DataSourceResult[] results = dSource.searchFor(query,2);

            System.out.println(dSource.getSourceName() + ":");
            for (int i = 0; i < results.length; i++){
                System.out.println(i+1 + ") " + results[i]);
            }
            System.out.println();
        }
    }

    public BaseSource[] getDataSources() {
        return dataSources;
    }

    public void setDataSources(BaseSource[] dataSources) {
        this.dataSources = dataSources;
    }
}
