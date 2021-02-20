package com.gray.datasources;

import java.nio.file.Path;
import java.util.List;

public class MdFile extends DataSourceResult{
    public List<String> tags;
    public Path dir;
    private int scoreForQuery;
    public String title;

    public MdFile(Path dir,String title, List<String> tags){
        super(title);
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
        return String.format("%s[file name=%s,title=%s]",getClass().getSimpleName(),
                dir.getFileName(),title);
    }
}
