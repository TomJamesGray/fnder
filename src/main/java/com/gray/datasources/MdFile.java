package com.gray.datasources;

import com.sandec.mdfx.MDFXNode;
import com.sun.tools.jdeprscan.scan.Scan;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

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

    public String getMdContent() {
        String content;
        try {
            content = new String(Files.readAllBytes(dir));
        }
        catch (IOException e){
            content = "ERROR: Could not read in file";
        }
        return(content);
    }

    public void openResult(VBox container){
        MDFXNode mdfx = new MDFXNode(getMdContent());
        mdfx.getStyleClass().add("mdArea");
        container.getStylesheets().add("/com/sandec/mdfx/mdfx-default.css");

        container.getChildren().add(mdfx);
    }
}
