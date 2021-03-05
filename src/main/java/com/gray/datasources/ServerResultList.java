package com.gray.datasources;

import java.io.Serializable;

public class ServerResultList implements Serializable {
    private String dSourceTitle;
    private DataSourceResult[] results;


    public String getdSourceTitle() {
        return dSourceTitle;
    }

    public void setdSourceTitle(String dSourceTitle) {
        this.dSourceTitle = dSourceTitle;
    }

    public DataSourceResult[] getResults() {
        return results;
    }

    public void setResults(DataSourceResult[] results) {
        this.results = results;
    }
}
