package com.gray.datasources;

import com.sandec.mdfx.MDFXNode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import org.scilab.forge.jlatexmath.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
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

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
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
                TeXFormula formula = new TeXFormula(mdContent[i]);
                BufferedImage imgB = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY,
                        25, new Color(0,0,0),
                        new Color(255,255,255));
                Image img = convertToFxImage(imgB);

                ImageView selectedImage = new ImageView();
                selectedImage.setImage(img);
                container.getChildren().add(selectedImage);
            }
        }

    }
}
