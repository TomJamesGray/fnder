package com.gray.datasources;

import com.sandec.mdfx.MDFXNode;
import com.sun.tools.jdeprscan.scan.Scan;
import io.github.egormkn.LatexView;
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

    /**
     * Retrieves the markdown content and equations in any markdown file.
     * Equations are supported in form $$\LaTeX$$
     * @return List of Strings, alternating between markdown and LaTeX equation
     */
    public String[] getMdContent() {
        String content;
        try {
            content = new String(Files.readAllBytes(dir));
        }
        catch (IOException e){
            content = "ERROR: Could not read in file";
        }
//        Split String between $$ tags
        return(content.split("(?:[\\^\\$\\$]+)"));
    }

    public void openResult(VBox container){
        String[] mdContent = getMdContent();
        for (int i=0; i<mdContent.length;i++){
            if (i % 2 == 0){
//                On MD section
                MDFXNode mdfx = new MDFXNode(mdContent[i]);
                mdfx.getStyleClass().add("mdArea");
                container.getStylesheets().add("/com/sandec/mdfx/mdfx-default.css");
                container.getChildren().add(mdfx);
            }
            else{
//                On LaTeX equation
                LatexView lv = new LatexView(mdContent[i]);
//                lv.setSize(30);
                container.getChildren().add(lv);
                System.out.println(mdContent[i]);
            }
        }

    }
}
