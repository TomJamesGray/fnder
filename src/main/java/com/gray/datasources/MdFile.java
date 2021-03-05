package com.gray.datasources;

import com.sandec.mdfx.MDFXNode;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.scilab.forge.jlatexmath.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;


public class MdFile extends GuiDataSourceResult implements Serializable {
    public List<String> tags;
    public String dir;
    private int scoreForQuery;
    public String title;

    public MdFile(String dir,String title, List<String> tags){
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
                dir,title);
    }

    /**
     * Retrieves the markdown content and equations in any markdown file.
     * Equations are supported in form $$\LaTeX$$
     * @return List of Strings, alternating between markdown and LaTeX equation
     */
    public String[] getMdContent() {
        String content;
        try {
            Scanner fScanner = new Scanner(new FileReader(dir));
            StringBuilder contentBuilder = new StringBuilder();
            boolean inComment = false;
            while (fScanner.hasNext()){
                String nL = fScanner.nextLine();
                if (nL.startsWith("<!---")){
//                    In a comment
                    inComment = true;
                }
                else if (nL.startsWith("--->") & inComment){
                    inComment = false;
                }
                else if (!inComment){
                    contentBuilder.append(nL);
                    contentBuilder.append(System.lineSeparator());
                }
            }
            fScanner.close();
            content = contentBuilder.toString();
        }
        catch (IOException e){
            content = "ERROR: Could not read in file";
        }
//        Split String between $$ tags
        return(content.split("(?:[\\^\\$\\$]+)"));
    }

    /**
     * Converts a Buffered image into an Image that can be displayed by javafx
     * @param image A Buffered image
     * @return A javafx image
     */
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

    /**
     * Generates the VBox that is rendered by JavaFx containing the markdown
     * @return VBox
     */
    public VBox genVboxResult(){
        String[] mdContent = getMdContent();
        VBox scrollContainer = new VBox();

        for (int i=0; i<mdContent.length;i++){
            if (i % 2 == 0){
//                On MD section
                MDFXNode mdfx = new MDFXNode(mdContent[i]);
                mdfx.getStyleClass().add("mdArea");
//                Get rid of main mdfx stylesheet
//                mdfx.getStylesheets().clear();
                mdfx.getStylesheets().add("/com/sandec/mdfx/mdfx-default.css");
                scrollContainer.getChildren().add(mdfx);
            }
            else{
                TeXFormula formula = new TeXFormula(mdContent[i]);
//                TODO Make img background somehow use "-search-bar-bg" from stylesheet
//                Or make it transparent
                BufferedImage imgB = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY,
                        25, new Color(0,0,0),
                        new Color(220,220,220));
                Image img = convertToFxImage(imgB);

                ImageView selectedImage = new ImageView();
                selectedImage.setImage(img);
                scrollContainer.getChildren().add(selectedImage);
            }
        }
        ScrollPane sp = new ScrollPane();
        sp.getStyleClass().add("resultsScrollPane");
        sp.setContent(scrollContainer);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        VBox spCont = new VBox();
        spCont.getChildren().add(sp);
        return spCont;
    }
}
