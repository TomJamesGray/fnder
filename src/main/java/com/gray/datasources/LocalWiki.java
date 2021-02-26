package com.gray.datasources;

import javafx.util.Pair;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalWiki implements BaseSource{
    public MdFile[] mdFiles;

    public LocalWiki() throws IOException {
//        Find all MD files in data directory
//        TODO Make this a CLI argument
        final Path dataDir = Paths.get(System.getProperty("user.dir"),"personal-wiki-data");
        Path[] mdFilesPaths = findMdFiles(dataDir).toArray(new Path[0]);

        this.mdFiles = new MdFile[mdFilesPaths.length];
        for(int i =0;i < mdFilesPaths.length; i++){
            Pair<String,List<String>> titleAndTags = getTagsAndTitleForMdFile(mdFilesPaths[i]);
            this.mdFiles[i] = new MdFile(mdFilesPaths[i],
                    titleAndTags.getKey(),
                    titleAndTags.getValue());
        }
    }

    /**
     * Searches for MD files with tags similar to query with a fuzzy search
     * @param query Query String
     * @param maxResults Maximum amount of results that can be returned
     * @return
     */
    public MdFile[] searchFor(String query, int maxResults){
//        Arbitrary value for now, may need to be adjusted.
        final int cutOffScore = 50;
        for (MdFile f : this.mdFiles){
//            Get score of highest matching tag with current query for each file
            f.setScoreForQuery(
                    FuzzySearch.extractOne(query,f.tags).getScore()
            );
        }
        MdFile[] scoreSort = Arrays.copyOf(this.mdFiles,this.mdFiles.length);
        Arrays.sort(scoreSort,
                Comparator.comparingInt(MdFile::getScoreForQuery));

//         Get results to be returned
        List<MdFile> scoresOutput = new ArrayList<MdFile>();
        int i = scoreSort.length - 1;
        while (scoresOutput.size() < maxResults){
            if (scoreSort[i].getScoreForQuery() > 50){
                scoresOutput.add(scoreSort[i]);
            }
            else{
//                None of the remaining MdFiles will have a high enough score
                break;
            }
            i = i-1;
        }
        return (scoresOutput.toArray(new MdFile[0]));

    }

    public String getSourceName() {
        return "Local Wiki";
    }

    /**
     * Generate tags for searching of markdown file, using the path given
     * and tags in the header of the file. Format expected is
     * <!---
     * 'tag1' tag2 "tag 3"
     * ---!>
     * @param fPath Markdown file
     * @return Pair<Title,Tags>
     */
    public static Pair<String,List<String>> getTagsAndTitleForMdFile(Path fPath) throws IOException {
        String tagsLine = new String();
        String title = null;
        Boolean fScannerClosed = false;
        Scanner fScanner = new Scanner(new FileReader(fPath.toString()));
        while (fScanner.hasNext()){
            String nL = fScanner.nextLine();
            if(nL.startsWith("<!---")) {
//                Next line contains tags
                tagsLine = fScanner.nextLine();

            }
            else if(nL.startsWith("--->")){
//                Next line contains title
                if(fScanner.hasNext()) {
                    title = fScanner.nextLine().replace("#", "");
                    if (title.startsWith(" ")){
//                        Remove leading space
                        title = title.substring(1);
                    }
                    System.out.println("Title " + title);
                    fScanner.close();
                    fScannerClosed = true;
                    break;
                }
            }
        }
        if(!fScannerClosed){
            fScanner.close();
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
        return(new Pair<>(title,tags));
    }

    /**
     * Finds all Markdown files (.md suffix) in a directory, and it's child directories
     * @param dir Directory to search
     * @return List of paths to the .md files
     */
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
