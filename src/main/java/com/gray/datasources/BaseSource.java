package com.gray.datasources;

public interface BaseSource {
    DataSourceResult[] searchFor(String query, int maxResults);
    String getSourceName();
}
