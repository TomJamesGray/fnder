package com.gray.datasources;

import java.nio.file.Path;
import java.util.List;

public class MdFile extends DataSourceResult{
    public List<String> tags;
    public Path dir;
    private int scoreForQuery;

    public MdFile(Path dir, List<String> tags){
        this.dir = dir;
        this.tags = tags;
    }

    public int getScoreForQuery() {
        return scoreForQuery;
    }

    public void setScoreForQuery(int scoreForQuery) {
        this.scoreForQuery = scoreForQuery;
    }

    public String toString(){
        return getClass().getSimpleName() + "[file name=" + dir.getFileName() + "]";
    }
}
