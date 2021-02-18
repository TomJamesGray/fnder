package com.gray.datasources;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalWiki extends BaseSource{
    public LocalWiki() throws IOException {
//        Find all MD files in data directory
        final Path dataDir = Paths.get("/tmp/data");
        List<Path> mdFiles = findMdFiles(dataDir);
        System.out.println(mdFiles);
        for (Path file : mdFiles){
            getTagsForMdFile(file);
        }
    }

    /**
     * Generate tags for searching of markdown file, using the path given
     * and tags in the header of the file. Format expected is
     * <!---
     * 'tag1' tag2 "tag 3"
     * ---!>
     * @param fPath Markdown file
     * @return List of strings
     */
    public static List<String> getTagsForMdFile(Path fPath) throws IOException {
        String tagsLine = new String();
        Scanner fScanner = new Scanner(new FileReader(fPath.toString()));
        while (fScanner.hasNext()){
            if(fScanner.nextLine().startsWith("<!---")){
//                Next line contains tags
                tagsLine = fScanner.nextLine();
                fScanner.close();
                break;
            }
        }
        List<String> tags = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher tagMatcher =regex.matcher(tagsLine);
        while(tagMatcher.find()){
            if(tagMatcher.group(1) != null){
//                Add tag in "..."
                tags.add(tagMatcher.group(1));
            }
            else if(tagMatcher.group(2) != null){
//                Add tag in '...'
                tags.add(tagMatcher.group(2));
            }
            else{
//                Add tag (singular word)
                tags.add(tagMatcher.group());
            }
        }
        return(tags);
    }

    public static List<Path> findMdFiles(Path dir){
        List<Path> mdFiles = new ArrayList<Path>();
        File folder =  new File(dir.toString());
        File[] files = folder.listFiles();
        for (File file : files){
            if (file.getName().endsWith(".md")){
                System.out.println(file.getName());
                mdFiles.add(Paths.get(file.toString()));
            }
            else if(file.isDirectory()){
                List<Path> additionalFiles = findMdFiles(Paths.get(dir.toString(),file.getName()));
                mdFiles.addAll(additionalFiles);
            }
        }
        return mdFiles;
    }

}
